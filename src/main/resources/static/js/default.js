
    function goFetch(url, options = {}) {
        const defaultHeaders = {
            'JY-ACCESS-TOKEN': getCookieValue('JY-ACCESS-TOKEN'),
        }

        const headers = new Headers({...defaultHeaders, ...options.headers});

        const newOptions = {...options, headers};

        return fetch(url, newOptions);
    }

    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('custom-li-a').addEventListener('click', function (event) {
            event.preventDefault();
            const url = event.target.href;
            goFetch(url)
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    }
                })
                .then(data =>{

                    document.open();
                    document.write(data);
                    history.pushState({}, '', url);
                    document.close();

                    // const tempElement = document.createElement('div');
                    // tempElement.innerHTML = data;
                    //
                    // const bodyContent = tempElement.querySelector('#editor-container').innerHTML;
                    //
                    // const script = document.createElement("script");
                    // script.type = 'text/javascript';
                    // script.src = '/static/js/editor.js';
                    //
                    // document.getElementById('content').innerHTML = bodyContent;
                    // history.pushState({}, '', url);
                    // document.head.appendChild(script);
                })
                .catch(error => {

                });
        });
    });


