
/**
 * 보기 페이지 구현
 */

let item = "";
function fetchView(contentNumber){

    const apiUrl = "/api/public/mains/relay/" + contentNumber + "/";
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const result = data.result;
            const payload = data.payload.data;

            // const originBodyData = payload.contentBody;
            // const newBodyData = originBodyData.querySelector(`<img src=""/>`);

            const contentDetails = document.getElementById("content-details");
            contentDetails.innerHTML = '';

            const contentElement = document.createElement("div");
            contentElement.className = "content";
            contentElement.innerHTML =
                 '<div class="content-title">' + payload.contentTitle + '</div>'
                +'<p class="content-body">' + payload.contentBody + '</p>';


            contentDetails.appendChild(contentElement);


        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}