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


    signButton.addEventListener('click', function () {
        const id_input = document.getElementById('signUpName').value;
        const password_input1 = document.getElementById('signUpPassword').value;
        const password_input2 = document.getElementById('signUpCheckPassword').value;
        const policy = document.getElementById('cd1').checked;

        try{
            if(id_input.length === 0){
                toastr.warning('아이디를 입력해주세요.');
                signMessageElement.textContent = "아이디를 입력해주세요.";
                signMessageElement.style.color = "red";
                throw Error;
            }else if(password_input1.length === 0 || password_input2.length === 0){
                toastr.warning('비밀번호를 입력해주세요.');
                signMessageElement.textContent = "비밀번호를 입력해주세요.";
                signMessageElement.style.color = "red";
                throw Error;
            }else if(password_input1 !== password_input2){
                toastr.error('입력된 비밀번호가 일치하지 않습니다.');
                signMessageElement.textContent = "입력된 비밀번호가 일치하지 않습니다.";
                signMessageElement.style.color = "red";
                throw Error;
            }else if(!policy){
                toastr.error('필수 약관을 동의해주세요.');
                throw Error;
            }

            if(password_input1 === password_input2){
                const validationResult1 = validatePassword(password_input1);
                const validationResult2 = validatePassword(password_input2);
                if(validationResult1 !== true && validationResult2 !== true) {
                    toastr.error(validationResult1 || validationResult2);
                    throw Error;
                }
            }

            const signUpURL = "/api/ko-jy/up/sign/";
            const e = 'OP_Guest';
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


    passwordInput.addEventListener('keyup', validatePasswords);
    passwordCheckInput.addEventListener('keyup', validatePasswords);

    function validatePasswords() {
        const password_input1 = passwordInput.value;
        const password_input2 = passwordCheckInput.value;

        if(password_input1 !== password_input2){
            signMessageElement.textContent = "Passwords do not match.";
            signMessageElement.style.color = "red";
        }else{
            signMessageElement.textContent = "Passwords do match.";
            signMessageElement.style.color = "green";
        }
    }

    function validatePassword(password){
        const minLength = 8;
        const hasUpperCase = /[A-Z]/;
        const hasLowerCase = /[a-z]/;
        const hasDigits = /\d/;
        const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;

        if (password.length < minLength) {
            return "비밀번호는 최소 8자 이상이어야 합니다.";
        }
        if (!hasUpperCase.test(password)) {
            return "비밀번호에는 최소 하나의 대문자가 포함되어야 합니다.";
        }
        if (!hasLowerCase.test(password)) {
            return "비밀번호에는 최소 하나의 소문자가 포함되어야 합니다.";
        }
        if (!hasDigits.test(password)) {
            return "비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.";
        }
        if (!hasSpecialChar.test(password)) {
            return "비밀번호에는 최소 하나의 특수 문자가 포함되어야 합니다.";
        }

        return true;
    }

});