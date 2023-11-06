document.addEventListener('DOMContentLoaded', function () {
    const contentTitle = document.getElementById('title-input');
    const imageUploadInput = document.getElementById('image-upload-input');
    const selectEl = document.getElementById('selectEl');
    const textField = document.querySelector('div[name="textField"]');
    const buttons = document.querySelectorAll('div[name="formatting-buttons"] button');
    const imageUploadButton = document.getElementById('image-upload-button');
    const submitButton = document.getElementById('submit-button');

    let showCode = false;
    let contentThumbnail = '';
    textField.contentEditable = "true";

    const buttonCommands = {
        createLink: 'createLink',
        showCode: 'showCode',
    };

    function executeCommand(cmd, value = null) {
        document.execCommand(cmd, false, value);
    }

    class DT_Files {
        constructor() {
            this.fileId = 0;
            this.filePath = '';
            this.fileName = '';
            this.mediaType = '';
            this.resultFile = [];
            this.content = null;
        }
    }

    class DT_Content {
        constructor() {
            this.contentId = 0;
            this.contentCategories = '';
            this.contentThumbnail = '';
            this.contentTitle = '';
            this.contentBody = '';
            this.views = 0;
            this.files = [];
            this.comments = [];
        }
    }

    const content = new DT_Content();
    const newFiles = [];
    const newFilesPath = [];



    imageUploadButton.addEventListener('click', () => {
        imageUploadInput.click();
    });

    imageUploadInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {

            const reader = new FileReader();
            reader.onload = (event) => {

                const arrayBufferView = new Uint8Array(event.target.result);
                const blob = new Blob([arrayBufferView], { type: file.type });
                const imageUrl = URL.createObjectURL(blob);


                const img = document.createElement('img');
                img.src = imageUrl;
                img.alt = 'Image';

                insertImageElement(img);

                newFilesPath.push(imageUrl);
                newFiles.push(file);
            };
            reader.readAsArrayBuffer(file);
        }
    });

    function insertImageElement(img) {
        const range = window.getSelection().getRangeAt(0);
        range.collapse(false);
        range.insertNode(img);
    }

    buttons.forEach(button => {
        button.addEventListener("click", async (e) => {
            const cmd = button.getAttribute('data-cmd');

            switch (cmd) {
                case buttonCommands.createLink:
                    const url = prompt('Enter Link URL:', "https://");
                    if (url) {
                        executeCommand(cmd, url);
                    }
                    break;

                case buttonCommands.showCode:
                    toggleCodeView();
                    break;

                default:
                    if (cmdIsSupported(cmd)) {
                        executeCommand(cmd);
                    }
                    break;
            }
        });
    });

    function insertHTML(html) {
        document.execCommand('insertHTML', false, html);
    }

    function toggleCodeView() {
        if (showCode) {
            textField.innerHTML = textField.textContent;
        } else {
            textField.textContent = textField.innerHTML;
        }
        showCode = !showCode;
    }

    function cmdIsSupported(cmd) {
        const supportedCommands = [
            'bold', 'italic', 'underline', 'justifyLeft',
            'justifyCenter', 'justifyFull', 'justifyRight',
            'insertOrderedList', 'insertUnorderedList'
        ];
        return supportedCommands.includes(cmd);
    }



    submitButton.addEventListener('click', function () {
        content.contentCategories = selectEl.value;
        content.contentTitle = contentTitle.value;
        content.contentBody = showCode ? textField.textContent : textField.innerHTML;
        content.views = 0;


        const fileConversionPromises = newFiles.map((file, index) => {
            return new Promise((resolve, reject) => {
                const dtFile = new DT_Files();
                dtFile.filePath = `<img src="${newFilesPath[index]}" />`;
                dtFile.fileName = file.name;
                dtFile.mediaType = file.type;

                const reader = new FileReader();
                reader.onload = function () {

                    const arrayBuffer = reader.result;
                    const byteArray = new Uint8Array(arrayBuffer);
                    dtFile.resultFile = Array.from(byteArray);
                    resolve(dtFile);
                };
                reader.onerror = reject;
                reader.readAsArrayBuffer(file);
            });
        });


        Promise.all(fileConversionPromises)
            .then(dtFiles => {

                try{

                    if(content.contentTitle === ''){
                        toastr.warning('제목을 입력하세요.');
                        throw Error;
                    }else if(content.contentBody === ''){
                        toastr.warning('본문을 입력하세요.');
                        throw Error;
                    }

                    content.files = dtFiles;
                    const url = '/api/auth/cwo/action';
                    return goFetch(url, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(content),
                    });
                }catch (error){

                }

            })
            .then(response => response.json())
            .then(data => {
                if(data.result.code == '200'){
                    toastr.success('등록완료.');
                    window.location.href = '/api/view/public/mains/';
                }else{
                    throw Error;
                }

            })
            .catch(error => {
                toastr.error('알수없는 오류.');
            });
    });

});
