<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
<h1>Main Page</h1>

<script>


    document.addEventListener("DOMContentLoaded", function() {
        const apiUrl = "/api/public/mains/?";
        fetch(apiUrl + new URLSearchParams({
            page: '0',
            size: '10',
            title: ''
        }))
            .then(response => response.json())
            .then(data => {

                // result
                const result = data.result;

                // payload
                const payload = data.payload.data.content;

                document.getElementById("code").textContent = result.code;
                document.getElementById("message").textContent = result.message;
                document.getElementById("response_time").textContent = result.response_time;
                document.getElementById("request_action").textContent = result.request_action;


                for (const content of payload) {
                    const contentElement = document.createElement("div");
                    contentElement.innerHTML = '<hr>' +
                        '<p>Content ID: ' + content.contentId + '</p>' +
                        '<p>Content Categories: ' + content.contentCategories + '</p>' +
                        '<p>Content Thumbnail: ' + content.contentThumbnail + '</p>' +
                        '<p>Content Title: ' + content.contentTitle + '</p>' +
                        '<p>Content Body: ' + content.contentBody + '</p>' +
                        '<p>Views: ' + content.views + '</p>';
                    document.getElementById("getdata").appendChild(contentElement);
                }

            })
            .catch(error => {
                console.error("Error fetching data:", error);
            });
    });
</script>

<!-- Display the result here -->
<div>
    <p>code: <span id="code"></span></p>
    <p>message: <span id="message"></span></p>
    <p>response_time: <span id="response_time"></span></p>
    <p>request_action: <span id="request_action"></span></p>
    <div id="getdata"></div>
</div>
</body>
</html>