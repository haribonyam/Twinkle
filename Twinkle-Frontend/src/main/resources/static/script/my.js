
 async function showMyInfo(){
        currentActiveTab = 'infoTab';
        const url ="http://localhost:8000/backend/user/"+username;
        console.log(url);
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(url,{
            headers:{
                'Authorization' : token
            }
        });

        if(response.status == 401){
            refreshToken();
            showMyInfo();
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
        const viewBox = document.getElementById("viewBox");
        viewBox.innerHTML ='';
        viewBox.innerHTML +=`
        <div class="my-topbar">
                    <div id="infoTab" class="tab-button active" onclick="switchTab('info')"><img src="/icon/category/내정보_60x32.png"></div>
                    <div id="tradeTab" class="tab-button" onclick="switchTab('trade')"><img src="/icon/category/마켓_60x32.png"></div>
                    <div id="snsTab" class="tab-button" onclick="switchTab('sns')"><img src="/icon/category/SNS_60x32.png"></div>
                    <div id="payTab" class="tab-button" onclick="switchTab('pay')"><img src="/icon/category/반짝페이_60x32.png"></div>
        </div>
        <div class="my-content" id="myContent">
             <div id="infoSection" class="section">
                <h2>내 정보</h2>
                <ul class="info-list">
                    <li><span class="info-title">아이디</span><span id="userId">${data.username}</span></li>
                    <li><span class="info-title">닉네임</span><span id="nickname">${data.nickname}</span></li>
                    <li><span class="info-title">이메일</span><span id="email">${data.email}</span></li>
                </ul>
                <div class="info-modify-button" onclick="openEmailVerificationModal()">내 정보 수정</div>
             </div>
        </div>
        <input type="hidden" id="code">
        `
        return;
    }


async function openEmailVerificationModal() {
    let email = null;
    if(document.getElementById("email")){
      email = document.getElementById("email").innerText;
    }
    if (confirm("이메일 인증 후 수정 가능합니다. 인증을 진행하시겠습니까?")) {
        try {
            const response = await fetch('http://localhost:8080/mailConfirm', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': accessToken
                },
                body: JSON.stringify({ 'email': email }) // 이메일 정보 전송
            });

            if (!response.ok) {
                throw new Error('이메일 전송 실패');
            }
            const code = await response.json();
            alert('인증 메일이 전송되었습니다.');
            createEmailVerifyModal(code.code); // 모달 창 생성
        } catch (error) {
            alert(error.message);
        }
    }
}
function createEmailVerifyModal(code) {
    const modal = document.createElement('div');
    modal.id = 'emailVerifyModal';

    modal.innerHTML = `
        <h2>이메일 인증</h2>
        <p>인증 코드를 입력해주세요.</p>
        <input type="text" id="emailVerificationCode" placeholder="인증 코드" />
        <div id="timer">05:00</div>
        <button onclick="submitVerificationCode()">확인</button>
        <button onclick="closeEmailVerifyModal()">닫기</button>
    `;
    document.body.appendChild(modal);
    startTimer();
    const codeInput = document.getElementById("code");
    codeInput.value = code;
}
function submitVerificationCode(){
    const code = document.getElementById("code").value;
    const input = document.getElementById("emailVerificationCode").value;
    if(input === code){
       closeEmailVerifyModal();
    }else{
       alert("인증코드가 일치하지 않습니다.");
    }

}

function closeEmailVerifyModal() {
    clearInterval(interval);
    const modal = document.getElementById('emailVerifyModal');
    if (modal) {
        document.body.removeChild(modal);
    }
}


function openModifyModal(){

}

function closeModifyModal(){

}

async function switchTab(tab) {
        const infoTab = document.getElementById('infoTab');
        const tradeTab = document.getElementById('tradeTab');
        const snsTab = document.getElementById('snsTab');
        const payTab = document.getElementById('payTab');
        const myContent = document.getElementById("myContent");
        const active = document.getElementById(currentActiveTab);
        active.classList.remove('active');
        myContent.innerHTML =``;
           switch(tab) {
               case 'info':
                   infoTab.classList.add('active');
                   showMyInfo();
                   currentActiveTab = 'infoTab';
                   break;
               case 'trade':
                   tradeTab.classList.add('active');
                   myContent.innerHTML =
                   `
                        <div id="postsSection" class="section">
                           <h2>중고거래 게시물</h2>
                           <div class="posts-container">
                               <ul class="post-list" id="tradePosts">
                               </ul>
                           </div>
                        </div>
                   `;

                   await myTradeContent();
                   currentActiveTab = 'tradeTab';
                   break;
               case 'sns':
                   snsTab.classList.add('active');
                   myContent.innerHTML =
                                      `
                                           <div id="postsSection" class="section">
                                              <h2>SNS 게시물</h2>
                                              <div class="posts-container">
                                                  <ul class="post-list" id="tradePosts">
                                                  </ul>
                                              </div>
                                           </div>
                                      `;
                   await mySnsContent('');
                   currentActiveTab = 'snsTab';
                   break;
               case 'pay':
                   payTab.classList.add('active');
                   myContent.innerHTML += await myPayContent(); // 예시
                   currentActiveTab = 'payTab';
                   break;
               default:
                   console.error("Unknown tab:", tab);
                   return;
           }

//        myContent.innerHTML=``;
//        if (tab === 'info') {
//         const my = await getMyInfo();
//            myContent.innerHTML += `
//             <div id="infoSection" class="section">
//                            <h2>내 정보</h2>
//                            <ul class="info-list">
//                                <li><span class="info-title">아이디</span><span id="userId">${my.username}</span></li>
//                                <li><span class="info-title">닉네임</span><span id="nickname">${my.nickname}</span></li>
//                                <li><span class="info-title">이메일</span><span id="email">${my.email}</span></li>
//                                <li><span class="info-title">보유 페이머니</span><span id="payMoney">10000원</span>
//                                    <div class="pay-action-button" onclick="chargeMoney()">충전하기</div>
//                                    <div class="pay-action-button" onclick="withdrawMoney()">환전하기</div>
//                                </li>
//                            </ul>
//                            <div class="info-modify-button" onclick="openVerificationModal()">내 정보 수정</div>
//                         </div>
//            `
//            infoTab.classList.add('active');
//            postsTab.classList.remove('active');
//        } else {
//           myContent.innerHTML += `
//            <div id="postsSection" class="section">
//                           <h2>내 게시물</h2>
//                           <div class="posts-container">
//                               <h3>SNS 게시물</h3>
//                               <ul class="post-list" id="snsPosts">
//                                   <li></li>
//                               </ul>
//                               <h3>중고거래 게시물</h3>
//                               <ul class="post-list" id="tradePosts">
//                                   <li>게시물 1</li>
//                               </ul>
//                           </div>
//                        </div>
//           `
//            infoTab.classList.remove('active');
//            postsTab.classList.add('active');
//        }
    }


