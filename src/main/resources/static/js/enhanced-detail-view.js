/**
 * Enhanced Detail View for Jelog Blog
 * Advanced post detail page with modern features
 */

class EnhancedDetailView {
    constructor() {
        this.contentNumber = null;
        this.postData = null;
        this.isLiked = false;
        this.isBookmarked = false;
        this.fontSize = 1;
        this.isDarkMode = false;
        this.currentUser = null;
        
        this.init();
    }
    
    init() {
        console.log('Initializing Enhanced Detail View...');
        
        this.bindEvents();
        this.setupReadingProgress();
        this.setupTableOfContents();
        this.setupKeyboardShortcuts();
        this.loadUserPreferences();
        this.setupIntersectionObserver();
    }
    
    async loadPost(contentNumber) {
        console.log('Loading post:', contentNumber);
        this.contentNumber = contentNumber;
        
        try {
            this.showLoading();
            
            const response = await fetch(`/api/public/mains/relay/${contentNumber}/`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload?.data) {
                this.postData = data.payload.data;
                this.renderPost(this.postData);
                this.loadRelatedPosts();
                this.renderComments(this.postData.comments || []);
                this.updateReadingTime();
                this.generateTableOfContents();
            } else {
                this.showError('게시물을 불러올 수 없습니다.');
            }
        } catch (error) {
            console.error('Error loading post:', error);
            this.showError('게시물을 불러오는 중 오류가 발생했습니다.');
        } finally {
            this.hideLoading();
        }
    }
    
    renderPost(post) {
        console.log('Rendering post:', post);
        
        // Update breadcrumb
        this.updateBreadcrumb(post);
        
        // Update meta information
        document.getElementById('postCategory').innerHTML = `
            <i class="fas fa-tag"></i>
            ${this.getCategoryName(post.contentCategories)}
        `;
        
        document.getElementById('postDate').innerHTML = `
            <i class="fas fa-calendar-alt"></i>
            ${this.formatDate(post.publishedDate || post.inDate)}
        `;
        
        // Update title
        document.getElementById('postTitle').textContent = post.contentTitle;
        document.getElementById('titleBreadcrumb').textContent = post.contentTitle;
        document.title = `${post.contentTitle} - Jelog`;
        
        // Update stats
        document.getElementById('postViews').textContent = post.views || 0;
        document.getElementById('postLikes').textContent = post.likes || 0;
        document.getElementById('postComments').textContent = (post.comments || []).length;
        
        // Update thumbnail if exists
        if (post.contentThumbnail) {
            const thumbnailContainer = document.getElementById('postThumbnail');
            const thumbnailImage = document.getElementById('thumbnailImage');
            thumbnailImage.src = post.contentThumbnail;
            thumbnailImage.alt = post.contentTitle;
            thumbnailContainer.style.display = 'block';
        }
        
        // Update content body
        const postBody = document.getElementById('postBody');
        postBody.innerHTML = this.processContent(post.contentBody);
        
        // Update tags if available
        if (post.tags && post.tags.length > 0) {
            this.renderTags(post.tags);
        }
        
        // Show the post container
        document.getElementById('postContainer').style.display = 'block';
        
        // Add animations
        this.addAnimations();
        
        // Update meta tags for social sharing
        this.updateMetaTags(post);
    }
    
    processContent(content) {
        if (!content) return '';
        
        // Process images with better handling
        content = this.processImages(content);
        
        // Process code blocks
        content = this.processCodeBlocks(content);
        
        // Process links
        content = this.processLinks(content);
        
        // Process headings for TOC
        content = this.processHeadings(content);
        
        return content;
    }
    
    processImages(content) {
        // Add loading attributes and improve image handling
        return content.replace(/<img([^>]*)>/g, (match, attrs) => {
            if (!attrs.includes('loading=')) {
                attrs += ' loading="lazy"';
            }
            if (!attrs.includes('class=')) {
                attrs += ' class="post-image"';
            }
            return `<img${attrs}>`;
        });
    }
    
