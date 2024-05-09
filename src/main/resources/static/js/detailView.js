
/**
 * 보기 페이지 구현
 */

let item = "";
function fetchView(contentNumber){

    const apiUrl = "/api/public/mains/relay/" + contentNumber + "/";
    goFetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const result = data.result;
            const payload = data.payload.data;
            const comments = payload.comments;

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

            let commentsHtml = '<div class="comments-list">';

            comments.forEach(comment => {
                commentsHtml += `<div class="comment-item">
                        <div class="comment-meta">
                            <span class="comment-author">${comment.commentName}</span> - 
                            <span class="comment-date">${comment.inDate}</span>
                        </div>
                        <div class="comment-content">${comment.commentBody}</div>
                     </div>`;
            });

            commentsHtml += '</div>';

            commentsHtml += '<div class="comment-form">' +
                '<input type="hidden" id="content-oid" value="' + contentNumber + '">' +
                '<textarea id="comment-text" placeholder="댓글을 입력하세요"></textarea>' +
                '<button onclick="submitComment()">등록</button>' +
                '</div>';

            contentCommentElement.innerHTML = commentsHtml;

            // contentCommentElement.innerHTML = '<div class="content-comment-list">' +
            //     '<span>해당 칸은 댓글 칸 입니다.</span>'+
            //     '<br>'+
            //     '<span>차후 RestAPI 연결 예정입니다.</span>'+
            // '</div>';


            contentDetails.appendChild(contentElement);
            contentDetails.appendChild(contentCommentElement);


        })
        .catch(error => {
            toastr.error('알수없는 오류.');
        });
}

class DT_Comment{
    constructor(){
        this.contentId = 0;
        this.commentId = 0;
        this.replyCommentId = 0;
        this.dnUid = '';
        this.commentBody = '';
        this.commentName = '';
        this.commentPwd = '';
        this.content = null;
    }
}
const comment = new DT_Comment();

function submitComment() {
    const commentText = document.getElementById('comment-text').value;
    if (!commentText.trim()) {
        toastr.warning('댓글을 입력하세요.');
        return;
    }
    const contentOid = document.getElementById('content-oid').value;
    comment.contentId = contentOid;
    comment.commentBody = commentText;
    const apiUrl = "/api/public/mains/relay/" + contentOid + "/comments/";
    goFetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(comment),
    }).then(response => response.json())
        .then(data => {

        });

    // 댓글 목록에 새 댓글 추가
    const commentsList = document.querySelector('.comments-list');
    const newComment = document.createElement('div');
    newComment.className = 'comment-item';
    newComment.textContent = commentText; // XSS 방지를 위해 textContent 사용
    commentsList.appendChild(newComment);

    // 입력 필드 초기화
    document.getElementById('comment-text').value = '';
}