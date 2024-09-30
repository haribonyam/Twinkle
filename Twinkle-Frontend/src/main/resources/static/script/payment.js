/*페이머니 충전 모달 열기*/
function openChargeModal(){
        const viewBox = document.getElementById("viewBox");

        viewBox.innerHTML +=`
            <div class="modal-overlay" id="modalOverlay">
                <div class="charge-modal" id="chargeModal">
                    <div class="close-charge-modal" id="closeButton" onclick="closeChargeModal()"></div>
                    <p class="charge-title" id="paymentType">반짝페이 충전</p>
                    <div class="charge-modal-item">
                        <input type="text" placeholder="충전금액 입력" id="chargeMoney">
                    </div>
                    <div class="charge-modal-item" onclick="userPayValidation('charge')">
                        <img class="kakao-pay" src="/img/payment_icon_yellow_small.png" alt="카카오페이 결제">
                    </div>
                </div>
            </div>
        `
}
/*페이머니 충전 모달 닫기*/
function closeChargeModal() {
        const chargeModal = document.getElementById('modalOverlay');
        if (chargeModal) {
            chargeModal.remove(); // 모달 HTML을 DOM에서 제거
        }
}
/* 페이머니 결제,충전시 사용자 검증 */
async function userPayValidation(navigator,sellerId){
        const userId = localStorage.getItem("id");
        const accessToken = localStorage.getItem("jwtToken");

        const url = "http://localhost:8000/backend/user/check/"+userId;
        const response = await fetch(url,{
        headers:{
        "Authorization" : accessToken,
        "Content-Type":"application/json"
        }
        });
        if(!response.ok){
            alert("로그인 정보가 일치하지 않습니다.");
            closePayModal(navigator);
            return;
        }else{
        const data = await response.text();
        console.log(data);
       if(confirm(data+"님 결제를 진행하시겠습니까?")){
            if(navigator ==="charge"){
            await payments(userId);
            }else if(navigator === "buy"){
            await buyItem(sellerId);
            }
        }
        }
 }
/* 사용자 검증 후 실제 결제 프로세스(아임포트(포트원)으로 진행) */
async function payments(userId) {
        const paymentTypeElement = document.getElementById("paymentType");
        const payMoneyElement = document.getElementById("chargeMoney");

            if (!paymentTypeElement || !payMoneyElement) {
                console.error('필수 HTML 요소가 누락되었습니다.');
                return;
            }
            console.log(payMoneyElement.value);
            const paymentType = paymentTypeElement.textContent.trim();
            const payMoney = parseFloat(payMoneyElement.value.trim());


        IMP.init("imp80340313"); // Iamport의 공용 API 키로 초기화

        IMP.request_pay({
            pg: "kakaopay", // 결제 게이트웨이
            pay_method: "kakaopay", // 결제 방법
            merchant_uid: `payment-${crypto.randomUUID()}`, // 주문 고유 번호
            name: paymentType, // 상품명
            amount: payMoney, // 결제 금액
            buyer_email: "", // 구매자 이메일
            buyer_name: `${userId}`, // 구매자 이름
            buyer_tel: "", // 구매자 전화번호
            buyer_addr: "", // 구매자 주소
            buyer_postcode: "", // 구매자 우편번호
        }, async function (res) {
            if (res.success) {
                const url = `http://localhost:8000/pay/payment/validation/${res.imp_uid}`;
                await paymentValidation(url, res); // await 추가 및 res를 매개변수로 전달
            } else {
                console.log(res.error_msg);
                if(confirm('결제 실패')){
                    closeChargeModal();
                }else{
                    closeChargeModal();
                }
            }
        });
}

/* 결제 성공 실패 여부 검증 */
async function paymentValidation(url, res) {
        console.log("paymentValidation : "+url);
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            const data = await response.json();
            console.log(data);
            await chargeMoney(data);
        } catch (error) {
            console.log(error);
            };

        }


