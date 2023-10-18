<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="editor-container">
    <input type="text" id="title-input" placeholder="Title">

    <div id="button-container">
        <div class="custom-button photo-button">
            <img src="/static/images/notifications.png">
            <span>사진</span>
        </div>
        <div class="custom-button file-button">
            <img src="/static/images/notifications.png">
            <span>파일</span>
        </div>
    </div>

    <div id="formatting-buttons">
        <button id="bold-button"><b>B</b></button>
        <button id="italic-button"><i>I</i></button>
        <button id="underline-button"><u>U</u></button>
        <select id="font-size-dropdown">
            <option value="12px">12px</option>
            <option value="16px">16px</option>
            <option value="20px">20px</option>
            <option value="24px">24px</option>
        </select>
    </div>

    <div id="text-area" contenteditable="true"></div>
</div>
