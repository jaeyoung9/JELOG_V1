document.addEventListener('DOMContentLoaded', function () {
    const contentTitle = document.getElementById('title-input');
    const contentBody = document.getElementById('text-area');
    const imageUploadInput = document.getElementById('image-upload-input');
    const selectEl = document.getElementById('selectEl');
    const textField = document.querySelector('div[name="textField"]');
    const buttons = document.querySelectorAll('div[name="formatting-buttons"] button');
    let showCode = false;
    let contentThumbnail = '';
    textField.contentEditable = "true";

    // Define DT_Files class to match the structure you want
    class DT_Files {
        constructor() {
            this.fileId = 0;
            this.filePath = '';
            this.fileName = '';
            this.mediaType = '';
            this.resultFile = []; // Initialize resultFile as an empty array
            this.content = null; // Initialize content as null
        }
    }

    // Define DT_Content class to match the structure you want
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

    const content = new DT_Content(); // Create a new instance of DT_Content
    const newFiles = []; // Initialize an array to store new files

    // Add an event listener to the "insertImage" button
    const imageUploadButton = document.getElementById('image-upload-button');
    imageUploadButton.addEventListener('click', () => {
        imageUploadInput.click();
    });

    imageUploadInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            // Handle the uploaded image here
            const reader = new FileReader();
            reader.onload = (event) => {
                // Convert the ArrayBuffer to a Base64 data URL
                const arrayBufferView = new Uint8Array(event.target.result);
                const blob = new Blob([arrayBufferView], { type: file.type });
                const imageUrl = URL.createObjectURL(blob);

                // Create an image element and set its source
                const img = document.createElement('img');
                img.src = imageUrl;
                img.alt = 'Image';

                // Insert the image into the content
                insertImageElement(img);

                // Store the file in the newFiles array
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
                case 'createLink':
                    const url = prompt('Enter Link URL:', "https://");
                    if (url) {
                        executeCommand(cmd, url);
                    }
                    break;

                case 'showCode':
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

    function executeCommand(cmd, value = null) {
        document.execCommand(cmd, false, value);
    }

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

    const submitButton = document.getElementById('submit-button');

    submitButton.addEventListener('click', function () {
        content.contentCategories = selectEl.value;
        content.contentTitle = contentTitle.value;
        content.contentBody = showCode ? textField.textContent : textField.innerHTML;
        content.contentThumbnail = contentThumbnail;
        content.views = 0;

        // Create an array to store promises for file conversions
        const fileConversionPromises = newFiles.map(file => {
            return new Promise((resolve, reject) => {
                const dtFile = new DT_Files();
                dtFile.filePath = `<img src="${URL.createObjectURL(file)}" />`;
                dtFile.fileName = file.name;
                dtFile.mediaType = file.type;

                const reader = new FileReader();
                reader.onload = function () {
                    // Convert the ArrayBuffer to a byte array
                    const arrayBuffer = reader.result;
                    const byteArray = new Uint8Array(arrayBuffer);
                    dtFile.resultFile = Array.from(byteArray);
                    resolve(dtFile);
                };
                reader.onerror = reject;
                reader.readAsArrayBuffer(file);
            });
        });

        // Wait for all file conversions to complete
        Promise.all(fileConversionPromises)
            .then(dtFiles => {
                content.files = dtFiles;

                // Send the content object as JSON to the server
                return goFetch('/api/auth/cwo/action', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(content),
                });
            })
            .then(response => response.json())
            .then(data => {
                // Handle the response data if needed
                console.log(data);
            })
            .catch(error => {
                // Handle any errors here
                console.error(error);
            });
    });
});
