function createTradeBoardWrite(){
    init();
    scrollTop();
    const viewBox = document.getElementById("viewBox");

    viewBox.innerHTML='';

    const tradeBoardWriteItem = `
    <div class="tradebaord-write-container">
                <h1>중고거래 게시물 작성</h1>
                    <div class="tradeboard-form-group">
                        <label for="title">제목</label><br></br>
                        <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required class="tradeboard-title">
                    </div>
                    <div class="tradeboard-form-group">
                        <label for="category">카테고리</label><br></br>
                        <select id="category" name="category" required class="tradeboard-category">
                            <option value="" disabled selected>카테고리를 선택하세요</option>
                            <option value="향수">향수</option>
                            <option value="화장품">화장품</option>
                            <option value="미용기기">미용기기</option>
                            <option value="기타">기타</option>
                        </select>
                    </div>
                    <div class="tradeboard-form-group">
                        <label for="price">가격</label><br></br>
                        <input type="text" id="price" name="price" class="tradeboard-price" placeholder="가격을 입력하세요" required>
                    </div>
                    <div class="tradeboard-form-group">
                        <label for="files">이미지 업로드</label><br></br>
                        <div class="image-upload-container">
                            <div class="image-preview" id="imagePreview">
                                <div class="upload-placeholder" onclick="document.getElementById('fileInput').click();">
                                    <span>이미지를 클릭하여 업로드하세요</span>
                                </div>
                            </div>
                            <input type="file" id="fileInput" name="files" accept="image/*" multiple style="display: none;" onchange="handleFiles(this.files)">
                        </div>
                        <div class="image-count" id="imageCount">0/5</div>
                    </div>
                    <div class="tradeboard-form-group">
                        <label for="content">내용</label><br></br>
                        <textarea id="content" name="content" rows="6" placeholder="내용을 입력하세요" required class="tradeboard-content"></textarea>
                    </div>
                    <div class="tradeboard-button" onclick="tradeboardWrite()">게시물 작성</div>
            </div>
    `;
    viewBox.innerHTML +=tradeBoardWriteItem;
}

let uploadedImages = []; // 업로드된 이미지 파일을 저장할 배열

function handleFiles(files) {
    const imagePreview = document.getElementById('imagePreview');
    const imageCount = document.getElementById('imageCount');

    // 선택된 파일을 배열로 변환하여 업로드된 이미지 배열에 추가
    Array.from(files).forEach(file => {
        if (uploadedImages.length < 5) {
            uploadedImages.push(file);
        }
    });

    // 미리보기 초기화
    imagePreview.innerHTML = '';

    const fileReaders = uploadedImages.map(file => {
        return new Promise((resolve) => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const div = document.createElement('div');
                div.classList.add('upload-placeholder');
                const img = document.createElement('img');
                img.src = e.target.result;
                img.classList.add('preview-img');
                div.appendChild(img);
                imagePreview.appendChild(div);
                resolve();
            };
            reader.readAsDataURL(file);
        });
    });

    Promise.all(fileReaders).then(() => {
        if (uploadedImages.length < 5) {
            const uploadPlaceholder = document.createElement('div');
            uploadPlaceholder.classList.add('upload-placeholder');
            uploadPlaceholder.onclick = () => document.getElementById('fileInput').click();
            uploadPlaceholder.innerHTML = '<span>이미지를 클릭하여 업로드하세요</span>';
            imagePreview.appendChild(uploadPlaceholder);
        }
            imageCount.textContent = `${uploadedImages.length}/5`;
    });
}

async function tradeboardWrite() {
    // 입력된 값 가져오기
    scrollTop();
    const title = document.getElementById('title').value;
    const category = document.getElementById('category').value;
    const price = document.getElementById('price').value;
    const content = document.getElementById('content').value;

    // FormData 객체 생성
    const formData = new FormData();

    // 게시물 정보를 객체로 생성
    const tradeBoardRequestDto = {
        title: title,
        category: category,
        price: price,
        content: content,
        nickname:username
    };

    // 객체를 JSON 문자열로 변환하여 FormData에 추가
    formData.append('tradeBoardRequestDto', new Blob([JSON.stringify(tradeBoardRequestDto)], { type: 'application/json' }));

    // 이미지 파일이 있는 경우 FormData에 추가
    uploadedImages.forEach(file => {
          formData.append('files', file);
      });

    const url = "http://localhost:8000/backend/tradeboard/save";

    try {
        // 서버로 POST 요청
        const response = await fetch(url, {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json',
                'Authorization': accessToken  // 토큰이 필요한 경우 사용
            }
        });

        // 응답 처리
        const data = await response.json();  // 응답 데이터를 JSON으로 파싱

        if (!response.ok) {
            console.error("오류 응답:", data);
        } else {
          await showTradeBoardInfo(data);
        }
    } catch (error) {
        console.error("서버에서 오류 발생:", error);
    }
}





