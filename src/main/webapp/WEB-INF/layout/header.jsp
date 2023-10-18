<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="header">
    <div class="logo">
        <!-- Your logo image or text here -->
    </div>
    <div class="sign-login"><button id="signIn" onclick="signInView()"></button></div>
    <input type="checkbox" id="hamburger-checkbox" class="hamburger-checkbox">
    <label for="hamburger-checkbox" class="hamburger-icon">&#9776;</label>
    <nav class="hamburger-menu">
        <ul>
            <li><a id="custom-li-a" class="fix-a-tag" href="/api/view/auth/cwo/new">Writing</a></li>
            <li><a href="/api/view/public/mains">Mains</a></li>
            <li><a href="#">Other</a></li>
            <li><a href="#">Java</a></li>
            <li><a href="#">JavaScript</a></li>
            <li><a href="#">C</a></li>
            <li><a href="#">Python</a></li>
            <li><a href="#">Shell</a></li>
            <li><a href="#">Security</a></li>
            <li><a href="#">DevSkills</a></li>
            <!-- Add more menu items as needed -->
        </ul>
    </nav>
    <script type="text/javascript" src="/static/js/headerJs.js"></script>
    <script type="text/javascript" src="/static/js/default.js"></script>
</header>
