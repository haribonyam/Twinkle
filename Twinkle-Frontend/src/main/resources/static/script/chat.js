//var stompClient = null;
//var roomId = null;
//var tradeBoardId = null;
//var username = localStorage.getItem("nickname");
//var accessToken = localStorage.getItem("jwtToken");
//let senderTemp=null;
//let isConnect = false;

async function startChat(tradeBoardId) {
    if(!validation()){
     return showToast("로그인이 필요한 서비스 입니다.");
    }
    const url = `http://localhost:8000/chat/room/${tradeBoardId}`;
        const response = await fetch(url, {
            headers: {
                "Authorization": accessToken,
                "Content-Type": "application/json"
            }
        });

        const data = await response.json();

        if (response.status === 401) {
            await refreshToken();
            return startChat(tradeBoardId);
        }

        if (!response.ok) {
            if (data.errorCodeName === '013') {

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

    const url = `http://localhost:8000/chat/room?id=${tradeBoardId}`;
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

        return await enterRoom(data.data.roomId);

    } catch (error) {
        console.error("Error:", error);
        alert("채팅방 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }
}

// 채팅방 리스트 가져오기
async function showChatList() {
    const url = "http://localhost:8000/chat/room";
    const response = await fetch(url, {
            headers: {
                'Authorization': accessToken, // Bearer 접두사 추가
                'Content-Type': 'application/json'
            }
        });
        if (response.status === 401) {
            await refreshToken();
            return showChatList();
        }
        const data = await response.json();
        console.log(data);
        if (!response.ok) {
          return notFound(data.message);
         }
        populateChatList(data);
}

/*채팅방 입장*/
async function enterRoom(roomId) {
    room = roomId;
    if(stompClient !== null){
        disconnect();
    }
    const url = 'http://localhost:8000/chat/' + roomId;
    const response = await fetch(url,{
        headers:{
            'Authorization': accessToken,
            'Content-Type':'application/json'
        }
       })

       if(response.status == 401){
           await refreshToken();
           return enterRoom(roomId);
       }
        const data = await response.json();
        console.log(data);
        populateChatRoom(data);
        if(data.chattings != null){
            createMessageHistory(data.chattings);
        }
        connect();

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
    console.log(data);
    const li = document.createElement('li');
    li.onclick = () => enterRoom(data.roomId);

    const thumbNail = document.createElement('div');
    const img = document.createElement('img');
    img.src = "http://localhost:8080"+data.thumbNail;
    img.classList.add("thumbNail");
    thumbNail.appendChild(img);

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
    console.log(data);
    const tradeBoard = data.tradeBoard;
    roomId = data.roomId; //global
    tradeBoardId = tradeBoard.id; //global
    const viewBox = document.querySelector(".view-box");
    viewBox.innerHTML = '';
    const roomData = JSON.stringify(data);

    const ul = document.createElement('ul');
    ul.classList.add('chat-room');
    let scheduleOrPayment = "notPayOrSchedule";
    if (tradeBoard.condition === "판매중" && Number(tradeBoard.memberId) === Number(userId)) {
        scheduleOrPayment = "schedule";
    } else if (tradeBoard.condition === "예약중" && Number(tradeBoard.buyer) === Number(userId)) {
        scheduleOrPayment = "payment";
    }

    const tradeBoardItem=`
    <input type="hidden" id="roomId" value="${roomId}">
    <div class="chat-tradeboard" onclick="showTradeBoardInfo(${tradeBoardId})">
                    <div class="tradeBoardThumbNail"><img class="thumbNail" src="http://localhost:8080${tradeBoard.paths[0]}"></img></div>
                    <div class="info">
                        <h4 class="h-title">${tradeBoard.title}</h4>
                        <h5 class="h-price">${tradeBoard.price}원</h4>
                    </div>
                    <div class="condition">
                        ${tradeBoard.condition}
                    </div>
    </div>
    <div class="chat-schedule">
    <div class="${scheduleOrPayment}" id="payOrSchedule" onclick='navigateToAction(this, ${roomData})'>
     ${scheduleOrPayment === 'schedule' ? '약속잡기' : '반짝페이'}
    </div>
    </div>
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
    if(connectCount > 3 ){
        alert("서버에 문제가 생겼습니다.")
        return;
    }
    var socket = new SockJS('http://localhost:8082/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({ Authorization: accessToken }, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe('/sub/' + roomId, onMessageReceived,{Authorization : accessToken});
}
let connectCount =0;
function onError(error) {
    console.log(error.body);
    connectCount++;
    if(refreshToken()){
    setTimeout(function() {
        enterRoom(room);
    }, 1000);
    }
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
    try{
        stompClient.send("/pub/" + roomId, { Authorization: accessToken }, JSON.stringify(chatMessage));
        saveMessage(chatMessage);
        messageInput.value = '';
    }catch(error){
        console.log(error);
    }
    }
}

function sendAppointment(){
     var senderId = localStorage.getItem("id");
     var username = localStorage.getItem("nickname");
        if (stompClient) {
            var chatMessage = {
                sender: username,
                content: "appointment-complete",
                senderId: senderId,
                tradeBoardId: tradeBoardId,
                roomId: roomId
            };
            stompClient.send("/pub/" + roomId, { Authorization: accessToken }, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
}

var reTryCount=0;

async function saveMessage(message) {
    const url = "http://localhost:8000/chat";
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
    if(message.content === "appointment-complete"){
        enterRoom(message.roomId);
        return;
    }

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

function navigateToAction(element,data) {
    const memberId = data.createMember;
    const roomId = data.roomId;
    const tradeBoardId = data.tradeBoard.id;
    const text = element.innerText.trim();

    if (text === "약속잡기") {
        if (confirm("약속을 잡으시겠습니까?")) {
            schedule(memberId,tradeBoardId,roomId);
        }
    } else if (text === "반짝페이") {
        if (confirm("반짝페이로 결제하시겠습니까?")) {
            openPaymentModal(data.tradeBoard.price,data.tradeBoard.memberId,tradeBoardId);
        }
    }
}

async function schedule(buyerId, tradeBoardId, roomId) {
    const url = "http://localhost:8080/api/tradeboard/request/"+tradeBoardId;

    try {
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Authorization": accessToken,
                "Content-Type": "application/json"
            },
            body:JSON.stringify(buyerId)
        });

        if (response.ok) {
            console.log("appointment success!!");
            sendAppointment();
        } else {
            console.log("Request failed with status: " + response.status);
        }
    } catch (e) {
        console.log("error: " + e);
    }
    enterRoom(roomId);
}
