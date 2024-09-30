function createSnsBoardWrite(){
    init();
    scrollTop();
    removeSnsScrollEvent();
    const viewBox = document.getElementById("viewBox");
    viewBox.innerHTML='';

    const snsWriteItem = `
    <div class="tradebaord-write-container">
                <h1>게시물 작성</h1>
                    <div class="tradeboard-form-group">
                        <label for="title">제목</label><br></br>
                        <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required class="tradeboard-title">
                    </div>
                    <div class="tradeboard-form-group">
                        <label for="category">카테고리</label><br></br>
                        <select id="category" name="category" required class="tradeboard-category">
                            <option value="" disabled selected>카테고리를 선택하세요</option>
                            <option value="리뷰">리뷰</option>
                            <option value="일반">일반</option>
                        </select>
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
                    <div class="tradeboard-button" onclick="snsBoardWrite()">게시물 작성</div>
            </div>
    `;
    viewBox.innerHTML += snsWriteItem;
}

async function snsBoardWrite() {
    // 입력된 값 가져오기
    const title = document.getElementById('title').value;
    const category = document.getElementById('category').value;
    const content = document.getElementById('content').value;
    const fileInput = document.getElementById('fileInput');

    // FormData 객체 생성
    const formData = new FormData();

    // 게시물 정보를 객체로 생성
    const SnsPostRequestDto = {
        title: title,
        category: category,
        content: content,
        nickname:username,
        memberId:userId
    };

    // 객체를 JSON 문자열로 변환하여 FormData에 추가
    formData.append('SnsPostRequestDto', new Blob([JSON.stringify(SnsPostRequestDto)], { type: 'application/json' }));

    // 이미지 파일이 있는 경우 FormData에 추가
    const files = fileInput.files;
    if(files.length>0){
    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }
}
    const url = "http://localhost:8000/sns/posts";


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
          await showSnsBoardInfo(data);
        }
    } catch (error) {
        console.error("서버에서 오류 발생:", error);
    }
}





function showSnsBoardList() {
            isShowBoard=true;
            isLastPage=false;
            const viewBox = document.getElementById('viewBox');
            viewBox.innerHTML = `
                <input type="hidden" id="page" value="0">
                <input type="hidden" id="category">
                <div class="tradeboard-bar">
                    <div>
                        <span onclick="setSnsCategory('')"><img src="/icon/category/전체_60x32.png"></span>
                        <span onclick="setSnsCategory('리뷰')"><img src="/icon/category/리뷰_60x32.png"></span>
                        <span onclick="setSnsCategory('일반')"><img src="/icon/category/일반_60x32.png"></span>
                    </div>
                    <div onclick="createSnsBoardWrite()"><img src="/icon/category/글쓰기_60x32.png"></div>
                </div>
                <div id="grid-container" class="grid-container"></div>
            `;

            snsBoardList('', 0);
            addSnsScrollEvent();
        }

        function setSnsCategory(category) {
            isLastPage=false;
            document.getElementById('category').value = category;
            snsBoardList(category, 0);
        }

        async function snsBoardList(category, pageNum) {
            const param = new URLSearchParams();
            if (pageNum !== undefined && pageNum !== '') {
                param.append("page", pageNum);
                param.append("size", 12);
            }
            if (category !== undefined && category !== '') {
                param.append("category", category);
            }

            const url = `http://localhost:8000/sns/posts?${param.toString()}`;
            try {
                const response = await fetch(url);
                if (response.status === 401) {
                    await refreshToken();
                    snsBoardList(category, pageNum);
                    return;
                }
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                createSnsBoardItem(data, pageNum + 1);
            } catch (error) {
                console.error('Error:', error);
            }
        }

       function createSnsBoardItem(data, pageNum) {
           const viewBox = document.getElementById("viewBox");
           const gridContainer = document.querySelector(".grid-container");

           if (pageNum === 1) {
               gridContainer.innerHTML = '';
           }

           data.content.forEach(content => {
               const gridItem = document.createElement("div");
               gridItem.className = "grid-item";
               gridItem.setAttribute('onclick', `showSnsBoardInfo(${content.id})`);

               //수정
               const img = document.createElement("img");
               //img.src = content.paths !== null && content.paths.length > 0 ? content.paths[0] : '';

               const itemInfo = document.createElement("div");
               itemInfo.className = "item-info";

               const date = dateDifference(content.createdDate);
               const info = `
                   <h2>${content.title}</h2>
                   <p class="condition">${content.category}</p>
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

        function addSnsScrollEvent() {
            if (!isSnsBoardLoaded) {
                window.addEventListener('scroll', handleSnsScroll);
                isSnsBoardLoaded = true;
            }
        }

        function removeSnsScrollEvent() {
            if (isSnsBoardLoaded) {
                window.removeEventListener('scroll', handleScroll);
                isSnsBoardLoaded = false;
            }
        }

       function handleSnsScroll() {
           if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 10 && isShowBoard) {
               loadSnsMore();
           }
       }

       function loadSnsMore() {
           if (isLoading || isLastPage) return;

           isLoading = true;
           const category = document.getElementById("category").value;
           const page = document.getElementById("page").value;

           snsBoardList(category, parseInt(page))
               .finally(() => {
                   isLoading = false;  // Set loading to false after request is completed
               });
       }

        /* tradeboard info*/
       async function showSnsBoardInfo(id) {
           removeSnsScrollEvent();

          const url = (userId !== null)
              ? `http://localhost:8000/sns/posts/${id}?memberId=${userId}`
              : `http://localhost:8000/sns/posts/${id}`;
          try{
           const response = await fetch(url,{
              headers:{
                "Authorization":accessToken
              }
           });
           if(response.status === 401){
               await refreshToken();
               showSnsBoardInfo(id);
               return;
           }
          if (!response.ok) {
                 throw new Error(`HTTP error! status: ${response.status}`);
          }
          const data = await response.json();
          createSnsBoardInfo(data);
           }catch(error){
            console.log(error);
           }
       }


       function createSnsBoardInfo(data){
            console.log(data);
            const viewBox = document.getElementById('viewBox');
            viewBox.innerHTML ='';
            var productDetailHTML = `
                <div class="product-detail">
                    <h1 class="product-title">${data.title}</h1>
                    <p class="product-seller">${data.nickname}</p>
                    <p class="product-seller">${data.viewCount} 읽음 • 댓글</p>
                    <div class="product-images">
                        ${data.images.map(img => `<img src="${img}" alt="Product Image" onclick="openModal('${img}')">`).join('')}
                    </div>
                    <div class="product-description">
                        <p>${data.content}</p>
                    </div>
                </div>
                <div id="imageModal" class="modal" onclick="imageCloseModal()">
                        <span class="close">&times;</span>
                        <img class="modal-content" id="modalImage">
                </div>
            `;

            viewBox.innerHTML = productDetailHTML;
       }

//                    <div class="product-view-info">
//                        <span class="view-icon">👁️</span>
//                        <span class="view-count"></span>
//                    </div>