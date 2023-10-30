
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

                    // if(!data.ok){
                    //     window.location.href = '/api/view/public/mains';
                    // }

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

function blobToFile(blob, fileName){
    return new File([blob], fileName);
}

function replaceBlobUrls(content) {
    const imgTags = content.match(/<img src="blob:(.*?)"/g);
    if (imgTags) {
        for (const imgTag of imgTags) {
            const matches = /<img src="blob:(.*?)"/.exec(imgTag);
            if (matches && matches[1]) {
                const blobUrl = matches[0]; // Get the Blob URL
                // Replace the img tag with the Blob URL
                content = content.replace(imgTag, `<img src="${blobUrl}"`);
            }
        }
    }
    return content;
}


