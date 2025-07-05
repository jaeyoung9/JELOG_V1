/**
 * JELOG - Consolidated Application JavaScript
 * Handles all main functionality including category filtering, post loading, and UI interactions
 */

class JelogApp {
    constructor() {
        console.log('JelogApp constructor called'); // Debug log
        this.currentPage = 0;
        this.currentCategory = null;
        this.currentSort = 'latest';
        this.isLoading = false;
        this.searchTimeout = null;
        
        console.log('JelogApp state initialized, calling init()...'); // Debug log
        this.init();
    }
    
    init() {
        console.log('JelogApp init() called'); // Debug log
        this.bindEvents();
        console.log('Events bound, loading initial data...'); // Debug log
        this.loadInitialData();
    }
    
    bindEvents() {
        // Category filter buttons
        document.querySelectorAll('.category-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                e.stopPropagation();
                this.handleCategoryFilter(e.target.closest('.category-btn'));
            });
        });
        
        // Sort dropdown
        const sortSelect = document.getElementById('sortSelect');
        if (sortSelect) {
            sortSelect.addEventListener('change', (e) => {
                this.handleSortChange(e.target.value);
            });
        }
        
        // Search functionality
        const searchInput = document.getElementById('main-search-input');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                this.handleSearch(e.target.value);
            });
            
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.performSearch(e.target.value);
                }
            });
        }
        
        // Hero search button
        const heroSearchBtn = document.querySelector('.hero-search-button');
        if (heroSearchBtn) {
            heroSearchBtn.addEventListener('click', () => {
                const input = document.getElementById('main-search-input');
                if (input) {
                    this.performSearch(input.value);
                }
            });
        }
        
        // Load more button
        const loadMoreBtn = document.getElementById('loadMoreBtn');
        if (loadMoreBtn) {
            loadMoreBtn.addEventListener('click', () => {
                this.loadMorePosts();
            });
        }
        
        // Window events
        window.addEventListener('scroll', () => this.handleScroll());
        window.addEventListener('resize', () => this.handleResize());
    }
    
    handleCategoryFilter(button) {
        if (this.isLoading) return;
        
        // Update UI state
        document.querySelectorAll('.category-btn').forEach(btn => {
            btn.classList.remove('active');
            btn.setAttribute('aria-pressed', 'false');
        });
        
        const category = button.dataset.category;
        
        if (this.currentCategory === category) {
            // Clicking same category - show all
            this.currentCategory = null;
            button.classList.remove('active');
        } else {
            // New category selected
            this.currentCategory = category;
            button.classList.add('active');
            button.setAttribute('aria-pressed', 'true');
        }
        
        // Reset pagination and load filtered content
        this.currentPage = 0;
        this.loadPosts(true);
        
        // Update URL without reload
        this.updateURL();
    }
    
    handleSortChange(sortValue) {
        if (this.isLoading) return;
        
        this.currentSort = sortValue;
        
        // Reset pagination and reload with new sort
        this.currentPage = 0;
        this.loadPosts(true);
        
        // Update URL without reload
        this.updateURL();
    }
    
    handleSearch(query) {
        // Clear previous timeout
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }
        
        // Show suggestions if query length > 1
        if (query.length > 1) {
            this.searchTimeout = setTimeout(() => {
                this.loadSearchSuggestions(query);
            }, 300);
        } else {
            this.hideSuggestions();
        }
    }
    
    performSearch(query) {
        if (!query || query.trim().length < 2) return;
        
        // Hide suggestions
        this.hideSuggestions();
        
        // Navigate to search results
        window.location.href = `/api/view/public/search?q=${encodeURIComponent(query.trim())}`;
    }
    
    async loadSearchSuggestions(query) {
        try {
            const response = await fetch(`/api/search/suggestions?query=${encodeURIComponent(query)}`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload.suggestions) {
                this.showSuggestions(data.payload.suggestions);
            }
        } catch (error) {
            console.error('Error loading search suggestions:', error);
        }
    }
    
    showSuggestions(suggestions) {
        const container = document.getElementById('searchSuggestions');
        if (!container) return;
        
        if (suggestions.length === 0) {
            container.style.display = 'none';
            return;
        }
        
        container.innerHTML = suggestions.map(suggestion => 
            `<div class="suggestion-item" onclick="jelogApp.performSearch('${suggestion}')">${suggestion}</div>`
        ).join('');
        
        container.style.display = 'block';
    }
    
    hideSuggestions() {
        const container = document.getElementById('searchSuggestions');
        if (container) {
            container.style.display = 'none';
        }
    }
    
    async loadInitialData() {
        // Load categories first
        await this.loadCategories();
        
        // Load initial statistics
        await this.loadStatistics();
        
        // Load initial posts
        await this.loadPosts(true);
    }
    
    async loadCategories() {
        try {
            const response = await fetch('/api/public/categories');
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                this.renderCategoryButtons(data.payload.data);
            }
        } catch (error) {
            console.error('Error loading categories:', error);
            // Use fallback categories if API fails
            this.renderFallbackCategories();
        }
    }
    
    renderCategoryButtons(categories) {
        const container = document.querySelector('.category-filters');
        if (!container) return;
        
        // Clear existing buttons except "All"
        const existingButtons = container.querySelectorAll('.category-btn:not([data-category=""])');
        existingButtons.forEach(btn => btn.remove());
        
        // Add dynamic category buttons
        categories.forEach(category => {
            const button = document.createElement('button');
            button.className = 'category-btn';
            button.setAttribute('data-category', category.slug);
            button.setAttribute('aria-pressed', 'false');
            
            button.innerHTML = `
                <i class="${category.icon || 'fas fa-folder'}"></i>
                <span>${category.name}</span>
            `;
            
            // Set color if available
            if (category.color) {
                button.style.setProperty('--category-color', category.color);
            }
            
            // Add event listener
            button.addEventListener('click', (e) => {
                e.preventDefault();
                e.stopPropagation();
                this.handleCategoryFilter(button);
            });
            
            container.appendChild(button);
        });
    }
    
    renderFallbackCategories() {
        // Keep existing hardcoded categories as fallback
        console.log('Using fallback categories');
    }

    async loadStatistics() {
        try {
            const response = await fetch('/api/public/stats');
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                this.updateHeroStats(data.payload.data);
            }
        } catch (error) {
            console.error('Error loading statistics:', error);
        }
    }
    
    updateHeroStats(stats) {
        const elements = {
            totalPosts: document.getElementById('totalPosts'),
            totalCategories: document.getElementById('totalCategories'),
            totalViews: document.getElementById('totalViews')
        };
        
        if (elements.totalPosts) elements.totalPosts.textContent = stats.totalPosts || 0;
        if (elements.totalCategories) elements.totalCategories.textContent = stats.totalCategories || 8;
        if (elements.totalViews) elements.totalViews.textContent = (stats.totalViews || 0).toLocaleString();
    }
    
    async loadPosts(reset = false) {
        if (this.isLoading) return;
        
        this.isLoading = true;
        this.showLoading();
        
        try {
            const params = new URLSearchParams({
                page: reset ? 0 : this.currentPage,
                size: 10,
                sort: this.currentSort
            });
            
            if (this.currentCategory) {
                params.append('Categories', this.currentCategory);
            }
            
            console.log('Loading posts with params:', params.toString()); // Debug log
            const response = await fetch(`/api/public/mains/?${params}`);
            console.log('Response status:', response.status); // Debug log
            
            const data = await response.json();
            console.log('Response data:', data); // Debug log
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                console.log('Posts loaded successfully:', data.payload.data.content?.length || 0, 'posts'); // Debug log
                if (reset) {
                    this.renderPosts(data.payload.data);
                } else {
                    this.appendPosts(data.payload.data);
                }
                this.updatePagination(data.payload.data);
            } else {
                console.warn('API response issue:', data); // Debug log
                this.showError('게시물을 불러올 수 없습니다.');
            }
        } catch (error) {
            console.error('Error loading posts:', error);
            this.showError('게시물을 불러오는 중 오류가 발생했습니다.');
        } finally {
            this.isLoading = false;
            this.hideLoading();
        }
    }
    
    renderPosts(pageData) {
        const container = document.getElementById('contentList');
        if (!container) return;
        
        const posts = pageData.content || [];
        
        if (posts.length === 0) {
            container.innerHTML = `
                <div class="no-posts">
                    <i class="fas fa-inbox"></i>
                    <h3>게시물이 없습니다</h3>
                    <p>선택한 카테고리에 게시물이 없습니다.</p>
                </div>
            `;
            return;
        }
        
        container.innerHTML = posts.map(post => this.createPostHTML(post)).join('');
        this.updateContentCount(pageData.totalElements || posts.length);
        
        // Add click handlers to post cards
        this.addPostClickHandlers(container);
    }
    
    appendPosts(pageData) {
        const container = document.getElementById('contentList');
        if (!container) return;
        
        const posts = pageData.content || [];
        const postsHTML = posts.map(post => this.createPostHTML(post)).join('');
        container.insertAdjacentHTML('beforeend', postsHTML);
        
        // Add click handlers to newly added posts
        this.addPostClickHandlers(container);
    }
    
    createPostHTML(post) {
        const date = new Date(post.publishedDate || post.inDate).toLocaleDateString('ko-KR');
        const category = this.getCategoryDisplayName(post.contentCategories);
        const summary = post.contentSummary || (post.contentBody?.substring(0, 150) + '...' || '');
        
        return `
            <article class="content-page" data-post-id="${post.contentId}">
                <div class="content-thumbnail-parents">
                    <img class="content-thumbnail" 
                         src="${post.contentThumbnail || ''}" 
                         alt="${post.contentTitle}"
                         onerror="this.src=''"
                    />
                </div>
                <div class="content-info">
                    <div class="content-category">${category}</div>
                    <h3 class="content-title">
                        <a href="/api/view/public/mains/relay/${post.contentId}/">${post.contentTitle}</a>
                    </h3>
                    <p class="content-excerpt">${summary}</p>
                    <div class="content-meta">
                        <div class="content-author">
                            <div class="content-author-avatar">${(post.author || 'Admin')[0].toUpperCase()}</div>
                            <span>${post.author || 'Admin'}</span>
                        </div>
                        <div class="content-stats">
                            <div class="content-stat">
                                <i class="fas fa-eye"></i>
                                <span>${post.views || 0}</span>
                            </div>
                            <div class="content-stat">
                                <i class="fas fa-comments"></i>
                                <span>${post.commentCount || 0}</span>
                            </div>
                            <div class="content-stat">
                                <i class="fas fa-calendar"></i>
                                <span>${date}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        `;
    }
    
    getCategoryDisplayName(category) {
        const categoryMap = {
            'Java_Categories': 'Java',
            'JavaScript_Categories': 'JavaScript',
            'Python': 'Python',
            'C_Categories': 'C/C++',
            'ShellScript_Categories': 'Shell',
            'Security_Categories': 'Security',
            'DeveloperCareerSkills_Categories': 'Career',
            'Other_Categories': 'Other'
        };
        return categoryMap[category] || category || 'Other';
    }
    
    updatePagination(pageData) {
        const loadMoreBtn = document.getElementById('loadMoreBtn');
        if (!loadMoreBtn) return;
        
        if (pageData.last || (pageData.content && pageData.content.length < 10)) {
            loadMoreBtn.style.display = 'none';
        } else {
            loadMoreBtn.style.display = 'block';
        }
    }
    
    updateContentCount(total) {
        const countElement = document.getElementById('contentCount');
        if (countElement) {
            countElement.textContent = `총 ${total.toLocaleString()}개의 게시물`;
        }
    }
    
    loadMorePosts() {
        this.currentPage++;
        this.loadPosts(false);
    }
    
    showLoading() {
        const loadingElement = document.getElementById('loadingState');
        if (loadingElement) {
            loadingElement.style.display = 'block';
        }
    }
    
    hideLoading() {
        const loadingElement = document.getElementById('loadingState');
        if (loadingElement) {
            loadingElement.style.display = 'none';
        }
    }
    
    showError(message) {
        const container = document.getElementById('contentList');
        if (container) {
            container.innerHTML = `
                <div class="error-state">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h3>오류 발생</h3>
                    <p>${message}</p>
                    <button onclick="jelogApp.loadPosts(true)" class="retry-btn">다시 시도</button>
                </div>
            `;
        }
    }
    
    updateURL() {
        const params = new URLSearchParams();
        
        if (this.currentCategory) {
            params.append('category', this.currentCategory);
        }
        
        if (this.currentSort && this.currentSort !== 'latest') {
            params.append('sort', this.currentSort);
        }
        
        const newURL = window.location.pathname + (params.toString() ? '?' + params.toString() : '');
        window.history.replaceState(null, '', newURL);
    }
    
    handleScroll() {
        // Implement infinite scroll or scroll-to-top functionality
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        // Show/hide scroll to top button
        const scrollTopBtn = document.getElementById('scrollTopBtn');
        if (scrollTopBtn) {
            if (scrollTop > 300) {
                scrollTopBtn.style.display = 'block';
            } else {
                scrollTopBtn.style.display = 'none';
            }
        }
    }
    
    handleResize() {
        // Handle responsive behavior if needed
    }
    
    scrollToTop() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }
}

