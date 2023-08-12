<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<div class="body-login-container">
    <div class="login-container">
        <h1>Login</h1>
        <form id="loginForm">
            <input class="sign-input" type="text" id="username" placeholder="SignID" required>
            <input class="sign-input" type="password" id="password" placeholder="Password" required>
            <button class="loginButton" type="button" id="loginButton">Login</button>
            <p id="message" class="message"></p>
        </form>
    </div>
</div>
<script type="text/javascript" src="/static/js/signJs.js"></script>
</body>
</html>
