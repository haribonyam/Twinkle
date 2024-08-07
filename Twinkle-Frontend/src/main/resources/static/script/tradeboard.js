function showTradeBoardList() {
            isLastPage=false;
            const viewBox = document.getElementById('viewBox');
            viewBox.innerHTML = `
                <input type="hidden" id="page">
                <input type="hidden" id="category">
                <div class="tradeboard-bar">
                    <div>
                        <span onclick="setCategory('')">전체</span>
                        <span onclick="setCategory('화장품')">화장품</span>
                        <span onclick="setCategory('향수')">향수</span>
                        <span onclick="setCategory('미용기기')">미용기기</span>
                    </div>
                    <div onclick="tradeBoardWrite()">글쓰기</div>
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
            const url = `http://localhost:8080/api/tradeboard/list?${param.toString()}`;
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
               //img.src = content.paths !== null && content.paths.length > 0 ? content.paths[0] : '';

               const itemInfo = document.createElement("div");
               itemInfo.className = "item-info";

               const date = dateDifference(content.createdDate);
               const info = `
                   <h2>${content.title}</h2>
                   <p class="condition">${content.condition}</p>
                   <p class="price">${content.price}원</p>
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
           if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 10) {
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
           const url = "http://localhost:8080/api/tradeboard/"+id;
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

       function closeModal() {
               const modal = document.getElementById('imageModal');
               modal.style.display = "none";
       }

       function createTradeBoardInfo(data){

            const viewBox = document.getElementById('viewBox');
            viewBox.innerHTML ='';
            const productDetailHTML = `
                <div class="product-detail">
                    <h1 class="product-title">${data.title}</h1>
                    <p class="product-price">${data.price.toLocaleString()}원</p>
                    <p class="product-seller">${data.nickname}</p>
                    <div class="product-view-info">
                        <span class="view-icon">👁️</span>
                        <span class="view-count">${data.view}</span>
                    </div>
                    <div class="product-images">
                        ${data.paths.map(img => `<img src="${img}" alt="Product Image" onclick="openModal('${img}')">`).join('')}
                    </div>
                    <div class="product-description">
                        <p>${data.content}</p>
                    </div>
                    <!-- 1:1 채팅 버튼 -->
                    <button class="chat-button" onclick="startChat(${data.id})">1:1 채팅</button>
                </div>
                <div id="imageModal" class="modal" onclick="closeModal()">
                        <span class="close">&times;</span>
                        <img class="modal-content" id="modalImage">
                </div>
            `;

            viewBox.innerHTML = productDetailHTML;
       }
       /*tradeboard info fin*/

       function tradeBoardWrite(){
           removeScrollEvent();
           const viewBox = document.getElementById("viewBox");
           viewBox.innerHTML ='';

       }



