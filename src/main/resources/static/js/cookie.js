
/**
 * Cookie Check
 * */
const getCookie = function(cookieName){
    const cookies = document.cookie.split("; ");
    for (const cookie of cookies) {
        const [name, value] = cookie.split("=");
        if (name === "JY-ACCESS-TOKEN") {
            return value;
        }
    }
    return null;
}

/**
 * Cookie Set
 * */
const setCookie = function (name, value){
    const date = new Date();
    date.setTime(date.getTime() + 1000 * 60 * 60 * 24);
    // Secure; = https 연결을 통해서 전송가능하게 함.
    // SameSite=Strict; = 테스트 목적으로 동일 사이트 속성 제거
    document.cookie = `${name}=${value}; expires=${date}; Secure; SameSite=Strict; Path=/; `;
}

/**
 * Cookie Delete
 * */
const deleteCookie = function (name){
    document.cookie = `${name}=; expires=Thu, 01 Jan 1999 00:00:10 GMT; Secure; SameSite=Strict; Path=/; `;
}
