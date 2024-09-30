// 'Enter' 키 입력 시 로그인 시도
document.getElementById('password').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        loginValidation();
    }
});

document.getElementById('username').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        loginValidation();
    }
});

async function loginValidation() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    if (username === "") {
        alert("아이디를 입력하세요");
        return;
    }
    if (password === "") {
        alert("패스워드를 입력하세요");
        return;
    }
    await loginPro(username, password);
}

async function loginPro(username, password) {
    const formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);
    const url = "http://localhost:8000/backend/login";

    try {
        const response = await fetch(url, {
            method: 'POST',
            body: formData
        });

        if (response.status === 401) {
            alert('아이디 혹은 비밀번호를 확인해 주세요.');
            return;
        }

        if (!response.ok) {
            alert('네트워크에 오류가 발생했습니다. 잠시후 다시 시도해 주세요.11');
            return;
        }

        const token = response.headers.get('Authorization');
        const nickname = response.headers.get('Nickname');
        const id = response.headers.get('Id');
        console.log(token);
        console.log(nickname);
        console.log(id);
        if (token) {
            localStorage.setItem('jwtToken', token);
            localStorage.setItem('nickname', nickname);
            localStorage.setItem('id', id);
            alert('로그인이 완료되었습니다.');
            window.location.href = "/";
        } else {
            alert('네트워크에 문제가 발생했습니다. 잠시후 다시 시도해주세요.22');
        }
    } catch (error) {
        console.error('로그인 중 오류 발생:', error);
        alert('로그인 중 오류가 발생했습니다. 잠시후 다시 시도해 주세요.');
    }
}
