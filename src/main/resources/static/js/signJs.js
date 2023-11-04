/**
 * 로그인 처리
 * */
document.addEventListener("DOMContentLoaded", function() {

    let checkedP = 'false';

    const loginForm = document.getElementById("loginForm");
    const loginButton = document.getElementById("loginButton");
    const messageElement = document.getElementById("message");

    loginButton.addEventListener("click", function() {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const signInURL = "/api/ko-jy/in/sign/";
        fetch(signInURL, {
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

                    window.location.replace("/api/view/public/mains");
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

    const signForm = document.getElementById('SignUpForm');
    const signButton = document.getElementById('SignUpButton');
    const signMessageElement = document.getElementById("SignMessage");

    signButton.addEventListener('click', function () {
        const id_input = document.getElementById('signUpName').value;
        const password_input1 = document.getElementById('signUpPassword').value;
        const password_input2 = document.getElementById('signUpCheckPassword').value;

        try{
            if(id_input.length === 0){
                toastr.warning('아이디를 입력해주세요.');
                throw Error;
            }else if(password_input1.length === 0 || password_input2.length === 0){
                toastr.warning('비밀번호를 입력해주세요.');
                throw Error;
            }else if(checkedP !== 'true'){
                toastr.error('입력된 비밀번호가 일치하지 않습니다.');
                throw Error;
            }

            const signUpURL = "/api/ko-jy/up/sign/";
            const e = 3;
            goFetch(signUpURL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    dnUserAuthEnum : e,
                    daSignID: id_input,
                    dnPasswd: password_input1
                })
            }).then(response => response.json())
                .then(data => {
                    const result = data.result;

                    if (result.code === 200) {
                        toastr.success('회원가입 완료.');
                        toggleForm('login');
                    }else {
                        toastr.error('다시 시도해주세요.');
                        throw Error;
                    }
                }).catch(error => {
                    toastr.error('다시 시도해주세요.');
                    throw Error;
            });
        }catch (error){

        }
    });



    document.getElementById('signUpCheckPassword').addEventListener('keyup', function (event){
        const password_input1 = document.getElementById('signUpPassword').value;
        const password_input2 = document.getElementById('signUpCheckPassword').value;

        if(password_input1 !== password_input2){
            signMessageElement.textContent = "Passwords do not match.";
            signMessageElement.style.color = "red";
            checkedP = 'false';
        }else if(password_input1 === password_input2){
            signMessageElement.textContent = "Passwords do match.";
            signMessageElement.style.color = "green";
            checkedP = 'true';
        }
    });

});