
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

                })
                .catch(error => {

                });
        });
    });

    function limitContentLength(contentBody) {
        if (contentBody.length > 20) {
            return contentBody.replace(/<img[^>]*>/g, '').trim().substring(0,20) + '<span class="content-more">(자세히)</span>';
        }
        return contentBody;
    }

    function findImgInBody(contentBody, files){
        const imgRegex = /<img[^>]*>/g;

        if(contentBody != null && files != null){
            const searchImg = contentBody.match(imgRegex);

            if (searchImg) {
                searchImg.forEach((img) => {
                    const src = img.match(/src="([^"]+)"/);
                    if (src) {
                        const imgSrcDataPart = src[1].substring(src[1].indexOf("blob")).split('"')[0];

                        files.forEach((file) => {
                            const filePath = file.filePath;
                            const filePathDataPart = filePath.substring(filePath.indexOf("blob")).split('"')[0];

                            if (imgSrcDataPart === filePathDataPart) {
                                const imgTag = new Image();
                                imgTag.src = `data:${file.mediaType};base64,` + file.resultFile;
                                contentBody = contentBody.replace(img, imgTag.outerHTML);
                            } else {
                                contentBody = contentBody.replace(img, '(이미지 보기 오류)');
                            }
                        });
                    }
                });
            }

            return contentBody;
        }
    }


