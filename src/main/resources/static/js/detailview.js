
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
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
                '<br>'+
                '<span>임시 댓글 내용</span>'+
            '</div>';


            contentDetails.appendChild(contentElement);
            contentDetails.appendChild(contentCommentElement);


        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}