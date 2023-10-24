<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="editor-container">
    <input type="text" id="title-input" placeholder="Title">

    <div name="formatting-buttons" id="formatting-buttons">
        <button type="button" data-cmd="justifyLeft"><i class="fas fa-align-left"></i></button>
        <button type="button" data-cmd="justifyCenter"><i class="fas fa-align-center"></i></button>
        <button type="button" data-cmd="justifyFull"><i class="fas fa-align-justify"></i></button>
        <button type="button" data-cmd="justifyRight"><i class="fas fa-align-right"></i></button>
        <button type="button" data-cmd="bold"><i class="fas fa-bold"></i></button>
        <button type="button" data-cmd="italic"><i class="fas fa-italic"></i></button>
        <button type="button" data-cmd="underline"><i class="fas fa-underline"></i></button>
        <button type="button" data-cmd="insertOrderedList"><i class="fas fa-list-ol"></i></button>
        <button type="button" data-cmd="insertUnorderedList"><i class="fas fa-list-ul"></i></button>
        <button type="button" data-cmd="createLink"><i class="fas fa-link"></i></button>
        <button type="button" data-cmd="insertImage"><i class="far fa-image"></i></button>
        <button type="button" data-cmd="showCode" name="active"><i class="fas fa-code"></i></button>
    </div>

    <div id="text-area" name="textField" contenteditable="true"></div>

    <script type="text/javascript" src="/static/js/editor.js"></script>
</div>