/* 결제 검증 여부 확인 후 서버에 충전 내용 저장 */
async function chargeMoney(data) {
    const userId = data.response.buyerName;
    const payMoney = data.response.amount;
    const couponMoney = data.response.couponMoney || 0;
    const key = data.response.merchantUid + payMoney + userId;

    const url = "http://localhost:8000/pay/payment/charge";


    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json", // 요청 본문을 JSON으로 처리하도록 명시
        },
        body: JSON.stringify({ // 요청 본문을 JSON 문자열로 변환
            memberId: userId,
            payMoney: payMoney,
            couponMoney: couponMoney
        }),
    });

    if (!response.ok) {
        // 오류 처리
            console.error('충전 실패:', response.statusText);
            showToast("충전이 실패했습니다.");
            closeChargeModal();
    }else{
    const responseData = await response.json(); // JSON 응답을 파싱
    console.log(responseData);
        showToast("충전이 완료되었습니다.");
        const myPayMoney = document.getElementById("myPayMoney");
        if(myPayMoney){
           const text = document.getElementById("payMoneyText");
           myPayMoney.value=Number(myPayMoney.value)+Number(responseData.payMoney);
           text.innerText = "나의 페이머니 : "+myPayMoney.value;

        }else{
            switchTab('pay');
            closeChargeModal();
        }
        closeChargeModal();
  }
}

/* 페이머니로 상품결제 진행 */
async function buyItem(id){
        const buyerId = localStorage.getItem("id");
        const sellerId = id;
        const tradeBoardId = document.getElementById("tradeBoardId").value;
        const myMoney = document.getElementById("myPayMoney").value;
        const price = document.getElementById("itemPrice").value;
        const url = "http://localhost:8083/payment/trade";

    //POST 요청 시 body에 JSON 데이터를 포함하고, Content-Type을 설정
    const response = await fetch(url, {
        method: "POST", // POST 요청으로 명시
        headers: {
            "Content-Type": "application/json", // 요청 본문을 JSON으로 처리하도록 명시
        },
        body: JSON.stringify({ // 요청 본문을 JSON 문자열로 변환
            memberId: buyerId,
            payMoney: price,
            sellerId : sellerId,
            tradeBoardId :tradeBoardId
        }),
    });
    if (!response.ok) {
        // 오류 처리
        const responseData = await response.json();
        if(responseData.errorCodeName === '019'){
          if(confirm(responseData.message + " 충전하시겠습니까?")){
               openChargeModal();
           }else{
                alert("걸제가 취소 되었습니다.");
                closePaymentModal();
           }
        }
        return;
    }

    const responseData = await response.json(); // JSON 응답을 파싱
    alert('상품결제가 완료되었습니다.');
    closePaymentModal();
    const roomId = document.getElementById("roomId").value;
    enterRoom(roomId);
}


/*반짝페이 모달*/

async function openPaymentModal(price,sellerId,id){
        const myPayMoney = await getPayMoney();

        const viewBox = document.getElementById("viewBox");

        viewBox.innerHTML +=`
            <input type="hidden" id="tradeBoardId" value="${id}">
            <input type="hidden" id="itemPrice" value="${price}">
            <input type="hidden" id="myPayMoney" value="${myPayMoney}">
            <div class="modal-overlay" id="paymentModalOverlay">
                <div class="payment-modal" id="paymentModal">
                    <div class="close-payment-modal" id="closeButton" onclick="closePaymentModal()"></div>
                    <p class="payment-title" id="paymentType">반짝페이 결제</p>
                    <div id="payMoneyText">나의 페이머니 :${myPayMoney}원</div>
                    <div class="payment-modal-item" onclick="userPayValidation('buy',${sellerId})">
                        결제하기
                    </div>
                </div>
            </div>
        `
}
/*페이머니 충전 모달 닫기*/
function closePaymentModal() {
        const paymentModal = document.getElementById('paymentModalOverlay');
        if (paymentModal) {
            paymentModal.remove(); // 모달 HTML을 DOM에서 제거
        }
}
function closePayModal(navigator){
    if(navigator === 'buy'){
        closePaymentModal();
    }else{
        closeChargeModal();
    }
}

async function getPayMoney() {
    const url = `http://localhost:8000/pay/payment/${userId}`;

        const response = await fetch(url);
        const data = await response.json();
        if (response.ok) {
            console.log(data);
        } else {
            if(response.status == 500){
                console.log("내부서버에 문제가 발생했습니다!");
                return;
            }
            else{
                return 0;
            }
        }
        return data.payMoney;
 }