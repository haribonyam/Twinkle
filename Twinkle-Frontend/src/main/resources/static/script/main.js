
  async function showMyInfo(){
        const nickname = localStorage.getItem("nickname");
        if(!nickname){alert("로그인이 필요한 서비스 입니다.")
            location.href="/login";
        }
        const url ="http://localhost:8080/api/user/"+nickname;
        console.log(url);
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(url,{
            method:'GET',
            headers:{
                'Authorization' : token
            }
        });

        if(response.status == 401){
            refreshToken();
            my();
            return;
        }
        if(!response.ok){
            alert("서버의 문제가 발생했습니다. 잠시후 다시 시도하세요.");
            return;
        }
        makeMyInfo(await response.json());
    }
    function makeMyInfo(data){
        console.log(data);
        return;
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
