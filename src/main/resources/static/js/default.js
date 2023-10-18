
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
                    const tempElement = document.createElement('div');
                    tempElement.innerHTML = data;

                    const bodyContent = tempElement.querySelector('#content').innerHTML;

                    document.getElementById('content').innerHTML = bodyContent;
                    history.pushState({}, '', url);
                })
                .catch(error => {

                });
        });
    });


