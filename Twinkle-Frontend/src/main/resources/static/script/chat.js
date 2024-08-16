//var stompClient = null;
//var roomId = null;
//var tradeBoardId = null;
//var username = localStorage.getItem("nickname");
//var accessToken = localStorage.getItem("jwtToken");
//let senderTemp=null;
//let isConnect = false;

async function startChat(tradeBoardId) {
    const url = `http://localhost:8082/room/${tradeBoardId}`;
        const response = await fetch(url, {
            headers: {
                "Authorization": localStorage.getItem("jwtToken"),
                "Content-Type": "application/json"
            }
        });

        const data = await response.json();

        if (response.status === 401) {
            await refreshToken();
            return startChat(tradeBoardId);
        }

        if (!response.ok) {
            if (data.errorCodeName === "013") {
                return createChatRoom(tradeBoardId);
            } else {
                return alert("서버에 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            }
        }
        enterRoom(data);
}
async function createChatRoom(tradeBoardId) {
    if(!validation()){
        return unauthorized();
    }

    const url = `http://localhost:8082/room?id=${tradeBoardId}`;
    const body = {
        nickname: username,
        memberId: userId   // ChatRequestDto의 memberId 필드
    };
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                "Authorization": accessToken,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        if (response.status === 401) {
            await refreshToken(); // JWT 토큰 새로 고침
            return createChatRoom(tradeBoardId); // 새 토큰으로 재요청
        }
        const data = await response.json();
        if (!response.ok) {
            return alert("서버에 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
        enterRoom(data.roomId);

    } catch (error) {
        console.error("Error:", error);
        alert("채팅방 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }
}

// 채팅방 리스트 가져오기
async function showChatList() {
    const token = localStorage.getItem("jwtToken");
    const url = "http://localhost:8082/room";

    try {
        let response = await fetch(url, {
            headers: {
                'Authorization': token, // Bearer 접두사 추가
                'Content-Type': 'application/json'
            }
        });
        if (response.status === 401) {
            await refreshToken();
            return chatList();
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
      }
        const data = await response.json();
        populateChatList(data);

    } catch (error) {
        console.error('Error fetching chat list:', error);
    }
}

/*채팅방 입장*/
async function enterRoom(roomId) {
    if(stompClient !== null){
        disconnect();
    }
    const url = 'http://localhost:8082/chat/' + roomId;
    const token = localStorage.getItem("jwtToken");
    try{
       let response = await fetch(url,{
        headers:{
            'Authorization': token,
            'Content-Type':'application/json'
        }
       })

       if(response.status == 401){
           await refreshToken();
           return enterRoom(roomId);
       }
        const data = await response.json();
        populateChatRoom(data);
        if(data.chattings != null){
            createMessageHistory(data.chattings);
        }
        connect();
    } catch (error) {
       console.error('Error fetching chat list:', error);
    }
}

function populateChatList(roomData) {
    const myNickname = localStorage.getItem("nickname");

    const viewBox = document.querySelector('.view-box');
    viewBox.innerHTML = '';

    const ul = document.createElement('ul');
    ul.classList.add('chat-list');

    roomData.forEach(data => {
        const chatItem = createChatItem(data, myNickname);
        ul.appendChild(chatItem);
    });

    viewBox.appendChild(ul);
}

function createChatItem(data, myNickname) {
    const li = document.createElement('li');
    li.onclick = () => enterRoom(data.roomId);

    const thumbNail = document.createElement('div');
    thumbNail.textContent = data.thumbNail;

    const info = document.createElement('div');
    info.textContent = (myNickname === data.nickname) ? data.roomName : data.nickname;

    const date = document.createElement('div');
    date.textContent = dateDifference(data.createdDate);

    li.appendChild(thumbNail);
    li.appendChild(info);
    li.appendChild(date);
    return li;
}

function populateChatRoom(data) {
    const tradeBoard = data.tradeBoard;
    roomId = data.roomId;
    tradeBoardId = tradeBoard.id;
    const viewBox = document.querySelector(".view-box");
    viewBox.innerHTML = '';

    const ul = document.createElement('ul');
    ul.classList.add('chat-room');
    const tradeBoardItem=`
    <div class="chat-tradeboard" onclick="showTradeBoardInfo(${tradeBoardId})">
                    <div><img></img></div>
                    <div class="info">
                        <h4 class="h-title">${tradeBoard.title}</h4>
                        <h5 class="h-price">${tradeBoard.price}원</h4>
                    </div>
                    <div class="condition">
                        ${tradeBoard.condition}
                    </div>
                    <div class="schedule" id="payOrSchedule" onclick="navigateToAction(this)"></div>

    `;

    const inputChat = `
        <div class="inputChat">
            <input type="text" id="messageInput" placeholder="메세지를 입력하세요."/>
            <div id="sendButton">버튼</div>
        </div>
    `;
    viewBox.innerHTML+= tradeBoardItem;
    viewBox.appendChild(ul);
    viewBox.innerHTML += inputChat;

    // 이벤트 리스너 추가
    document.getElementById('sendButton').addEventListener('click', sendMessage);
    document.getElementById('messageInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage(event);
        }
    });
}

function createChatHistory(chattings) {
    const ul = document.createElement('ul');
    chattings.forEach(chat => {
        const li = document.createElement('li');
        li.textContent = chat.contents; // 예시로 메시지를 추가
        ul.appendChild(li);
    });
    return ul;
}

function dateDifference(date) {
    const inputDate = new Date(date);
    const currentDate = new Date();

    inputDate.setHours(0, 0, 0, 0);
    currentDate.setHours(0, 0, 0, 0);

    const timeDifference = currentDate - inputDate;

    const oneDayInMillis = 24 * 60 * 60 * 1000;

    const dayDifference = Math.floor(timeDifference / oneDayInMillis);

    if (dayDifference === 0) {
        return "오늘";
    } else if (dayDifference === 1) {
        return "하루 전";
    } else if (dayDifference > 1) {
        return `${dayDifference}일 전`;
    }
}

// stomp connect & pub/sub
function connect() {
    var socket = new SockJS('http://localhost:8082/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({ Authorization: accessToken }, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe('/sub/' + roomId, onMessageReceived);
}

function onError(error) {
    console.error('Could not connect to WebSocket server. Please refresh this page to try again!', error);
}

function sendMessage(event) {
    var senderId = localStorage.getItem("id");
    var messageInput = document.getElementById('messageInput');
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            senderId: senderId,
            tradeBoardId: tradeBoardId,
            roomId: roomId
        };
        stompClient.send("/pub/" + roomId, { Authorization: accessToken }, JSON.stringify(chatMessage));
        saveMessage(chatMessage);
        messageInput.value = '';
    }
}
var reTryCount=0;

async function saveMessage(message) {
    const url = "http://localhost:8082/chat";
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': accessToken, // Bearer 접두사 추가
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        });

        if (response.status === 401) {
            await refreshToken(); // 토큰 갱신
            return saveMessage(message); // 갱신 후 다시 시도
        }

        if (!response.ok) {
            throw new Error('네트워크 상태가 좋지 않습니다');
        }

    } catch (error) {
        retrySaveMessage(message);
    }
}

