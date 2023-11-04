/**
*  Init 방식 변경 필요.
* */
window.onload = signInit();

/**
 * 버튼 로그인, 로그아웃  표시.
 * */
function signInit(){
    let cookie = getCookieValue("JY-ACCESS-TOKEN");
    const sign = document.getElementById("signIn");

    if(cookie == null){
        sign.innerText = "Login";
        document.getElementById('header-nav-sign').style.display = 'none';
    }else{
        document.getElementById('header-nav-sign').style.display = 'block';
        sign.innerText = "Logout";
    }
}

/**
 * 메뉴 토글
 * */
function toggleMenu() {
    const menu = document.getElementById("menu");
    menu.classList.toggle("active");
}


/**
 * 1.로그인페이지 혹은 메인페이지 리다이렉트.
 * 2.로그아웃 클릭시 토큰 삭제.
 * */
function signInView() {
    let cookie = getCookieValue("JY-ACCESS-TOKEN");
    const signCheck = document.getElementById("signIn").innerText;

    if(signCheck == "Logout"){
        deleteCookie('JY-ACCESS-TOKEN');
        const mainUrl = "/api/view/public/mains/";
        window.location.replace(mainUrl);
    }

    if(signCheck !== "Logout" && cookie == null){
        const signUrl = "/api/view/public/in/sign/";
        window.location.replace(signUrl);
    }else{
        const mainUrl = "/api/view/public/mains/";
        window.location.replace(mainUrl);
    }
}

