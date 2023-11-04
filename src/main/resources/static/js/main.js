/**
 * 메인 페이지 구현 및 페이징 구현
 * */
document.addEventListener("DOMContentLoaded", function() {

    let currentPage = 0;
    let fetchedPages = 0;
    let title_input = '';
    let categories_input = '';

    if(DataFromURL() !== ''){
        const item1 = DataFromURL().split(',')[0].split('ct=').pop();
        const item2 = DataFromURL().split(',')[1].split('sq=').pop();
        categories_input = item1 === 'null'? '' : item1;
        title_input = item2 === 'null'? '' : item2;
    }

    const pageSize = 8;
    const apiUrl = "/api/public/mains/?";


    function fetchPage(page) {
        fetch(apiUrl + new URLSearchParams({
            page: page.toString(),
            size: pageSize.toString(),
            Title: decodeURI(title_input),
            Categories : categories_input
        }))
            .then(response => {
                if (!response.ok){
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const result = data.result;
                const payload = data.payload.data.content;

                const contentList = document.getElementById("contentList");
                contentList.innerHTML = '';

                for (const content of payload) {
                    blobFilesToImg(content)
                        .then(imgTag => {
                            const contentElement = document.createElement("div");
                            contentElement.className = "content-page";
                            contentElement.innerHTML =
                                '<a  class="fix-a-tag" href=' + '"/api/view/public/mains/relay/' + content.contentId +'/">' +
                                    '<div class="content-thumbnail-parents">'+
                                        '<div class="content-thumbnail">' +
                                            imgTag.outerHTML +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="content-details">' +
                                        '<h3 class="content-title">' + content.contentTitle + '</h3>' +
                                        '<p class="content-body">' + limitContentLength(findImgInBody(content.contentBody, content.files)) + '</p>' +
                                '</a>' +
                                        '<div class="content-footer">' +
                                            '<hr class="content-footer-hr">' +
                                            '<p class="content-views">Views: ' + content.views + '</p>' +
                                        '</div>' +
                                    '</div>';
                            contentList.appendChild(contentElement);
                        })
                        .catch(error => {
                            toastr.error('다시 시도해주세요.');
                        });
                }

                const totalPages = data.payload.data.totalPages;
                currentPage = page;
                fetchedPages++;
                updatePaginationControls(totalPages, currentPage);
            })
            .catch(error => {
                toastr.error('다시 시도해주세요.');
            });
    }

    function updatePaginationControls(totalPages, currentPage) {

        const pageButtonsContainer = document.getElementById("pageButtons");
        pageButtonsContainer.innerHTML = '';

        const startPage = Math.max(0, currentPage - 5);
        const endPage = Math.min(totalPages - 1, startPage + 9);

        for (let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement("button");
            pageButton.textContent = i + 1;
            if (i === currentPage) {
                pageButton.disabled = true;
                pageButton.classList.add("active");
            }
            pageButton.addEventListener("click", () => {
                fetchPage(i);
            });
            pageButtonsContainer.appendChild(pageButton);
        }
    }

    fetchPage(currentPage);
});

document.getElementById('search-button').addEventListener('keyup', function (event){

    if(event.code === 'Enter'){
        const search_data = document.getElementById('main-search-input').value;
        SearchToURL(search_data);
    }

});