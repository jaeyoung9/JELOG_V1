/**
 * 메인 페이지 구현 및 페이징 구현
 * */
document.addEventListener("DOMContentLoaded", function() {
    let currentPage = 0;
    let fetchedPages = 0;
    const pageSize = 8;
    const apiUrl = "/api/public/mains/?";

    function fetchPage(page) {
        fetch(apiUrl + new URLSearchParams({
            page: page.toString(),
            size: pageSize.toString(),
            title: '',
            Categories : ''
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
                            contentElement.className = "content";
                            contentElement.innerHTML =
                                '<a  class="fix-a-tag" href=' + '"/api/view/public/mains/relay/' + content.contentId +'/">' +
                                    '<div class="content-thumbnail-parents">'+
                                        '<div class="content-thumbnail">' +
                                            imgTag.outerHTML +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="content-details">' +
                                        '<h3 class="content-title">' + content.contentTitle + '</h3>' +
                                        '<p class="content-body">' + limitContentLength(content.contentBody) + '</p>' +
                                '</a>' +
                                    '<p class="content-views">Views: ' + content.views + '</p>' +
                                    '</div>';
                            contentList.appendChild(contentElement);
                        })
                        .catch(error => {
                            console.error(error);
                        });
                }

                const totalPages = data.payload.data.totalPages;
                currentPage = page;
                fetchedPages++;
                updatePaginationControls(totalPages, currentPage);
            })
            .catch(error => {
                console.error("Error fetching data:", error);
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

    function limitContentLength(contentBody) {
        if (contentBody.length > 20) {
            return contentBody.substring(0, 20) + '<span class="content-more">(자세히)</span>';
        }
        return contentBody;
    }

    function blobFilesToImg(data) {
        return new Promise((resolve, reject) => {
            if (data.contentThumbnail != null && data.files && data.files[0]) {
                try {
                    const base64Data = data.files[0].resultFile; // Assuming the data is already base64-encoded

                    const imgTag = new Image();
                    imgTag.onload = function () {
                        imgTag.alt = data.contentTitle;
                        resolve(imgTag);
                    };
                    imgTag.onerror = function () {
                        reject('Error loading image');
                    };
                    imgTag.src = `data:${data.files.mediaType};base64,` + base64Data;
                } catch (error) {
                    reject('Error creating image');
                }
            } else {
                resolve('null');
            }
        });
    }

    fetchPage(currentPage);
});