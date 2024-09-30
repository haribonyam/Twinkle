let isTradeBoardLoaded = false;
let isSnsBoardLoaded = false;
let isLoading = false;
let isLastPage = false;
let stompClient = null;
let roomId = null;
let tradeBoardId = null;
let username = localStorage.getItem("nickname");
let userId = localStorage.getItem("id");
let accessToken = localStorage.getItem("jwtToken");
let senderTemp = null;
let retryCount = 0;
let isShowTradeBoard = false;
let currentActiveTab = null;
let room = null;
function navigateToPage(page) {
    init();
    const currentContent = document.getElementById("viewBox").innerHTML;
    saveState(currentContent);

    switch (page) {
        case 'home':
            window.location.href = "/";
            break;
        case 'tradeboard':
            showTradeBoardList('', 0);
            break;
        case 'sns':
            showSnsBoardList('',0);
            break;
        case 'my':
        if(!validation()){
            return showToast("로그인이 필요한 서비스 입니다.");
        }else{
            showMyInfo();
        }
            break;
        case 'chat':
        if(!validation()){
            return showToast("로그인이 필요한 서비스 입니다.");
        }else{
            showChatList();
        }
            break;
        default:
            console.error('Unknown page:', page);
            break;
    }
}

function init() {
    isShowTradeBoard = false;
    isTradeBoardLoaded = false;
    isSnsBoardLoaded = false;
    isLoading = false;
    isLastPage = false;
    roomId = null;
    tradeBoardId = null;
    senderTemp = null;
    activeTab = null;
    room = null;
    uploadedImages = [];
    if (stompClient!==null) {
        disconnect();
    }
    retryCount=0;
}

async function refreshToken() {
    console.log("refresh Token pro");
    console.log(accessToken);
    const oldAccess = localStorage.getItem('jwtToken');
    const url = "http://localhost:8000/backend/token";
    const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': oldAccess,
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            console.log(response.status);
            alert("토큰 갱신에 실패했습니다. 다시 로그인 해주세요.");
            location.href = '/login';
            return false;
        }
        const data = await response.json();
        localStorage.setItem('jwtToken', data.accessToken);
        console.log(data.accessToken);
        accessToken = data.accessToken;
        return true;
}

function logoutPro() {
    const accessToken = localStorage.getItem('jwtToken');
    fetch('http://localhost:8000/backend/token', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': accessToken
        }
    });
    localStorage.clear();
    location.href = '/';
}

window.onload = function() {
    checkLoginStatus();
};

function checkLoginStatus() {
    const loginButton = document.getElementById('loginButton');
    const nickname = localStorage.getItem('nickname');

    if (nickname) {
        loginButton.textContent = '로그아웃';
        loginButton.setAttribute('onclick', 'logoutPro()');
    } else {
        loginButton.textContent = '로그인';
        loginButton.setAttribute('onclick', 'location.href="/login"');
    }
}

function validation(){
    if(username===null || userId===null){
        return false;
    }
    return true;
}

function unauthorized() {
    alert("로그인이 필요한 서비스 입니다.");
    if (confirm("로그인 페이지로 이동하겠습니까?")) {
        window.location.href = '/login';
    }
}

function scrollTop(){
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

let interval;

function startTimer() {
    let time = 300; // 5분 = 300초
    const timerElement = document.getElementById('timer');

    interval = setInterval(() => {
        const minutes = Math.floor(time / 60);
        const seconds = time % 60;
        timerElement.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        time--;

        if (time < 0) {
            clearInterval(interval);
            alert('인증 시간이 만료되었습니다.');
            closeEmailVerifyModal();
        }
    }, 1000);
}


/* handle window.history */
function saveState(currentContent) {
        // 이전 상태와 비교하여 중복 저장 방지
        const lastState = window.history.state;

        if (!lastState || lastState.content !== currentContent) {

            window.history.pushState({ content: currentContent }, '', '');  // 히스토리 스택에 상태 저장
        }
    }
   window.addEventListener('DOMContentLoaded', () => {
            const initialContent = document.getElementById('viewBox').innerHTML;
            // 초기 상태를 히스토리에 저장 (새 기록을 추가하지 않음)
            window.history.replaceState({ content: initialContent }, '', '');
        });

window.addEventListener('popstate', (event) => {
            if (event.state && event.state.content) {
                console.log(event.state);
                document.getElementById('viewBox').innerHTML = event.state.content;
            } else {
               window.history.go(-1);
            }
});

function showToast(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerText = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000); // 3초 후 사라짐
}

function notFound(message){
    const viewBox = document.getElementById("viewBox");
    viewBox.innerHTML ='';
    const div = document.createElement("div");
    div.classList.add("not-found");
    div.innerText = message;
    viewBox.append(div);
}