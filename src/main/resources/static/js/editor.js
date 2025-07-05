/**
 * Enhanced Editor JavaScript
 * Modern post editor with improved functionality
 */

class PostEditor {
    constructor() {
        this.initElements();
        this.initEventListeners();
        this.initTagsSystem();
        this.initCharCounters();
        this.initAutoSave();
        
        this.tags = [];
        this.isCodeView = false;
        this.originalContent = '';
        
        console.log('PostEditor initialized');
    }

    initElements() {
        this.titleInput = document.getElementById('title-input');
        this.textArea = document.getElementById('text-area');
        this.categorySelect = document.getElementById('selectEl');
        this.tagsInput = document.getElementById('tags-input');
        this.tagsList = document.getElementById('tags-list');
        this.summaryInput = document.getElementById('summary-input');
        this.submitButton = document.getElementById('submit-button');
        this.previewButton = document.getElementById('preview-button');
        this.imageUploadInput = document.getElementById('image-upload-input');
        this.imageUploadButton = document.getElementById('image-upload-button');
        this.formattingButtons = document.querySelectorAll('.formatting-button');
        this.previewModal = document.getElementById('preview-modal');
        this.wordCountSpan = document.getElementById('word-count');
        this.titleCounter = document.getElementById('title-counter');
        this.summaryCounter = document.getElementById('summary-counter');
    }

    initEventListeners() {
        // Formatting buttons
        this.formattingButtons.forEach(button => {
            button.addEventListener('click', (e) => {
                e.preventDefault();
                const cmd = button.getAttribute('data-cmd');
                this.executeCommand(cmd, button);
            });
        });

        // Submit button
        this.submitButton.addEventListener('click', () => this.submitPost());
        
        // Preview button
        this.previewButton.addEventListener('click', () => this.showPreview());
        
        // Image upload
        this.imageUploadButton.addEventListener('click', () => {
            this.imageUploadInput.click();
        });
        
        this.imageUploadInput.addEventListener('change', (e) => {
            this.handleImageUpload(e.target.files);
        });

        // Keyboard shortcuts
        this.textArea.addEventListener('keydown', (e) => {
            this.handleKeyboardShortcuts(e);
        });

        // Word count
        this.textArea.addEventListener('input', () => {
            this.updateWordCount();
        });

        // Auto-resize text area based on content
        this.textArea.addEventListener('input', () => {
            this.autoResize();
        });

        // Drag and drop for images
        this.textArea.addEventListener('dragover', (e) => {
            e.preventDefault();
            this.textArea.classList.add('drag-over');
        });

        this.textArea.addEventListener('dragleave', () => {
            this.textArea.classList.remove('drag-over');
        });

        this.textArea.addEventListener('drop', (e) => {
            e.preventDefault();
            this.textArea.classList.remove('drag-over');
            
            const files = Array.from(e.dataTransfer.files).filter(file => 
                file.type.startsWith('image/')
            );
            
            if (files.length > 0) {
                this.handleImageUpload(files);
            }
        });

        // Paste event for images
        this.textArea.addEventListener('paste', (e) => {
            const items = e.clipboardData.items;
            for (let item of items) {
                if (item.type.startsWith('image/')) {
                    e.preventDefault();
                    const file = item.getAsFile();
                    this.handleImageUpload([file]);
                    break;
                }
            }
        });
    }

