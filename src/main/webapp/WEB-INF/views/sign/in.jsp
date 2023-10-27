<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body class="login-body">
<div class="body-login-container">
    <div class="login-container">
        <h1 class="login-h1">Login</h1>
        <form class="loginForm" id="loginForm">
            <div class="login-input-box" id="login-input-box">
                <div class="login-input-group">
                    <input class="sign-input" type="text" id="username" placeholder="SignID" required autocomplete="username">
                    <input class="sign-input" type="password" id="password" placeholder="Password" required autocomplete="current-password">
                </div>
                <button class="loginButton" type="button" id="loginButton">Login</button>
                <p id="message" class="message"></p>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" src="/static/js/signJs.js"></script>
</body>
</html>