async function retrySaveMessage(message) {
    retryCount++;
    if (retryCount > 6) {
        messageSaveFail(); // 재시도 횟수 초과 시 실패 처리
        return;
    }
    setTimeout(async () => {
        await saveMessage(message);
    }, 5000);
}

function createMessageHistory(chattings){
    const messageArea = document.querySelector('.chat-room');
    let temp =null;
    chattings.forEach(chat => {

        if(chat.sender !=username && chat.sender != temp){
           const nicknameElement = document.createElement('li');
           nicknameElement.className='message';
           nicknameElement.classList.add('chat-received');
            const nicknameContent = document.createElement('div');
                  nicknameContent.textContent = chat.sender;
                  nicknameContent.className='sender';
                  nicknameElement.appendChild(nicknameContent);
                  messageArea.appendChild(nicknameElement);
        }

        const messageElement = document.createElement('li');
        messageElement.className = 'message';
        messageElement.classList.add(chat.sender === username ? 'chat-sent' : 'chat-received');

        const messageContent = document.createElement('div');
        messageContent.className='messageItem';
        messageContent.textContent = chat.content;

        messageElement.appendChild(messageContent);
        messageArea.appendChild(messageElement);
        temp = chat.sender;
    });
    senderTemp = temp;
    messageArea.scrollTop = messageArea.scrollHeight;

}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageArea = document.querySelector('.chat-room');
    if(message.sender!=username && message.sender != senderTemp){
          const nicknameElement = document.createElement('li');
          nicknameElement.className='message';
          nicknameElement.classList.add('chat-received');
          const nicknameContent = document.createElement('div');
          nicknameContent.textContent = message.sender;
          nicknameContent.className='sender';
          nicknameElement.appendChild(nicknameContent);
          messageArea.appendChild(nicknameElement);
    }
    const messageElement = document.createElement('li');
    const messageContent = document.createElement('div');

    messageElement.className = 'message';
    messageElement.classList.add(message.sender === username ? 'chat-sent' : 'chat-received');
    messageContent.className='messageItem';
    messageContent.textContent = message.content;

    messageElement.appendChild(messageContent);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;

    senderTemp=message.sender;
}

function disconnect(){
     if (stompClient !== null) {
            stompClient.disconnect(function() {
               stompClient=null;
            });
        }
}

function navigateToAction(element){
        const text = element.textContent.trim();
        if(text === "약속잡기"){
            console.log("약속잡기 핸들링");
        }else{
            console.log("페이결제 핸들링");
        }
    }
