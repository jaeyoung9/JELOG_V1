<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="header">

        <div class="logo">

        </div>

        <nav class="hamburger-menu">
            <div class="profile-group">
                <div class="profile-body">
                    <div class="profile-img">
                        <img src="/static/images/notifications.png">
                    </div>
                    <div class="profile-content">
                        <span>소개 내용글</span>
                    </div>
                </div>
            </div>
            <ul id="search-li-a">
                <li id="header-nav-sign"><a id="custom-li-a" class="fix-a-tag" href="/api/view/auth/cwo/new">Writing</a></li>
                <li><a href="/api/view/public/mains/" class="main-li-a">Mains</a></li>
                <li><a class="main-li-a" onclick="DataToURL('Other');">Other</a></li>
                <li><a class="main-li-a" onclick="DataToURL('Java');">Java</a></li>
                <li><a class="main-li-a" onclick="DataToURL('JavaScript');">JavaScript</a></li>
                <li><a class="main-li-a" onclick="DataToURL('C');">C</a></li>
                <li><a class="main-li-a" onclick="DataToURL('Python');">Python</a></li>
                <li><a class="main-li-a" onclick="DataToURL('Shell');">Shell</a></li>
                <li><a class="main-li-a" onclick="DataToURL('Security');">Security</a></li>
                <li><a class="main-li-a" onclick="DataToURL('DeveloperCareerSkills');">DevSkills</a></li>
                <!-- Add more menu items as needed -->
            </ul>
            <div class="sign-login"><button id="signIn" onclick="signInView()">Sign In</button></div>
        </nav>

    <script type="text/javascript" src="/static/js/headerJs.js"></script>
    <script type="text/javascript" src="/static/js/default.js"></script>
</header>