function showTradeBoardList() {
scrollTop();
            isShowTradeBoard=true;
            isLastPage=false;
            const viewBox = document.getElementById('viewBox');
            viewBox.innerHTML = `
                <input type="hidden" id="page" value="0">
                <input type="hidden" id="category">
                <div class="tradeboard-bar">
                    <div>
                        <span onclick="setCategory('')"><img src="/icon/category/전체_60x32.png"></span>
                        <span onclick="setCategory('화장품')"><img src="/icon/category/화장품_60x32.png"></span>
                        <span onclick="setCategory('향수')"><img src="/icon/category/향수_60x32.png"></span>
                        <span onclick="setCategory('미용기기')"><img src="/icon/category/미용기기_60x32.png"></span>
                    </div>
                    <div onclick="createTradeBoardWrite()"><img src="/icon/category/글쓰기_60x32.png"></div>
                </div>
                <div id="grid-container" class="grid-container"></div>
            `;

            tradeBoardList('', 0);
            addScrollEvent();
        }

        function setCategory(category) {
            isLastPage=false;
            document.getElementById('category').value = category;
            tradeBoardList(category, 0);
        }

        async function tradeBoardList(category, pageNum) {
            const param = new URLSearchParams();
            if (pageNum !== undefined && pageNum !== '') {
                param.append("page", pageNum);
                param.append("size", 12);
            }
            if (category !== undefined && category !== '') {
                param.append("category", category);
            }
            const url = `http://localhost:8000/backend/tradeboard/list?${param.toString()}`;
            try {
                const response = await fetch(url, {
                    headers: {
                        'Authorization': localStorage.getItem('jwtToken')
                    }
                });
                if (response.status === 401) {
                    await refreshToken();
                    tradeBoardList(category, pageNum);
                    return;
                }
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                createTradeBoardItem(data, pageNum + 1);
            } catch (error) {
                console.error('Error:', error);
            }
        }

       function createTradeBoardItem(data, pageNum) {
           const viewBox = document.getElementById("viewBox");
           const gridContainer = document.querySelector(".grid-container");

           if (pageNum === 1) {
               gridContainer.innerHTML = '';
           }

           data.content.forEach(content => {
               const gridItem = document.createElement("div");
               gridItem.className = "grid-item";
               gridItem.setAttribute('onclick', `showTradeBoardInfo(${content.id})`);

               const img = document.createElement("img");
               img.src = content.paths !== null && content.paths.length > 0 ? 'http://localhost:8080'+content.paths[0] : '';

               const itemInfo = document.createElement("div");
               itemInfo.className = "item-info";

               const date = dateDifference(content.createdDate);
               const info = `
                   <h2>${content.title}</h2>
                   <p class="condition">${content.condition}</p>
                   <p class="price">${content.price.toLocaleString()}원</p>
                   <p class="date">${date}</p>
               `;
               itemInfo.innerHTML = info;

               gridItem.appendChild(img);
               gridItem.appendChild(itemInfo);
               gridContainer.appendChild(gridItem);
           });

           const page = document.getElementById("page");

           if (pageNum === 1) {
               page.value = data.number + 1;
           } else if (pageNum > 1 && pageNum < data.totalPages) {
               page.value = data.number + 1;
           } else {
               const button = document.getElementById("load-more");
               if (button) button.style.display = "none";
               isLastPage = true;
           }
       }

        function addScrollEvent() {
            if (!isTradeBoardLoaded) {
                window.addEventListener('scroll', handleScroll);
                isTradeBoardLoaded = true;
            }
        }

        function removeScrollEvent() {
            if (isTradeBoardLoaded) {
                window.removeEventListener('scroll', handleScroll);
                isTradeBoardLoaded = false;
            }
        }

       function handleScroll() {
           if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 10 && isShowTradeBoard) {
               loadMore();
           }
       }

       function loadMore() {
           if (isLoading || isLastPage) return;

           isLoading = true;
           const category = document.getElementById("category").value;
           const page = document.getElementById("page").value;

           tradeBoardList(category, parseInt(page))
               .finally(() => {
                   isLoading = false;  // Set loading to false after request is completed
               });
       }

        /* tradeboard info*/
       async function showTradeBoardInfo(id) {
           removeScrollEvent();
           const url = "http://localhost:8000/backend/tradeboard/"+id;
           try{
           const response = await fetch(url,{
              headers:{
                "Authorization":localStorage.getItem("jwtToken")
              }
           });
           if(response.status === 401){
               await refreshToken();
               showTradeBoardInfo(id);
               return;
           }
          if (!response.ok) {
                 throw new Error(`HTTP error! status: ${response.status}`);
          }
          const data = await response.json();
          createTradeBoardInfo(data);
           }catch(error){
            console.log(error);
           }
       }
       function openModal(src) {
               const modal = document.getElementById('imageModal');
               const modalImage = document.getElementById('modalImage');
               modal.style.display = "block";
               modalImage.src = src;
           }

       function imageCloseModal() {
               const modal = document.getElementById('imageModal');
               modal.style.display = "none";
       }

       function createTradeBoardInfo(data){
            scrollTop();

            const viewBox = document.getElementById('viewBox');

            viewBox.innerHTML ='';
            var productDetailHTML = `
                <div class="product-detail">
                    <h1 class="product-title">${data.title}</h1>
                    <p class="product-price">${data.price.toLocaleString()}원</p>
                    <p class="product-seller">${data.nickname}</p>
                    <div class="product-view-info">
                        <span class="view-icon">👁️</span>
                        <span class="view-count">${data.view}</span>
                    </div>
                    <div class="product-images">
                        ${data.paths.map(img => `<img src="http://localhost:8080${img}${img}" alt="Product Image" onclick="openModal('http://localhost:8080${img}')">`).join('')}
                    </div>
                    <div class="product-description">
                        <p>${data.content}</p>
                    </div>
                    <!-- 1:1 채팅 버튼 -->
                    <button class="chat-button" onclick="startChat(${data.id})">1:1 채팅</button>
                </div>
                <div id="imageModal" class="modal" onclick="imageCloseModal()">
                        <span class="close">&times;</span>
                        <img class="modal-content" id="modalImage">
                </div>
            `;
            if(Number(data.memberId) === Number(userId)){
                productDetailHTML=`
                <div class="product-detail">
                                    <h1 class="product-title">${data.title}</h1>
                                    <p class="product-price">${data.price.toLocaleString()}원</p>
                                    <p class="product-seller">${data.nickname}</p>
                                    <div class="product-view-info">
                                        <span class="view-icon">👁️</span>
                                        <span class="view-count">${data.view}</span>
                                    </div>
                                    <div class="product-images">
                                        ${data.paths.map(img => `<img src="http://localhost:8080${img}" alt="Product Image" onclick="openModal('http://localhost:8080${img}')">`).join('')}
                                    </div>
                                    <div class="product-description">
                                        <p>${data.content}</p>
                                    </div>
                                    <div class="tradeboard-delete" onclick=deletePost(${data.id})></div>
                                </div>
                                <div id="imageModal" class="modal" onclick="imageCloseModal()">
                                        <span class="close">&times;</span>
                                        <img class="modal-content" id="modalImage">
                                </div>`

           }else if(data.condition === "판매완료"){
             productDetailHTML=`
             <div class="product-detail">
                 <h1 class="product-title">${data.title}</h1>
                 <p class="product-price">${data.price.toLocaleString()}원</p>
                 <p class="product-seller">${data.nickname}</p>
                 <div class="product-view-info">
                      <span class="view-icon">👁️</span>
                      <span class="view-count">${data.view}</span>
                 </div>
                 <div class="product-images">
                    ${data.paths.map(img => `<img src="http://localhost:8080${img}" alt="Product Image" onclick="openModal('http://locahlost:8080${img}')">`).join('')}
                 </div>
                 <div class="product-description">
                    <p>${data.content}</p>
                 </div>
           </div>
           <div id="imageModal" class="modal" onclick="imageCloseModal()">
                 <span class="close">&times;</span>
                <img class="modal-content" id="modalImage">
           </div>`
           }

            viewBox.innerHTML = productDetailHTML;
       }
       /*tradeboard info fin*/

       function tradeBoardWrite(){
           removeScrollEvent();
           const viewBox = document.getElementById("viewBox");
           viewBox.innerHTML ='';

       }

async function deletePost(id) {
    if(!confirm("게시글을 삭제하시겠습니까?")){
        return;
    }
    const url = `http://localhost:8000/backend/tradeboard/${id}`;

    try {
        const response = await fetch(url, {
            method: "DELETE",
            headers: {
                "Authorization": accessToken
            }
        });

        if (response.status === 204) {
            alert("게시글이 삭제되었습니다.");
            navigateToPage('tradeboard');
        } else {
            alert("서버에 문제가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    } catch (error) {
        console.error("삭제 요청 중 오류 발생:", error);
        alert("서버와의 연결에 문제가 발생했습니다. 나중에 다시 시도해 주세요.");
    }
}

