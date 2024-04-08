/**
 * 로그인 처리
 * */
document.addEventListener("DOMContentLoaded", function() {

    let checkedP = 'false';

    const loginForm = document.getElementById("loginForm");
    const loginButton = document.getElementById("loginButton");
    const messageElement = document.getElementById("message");
    const signForm = document.getElementById('SignUpForm');
    const signButton = document.getElementById('SignUpButton');
    const signMessageElement = document.getElementById("SignMessage");
    const passwordInput = document.getElementById('signUpPassword');
    const passwordCheckInput = document.getElementById('signUpCheckPassword');

    loginButton.addEventListener("click", function() {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const signInURL = "/api/ko-jy/in/sign/";
        goFetch(signInURL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                daSignID: username,
                dnPasswd: password
            })
        })
            .then(response => response.json())
            .then(data => {
                const result = data.result;

                if (result.code === 200) {
                    messageElement.textContent = "Login successful!";
                    messageElement.style.color = "green";

                    // const refreshToken = data.payload["JY-REFRESH-TOKEN"];
                    const accessToken = data.payload["JY-ACCESS-TOKEN"];
                    setCookie('JY-ACCESS-TOKEN', accessToken);

                    window.location.replace("/api/view/republic/mains");
                } else {
                    messageElement.textContent = "Login failed. Please check your credentials.";
                    messageElement.style.color = "red";
                }
            })
            .catch(error => {
                toastr.error('알수없는 오류.');
                messageElement.textContent = "Login failed. Please check your credentials.";
                messageElement.style.color = "red";
            });
    });

});