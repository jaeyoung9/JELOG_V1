document.addEventListener('DOMContentLoaded', function () {
    const textField = document.querySelector('div[name="textField"]');
    const btnForm = document.querySelector('div[name="formatting-buttons"]');
    let showCode = false;

    textField.contentEditable = "true";

    btnForm.addEventListener("click", async (e) => {
        const cmd = e.target.getAttribute('data-cmd');

        switch (cmd) {
            case 'justifyLeft':
            case 'justifyCenter':
            case 'justifyFull':
            case 'justifyRight':
            case 'bold':
            case 'italic':
            case 'underline':
            case 'insertOrderedList':
            case 'insertUnorderedList':
                alert(cmd)
                document.execCommand(cmd, false, null);
                break;

            case 'createLink':
                const url = prompt('Enter Link URL:', "https://");
                if (url) {
                    document.execCommand(cmd, false, url);
                }
                break;

            case 'insertImage':
                const imageUrl = prompt('Enter Image URL:', "https://");
                if (imageUrl) {
                    const imgHTML = `<img src="${imageUrl}" alt="Image">`;
                    document.execCommand('insertHTML', false, imgHTML);
                }
                break;

            case 'showCode':
                if (showCode) {
                    textField.innerHTML = textField.textContent;
                    showCode = false;
                } else {
                    textField.textContent = textField.innerHTML;
                    showCode = true;
                }
                break;

            default:
                break;
        }
    });
});
