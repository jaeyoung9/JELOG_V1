
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
                +'<p class="content-body">' + findImgInBody(payload.contentBody, payload.files) + '</p>';

            const contentCommentElement = document.createElement("div");
            contentCommentElement.className = 'content-comment';
            contentCommentElement.innerHTML = '<div class="content-comment-list">' +
                '<span>해당 칸은 댓글 칸 입니다.</span>'+
                '<br>'+
                '<span>차후 RestAPI 연결 예정입니다.</span>'+
            '</div>';


            contentDetails.appendChild(contentElement);
            contentDetails.appendChild(contentCommentElement);


        })
        .catch(error => {
            toastr.error('알수없는 오류.');
        });
}