<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
<h1>Main Page</h1>

<script type="text/javascript">

    document.addEventListener("DOMContentLoaded", function() {
        let currentPage = 0;
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
                    updatePaginationControls(totalPages, currentPage);
                })
                .catch(error => {
                    console.error("Error fetching data:", error);
                });
        }

        function updatePaginationControls(totalPages, currentPage) {
            const paginationControls = document.getElementById("paginationControls");
            paginationControls.innerHTML = '';

            const pageButtonsContainer = document.getElementById("pageButtons");
            pageButtonsContainer.innerHTML = '';

            for (let i = 1; i <= totalPages; i++) {
                const pageButton = document.createElement("button");
                pageButton.textContent = i;
                if (i === currentPage + 1) {
                    pageButton.disabled = true;
                    pageButton.classList.add("active");
                }
                pageButton.addEventListener("click", () => {
                    fetchPage(i - 1);
                });
                pageButtonsContainer.appendChild(pageButton);
            }

            const previousPageLink = document.getElementById("previousPage");
            const nextPageLink = document.getElementById("nextPage");

            if (currentPage > 0) {
                previousPageLink.textContent = "Previous";
                previousPageLink.style.display = "inline";
                previousPageLink.addEventListener("click", () => {
                    fetchPage(currentPage - 1);
                });
            } else {
                previousPageLink.style.display = "none";
            }

            if (currentPage < totalPages - 1) {
                nextPageLink.textContent = "Next";
                nextPageLink.style.display = "inline";
                nextPageLink.addEventListener("click", () => {
                    fetchPage(currentPage + 1);
                });
            } else {
                nextPageLink.style.display = "none";
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

<!-- Pagination controls -->
<div class="pagination-container" id="paginationControls"></div>

<!-- "Next" and "Previous" links -->
<div class="pagination-container">
    <a class="before-next" id="previousPage">Previous</a>
    <div class="pagination-pages" id="pageButtons"></div>
    <a class="before-next" id="nextPage">Next</a>
</div>

</body>
</html>
