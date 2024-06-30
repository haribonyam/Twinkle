
function joinPro() {
        // 폼 데이터를 가져오기
        let formData = {
            username: document.getElementById("username").value,
            nickname: document.getElementById("nickname").value,
            password: document.getElementById("password").value,
            email: document.getElementById("email").value
        };

        // 서버로 데이터를 전송
        fetch('localhost:8080/api/user/save', {
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
            // 여기에 원하는 리다이렉션 또는 다음 동작을 추가할 수 있습니다.
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('회원가입 중 오류가 발생했습니다.');
        });
    }


