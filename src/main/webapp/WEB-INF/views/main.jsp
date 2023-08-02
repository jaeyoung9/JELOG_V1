<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
<h1>Main Page</h1>


<script>
    const apiUrl = "/api/public/mains/";
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const result = data.result;
            const payload = data.payload;
            // Json Data
            document.getElementById("code").innerText = result.code;
            document.getElementById("message").innerText = result.message;
            document.getElementById("response_time").innerText = result.response_time;
            document.getElementById("request_action").innerText = result.request_action;

            document.getElementById("test").innerText = payload.test;
            document.getElementById("test1").innerText = payload.test1;
            document.getElementById("test2").innerText = payload.test2;
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
</script>

<!-- Display the result here -->
<div>
    <p>result: <span id="code"></span></p>
    <p>result: <span id="message"></span></p>
    <p>result: <span id="response_time"></span></p>
    <p>result: <span id="request_action"></span></p>
    <p>Test: <span id="test"></span></p>
    <p>Test1: <span id="test1"></span></p>
    <p>Test2: <span id="test2"></span></p>
</div>
</body>
</html>