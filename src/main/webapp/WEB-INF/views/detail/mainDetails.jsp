<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>

    <div class="content-details" id="content-details"></div>
</div>

<script type="text/javascript" src="/static/js/detailview.js"></script>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        fetchView(${contentNumber});
    });
</script>