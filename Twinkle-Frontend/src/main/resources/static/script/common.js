let isTradeBoardLoaded = false;
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

function navigateToPage(page) {
    init();
    switch (page) {
        case 'home':
            window.location.href = "/";
            break;
        case 'tradeboard':
            showTradeBoardList('', 0);
            break;
        case 'sns':
            showSNSBoard();
            break;
        case 'my':
            showMyInfo();
            break;
        case 'chat':
            showChatList();
            break;
        default:
            console.error('Unknown page:', page);
            break;
    }
}

function init() {
    isTradeBoardLoaded = false;
    isLoading = false;
    isLastPage = false;
    roomId = null;
    tradeBoardId = null;
    senderTemp = null;
    if (stompClient!==null) {
        disconnect();
    }
    retryCount=0;
}

async function refreshToken() {
    const oldAccess = localStorage.getItem('jwtToken');
    const url = "http://localhost:8080/token/refresh";

    try {
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
            return;
        }
        const data = await response.json();
        localStorage.setItem('jwtToken', data.accessToken);
        console.log("토큰 갱신 성공:", data.accessToken);
    } catch (error) {
        console.error('Error:', error);
        alert("토큰 갱신 중 오류가 발생했습니다.");
        return;
    }
}

function logoutPro() {
    const accessToken = localStorage.getItem('jwtToken');
    fetch('http://localhost:8080/token', {
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