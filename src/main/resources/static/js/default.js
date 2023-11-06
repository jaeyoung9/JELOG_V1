
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
                                imgTag.style = 'width: 500px; height:auto; object-fit:cover;';
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

    function blobFilesToImg(data) {
        return new Promise((resolve, reject) => {
            if (data.contentThumbnail != null && data.files && data.files[0]) {
                try {
                    const base64Data = data.files[0].resultFile;

                    const imgTag = new Image();
                    imgTag.onload = function () {
                        imgTag.alt = data.contentTitle;
                        resolve(imgTag);
                    };
                    imgTag.onerror = function () {
                        reject('Error loading image');
                    };
                    imgTag.src = `data:${data.files.mediaType};base64,` + base64Data;
                } catch (error) {
                    reject('Error creating image');
                }
            } else {
                resolve('null');
            }
        });
    }



    function DataToURL(data){
        const url = new URL(window.location);
        url.hash = '#ct=' + data + ',#sq=null';

        window.history.pushState(null, null, url.toString());
        location.reload(url.toString());
    }

    function SearchToURL(data){
        const url = new URL(window.location);

        let ct_index = '';
        if(url.hash !== '')
        {
            ct_index = DataFromURL().split(',')[0].split('ct=').pop();
        }

        if(DataFromURL() === '')
        { url.hash = '#ct=null,#sq=' + data;}
        else if(ct_index !== 'null'){ url.hash = '#ct=' + ct_index + ',#sq=' + data;}
        else{ url.hash = '#ct=null,#sq=' + data; }

        window.history.pushState(null, null, url.toString());
        location.reload(url.toString());
    }

    function DataFromURL(){
        const url = new URL(window.location);
        const data = url.hash.slice(1);
        return data;
    }

    function toggleForm(formToShow) {
        if (formToShow === 'login') {
            document.getElementById('login-container').style.display = 'block';
            document.getElementById('signup-container').style.display = 'none';
        } else if (formToShow === 'signup') {
            document.getElementById('login-container').style.display = 'none';
            document.getElementById('signup-container').style.display = 'block';
        }
    }

