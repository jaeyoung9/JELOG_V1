<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>
    <p>code: <span id="code"></span></p>
    <p>message: <span id="message"></span></p>
    <p>response_time: <span id="response_time"></span></p>
    <p>request_action: <span id="request_action"></span></p>
    <div class="content-details" id="content-details"></div>
</div>

<script type="text/javascript" src="/static/js/detailview.js"></script>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        fetchView(${contentNumber});
    });
</script>