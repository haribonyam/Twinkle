/*페이머니 충전 모달 열기*/
function openChargeModal(){
        const viewBox = document.getElementById("viewBox");

        viewBox.innerHTML +=`
            <div class="modal-overlay" id="modalOverlay">
                <div class="charge-modal" id="chargeModal">
                    <div class="close-charge-modal" id="closeButton" onclick="closeModal()"></div>
                    <p class="charge-title" id="paymentType">반짝페이 충전</p>
                    <div class="charge-modal-item">
                        <input type="text" placeholder="충전금액 입력" id="payMoney">
                    </div>
                    <div class="charge-modal-item" onclick="userPayValidation()">
                        <img class="kakao-pay" src="/img/payment_icon_yellow_small.png" alt="카카오페이 결제">
                    </div>
                </div>
            </div>
        `
}
/*페이머니 충전 모달 닫기*/
function closeModal() {
        const chargeModal = document.getElementById('modalOverlay');
        if (chargeModal) {
            chargeModal.remove(); // 모달 HTML을 DOM에서 제거
        }
}
/* 페이머니 결제,충전시 사용자 검증 */
async function userPayValidation(){
        const userId = localStorage.getItem("id");
        const accessToken = localStorage.getItem("jwtToken");

        const url = "http://localhost:8080/api/user/check/"+userId;
        const response = await fetch(url,{
        headers:{
        "Authorization" : accessToken,
        "Content-Type":"application/json"
        }
        });
        if(!response.ok){
            if(confirm("로그인 정보가 일치하지 않습니다.")){
                closeModal();
            }else{
                closeModal();
            }
        }else{
        const data = await response.text();
        if(confirm(data+"님 결제를 진행하시겠습니까?")){
            await payments(userId);
        }
        }
    }
/* 사용자 검증 후 실제 결제 프로세스(아임포트(포트원)으로 진행) */
async function payments(userId) {
        const paymentTypeElement = document.getElementById("paymentType");
        const payMoneyElement = document.getElementById("payMoney");

            if (!paymentTypeElement || !payMoneyElement) {
                console.error('필수 HTML 요소가 누락되었습니다.');
                return;
            }

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
                const url = `http://localhost:8083/payment/validation/${res.imp_uid}`;
                await validation(url, res); // await 추가 및 res를 매개변수로 전달
            } else {
                console.log(res.error_msg);
                if(confirm('결제 실패')){
                    closeModal();
                }else{
                    closeModal();
                }
            }
        });
}

/* 결제 성공 실패 여부 검증 */
async function validation(url, res) {
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            const data = await response.json();
            await chargeMoney(data);
        } catch (error) {
            console.log(error);
            if(confirm("결제서버에 문제가 발생했습니다. 잠시후 다시 시도해주세요.")){
                closeModal();
            }else{
                closeModal();
            }
            };

        }


/* 결제 검증 여부 확인 후 서버에 충전 내용 저장 */
async function chargeMoney(data) {
    const userId = data.response.buyerName; // buyerName에서 userId를 가져옴
    const payMoney = data.response.amount; // 결제 금액
    const couponMoney = data.response.couponMoney || 0; // couponMoney가 있으면 가져오고, 없으면 0으로 설정
    const key = data.response.merchantUid + payMoney + userId;

    console.log(userId);
    console.log(key.replaceAll("-", ""));
    console.log(data);

    const url = "http://localhost:8083/payment/charge";

    // POST 요청 시 body에 JSON 데이터를 포함하고, Content-Type을 설정
    const response = await fetch(url, {
        method: "POST", // POST 요청으로 명시
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
        console.error('결제 충전 실패:', response.statusText);
        if(confirm('결제 충전에 실패했습니다.')){
            closeModal();
        }else{
            closeModal();
        };
        return;
    }

    const responseData = await response.json(); // JSON 응답을 파싱
    console.log(responseData);
    if(confirm('결제 충전이 완료되었습니다.')){
        closeModal();
    }else{
        closeModal();
    }

}

/* 페이머니로 상품결제 진행 */
async function buyItem(){
        const buyerId = localStorage.getItem("id");
        const sellerId = "1"
        const payMoney = "3000";

        const url = "http://localhost:8083/payment/trade";

    // POST 요청 시 body에 JSON 데이터를 포함하고, Content-Type을 설정
    const response = await fetch(url, {
        method: "POST", // POST 요청으로 명시
        headers: {
            "Content-Type": "application/json", // 요청 본문을 JSON으로 처리하도록 명시
        },
        body: JSON.stringify({ // 요청 본문을 JSON 문자열로 변환
            memberId: buyerId,
            payMoney: payMoney,
            sellerId : sellerId
        }),
    });

    if (!response.ok) {
        // 오류 처리
        console.error('상품 구매 실패:', response.statusText);
        alert('상품구매가 실패했습니다.');
        return;
    }

    const responseData = await response.json(); // JSON 응답을 파싱
    console.log(responseData);
    alert('상품결제가 완료되었습니다.');

}