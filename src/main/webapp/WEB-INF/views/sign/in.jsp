<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body class="login-body">
<div class="body-login-container">
    <div class="login-container" id="login-container" style="display: block">
        <div class="login-SignIn" id="loginFormContainer">
            <h1 class="login-h1">Login</h1>
            <form class="loginForm" id="loginForm">
                <div class="login-input-box" id="login-input-box">
                    <div class="login-input-group">
                        <input class="sign-input" type="text" id="username" name="username" placeholder="ID" required autocomplete="username">
                        <input class="sign-input" type="password" id="password" name="password" placeholder="Password" required autocomplete="current-password">
                    </div>
                    <button class="loginButton" type="button" id="loginButton">SignIn</button>
                    <input class="SignUpButton" type="button" onclick="toggleForm('signup')" value="Signup"/>
                    <p id="message" class="message"></p>
                </div>
            </form>
        </div>
    </div>
    <div class="signup-container" id="signup-container">
        <div class="login-SignUp" id="signupFormContainer">
            <h1 class="login-h1">SignUp</h1>
            <form class="SignUpForm" id="SignUpForm">
                <div class="login-input-box" id="Sign-input-box">
                    <div class="login-input-group">
                        <input class="sign-input" type="text" id="signUpName" name="signUpName" placeholder="ID"/>
                        <input class="sign-input" type="Password" id="signUpPassword" name="signUpPassword" placeholder="Password"/>
                        <input class="sign-input" type="Password" id="signUpCheckPassword" name="signUpCheckPassword" placeholder="Re-Password"/>
                    </div>
                    <input type="checkbox" id="cd1">
                    <label for="cd1" style="font-size: 12px;"><a href="#">개인정보 활용동의</a></label>
                    <br>
                    <button class="SignUpButton" type="button" id="SignUpButton">SignUp</button>
                    <input class="loginButton" type="button" onclick="toggleForm('login')" value="SignIn"/>
                    <p id="SignMessage" class="message"></p>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="/static/js/signJs.js"></script>
</body>
</html>