async function myInfoPopulation(){
        const nickname = localStorage.getItem("nickname");
        const userId = localStorage.getItem("id");
        if(!nickname || !userId){
            alert("로그인이 필요한 서비스 입니다.")
            location.href="/login";
        }
        const url ="http://localhost:8000/backend/user/"+nickname;
        console.log(url);
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(url,{
            headers:{
                'Authorization' : token
            }
        });

        if(response.status == 401){
            refreshToken();
            showMyInfo();
            return;
        }
        if(!response.ok){
            alert("서버의 문제가 발생했습니다. 잠시후 다시 시도하세요.");
            return;
        }
        //makeMyInfo(await response.json());
        return await response.json();
    }

 async function myTradeContent(){
    const url = "http://localhost:8000/backend/tradeboard/list/"+username;

    const response = await fetch(url,{
        headers :{
            "Authorization" : accessToken
        }
    });
    const data = await response.json();
    console.log(data);
    if(response.status === 401){
        console.log("token expired");
        refreshToken();
        myTradePopulation();
    }else if(!response.ok){
        return showToast("내부서버에 문제가 발생했습니다.");
    }else if(data.length === 0){
        return myContentNotFound("작성한 중고거래 게시물이 없습니다.");
    }

    const tradePosts = document.getElementById("tradePosts");
    data.forEach(post => {
    const li = `<li onclick="showTradeBoardInfo(${post.id})">${post.title}</li>`;
    tradePosts.innerHTML+=li;
    })

    return;
 }

  async function mySnsContent(pageNum) {
             const param = new URLSearchParams();
             if (pageNum !== undefined && pageNum !== '') {
                 param.append("page", pageNum);
                 param.append("size", 12);
             }
             param.append("memberId",userId);
             const url = `http://localhost:8000/sns/posts?${param.toString()}`;

                 const response = await fetch(url);
                 if (response.status === 401) {
                     await refreshToken();
                     snsBoardList(category, pageNum);
                 }
                 const data = await response.json();
                 if (!response.ok) {
                    return showToast("내부서버에 문제가 발생했습니다.");
                 }else if(data.content.length === 0){
                    return myContentNotFound("작성한 SNS 게시물이 없습니다.");
                 }

                       const tradePosts = document.getElementById("tradePosts");

                       data.content.forEach(post => {
                       const li = `<li onclick="showSnsBoardInfo(${post.id})">${post.title}</li>`;
                       tradePosts.innerHTML+=li;
                       })



      return;

  }


async function myPayContent(){

   const url = "http://localhost:8000/pay/payment/"+userId;

   const response = await fetch(url,{
    headers : {
      "Authorization" : accessToken
    }
   });
   const data = await response.json();
   console.log(data);
   if(response.status === 401){
    refreshToken();
    myPayContent();
   }else if(!response.ok){
     return payContentNotFound();
   }else{
   const content  =`
      <div id="infoSection" class="section">
                      <h2>반짝페이</h2>
                      <ul class="info-list">
                          <li><span class="info-title">보유 페이머니</span><span id="payMoney">${data.payMoney.toLocaleString()} 원</span></li>
                      </ul>
                      <div class="pay-button-container">
                      <div class="info-modify-button" onclick="openChargeModal()">충전하기</div>
                      <div class="info-modify-button" onclick="withdrawMoney()">환전하기</div>
                      </div>
                   </div>
      `
    return content;
   }

}

function myContentNotFound(message){
       console.log(message);
       const myContent = document.getElementById("myContent");
       myContent.innerHTML = "";
       const div = document.createElement("div");
       div.classList.add("not-found");
       div.innerText = message;
       myContent.append(div);
}
function payContentNotFound(){
      const content  =`
         <div id="infoSection" class="section">
                         <h2>반짝페이</h2>
                         <ul class="info-list">
                             <li><span class="info-title">보유 페이머니</span><span id="payMoney"> 0 원</span></li>
                         </ul>
                         <div class="pay-button-container">
                         <div class="info-modify-button" onclick="openChargeModal()">충전하기</div>
                         <div class="info-modify-button" onclick="withdrawMoney()">환전하기</div>
                         </div>
                      </div>
         `
}