// Enhanced Detail View functionality
class EnhancedDetailView {
    constructor(contentId) {
        this.contentId = contentId;
        this.postData = null;
        this.darkMode = localStorage.getItem('darkMode') === 'true';
        
        this.init();
    }
    
    async init() {
        this.setupDarkMode();
        this.bindEvents();
        await this.loadPostData();
        this.setupReadingProgress();
        this.generateTableOfContents();
        this.setupKeyboardShortcuts();
    }
    
    setupDarkMode() {
        if (this.darkMode) {
            document.body.classList.add('dark-mode');
        }
    }
    
    bindEvents() {
        // Dark mode toggle
        const darkModeBtn = document.getElementById('darkModeBtn');
        if (darkModeBtn) {
            darkModeBtn.addEventListener('click', () => this.toggleDarkMode());
        }
        
        // Social sharing
        document.querySelectorAll('.share-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                this.sharePost(btn.dataset.platform);
            });
        });
        
        // Print functionality
        const printBtn = document.getElementById('printBtn');
        if (printBtn) {
            printBtn.addEventListener('click', () => window.print());
        }
        
        // Font size controls
        const fontSizeControls = document.querySelectorAll('.font-size-btn');
        fontSizeControls.forEach(btn => {
            btn.addEventListener('click', () => this.changeFontSize(btn.dataset.size));
        });
    }
    
    async loadPostData() {
        try {
            const response = await fetch(`/api/public/mains/relay/${this.contentId}/`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                this.postData = data.payload.data;
                this.renderPostContent();
                await this.loadRelatedPosts();
                this.showPostContainer();
            } else {
                this.showError('게시물을 찾을 수 없습니다.');
            }
        } catch (error) {
            console.error('Error loading post:', error);
            this.showError('게시물을 불러오는 중 오류가 발생했습니다.');
        }
    }
    
    renderPostContent() {
        if (!this.postData) return;
        
        // Update page title
        document.title = `${this.postData.contentTitle} - Jelog`;
        
        // Update breadcrumb
        const categoryBreadcrumb = document.getElementById('categoryBreadcrumb');
        if (categoryBreadcrumb) {
            categoryBreadcrumb.textContent = this.getCategoryDisplayName(this.postData.contentCategories);
        }
        
        const titleBreadcrumb = document.getElementById('titleBreadcrumb');
        if (titleBreadcrumb) {
            titleBreadcrumb.textContent = this.postData.contentTitle;
        }
        
        // Update post meta
        document.getElementById('postCategory').innerHTML = `
            <i class="fas fa-tag"></i>
            ${this.getCategoryDisplayName(this.postData.contentCategories)}
        `;
        
        document.getElementById('postDate').innerHTML = `
            <i class="fas fa-calendar-alt"></i>
            ${new Date(this.postData.publishedDate || this.postData.inDate).toLocaleDateString('ko-KR')}
        `;
        
        document.getElementById('readingTime').innerHTML = `
            <i class="fas fa-clock"></i>
            ${this.calculateReadingTime(this.postData.contentBody)} 분 읽기
        `;
        
        // Update post title and content
        document.getElementById('postTitle').textContent = this.postData.contentTitle;
        document.getElementById('postContent').innerHTML = this.postData.contentBody;
        
        // Update post stats
        document.getElementById('postViews').textContent = (this.postData.views || 0).toLocaleString();
    }
    
    getCategoryDisplayName(category) {
        const categoryMap = {
            'Java_Categories': 'Java',
            'JavaScript_Categories': 'JavaScript',
            'Python': 'Python',
            'C_Categories': 'C/C++',
            'ShellScript_Categories': 'Shell',
            'Security_Categories': 'Security',
            'DeveloperCareerSkills_Categories': 'Career',
            'Other_Categories': 'Other'
        };
        return categoryMap[category] || category || 'Other';
    }
    
    calculateReadingTime(content) {
        const wordsPerMinute = 200;
        const words = content.replace(/<[^>]*>/g, '').split(/\s+/).length;
        return Math.ceil(words / wordsPerMinute);
    }
    
    generateTableOfContents() {
        const content = document.getElementById('postContent');
        const tocContainer = document.getElementById('tableOfContents');
        
        if (!content || !tocContainer) return;
        
        const headings = content.querySelectorAll('h1, h2, h3, h4, h5, h6');
        
        if (headings.length === 0) {
            tocContainer.style.display = 'none';
            return;
        }
        
        const tocList = document.createElement('ul');
        tocList.className = 'toc-list';
        
        headings.forEach((heading, index) => {
            const id = `heading-${index}`;
            heading.id = id;
            
            const tocItem = document.createElement('li');
            tocItem.className = `toc-item toc-${heading.tagName.toLowerCase()}`;
            
            const tocLink = document.createElement('a');
            tocLink.href = `#${id}`;
            tocLink.textContent = heading.textContent;
            tocLink.addEventListener('click', (e) => {
                e.preventDefault();
                heading.scrollIntoView({ behavior: 'smooth' });
            });
            
            tocItem.appendChild(tocLink);
            tocList.appendChild(tocItem);
        });
        
        tocContainer.appendChild(tocList);
    }
    
    setupReadingProgress() {
        const progressBar = document.getElementById('progressBar');
        if (!progressBar) return;
        
        window.addEventListener('scroll', () => {
            const postContent = document.getElementById('postContent');
            if (!postContent) return;
            
            const contentRect = postContent.getBoundingClientRect();
            const windowHeight = window.innerHeight;
            const documentHeight = document.documentElement.scrollHeight - windowHeight;
            const scrollTop = window.pageYOffset;
            
            let progress = 0;
            if (contentRect.top < windowHeight && contentRect.bottom > 0) {
                const visibleHeight = Math.min(contentRect.bottom, windowHeight) - Math.max(contentRect.top, 0);
                progress = (scrollTop / documentHeight) * 100;
            }
            
            progressBar.style.width = `${Math.min(progress, 100)}%`;
        });
    }
    
    setupKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey) {
                switch (e.key) {
                    case 'd':
                        e.preventDefault();
                        this.toggleDarkMode();
                        break;
                    case 'p':
                        e.preventDefault();
                        window.print();
                        break;
                }
            }
        });
    }
    
    toggleDarkMode() {
        this.darkMode = !this.darkMode;
        document.body.classList.toggle('dark-mode');
        localStorage.setItem('darkMode', this.darkMode.toString());
    }
    
    sharePost(platform) {
        const url = window.location.href;
        const title = this.postData?.contentTitle || document.title;
        
        const shareUrls = {
            twitter: `https://twitter.com/intent/tweet?url=${encodeURIComponent(url)}&text=${encodeURIComponent(title)}`,
            facebook: `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}`,
            linkedin: `https://www.linkedin.com/sharing/share-offsite/?url=${encodeURIComponent(url)}`,
            copy: url
        };
        
        if (platform === 'copy') {
            navigator.clipboard.writeText(url).then(() => {
                this.showToast('링크가 복사되었습니다.');
            });
        } else if (shareUrls[platform]) {
            window.open(shareUrls[platform], '_blank', 'width=600,height=400');
        }
    }
    
    changeFontSize(size) {
        const content = document.getElementById('postContent');
        if (!content) return;
        
        content.classList.remove('font-small', 'font-medium', 'font-large');
        content.classList.add(`font-${size}`);
        
        localStorage.setItem('fontSize', size);
    }
    
    async loadRelatedPosts() {
        if (!this.postData) return;
        
        try {
            const response = await fetch(`/api/search/related/${this.contentId}?limit=3`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.relatedPosts) {
                this.renderRelatedPosts(data.payload.relatedPosts);
            }
        } catch (error) {
            console.error('Error loading related posts:', error);
        }
    }
    
    renderRelatedPosts(posts) {
        const container = document.getElementById('relatedPostsList');
        if (!container || posts.length === 0) return;
        
        container.innerHTML = posts.map(post => `
            <article class="related-post">
                <h4 class="related-post-title">
                    <a href="/api/view/public/mains/relay/${post.contentId}/">${post.contentTitle}</a>
                </h4>
                <div class="related-post-meta">
                    <span class="related-post-category">${this.getCategoryDisplayName(post.contentCategories)}</span>
                    <span class="related-post-date">${new Date(post.publishedDate || post.inDate).toLocaleDateString('ko-KR')}</span>
                </div>
            </article>
        `).join('');
    }
    
    showPostContainer() {
        const loading = document.getElementById('postLoading');
        const container = document.getElementById('postContainer');
        
        if (loading) loading.style.display = 'none';
        if (container) container.style.display = 'block';
    }
    
    showError(message) {
        const loading = document.getElementById('postLoading');
        if (loading) {
            loading.innerHTML = `
                <div class="error-container">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h3>오류 발생</h3>
                    <p>${message}</p>
                    <button onclick="window.location.reload()" class="retry-btn">다시 시도</button>
                </div>
            `;
        }
    }
    
    showToast(message) {
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.textContent = message;
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);
        
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                document.body.removeChild(toast);
            }, 300);
        }, 3000);
    }
}

// Initialize app based on page type
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, checking for main page elements...'); // Debug log
    // Check if we're on the main page
    if (document.getElementById('contentList')) {
        console.log('Main page detected, initializing Jelog App...'); // Debug log
        try {
            window.jelogApp = new JelogApp();
            console.log('Jelog App initialized successfully'); // Debug log
        } catch (error) {
            console.error('Failed to initialize Jelog App:', error); // Debug log
        }
    } else {
        console.log('Not on main page, contentList element not found'); // Debug log
    }
    
    // Check if we're on a detail page
    const contentNumber = window.contentNumber || (window.location.pathname.match(/\/relay\/(\d+)\//)?.[1]);
    if (contentNumber && document.getElementById('postContainer')) {
        window.enhancedDetailView = new EnhancedDetailView(parseInt(contentNumber));
    }
});

// Global function for backward compatibility
function initializeEnhancedDetailView(contentId) {
    window.enhancedDetailView = new EnhancedDetailView(contentId);
}