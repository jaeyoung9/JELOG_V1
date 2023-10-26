function getCookieValue(key) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim(); // Remove spaces around cookie string
        const cookieParts = cookie.split('=');
        if (cookieParts[0] === key) {
            return cookieParts[1];
        }
    }
    return null; // Cookie not found
}

/**
 * Cookie Set
 * */
function setCookie(name, value){
    const date = new Date();
    date.setTime(date.getTime() + 1000 * 60 * 60 * 24);
    // Secure; = https 연결을 통해서 전송가능하게 함.
    // SameSite=Strict; = 테스트 목적으로 동일 사이트 속성 제거
    document.cookie = `${name}=${value}; expires=${date}; Secure; SameSite=Strict; Path=/; `;
}

/**
 * Cookie Delete
 * */
function deleteCookie(name){
    document.cookie = `${name}=; expires=Thu, 01 Jan 1999 00:00:10 GMT; Secure; SameSite=Strict; Path=/; `;
}