    initTagsSystem() {
        this.tagsInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ',') {
                e.preventDefault();
                this.addTag(this.tagsInput.value.trim());
                this.tagsInput.value = '';
            }
        });

        this.tagsInput.addEventListener('blur', () => {
            if (this.tagsInput.value.trim()) {
                this.addTag(this.tagsInput.value.trim());
                this.tagsInput.value = '';
            }
        });
    }

    initCharCounters() {
        this.titleInput.addEventListener('input', () => {
            this.titleCounter.textContent = this.titleInput.value.length;
        });

        this.summaryInput.addEventListener('input', () => {
            this.summaryCounter.textContent = this.summaryInput.value.length;
        });
    }

    initAutoSave() {
        // Auto-save every 30 seconds
        setInterval(() => {
            this.autoSave();
        }, 30000);

        // Save on page unload
        window.addEventListener('beforeunload', () => {
            this.autoSave();
        });
    }

    executeCommand(cmd, button) {
        this.textArea.focus();

        switch (cmd) {
            case 'bold':
            case 'italic':
            case 'underline':
            case 'strikeThrough':
                document.execCommand(cmd);
                break;
                
            case 'justifyLeft':
            case 'justifyCenter':
            case 'justifyRight':
            case 'justifyFull':
                document.execCommand(cmd);
                break;
                
            case 'insertOrderedList':
            case 'insertUnorderedList':
                document.execCommand(cmd);
                break;
                
            case 'indent':
            case 'outdent':
                document.execCommand(cmd);
                break;
                
            case 'undo':
            case 'redo':
                document.execCommand(cmd);
                break;
                
            case 'createLink':
                this.insertLink();
                break;
                
            case 'insertImage':
                this.imageUploadInput.click();
                break;
                
            case 'insertCode':
                this.insertCodeBlock();
                break;
                
            case 'showCode':
                this.toggleCodeView(button);
                break;
        }

        this.updateButtonStates();
    }

    insertLink() {
        const selection = window.getSelection();
        const selectedText = selection.toString();
        
        const url = prompt('링크 URL을 입력하세요:', 'https://');
        if (url && url !== 'https://') {
            if (selectedText) {
                document.execCommand('createLink', false, url);
            } else {
                const linkText = prompt('링크 텍스트를 입력하세요:', url);
                if (linkText) {
                    document.execCommand('insertHTML', false, `<a href="${url}" target="_blank">${linkText}</a>`);
                }
            }
        }
    }

    insertCodeBlock() {
        const code = prompt('코드를 입력하세요:');
        if (code) {
            const codeBlock = `<pre><code>${this.escapeHtml(code)}</code></pre>`;
            document.execCommand('insertHTML', false, codeBlock);
        }
    }

    toggleCodeView(button) {
        this.isCodeView = !this.isCodeView;
        
        if (this.isCodeView) {
            // Switch to code view
            this.originalContent = this.textArea.innerHTML;
            this.textArea.textContent = this.textArea.innerHTML;
            this.textArea.style.fontFamily = 'monospace';
            button.classList.add('active');
        } else {
            // Switch back to WYSIWYG view
            this.textArea.innerHTML = this.textArea.textContent;
            this.textArea.style.fontFamily = '';
            button.classList.remove('active');
        }
    }

    async handleImageUpload(files) {
        for (const file of files) {
            if (!file.type.startsWith('image/')) continue;

            try {
                // Show loading indicator
                const loadingText = `<span class="image-loading">이미지 업로드 중... (${file.name})</span>`;
                document.execCommand('insertHTML', false, loadingText);

                // Create FormData for upload
                const formData = new FormData();
                formData.append('file', file);

                // Upload image
                const token = this.getAuthToken();
                const headers = {};
                
                // Add token to headers if available
                if (token) {
                    headers['JY-ACCESS-TOKEN'] = token;
                }
                
                const response = await fetch('/api/files/images', {
                    method: 'POST',
                    body: formData,
                    headers: headers
                });

                if (response.ok) {
                    const data = await response.json();
                    if (data.result === 'SUCCESS') {
                        // Replace loading text with actual image
                        const imageHtml = `<img src="${data.payload.fileUrl}" alt="${file.name}" style="max-width: 100%; height: auto; margin: 1rem 0;">`;
                        this.textArea.innerHTML = this.textArea.innerHTML.replace(loadingText, imageHtml);
                    } else {
                        throw new Error(data.message || 'Upload failed');
                    }
                } else {
                    throw new Error('Upload failed');
                }

            } catch (error) {
                console.error('Image upload failed:', error);
                // Replace loading text with error message
                const errorText = `<span style="color: red;">이미지 업로드 실패: ${file.name}</span>`;
                this.textArea.innerHTML = this.textArea.innerHTML.replace(
                    `<span class="image-loading">이미지 업로드 중... (${file.name})</span>`,
                    errorText
                );
                
                // Show user-friendly error
                this.showNotification('이미지 업로드에 실패했습니다.', 'error');
            }
        }
    }

    addTag(tagText) {
        if (!tagText || this.tags.includes(tagText) || this.tags.length >= 10) return;
        
        this.tags.push(tagText);
        this.renderTags();
    }

    removeTag(tagText) {
        this.tags = this.tags.filter(tag => tag !== tagText);
        this.renderTags();
    }

    renderTags() {
        this.tagsList.innerHTML = this.tags.map(tag => `
            <span class="tag-item">
                ${tag}
                <span class="tag-remove" onclick="window.postEditor.removeTag('${tag}')">&times;</span>
            </span>
        `).join('');
    }

    updateWordCount() {
        const text = this.textArea.innerText || this.textArea.textContent || '';
        const wordCount = text.trim().length;
        this.wordCountSpan.textContent = wordCount.toLocaleString();
    }

    updateButtonStates() {
        // Update button active states based on current selection
        const commands = ['bold', 'italic', 'underline', 'strikeThrough'];
        
        commands.forEach(cmd => {
            const button = document.querySelector(`[data-cmd="${cmd}"]`);
            if (button) {
                if (document.queryCommandState(cmd)) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
            }
        });
    }

    handleKeyboardShortcuts(e) {
        if (e.ctrlKey || e.metaKey) {
            switch (e.key) {
                case 'b':
                    e.preventDefault();
                    document.execCommand('bold');
                    break;
                case 'i':
                    e.preventDefault();
                    document.execCommand('italic');
                    break;
                case 'u':
                    e.preventDefault();
                    document.execCommand('underline');
                    break;
                case 's':
                    e.preventDefault();
                    this.autoSave();
                    break;
            }
        }
    }

    autoResize() {
        // Auto-resize text area if needed
        this.textArea.style.height = 'auto';
        this.textArea.style.height = Math.max(500, this.textArea.scrollHeight) + 'px';
    }

    showPreview() {
        const title = this.titleInput.value || 'Untitled';
        const content = this.textArea.innerHTML;
        const category = this.categorySelect.value;
        const summary = this.summaryInput.value;
        
        const previewBody = document.getElementById('preview-body');
        previewBody.innerHTML = `
            <div class="preview-post">
                <div class="preview-meta">
                    <span class="preview-category">${this.getCategoryName(category)}</span>
                    <span class="preview-tags">${this.tags.join(', ')}</span>
                </div>
                <h1 class="preview-title">${title}</h1>
                ${summary ? `<p class="preview-summary">${summary}</p>` : ''}
                <div class="preview-content">${content}</div>
            </div>
        `;
        
        this.previewModal.style.display = 'flex';
    }

    async submitPost() {
        // Validate required fields
        if (!this.titleInput.value.trim()) {
            this.showNotification('제목을 입력해주세요.', 'error');
            this.titleInput.focus();
            return;
        }

        if (!this.textArea.innerHTML.trim()) {
            this.showNotification('내용을 입력해주세요.', 'error');
            this.textArea.focus();
            return;
        }

        // Map frontend category values to backend enum TITLE values (not names)
        const categoryTitleMapping = {
            'Java': 'Java_Categories',
            'JavaScript': 'JavaScript_Categories', 
            'Python': 'Python',
            'C': 'C_Categories',
            'Shell': 'ShellScript_Categories',
            'Security': 'Security_Categories',
            'DeveloperCareerSkills': 'DeveloperCareerSkills_Categories',
            'Other': 'Other_Categories'
        };
        
        const selectedCategory = this.categorySelect.value || 'Other';
        const enumTitle = categoryTitleMapping[selectedCategory] || 'Other_Categories';
        
        console.log('Selected category:', selectedCategory, '-> Enum title:', enumTitle);

        // Prepare post data
        const postData = {
            contentTitle: this.titleInput.value.trim(),
            contentBody: this.textArea.innerHTML,
            contentCategories: enumTitle, // Use enum title that matches @JsonCreator
            contentSummary: this.summaryInput.value.trim(),
            tags: this.tags,
            views: 0,
            status: 'PUBLISHED',
            allowComments: true,
            featured: false,
            author: '작성자',
            files: [] // Add empty files array
        };

        try {
            // Show loading state
            this.submitButton.disabled = true;
            this.submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 게시 중...';

            // Submit post
            const token = this.getAuthToken();
            console.log('Auth token:', token ? 'Present' : 'Missing');
            
            const headers = {
                'Content-Type': 'application/json'
            };
            
            // Add token to headers if available
            if (token) {
                headers['JY-ACCESS-TOKEN'] = token;
            }
            
            console.log('Submitting post data:', postData);
            console.log('Request headers:', headers);
            
            const response = await fetch('/api/auth/cwo/action', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(postData)
            });

            console.log('Response status:', response.status);
            
            const data = await response.json();
            console.log('Response data:', data);

            if (data.result === 'SUCCESS') {
                this.showNotification('게시물이 성공적으로 발행되었습니다!', 'success');
                
                // Clear auto-save
                this.clearAutoSave();
                
                // Redirect to main page
                setTimeout(() => {
                    window.location.href = '/api/view/public/pages/main';
                }, 1500);
                
            } else {
                throw new Error(data.message || '게시물 발행에 실패했습니다.');
            }

        } catch (error) {
            console.error('Post submission failed:', error);
            this.showNotification('게시물 발행에 실패했습니다. 다시 시도해주세요.', 'error');
        } finally {
            // Reset button state
            this.submitButton.disabled = false;
            this.submitButton.innerHTML = '<i class="fas fa-paper-plane"></i> 게시물 발행';
        }
    }

    autoSave() {
        const draftData = {
            title: this.titleInput.value,
            content: this.textArea.innerHTML,
            category: this.categorySelect.value,
            summary: this.summaryInput.value,
            tags: this.tags,
            timestamp: Date.now()
        };

        localStorage.setItem('post_draft', JSON.stringify(draftData));
        console.log('Draft auto-saved');
    }

    loadDraft() {
        const savedDraft = localStorage.getItem('post_draft');
        if (savedDraft) {
            const draft = JSON.parse(savedDraft);
            
            // Only load if draft is less than 24 hours old
            if (Date.now() - draft.timestamp < 24 * 60 * 60 * 1000) {
                if (confirm('저장된 임시 초안이 있습니다. 불러오시겠습니까?')) {
                    this.titleInput.value = draft.title || '';
                    this.textArea.innerHTML = draft.content || '';
                    this.categorySelect.value = draft.category || '';
                    this.summaryInput.value = draft.summary || '';
                    this.tags = draft.tags || [];
                    this.renderTags();
                    this.updateWordCount();
                }
            }
        }
    }

    clearAutoSave() {
        localStorage.removeItem('post_draft');
    }

    getCategoryName(category) {
        const categoryMap = {
            'Java': 'Java',
            'JavaScript': 'JavaScript',
            'Python': 'Python',
            'C': 'C/C++',
            'Shell': 'Shell Script',
            'Security': '보안',
            'DeveloperCareerSkills': '개발자 커리어',
            'Other': '기타'
        };
        return categoryMap[category] || '기타';
    }

    getAuthToken() {
        // Get JWT token from cookie
        return this.getCookie('JY-ACCESS-TOKEN') || '';
    }

    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    showNotification(message, type = 'info') {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <i class="fas fa-${type === 'error' ? 'exclamation-circle' : type === 'success' ? 'check-circle' : 'info-circle'}"></i>
            ${message}
        `;
        
        // Add to page
        document.body.appendChild(notification);
        
        // Show with animation
        setTimeout(() => notification.classList.add('show'), 100);
        
        // Remove after 5 seconds
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => document.body.removeChild(notification), 300);
        }, 5000);
    }

    escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, m => map[m]);
    }
}

// Global functions for inline event handlers
function closePreview() {
    document.getElementById('preview-modal').style.display = 'none';
}

function saveDraft() {
    if (window.postEditor) {
        window.postEditor.autoSave();
        window.postEditor.showNotification('임시저장이 완료되었습니다.', 'success');
    }
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    window.postEditor = new PostEditor();
    
    // Load any saved draft
    window.postEditor.loadDraft();
    
    // Add notification styles
    const notificationStyles = `
        <style>
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 1rem 1.5rem;
            border-radius: 8px;
            color: white;
            font-weight: 500;
            z-index: 10000;
            transform: translateX(100%);
            transition: transform 0.3s ease;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            max-width: 400px;
        }
        
        .notification.show {
            transform: translateX(0);
        }
        
        .notification-success {
            background: #10b981;
        }
        
        .notification-error {
            background: #ef4444;
        }
        
        .notification-info {
            background: #3b82f6;
        }
        
        .text-area.drag-over {
            border: 2px dashed #3b82f6 !important;
            background: rgba(59, 130, 246, 0.05);
        }
        
        .image-loading {
            background: #f3f4f6;
            padding: 0.5rem;
            border-radius: 4px;
            color: #6b7280;
            font-style: italic;
        }
        </style>
    `;
    
    document.head.insertAdjacentHTML('beforeend', notificationStyles);
});