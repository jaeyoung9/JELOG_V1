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
            title: ''
        }))
            .then(response => response.json())
            .then(data => {
                const result = data.result;
                const payload = data.payload.data.content;


                const contentList = document.getElementById("contentList");
                contentList.innerHTML = '';

                for (const content of payload) {
                    const contentElement = document.createElement("div");
                    contentElement.className = "content";
                    contentElement.innerHTML =
                        '<a  class="fix-a-tag" href=' + '"/api/view/public/mains/relay/' + content.contentId +'/">' +
                        '<div class="content-thumbnail">' +
                        '<img src="' + content.contentThumbnail + '" alt="Thumbnail">' +
                        '</div>' +
                        '<div class="content-details">' +
                        '<h3 class="content-title">' + content.contentTitle + '</h3>' +
                        '<p class="content-body">' +
                        (content.contentBody.length > 20 ? content.contentBody.substring(0, 20) + '<span class="content-more">(자세히)</span>' : content.contentBody) +
                        '</p>' +
                        '</a>' +
                        '<p class="content-views">Views: ' + content.views + '</p>' +
                        '</div>';
                    contentList.appendChild(contentElement);
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
    fetchPage(currentPage);
});