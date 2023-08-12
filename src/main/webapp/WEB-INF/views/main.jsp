<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
</head>
<body>

<script type="text/javascript">
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

                    document.getElementById("code").textContent = result.code;
                    document.getElementById("message").textContent = result.message;
                    document.getElementById("response_time").textContent = result.response_time;
                    document.getElementById("request_action").textContent = result.request_action;

                    const contentList = document.getElementById("contentList");
                    contentList.innerHTML = '';

                    for (const content of payload) {
                        const contentElement = document.createElement("div");
                        contentElement.className = "content";
                        contentElement.innerHTML = '<div class="content-thumbnail">' +
                            '<img src="' + content.contentThumbnail + '" alt="Thumbnail">' +
                            '</div>' +
                            '<div class="content-details">' +
                            '<h3 class="content-title">' + content.contentTitle + '</h3>' +
                            '<p class="content-body">' +
                            (content.contentBody.length > 20 ? content.contentBody.substring(0, 20) + '<span class="content-more">(자세히)</span>' : content.contentBody) +
                            '</p>' +
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
</script>

<!-- Display the result here -->
<div>
    <p>code: <span id="code"></span></p>
    <p>message: <span id="message"></span></p>
    <p>response_time: <span id="response_time"></span></p>
    <p>request_action: <span id="request_action"></span></p>
    <div class="content-list" id="contentList"></div>
</div>

<div class="pagination-container">
    <div class="pagination-pages" id="pageButtons"></div>
</div>

</body>
</html>