    processCodeBlocks(content) {
        // Enhanced code block processing
        return content.replace(/<pre><code([^>]*)>([\s\S]*?)<\/code><\/pre>/g, (match, attrs, code) => {
            const language = attrs.match(/class="language-(\w+)"/);
            const langClass = language ? ` language-${language[1]}` : '';
            
            return `
                <div class="code-block">
                    <div class="code-header">
                        <span class="code-language">${language ? language[1] : 'code'}</span>
                        <button class="copy-code-btn" onclick="copyCode(this)">
                            <i class="fas fa-copy"></i>
                            복사
                        </button>
                    </div>
                    <pre><code class="hljs${langClass}">${code}</code></pre>
                </div>
            `;
        });
    }
    
    processLinks(content) {
        // Process external links
        return content.replace(/<a([^>]*href="[^"]*"[^>]*)>/g, (match, attrs) => {
            if (attrs.includes('http') && !attrs.includes(window.location.hostname)) {
                if (!attrs.includes('target=')) {
                    attrs += ' target="_blank" rel="noopener noreferrer"';
                }
                if (!attrs.includes('class=')) {
                    attrs += ' class="external-link"';
                }
            }
            return `<a${attrs}>`;
        });
    }
    
    processHeadings(content) {
        // Add IDs to headings for TOC
        return content.replace(/<(h[1-6])([^>]*)>(.*?)<\/h[1-6]>/g, (match, tag, attrs, text) => {
            const id = this.generateHeadingId(text);
            if (!attrs.includes('id=')) {
                attrs += ` id="${id}"`;
            }
            return `<${tag}${attrs}>${text}</${tag}>`;
        });
    }
    
    generateHeadingId(text) {
        return text
            .replace(/<[^>]*>/g, '') // Remove HTML tags
            .replace(/[^\w\s가-힣]/g, '') // Keep only word characters and Korean
            .replace(/\s+/g, '-') // Replace spaces with hyphens
            .toLowerCase()
            .substring(0, 50); // Limit length
    }
    
    generateTableOfContents() {
        const tocContent = document.getElementById('tocContent');
        if (!tocContent) return;
        
        const headings = document.querySelectorAll('.post-body h1, .post-body h2, .post-body h3, .post-body h4');
        
        if (headings.length === 0) {
            document.querySelector('.table-of-contents').style.display = 'none';
            return;
        }
        
        let tocHTML = '';
        headings.forEach((heading, index) => {
            const level = parseInt(heading.tagName.substring(1));
            const text = heading.textContent;
            const id = heading.id || `heading-${index}`;
            
            if (!heading.id) {
                heading.id = id;
            }
            
            const indent = (level - 1) * 1; // 1rem per level
            tocHTML += `
                <a href="#${id}" class="toc-item" style="padding-left: ${indent}rem" data-target="${id}">
                    ${text}
                </a>
            `;
        });
        
        tocContent.innerHTML = tocHTML;
        
        // Add click events for smooth scrolling
        tocContent.querySelectorAll('.toc-item').forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const targetId = item.dataset.target;
                const targetElement = document.getElementById(targetId);
                if (targetElement) {
                    targetElement.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                    this.updateActiveTocItem(targetId);
                }
            });
        });
    }
    
    updateActiveTocItem(activeId) {
        document.querySelectorAll('.toc-item').forEach(item => {
            item.classList.remove('active');
        });
        
        const activeItem = document.querySelector(`[data-target="${activeId}"]`);
        if (activeItem) {
            activeItem.classList.add('active');
        }
    }
    
    renderTags(tags) {
        const tagsContainer = document.getElementById('postTags');
        const tagsList = document.getElementById('tagsList');
        
        if (!tags || tags.length === 0) return;
        
        const tagsHTML = tags.map(tag => `
            <a href="/search?tag=${encodeURIComponent(tag)}" class="tag-item">
                <i class="fas fa-tag"></i>
                ${tag}
            </a>
        `).join('');
        
        tagsList.innerHTML = tagsHTML;
        tagsContainer.style.display = 'block';
    }
    
    async renderComments(comments) {
        const commentsList = document.getElementById('commentsList');
        const commentsCount = document.getElementById('commentsCount');
        
        commentsCount.textContent = comments.length;
        
        if (comments.length === 0) {
            commentsList.innerHTML = `
                <div class="no-comments">
                    <i class="fas fa-comment"></i>
                    <h4>첫 번째 댓글을 작성해보세요!</h4>
                    <p>여러분의 소중한 의견을 기다리고 있습니다.</p>
                </div>
            `;
            return;
        }
        
        const commentsHTML = comments.map(comment => `
            <div class="comment-item">
                <div class="comment-header">
                    <div class="comment-author">
                        <i class="fas fa-user-circle"></i>
                        ${comment.commentName || 'Anonymous'}
                    </div>
                    <div class="comment-date">
                        ${this.formatDate(comment.inDate)}
                    </div>
                </div>
                <div class="comment-content">
                    ${comment.commentBody}
                </div>
            </div>
        `).join('');
        
        commentsList.innerHTML = commentsHTML;
    }
    
    async loadRelatedPosts() {
        try {
            const category = this.postData?.contentCategories;
            if (!category) return;
            
            const response = await fetch(`/api/public/mains/?page=0&size=3&Categories=${category}`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload?.data?.content) {
                const relatedPosts = data.payload.data.content
                    .filter(post => post.contentId !== this.contentNumber)
                    .slice(0, 3);
                
                this.renderRelatedPosts(relatedPosts);
            }
        } catch (error) {
            console.error('Error loading related posts:', error);
        }
    }
    
    renderRelatedPosts(posts) {
        const relatedPostsGrid = document.getElementById('relatedPostsGrid');
        
        if (!posts || posts.length === 0) {
            document.getElementById('relatedPosts').style.display = 'none';
            return;
        }
        
        const postsHTML = posts.map(post => `
            <div class="related-post-card" onclick="navigateToPost(${post.contentId})">
                <img class="related-post-image" 
                     src="${post.contentThumbnail || '/static/images/default-thumbnail.jpg'}" 
                     alt="${post.contentTitle}"
                     loading="lazy">
                <div class="related-post-content">
                    <h4 class="related-post-title">${post.contentTitle}</h4>
                    <p class="related-post-excerpt">
                        ${post.contentSummary || this.extractExcerpt(post.contentBody)}
                    </p>
                    <div class="related-post-meta">
                        <span>${this.formatDate(post.publishedDate || post.inDate)}</span>
                        <span>
                            <i class="fas fa-eye"></i>
                            ${post.views || 0}
                        </span>
                    </div>
                </div>
            </div>
        `).join('');
        
        relatedPostsGrid.innerHTML = postsHTML;
    }
    
    setupReadingProgress() {
        window.addEventListener('scroll', () => {
            const postBody = document.querySelector('.post-body');
            if (!postBody) return;
            
            const scrollTop = window.pageYOffset;
            const docHeight = document.documentElement.scrollHeight - window.innerHeight;
            const scrollPercent = (scrollTop / docHeight) * 100;
            
            const progressBar = document.getElementById('progressBar');
            if (progressBar) {
                progressBar.style.width = `${Math.min(scrollPercent, 100)}%`;
            }
            
            // Update active TOC item based on scroll position
            this.updateTocOnScroll();
        });
    }
    
    updateTocOnScroll() {
        const headings = document.querySelectorAll('.post-body h1, .post-body h2, .post-body h3, .post-body h4');
        const scrollTop = window.pageYOffset + 100;
        
        let activeHeading = null;
        headings.forEach(heading => {
            if (heading.offsetTop <= scrollTop) {
                activeHeading = heading.id;
            }
        });
        
        if (activeHeading) {
            this.updateActiveTocItem(activeHeading);
        }
    }
    
    setupTableOfContents() {
        // TOC will be generated after content is loaded
    }
    
    setupKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            // Escape key to close floating menu
            if (e.key === 'Escape') {
                this.closeFloatingMenu();
            }
            
            // L key to toggle like
            if (e.key === 'l' || e.key === 'L') {
                if (!this.isInputFocused()) {
                    this.toggleLike();
                }
            }
            
            // B key to toggle bookmark
            if (e.key === 'b' || e.key === 'B') {
                if (!this.isInputFocused()) {
                    this.toggleBookmark();
                }
            }
            
            // D key to toggle dark mode
            if (e.key === 'd' || e.key === 'D') {
                if (!this.isInputFocused()) {
                    this.toggleDarkMode();
                }
            }
            
            // + key to increase font size
            if (e.key === '+' || e.key === '=') {
                if (!this.isInputFocused()) {
                    this.increaseFontSize();
                }
            }
            
            // - key to decrease font size
            if (e.key === '-') {
                if (!this.isInputFocused()) {
                    this.decreaseFontSize();
                }
            }
        });
    }
    
    isInputFocused() {
        const activeElement = document.activeElement;
        return activeElement && (
            activeElement.tagName === 'INPUT' || 
            activeElement.tagName === 'TEXTAREA' ||
            activeElement.contentEditable === 'true'
        );
    }
    
    setupIntersectionObserver() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('fade-in');
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        });
        
        // Observe elements that should animate on scroll
        setTimeout(() => {
            const elements = document.querySelectorAll(
                '.post-body > *, .related-post-card, .comment-item'
            );
            elements.forEach(el => observer.observe(el));
        }, 100);
    }
    
    bindEvents() {
        // Comment form submission
        const commentForm = document.getElementById('commentForm');
        if (commentForm) {
            commentForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitComment();
            });
        }
        
        // Character count for comment textarea
        const commentText = document.getElementById('commentText');
        if (commentText) {
            commentText.addEventListener('input', () => {
                const count = commentText.value.length;
                document.getElementById('charCount').textContent = count;
                
                if (count > 500) {
                    commentText.value = commentText.value.substring(0, 500);
                    document.getElementById('charCount').textContent = 500;
                }
            });
        }
        
        // Floating menu toggle
        const floatingMenuBtn = document.querySelector('.floating-btn.main-btn');
        if (floatingMenuBtn) {
            floatingMenuBtn.addEventListener('click', () => {
                this.toggleFloatingMenu();
            });
        }
    }
    
    async submitComment() {
        const commentName = document.getElementById('commentName').value.trim();
        const commentText = document.getElementById('commentText').value.trim();
        const commentEmail = document.getElementById('commentEmail').value.trim();
        
        if (!commentName || !commentText) {
            this.showNotification('이름과 댓글 내용을 입력해주세요.', 'warning');
            return;
        }
        
        const submitBtn = document.querySelector('.submit-btn');
        const originalText = submitBtn.innerHTML;
        
        try {
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 등록 중...';
            submitBtn.disabled = true;
            
            const commentData = {
                contentId: this.contentNumber,
                commentName: commentName,
                commentBody: commentText,
                commentEmail: commentEmail
            };
            
            const response = await fetch(`/api/public/mains/relay/${this.contentNumber}/comments/`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(commentData)
            });
            
            const result = await response.json();
            
            if (result.result === 'SUCCESS') {
                this.showNotification('댓글이 등록되었습니다.', 'success');
                
                // Clear form
                document.getElementById('commentForm').reset();
                document.getElementById('charCount').textContent = '0';
                
                // Reload comments
                this.loadPost(this.contentNumber);
            } else {
                this.showNotification('댓글 등록에 실패했습니다.', 'error');
            }
        } catch (error) {
            console.error('Error submitting comment:', error);
            this.showNotification('댓글 등록 중 오류가 발생했습니다.', 'error');
        } finally {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }
    }
    
    toggleLike() {
        const likeBtn = document.getElementById('likeBtn');
        const likesCount = document.getElementById('postLikes');
        
        if (this.isLiked) {
            likeBtn.classList.remove('active');
            likesCount.textContent = parseInt(likesCount.textContent) - 1;
            this.showNotification('좋아요를 취소했습니다.', 'info');
        } else {
            likeBtn.classList.add('active');
            likesCount.textContent = parseInt(likesCount.textContent) + 1;
            this.showNotification('좋아요를 눌렀습니다!', 'success');
        }
        
        this.isLiked = !this.isLiked;
        this.saveUserPreferences();
    }
    
    toggleBookmark() {
        this.isBookmarked = !this.isBookmarked;
        
        if (this.isBookmarked) {
            this.showNotification('북마크에 추가했습니다.', 'success');
        } else {
            this.showNotification('북마크에서 제거했습니다.', 'info');
        }
        
        this.saveUserPreferences();
    }
    
    sharePost() {
        if (navigator.share) {
            navigator.share({
                title: this.postData?.contentTitle || document.title,
                text: this.postData?.contentSummary || '흥미로운 게시물을 공유합니다.',
                url: window.location.href
            });
        } else {
            this.copyLink();
        }
    }
    
    shareToTwitter() {
        const text = encodeURIComponent(`${this.postData?.contentTitle || document.title} - Jelog`);
        const url = encodeURIComponent(window.location.href);
        window.open(`https://twitter.com/intent/tweet?text=${text}&url=${url}`, '_blank');
    }
    
    shareToFacebook() {
        const url = encodeURIComponent(window.location.href);
        window.open(`https://www.facebook.com/sharer/sharer.php?u=${url}`, '_blank');
    }
    
    shareToLinkedIn() {
        const url = encodeURIComponent(window.location.href);
        const title = encodeURIComponent(this.postData?.contentTitle || document.title);
        window.open(`https://www.linkedin.com/sharing/share-offsite/?url=${url}&title=${title}`, '_blank');
    }
    
    copyLink() {
        navigator.clipboard.writeText(window.location.href).then(() => {
            this.showNotification('링크가 클립보드에 복사되었습니다.', 'success');
        }).catch(() => {
            this.showNotification('링크 복사에 실패했습니다.', 'error');
        });
    }
    
    toggleFloatingMenu() {
        const floatingActions = document.getElementById('floatingActions');
        floatingActions.classList.toggle('active');
    }
    
    closeFloatingMenu() {
        const floatingActions = document.getElementById('floatingActions');
        floatingActions.classList.remove('active');
    }
    
    scrollToTop() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }
    
    scrollToComments() {
        const commentsSection = document.getElementById('commentsSection');
        if (commentsSection) {
            commentsSection.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    }
    
    toggleDarkMode() {
        this.isDarkMode = !this.isDarkMode;
        document.documentElement.setAttribute('data-theme', this.isDarkMode ? 'dark' : 'light');
        
        const darkModeBtn = document.querySelector('.floating-btn[onclick="toggleDarkMode()"]');
        if (darkModeBtn) {
            const icon = darkModeBtn.querySelector('i');
            icon.className = this.isDarkMode ? 'fas fa-sun' : 'fas fa-moon';
        }
        
        this.showNotification(
            this.isDarkMode ? '다크 모드가 활성화되었습니다.' : '라이트 모드가 활성화되었습니다.',
            'info'
        );
        
        this.saveUserPreferences();
    }
    
    increaseFontSize() {
        if (this.fontSize < 1.5) {
            this.fontSize += 0.1;
            this.applyFontSize();
            this.showNotification('글자 크기가 증가했습니다.', 'info');
            this.saveUserPreferences();
        }
    }
    
    decreaseFontSize() {
        if (this.fontSize > 0.8) {
            this.fontSize -= 0.1;
            this.applyFontSize();
            this.showNotification('글자 크기가 감소했습니다.', 'info');
            this.saveUserPreferences();
        }
    }
    
    applyFontSize() {
        const postBody = document.querySelector('.post-body');
        if (postBody) {
            postBody.style.fontSize = `${this.fontSize}rem`;
        }
    }
    
    updateBreadcrumb(post) {
        const categoryBreadcrumb = document.getElementById('categoryBreadcrumb');
        if (categoryBreadcrumb) {
            categoryBreadcrumb.textContent = this.getCategoryName(post.contentCategories);
        }
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
    
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }
    
    extractExcerpt(content, length = 150) {
        if (!content) return '';
        const text = content.replace(/<[^>]*>/g, '');
        return text.length > length ? text.substring(0, length) + '...' : text;
    }
    
    updateReadingTime() {
        const postBody = document.querySelector('.post-body');
        if (!postBody) return;
        
        const text = postBody.textContent || '';
        const wordsPerMinute = 200; // Average reading speed
        const wordCount = text.trim().split(/\s+/).length;
        const readingTime = Math.ceil(wordCount / wordsPerMinute);
        
        const readingTimeElement = document.getElementById('readingTime');
        if (readingTimeElement) {
            readingTimeElement.innerHTML = `
                <i class="fas fa-clock"></i>
                ${readingTime}분 읽기
            `;
        }
    }
    
    updateMetaTags(post) {
        // Update meta tags for better social sharing
        const description = post.contentSummary || this.extractExcerpt(post.contentBody);
        
        // Update existing meta tags or create new ones
        this.updateMetaTag('description', description);
        this.updateMetaTag('og:title', post.contentTitle);
        this.updateMetaTag('og:description', description);
        this.updateMetaTag('og:url', window.location.href);
        
        if (post.contentThumbnail) {
            this.updateMetaTag('og:image', post.contentThumbnail);
        }
    }
    
    updateMetaTag(name, content) {
        let meta = document.querySelector(`meta[name="${name}"], meta[property="${name}"]`);
        if (!meta) {
            meta = document.createElement('meta');
            meta.setAttribute(name.startsWith('og:') ? 'property' : 'name', name);
            document.head.appendChild(meta);
        }
        meta.setAttribute('content', content);
    }
    
    addAnimations() {
        // Add staggered animations to content elements
        const elements = document.querySelectorAll('.post-body > *');
        elements.forEach((element, index) => {
            element.style.animationDelay = `${index * 0.1}s`;
            element.classList.add('slide-in-left');
        });
    }
    
    showLoading() {
        document.getElementById('postLoading').style.display = 'flex';
        document.getElementById('postContainer').style.display = 'none';
    }
    
    hideLoading() {
        document.getElementById('postLoading').style.display = 'none';
    }
    
    showError(message) {
        document.getElementById('postLoading').innerHTML = `
            <div class="error-container">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>오류가 발생했습니다</h3>
                <p>${message}</p>
                <button onclick="location.reload()" class="retry-btn">
                    <i class="fas fa-redo"></i>
                    다시 시도
                </button>
            </div>
        `;
    }
    
    showNotification(message, type = 'info') {
        if (typeof toastr !== 'undefined') {
            toastr[type](message);
        } else {
            // Fallback notification
            console.log(`${type.toUpperCase()}: ${message}`);
            
            // Create simple notification
            const notification = document.createElement('div');
            notification.className = `notification notification-${type}`;
            notification.textContent = message;
            notification.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                padding: 1rem 1.5rem;
                background: var(--primary-color);
                color: white;
                border-radius: var(--radius-lg);
                box-shadow: var(--shadow-lg);
                z-index: 10000;
                animation: slideInRight 0.3s ease;
            `;
            
            document.body.appendChild(notification);
            
            setTimeout(() => {
                notification.remove();
            }, 3000);
        }
    }
    
    loadUserPreferences() {
        try {
            const prefs = localStorage.getItem('jelog-user-preferences');
            if (prefs) {
                const preferences = JSON.parse(prefs);
                this.fontSize = preferences.fontSize || 1;
                this.isDarkMode = preferences.isDarkMode || false;
                this.isLiked = preferences.likedPosts?.includes(this.contentNumber) || false;
                this.isBookmarked = preferences.bookmarkedPosts?.includes(this.contentNumber) || false;
                
                // Apply preferences
                if (this.isDarkMode) {
                    this.toggleDarkMode();
                }
                this.applyFontSize();
            }
        } catch (error) {
            console.error('Error loading user preferences:', error);
        }
    }
    
    saveUserPreferences() {
        try {
            const prefs = JSON.parse(localStorage.getItem('jelog-user-preferences') || '{}');
            
            prefs.fontSize = this.fontSize;
            prefs.isDarkMode = this.isDarkMode;
            prefs.likedPosts = prefs.likedPosts || [];
            prefs.bookmarkedPosts = prefs.bookmarkedPosts || [];
            
            // Update liked posts
            if (this.isLiked && !prefs.likedPosts.includes(this.contentNumber)) {
                prefs.likedPosts.push(this.contentNumber);
            } else if (!this.isLiked) {
                prefs.likedPosts = prefs.likedPosts.filter(id => id !== this.contentNumber);
            }
            
            // Update bookmarked posts
            if (this.isBookmarked && !prefs.bookmarkedPosts.includes(this.contentNumber)) {
                prefs.bookmarkedPosts.push(this.contentNumber);
            } else if (!this.isBookmarked) {
                prefs.bookmarkedPosts = prefs.bookmarkedPosts.filter(id => id !== this.contentNumber);
            }
            
            localStorage.setItem('jelog-user-preferences', JSON.stringify(prefs));
        } catch (error) {
            console.error('Error saving user preferences:', error);
        }
    }
}

// Global instance
let enhancedDetailView;

// Initialize function
function initializeEnhancedDetailView(contentNumber) {
    enhancedDetailView = new EnhancedDetailView();
    enhancedDetailView.loadPost(contentNumber);
}

// Global functions for onclick handlers
function toggleLike() {
    if (enhancedDetailView) enhancedDetailView.toggleLike();
}

function toggleBookmark() {
    if (enhancedDetailView) enhancedDetailView.toggleBookmark();
}

function sharePost() {
    if (enhancedDetailView) enhancedDetailView.sharePost();
}

function shareToTwitter() {
    if (enhancedDetailView) enhancedDetailView.shareToTwitter();
}

function shareToFacebook() {
    if (enhancedDetailView) enhancedDetailView.shareToFacebook();
}

function shareToLinkedIn() {
    if (enhancedDetailView) enhancedDetailView.shareToLinkedIn();
}

function copyLink() {
    if (enhancedDetailView) enhancedDetailView.copyLink();
}

function toggleFloatingMenu() {
    if (enhancedDetailView) enhancedDetailView.toggleFloatingMenu();
}

function scrollToTop() {
    if (enhancedDetailView) enhancedDetailView.scrollToTop();
}

function scrollToComments() {
    if (enhancedDetailView) enhancedDetailView.scrollToComments();
}

function toggleDarkMode() {
    if (enhancedDetailView) enhancedDetailView.toggleDarkMode();
}

function increaseFontSize() {
    if (enhancedDetailView) enhancedDetailView.increaseFontSize();
}

function decreaseFontSize() {
    if (enhancedDetailView) enhancedDetailView.decreaseFontSize();
}

function navigateToPost(contentId) {
    window.location.href = `/api/view/public/mains/relay/${contentId}/`;
}

function copyCode(button) {
    const codeBlock = button.closest('.code-block');
    const code = codeBlock.querySelector('code').textContent;
    
    navigator.clipboard.writeText(code).then(() => {
        button.innerHTML = '<i class="fas fa-check"></i> 복사됨';
        setTimeout(() => {
            button.innerHTML = '<i class="fas fa-copy"></i> 복사';
        }, 2000);
    }).catch(() => {
        console.error('Failed to copy code');
    });
}