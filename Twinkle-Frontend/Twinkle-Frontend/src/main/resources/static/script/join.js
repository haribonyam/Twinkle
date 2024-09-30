var code = null;

function joinPro() {
    // 폼 데이터를 가져오기
    let formData = {
        username: document.getElementById("username").value,
        nickname: document.getElementById("nickname").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value
    };

    // 서버로 데이터를 전송
    fetch('http://localhost:8080/api/user/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('회원가입이 완료되었습니다.');
        location.href="/";
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('회원가입 중 오류가 발생했습니다.');
    });
}

function joinCheck(type){
 const baseUri = "http://localhost:8080/api/user/check/" + type;
 const param = document.getElementById(type).value;
 const params = new URLSearchParams();
 params.append(type,param);

 var messages={
    username:"아이디",
    nickname:"닉네임",
    email:"이메일"
 };
 var message = messages[type];

 if(param==""){
    alert(message +"를 입력해 주세요.");
    return;
 }

     fetch(`${baseUri}?${params.toString()}`)
     .then(response=>{
         if(response.ok){
             alert("사용 가능한 "+message+" 입니다.");
             if(type==="email"){
               sendEmail(param);
             }

         }else{
             alert("중복된 "+message+" 입니다.");
         }
     })
     .catch(error=>{
         alert(message+" 중복확인 도중 오류가 발생하였습니다.");
     });

 }

var code = null;

function sendEmail(email) {
    const url = "http://localhost:8080/mailConfirm";
    const body = JSON.stringify({
        "email": email
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: body
    })
    .then(async response => {
        if (!response.ok) {
            alert("네트워크 문제로 메일 전송에 실패했습니다.123123");
            return;
        }
        const data = await response.json();
        alert("입력하신 메일로 인증코드를 전송했습니다.");
        code = data.code;
        createEmailConfirm();
        console.log(code);
    })
    .catch(error => {
        console.error('Error:', error);
        alert("네트워크 문제로 메일 전송에 실패했습니다.");
    });
}


function emailConfirmPro() {
    const input = document.getElementById("emailConfirm").value;
    if (code === input) {
        alert("이메일 인증에 성공했습니다.");
    } else {
        if (confirm("이메일 인증에 실패했습니다. 코드를 재전송 하시겠습니까?")) {
            sendEmail(document.getElementById("email").value);
        }
    }
}

function createEmailConfirm() {
    const container = document.getElementById("joinContainer");
    const body = `
        <li>
            <div>인증코드</div>
            <input type="text" id="emailConfirm">
            <span class="join-check" onclick="emailConfirmPro()">인증</span>
        </li>`;
    container.innerHTML += body;
    return;
}
