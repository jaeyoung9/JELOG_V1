/**
 * Enhanced Main Page JavaScript
 * Jelog Blog Platform
 */

class BlogApp {
    constructor() {
        this.currentPage = 0;
        this.currentCategory = '';
        this.currentKeyword = '';
        this.currentSort = 'latest';
        this.currentView = 'grid';
        this.isLoading = false;
        this.searchTimeout = null;
        this.hasNextPage = true;
        this.totalPages = 0;
        
        this.init();
    }

    init() {
        console.log('BlogApp init() starting...');
        console.log('DOM ready state:', document.readyState);
        
        this.bindEvents();
        
        // Add a small delay to ensure DOM is fully ready
        setTimeout(() => {
            console.log('Starting data loading...');
            this.loadFeaturedPosts();
            this.loadPopularTags();
            this.loadPosts();
            
            // Load real statistics for hero section
            this.loadRealStatistics();
            
            // Fallback timeout - if still loading after 10 seconds, show mock data
            setTimeout(() => {
                if (this.isLoading) {
                    console.warn('API loading timeout - showing mock data');
                    this.isLoading = false;
                    this.hideLoading();
                    this.showMockPosts();
                }
            }, 10000);
        }, 100);
    }

    bindEvents() {
        // Search functionality
        const searchInput = document.getElementById('main-search-input');
        const searchButton = document.querySelector('.hero-search-button');
        
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                clearTimeout(this.searchTimeout);
                this.searchTimeout = setTimeout(() => {
                    this.handleSearch(e.target.value);
                }, 300);
            });
            
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    this.handleSearch(e.target.value);
                }
            });
            
            searchInput.addEventListener('focus', () => {
                this.showSearchSuggestions();
            });
            
            searchInput.addEventListener('blur', () => {
                setTimeout(() => this.hideSearchSuggestions(), 200);
            });
        }

        if (searchButton) {
            searchButton.addEventListener('click', () => {
                this.handleSearch(searchInput.value);
            });
        }

        // Category filters - remove any existing event listeners first
        document.querySelectorAll('.category-btn').forEach(btn => {
            // Remove onclick attribute to prevent conflicts
            btn.removeAttribute('onclick');
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                const category = e.currentTarget.dataset.category || '';
                console.log('Category button clicked:', category, 'Button element:', e.currentTarget);
                this.handleCategoryChange(category);
            });
        });
        
        console.log('Category buttons found and bound:', document.querySelectorAll('.category-btn').length);

        // Sort functionality
        const sortSelect = document.getElementById('sortSelect');
        if (sortSelect) {
            sortSelect.addEventListener('change', (e) => {
                this.currentSort = e.target.value;
                this.currentPage = 0;
                this.hasNextPage = true;
                this.totalPages = 0;
                this.loadPosts();
            });
        }

        // View toggle
        document.querySelectorAll('.view-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                this.handleViewChange(e.target.dataset.view);
            });
        });

        // Infinite scroll (optional)
        window.addEventListener('scroll', () => {
            if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 1000) {
                console.log('Scroll trigger - hasNextPage:', this.hasNextPage, 'isLoading:', this.isLoading);
                this.loadMorePosts();
            }
        });
    }

    async handleSearch(keyword) {
        this.currentKeyword = keyword.trim();
        this.currentPage = 0;
        this.hasNextPage = true;
        this.totalPages = 0;
        await this.loadPosts();
    }

    async showSearchSuggestions() {
        const keyword = document.getElementById('main-search-input').value.trim();
        if (keyword.length < 2) return;

        try {
            const response = await fetch(`/api/search/suggestions?query=${encodeURIComponent(keyword)}`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload.suggestions.length > 0) {
                this.renderSearchSuggestions(data.payload.suggestions);
            }
        } catch (error) {
            console.error('Error loading search suggestions:', error);
        }
    }

    renderSearchSuggestions(suggestions) {
        const container = document.getElementById('searchSuggestions');
        if (!container) return;

        container.innerHTML = suggestions.map(suggestion => 
            `<div class="suggestion-item" onclick="selectSuggestion('${suggestion}')">${suggestion}</div>`
        ).join('');
        
        container.style.display = 'block';
    }

    hideSearchSuggestions() {
        const container = document.getElementById('searchSuggestions');
        if (container) {
            container.style.display = 'none';
        }
    }

    handleCategoryChange(category) {
        console.log('Category changed to:', category);
        
        // Update active button
        document.querySelectorAll('.category-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.category === category);
        });

        this.currentCategory = category;
        this.currentPage = 0;
        this.hasNextPage = true;
        this.totalPages = 0;
        
        console.log('About to load posts for category:', category);
        this.loadPosts();
    }

    handleViewChange(view) {
        // Update active button
        document.querySelectorAll('.view-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.view === view);
        });

        this.currentView = view;
        const contentList = document.getElementById('contentList');
        if (contentList) {
            contentList.className = `content-list ${view}-view`;
        }
    }

    async loadFeaturedPosts() {
        try {
            const response = await fetch('/api/public/mains/featured?size=3');
            const data = await response.json();
            
            console.log('Featured posts response:', data);
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                const pageResponse = data.payload.data;
                if (Array.isArray(pageResponse.content) && pageResponse.content.length > 0) {
                    this.renderFeaturedPosts(pageResponse.content);
                    document.getElementById('featuredSection').style.display = 'block';
                }
            }
        } catch (error) {
            console.error('Error loading featured posts:', error);
        }
    }

    renderFeaturedPosts(posts) {
        const container = document.getElementById('featuredPosts');
        if (!container) return;

        container.innerHTML = posts.map(post => `
            <article class="featured-post" onclick="viewPost(${post.contentId})">
                <div class="featured-thumbnail">
                    ${post.contentThumbnail ? 
                        `<img src="${post.contentThumbnail}" alt="${post.contentTitle}" loading="lazy">` :
                        '<div class="placeholder-thumbnail"></div>'
                    }
                    <div class="featured-overlay">
                        <span class="featured-badge">추천</span>
                    </div>
                </div>
                <div class="featured-content">
                    <h3 class="featured-title">${post.contentTitle}</h3>
                    <p class="featured-summary">${post.contentSummary || ''}</p>
                    <div class="featured-meta">
                        <span class="featured-category">${this.getCategoryName(post.contentCategories)}</span>
                        <span class="featured-date">${this.formatDate(post.publishedDate)}</span>
                        <span class="featured-views">${post.views.toLocaleString()} 조회</span>
                    </div>
                </div>
            </article>
        `).join('');
    }

    async loadPopularTags() {
        try {
            const response = await fetch('/api/search/popular-keywords?limit=10');
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload.keywords?.length > 0) {
                this.renderPopularTags(data.payload.keywords);
            }
        } catch (error) {
            console.error('Error loading popular tags:', error);
        }
    }

    renderPopularTags(tags) {
        const container = document.getElementById('popularTags');
        if (!container) return;

        container.innerHTML = tags.map(tag => 
            `<a href="#" class="tag-item" onclick="searchByTag('${tag}')">${tag}</a>`
        ).join('');
    }

    async loadPosts() {
        if (this.isLoading) return;

        // Remove excessive debug logging for production
        // console.log('Loading posts...', {
        //     page: this.currentPage,
        //     sort: this.currentSort,
        //     keyword: this.currentKeyword,
        //     category: this.currentCategory
        // });

        this.isLoading = true;
        this.showLoading();

        try {
            let url = `/api/public/mains/?page=${this.currentPage}&size=12`;

            // Only append relevant query parameters
            // Sorting
            if (this.currentSort === 'latest') {
                url += `&sort=latest`;
            } else if (this.currentSort === 'popular' || this.currentSort === 'views') {
                url += `&sort=views`;
            } else if (this.currentSort === 'comments') {
                url += `&sort=comments`;
            }

            // Keyword
            if (this.currentKeyword) {
                url += `&Title=${encodeURIComponent(this.currentKeyword)}`;
            }

            // Category, mapping frontend to backend enum
            if (this.currentCategory) {
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
                const enumTitle = categoryTitleMapping[this.currentCategory] || this.currentCategory;
                url += `&Categories=${encodeURIComponent(enumTitle)}`;
            }

            // Only one fetch per filter set
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            if (data.result === 'SUCCESS') {
                let posts = [];
                let totalElements = 0;
                let pageData = {};
                // Unified parsing for Spring Page object
                if (data.payload && data.payload.data && Array.isArray(data.payload.data.content)) {
                    const pageResponse = data.payload.data;
                    posts = pageResponse.content;
                    totalElements = pageResponse.totalElements || posts.length;
                    this.totalPages = pageResponse.totalPages || Math.ceil(totalElements / 12);
                    this.hasNextPage = !pageResponse.last;
                    pageData = {
                        currentPage: pageResponse.number || this.currentPage,
                        totalPages: this.totalPages,
                        hasNext: this.hasNextPage,
                        hasPrevious: (pageResponse.number || this.currentPage) > 0
                    };
                } else {
                    // Fallback for unexpected structure
                    posts = [];
                    totalElements = 0;
                    this.totalPages = 0;
                    this.hasNextPage = false;
                    pageData = {
                        currentPage: 0,
                        totalPages: 0,
                        hasNext: false,
                        hasPrevious: false
                    };
                }
                this.renderPosts(posts, totalElements);
                this.renderPagination(pageData);
            } else {
                this.showMockPosts();
            }
        } catch (error) {
            this.showMockPosts();
        } finally {
            this.isLoading = false;
            this.hideLoading();
        }
    }

    async loadMorePosts() {
        if (this.isLoading || !this.hasNextPage) return;
        
        this.currentPage++;
        await this.loadPosts();
    }

    renderPosts(posts, totalCount) {
        console.log('Rendering posts:', { count: posts.length, totalCount, currentCategory: this.currentCategory });
        
        const container = document.getElementById('contentList');
        const countElement = document.getElementById('contentCount');
        const emptyState = document.getElementById('emptyState');
        
        if (!container) {
            console.error('Content list container not found');
            return;
        }

        // Update count
        if (countElement) {
            countElement.textContent = totalCount.toLocaleString();
        }

        // Show/hide container
        container.style.display = 'grid';

        if (!posts || posts.length === 0) {
            console.log('No posts to display for category:', this.currentCategory);
            container.innerHTML = `
                <div class="no-content">
                    <i class="fas fa-inbox"></i>
                    <h3>게시물이 없습니다</h3>
                    <p>${this.currentCategory ? `'${this.getCategoryName(this.currentCategory)}' 카테고리에 게시물이 없습니다.` : '아직 게시물이 작성되지 않았습니다.'}</p>
                </div>
            `;
            if (emptyState) {
                emptyState.style.display = 'none';
            }
            return;
        }

        if (emptyState) {
            emptyState.style.display = 'none';
        }

        try {
            const postsHtml = posts.map(post => this.renderPostCard(post)).join('');
            
            if (this.currentPage === 0) {
                container.innerHTML = postsHtml;
            } else {
                container.innerHTML += postsHtml;
            }
            
            console.log('Posts rendered successfully');
        } catch (error) {
            console.error('Error rendering posts:', error);
            container.innerHTML = `
                <div class="no-content">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h3>렌더링 오류</h3>
                    <p>게시물을 표시하는 중 오류가 발생했습니다.</p>
                </div>
            `;
        }
    }

    renderPostCard(post) {
        if (!post) {
            console.error('Post is null or undefined');
            return '<div class="content-page">Invalid post data</div>';
        }

        try {
            const categoryName = this.getCategoryName(post.contentCategories || 'Other');
            const formattedDate = this.formatDate(post.publishedDate || post.inDate || new Date());
            const thumbnailUrl = post.contentThumbnail || window.defaultThumbnail || 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZjNmNGY2Ii8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZmlsbD0iIzljYTNhZiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPk5vIEltYWdlPC90ZXh0Pjwvc3ZnPg==';
            const excerpt = post.contentSummary || this.extractSummary(post.contentBody) || '내용 요약이 없습니다.';
            const authorInitial = (post.author || 'A').charAt(0).toUpperCase();
            const title = post.contentTitle || 'Untitled';
            const contentId = post.contentId || post.id || 0;

            return `
                <article class="content-page" onclick="viewPost(${contentId})" data-post-id="${contentId}">
                    <div class="content-thumbnail-parents">
                        <img class="content-thumbnail" 
                             src="${thumbnailUrl}" 
                             alt="${title}"
                             loading="lazy"
                             onerror="this.src='${window.defaultThumbnail || thumbnailUrl}'">
                    </div>
                    <div class="content-info">
                        <span class="content-category">${categoryName}</span>
                        <h3 class="content-title" title="${title}">${title}</h3>
                        <p class="content-excerpt">${excerpt}</p>
                        <div class="content-meta">
                            <div class="content-author">
                                <div class="content-author-avatar">${authorInitial}</div>
                                <span>${post.author || 'Admin'}</span>
                            </div>
                            <div class="content-stats">
                                <div class="content-stat">
                                    <i class="fas fa-eye"></i>
                                    <span>${(post.views || 0).toLocaleString()}</span>
                                </div>
                                <div class="content-stat">
                                    <i class="fas fa-comment"></i>
                                    <span>${post.commentCount || 0}</span>
                                </div>
                                <div class="content-stat">
                                    <i class="fas fa-heart"></i>
                                    <span>${post.likes || 0}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </article>
            `;
        } catch (error) {
            console.error('Error rendering post card:', error, post);
            return `
                <div class="content-page">
                    <div class="content-info">
                        <h3 class="content-title">Error rendering post</h3>
                        <p class="content-excerpt">Failed to render post data</p>
                    </div>
                </div>
            `;
        }
    }

    renderPagination(pageData) {
        const container = document.getElementById('pageButtons');
        if (!container) return;

        const { currentPage, totalPages, hasNext, hasPrevious } = pageData;
        
        // Don't show pagination if there's only one page or no posts
        if (totalPages <= 1) {
            container.innerHTML = '';
            return;
        }
        
        let paginationHtml = '';

        // Previous button
        if (hasPrevious) {
            paginationHtml += `<button class="page-button" onclick="goToPage(${currentPage - 1})">이전</button>`;
        }

        // Page numbers - fix the issue with showing "1" instead of actual page numbers
        const startPage = Math.max(0, currentPage - 2);
        const endPage = Math.min(totalPages - 1, currentPage + 2);

        // Show first page if we're not starting from 0
        if (startPage > 0) {
            paginationHtml += `<button class="page-button" onclick="goToPage(0)">1</button>`;
            if (startPage > 1) {
                paginationHtml += `<span class="ellipsis">...</span>`;
            }
        }

        // Show page numbers around current page
        for (let i = startPage; i <= endPage; i++) {
            const isActive = i === currentPage ? 'active' : '';
            paginationHtml += `<button class="page-button ${isActive}" onclick="goToPage(${i})">${i + 1}</button>`;
        }

        // Show last page if we're not ending at the last page
        if (endPage < totalPages - 1) {
            if (endPage < totalPages - 2) {
                paginationHtml += `<span class="ellipsis">...</span>`;
            }
            paginationHtml += `<button class="page-button" onclick="goToPage(${totalPages - 1})">${totalPages}</button>`;
        }

        // Next button
        if (hasNext) {
            paginationHtml += `<button class="page-button" onclick="goToPage(${currentPage + 1})">다음</button>`;
        }

        container.innerHTML = paginationHtml;
    }

    showLoading() {
        const loadingState = document.getElementById('loadingState');
        const contentList = document.getElementById('contentList');
        const emptyState = document.getElementById('emptyState');
        
        if (loadingState) {
            loadingState.style.display = 'block';
        }
        if (contentList) {
            contentList.style.display = 'none';
        }
        if (emptyState) {
            emptyState.style.display = 'none';
        }
        
        console.log('Loading state shown');
    }

    hideLoading() {
        const loadingState = document.getElementById('loadingState');
        const contentList = document.getElementById('contentList');
        
        if (loadingState) {
            loadingState.style.display = 'none';
        }
        if (contentList) {
            contentList.style.display = 'grid';
        }
        
        console.log('Loading state hidden');
    }

    showError(message) {
        // Show error message (implement toast or modal)
        console.error(message);
    }

    getCategoryName(category) {
        const categoryMap = {
            'Java': 'Java',
            'JavaScript': 'JavaScript',
            'Python': 'Python',
            'C': 'C',
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

    extractSummary(content) {
        if (!content) return '';
        const text = content.replace(/<[^>]*>/g, '');
        return text.length > 150 ? text.substring(0, 150) + '...' : text;
    }

    // Load real statistics from the API (method added to BlogApp class)
    async loadRealStatistics() {
        console.log('Loading real statistics...');
        try {
            const response = await fetch('/api/public/stats');
            console.log('Stats API response:', response.status);
            const data = await response.json();
            console.log('Stats data received:', data);
            
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                const stats = data.payload.data;
                console.log('Statistics to update:', stats);
                
                // Update total posts
                const totalPostsElement = document.getElementById('totalPosts');
                console.log('totalPosts element found:', !!totalPostsElement);
                if (totalPostsElement && typeof stats.totalPosts === 'number') {
                    console.log('Updating totalPosts to:', stats.totalPosts);
                    this.animateCounterUpdate(totalPostsElement, stats.totalPosts);
                }
                
                // Update total views
                const totalViewsElement = document.getElementById('totalViews');
                console.log('totalViews element found:', !!totalViewsElement);
                if (totalViewsElement && typeof stats.totalViews === 'number') {
                    console.log('Updating totalViews to:', stats.totalViews);
                    this.animateCounterUpdate(totalViewsElement, stats.totalViews);
                }
                
                // Categories count
                const totalCategoriesElement = document.getElementById('totalCategories');
                console.log('totalCategories element found:', !!totalCategoriesElement);
                if (totalCategoriesElement && typeof stats.totalCategories === 'number') {
                    console.log('Updating totalCategories to:', stats.totalCategories);
                    totalCategoriesElement.textContent = stats.totalCategories;
                }
            } else {
                console.error('Invalid stats response structure:', data);
            }
        } catch (error) {
            console.error('Error loading statistics:', error);
            // Fallback to default values
            const totalPostsElement = document.getElementById('totalPosts');
            const totalViewsElement = document.getElementById('totalViews');
            if (totalPostsElement) totalPostsElement.textContent = '0';
            if (totalViewsElement) totalViewsElement.textContent = '0';
        }
    }

    // Animate counter updates (method added to BlogApp class)
    animateCounterUpdate(element, targetValue) {
        const duration = 1500;
        const startValue = 0;
        const increment = targetValue / (duration / 16);
        let currentValue = startValue;
        
        const timer = setInterval(() => {
            currentValue += increment;
            if (currentValue >= targetValue) {
                currentValue = targetValue;
                clearInterval(timer);
            }
            element.textContent = Math.floor(currentValue).toLocaleString();
        }, 16);
    }

    showMockPosts() {
        console.log('Showing mock posts as fallback');
        
        const mockPosts = [
            {
                contentId: 1,
                contentTitle: 'Spring Boot REST API 개발 가이드',
                contentSummary: 'Spring Boot를 사용하여 RESTful API를 개발하는 방법에 대한 완전한 가이드입니다. 기본 설정부터 고급 기능까지 단계별로 설명합니다.',
                contentCategories: 'Java',
                author: 'Admin',
                inDate: '2024-01-15T10:00:00',
                views: 1234,
                commentCount: 5,
                likes: 12,
                contentThumbnail: null
            },
            {
                contentId: 2,
                contentTitle: 'JavaScript ES6 새로운 기능들',
                contentSummary: 'ES6에서 도입된 새로운 JavaScript 기능들을 실제 예제와 함께 살펴봅니다. let, const, 화살표 함수, 클래스 등을 다룹니다.',
                contentCategories: 'JavaScript',
                author: 'Admin',
                inDate: '2024-01-14T10:00:00',
                views: 856,
                commentCount: 3,
                likes: 8,
                contentThumbnail: null
            },
            {
                contentId: 3,
                contentTitle: 'MySQL 성능 최적화 방법',
                contentSummary: '데이터베이스 성능을 향상시키는 다양한 방법들을 소개합니다. 인덱스 최적화, 쿼리 튜닝, 서버 설정 등을 다룹니다.',
                contentCategories: 'Other',
                author: 'Admin',
                inDate: '2024-01-13T10:00:00',
                views: 642,
                commentCount: 7,
                likes: 15,
                contentThumbnail: null
            },
            {
                contentId: 4,
                contentTitle: 'Python 데이터 분석 입문',
                contentSummary: 'Python을 사용한 데이터 분석의 기초를 배워봅니다. pandas, numpy, matplotlib 라이브러리 사용법을 익힙니다.',
                contentCategories: 'Python',
                author: 'Admin',
                inDate: '2024-01-12T10:00:00',
                views: 923,
                commentCount: 4,
                likes: 11,
                contentThumbnail: null
            },
            {
                contentId: 5,
                contentTitle: '웹 보안 기초와 실무',
                contentSummary: '웹 애플리케이션 보안의 기본 개념과 실무에서 적용할 수 있는 보안 기법들을 소개합니다. XSS, CSRF, SQL 인젝션 등을 다룹니다.',
                contentCategories: 'Security',
                author: 'Admin',
                inDate: '2024-01-11T10:00:00',
                views: 1567,
                commentCount: 9,
                likes: 23,
                contentThumbnail: null
            },
            {
                contentId: 6,
                contentTitle: 'Shell 스크립팅 완전 정복',
                contentSummary: 'Linux/Unix 환경에서 효율적인 작업을 위한 Shell 스크립팅 기법을 마스터합니다. 기본 문법부터 고급 테크닉까지 다룹니다.',
                contentCategories: 'Shell',
                author: 'Admin',
                inDate: '2024-01-10T10:00:00',
                views: 445,
                commentCount: 2,
                likes: 6,
                contentThumbnail: null
            }
        ];

        console.log('Mock posts created:', mockPosts);
        this.renderPosts(mockPosts, mockPosts.length);
        
        // Mock pagination
        const mockPageData = {
            currentPage: 0,
            totalPages: 1,
            hasNext: false,
            hasPrevious: false,
            totalElements: mockPosts.length
        };
        this.renderPagination(mockPageData);
        
        console.log('Mock posts rendered with pagination');
    }
}

// Global functions for onclick handlers
function viewPost(postId) {
    window.location.href = `/api/view/public/mains/relay/${postId}/`;
}

function goToPage(page) {
    window.blogApp.currentPage = page;
    window.blogApp.loadPosts();
    window.scrollTo(0, 0);
}

function selectSuggestion(suggestion) {
    document.getElementById('main-search-input').value = suggestion;
    window.blogApp.handleSearch(suggestion);
    window.blogApp.hideSearchSuggestions();
}

function searchByTag(tag) {
    document.getElementById('main-search-input').value = tag;
    window.blogApp.handleSearch(tag);
}

function resetSearch() {
    document.getElementById('main-search-input').value = '';
    window.blogApp.currentKeyword = '';
    window.blogApp.currentCategory = '';
    window.blogApp.currentPage = 0;
    
    // Reset category buttons
    document.querySelectorAll('.category-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.category === '');
    });
    
    window.blogApp.loadPosts();
}

async function toggleLike(postId) {
    try {
        const response = await fetch(`/api/public/posts/${postId}/like`, {
            method: 'POST'
        });
        const data = await response.json();
        
        if (data.result === 'SUCCESS') {
            // Update like count in UI
            const likeBtn = event.target.closest('.like-btn');
            const countSpan = likeBtn.querySelector('span');
            countSpan.textContent = data.payload.likes;
            
            // Toggle heart icon
            const icon = likeBtn.querySelector('i');
            icon.classList.toggle('far');
            icon.classList.toggle('fas');
        }
    } catch (error) {
        console.error('Error toggling like:', error);
    }
}

// Initialize the app when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    window.blogApp = new BlogApp();

    // Initialize sidebar if present
    if (document.getElementById('sidebarTotalPosts')) {
        loadSidebarData();
    }
});

// Load sidebar data
async function loadSidebarData() {
    console.log('Loading sidebar data...');

    try {
        // Load popular posts using the existing API
        const popularResponse = await fetch('/api/public/mains/?sort=views&size=5');
        if (popularResponse.ok) {
            const popularData = await popularResponse.json();
            console.log('Popular posts response:', popularData);
            if (popularData.result === 'SUCCESS') {
                let posts = [];
                if (Array.isArray(popularData.payload?.content)) {
                    posts = popularData.payload.content;
                } else if (Array.isArray(popularData.payload)) {
                    posts = popularData.payload;
                }
                updateSidebarPopularPosts(posts);
            }
        }

        // Load all posts to calculate stats
        const allPostsResponse = await fetch('/api/public/mains/?size=1000');
        if (allPostsResponse.ok) {
            const allPostsData = await allPostsResponse.json();
            if (allPostsData.result === 'SUCCESS') {
                let allPosts = [];
                if (Array.isArray(allPostsData.payload?.content)) {
                    allPosts = allPostsData.payload.content;
                } else if (Array.isArray(allPostsData.payload)) {
                    allPosts = allPostsData.payload;
                }

                // Calculate stats from all posts
                const totalPosts = allPosts.length;
                const totalViews = allPosts.reduce((sum, post) => sum + (post.views || 0), 0);
                const totalComments = allPosts.reduce((sum, post) => sum + (post.commentCount || 0), 0);

                updateSidebarStats({
                    totalPosts,
                    totalViews,
                    totalComments
                });
            }
        }

        // Load recent comments - use mock data for now as there's no direct endpoint
        updateSidebarRecentComments([
            { content: '정말 유용한 정보네요! 감사합니다.', author: 'User1', date: new Date().toLocaleDateString() },
            { content: '실제 프로젝트에 적용해보겠습니다.', author: 'User2', date: new Date().toLocaleDateString() },
            { content: '더 자세한 설명이 있으면 좋겠어요.', author: 'User3', date: new Date().toLocaleDateString() }
        ]);

    } catch (error) {
        console.error('Error loading sidebar data:', error);
        showMockSidebarData();
    }
}

function updateSidebarStats(stats) {
    const totalPostsEl = document.getElementById('sidebarTotalPosts');
    const totalViewsEl = document.getElementById('sidebarTotalViews');
    const totalCommentsEl = document.getElementById('sidebarTotalComments');

    if (totalPostsEl) totalPostsEl.textContent = (stats.totalPosts || 0).toLocaleString();
    if (totalViewsEl) totalViewsEl.textContent = (stats.totalViews || 0).toLocaleString();
    if (totalCommentsEl) totalCommentsEl.textContent = (stats.totalComments || 0).toLocaleString();
}

function updateSidebarPopularPosts(posts) {
    const container = document.getElementById('sidebarPopularPosts');
    if (!container) return;

    if (!posts || posts.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #6b7280;">인기 게시물이 없습니다.</p>';
        return;
    }

    container.innerHTML = posts.map(post => `
        <div class="popular-post-item" onclick="viewPost(${post.contentId})">
            <div class="popular-post-thumb">
                ${post.contentThumbnail ? 
                    `<img src="${post.contentThumbnail}" alt="${post.contentTitle}">` :
                    '<div style="width: 100%; height: 100%; background: #d1d5db; display: flex; align-items: center; justify-content: center; font-size: 0.75rem; color: #6b7280;">📄</div>'
                }
            </div>
            <div class="popular-post-info">
                <h4 class="popular-post-title">${post.contentTitle}</h4>
                <div class="popular-post-meta">
                    <span>👁 ${(post.views || 0).toLocaleString()}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function updateSidebarRecentComments(comments) {
    const container = document.getElementById('sidebarRecentComments');
    if (!container) return;

    if (!comments || comments.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #6b7280;">최근 댓글이 없습니다.</p>';
        return;
    }

    container.innerHTML = comments.map(comment => `
        <div class="recent-comment-item">
            <p class="recent-comment-content">"${comment.content}"</p>
            <div class="recent-comment-meta">
                ${comment.author} • ${comment.date}
            </div>
        </div>
    `).join('');
}

function showMockSidebarData() {
    updateSidebarStats({
        totalPosts: 45,
        totalViews: 15420,
        totalComments: 128
    });

    updateSidebarPopularPosts([
        { contentId: 1, contentTitle: 'Spring Boot REST API 개발 가이드', views: 1234, contentThumbnail: null },
        { contentId: 2, contentTitle: 'JavaScript ES6 새로운 기능들', views: 856, contentThumbnail: null },
        { contentId: 3, contentTitle: '웹 보안 기초와 실무', views: 642, contentThumbnail: null },
        { contentId: 4, contentTitle: 'Python 데이터 분석 입문', views: 523, contentThumbnail: null },
        { contentId: 5, contentTitle: 'MySQL 성능 최적화', views: 412, contentThumbnail: null }
    ]);

    updateSidebarRecentComments([
        { content: '정말 유용한 정보네요! 감사합니다.', author: 'User1', date: '2024-01-15' },
        { content: '실제 프로젝트에 적용해보겠습니다.', author: 'User2', date: '2024-01-14' },
        { content: '더 자세한 설명이 있으면 좋겠어요.', author: 'User3', date: '2024-01-13' }
    ]);
}

// Global functions that need to be accessible from HTML onclick handlers
window.goToPage = function(pageNumber) {
    if (window.blogApp && pageNumber >= 0) {
        window.blogApp.currentPage = pageNumber;
        window.blogApp.loadPosts();
    }
};

window.viewPost = function(contentId) {
    window.location.href = `/api/view/public/mains/relay/${contentId}/`;
};

window.filterByCategory = function(category) {
    console.log('filterByCategory called with:', category);
    if (window.blogApp) {
        console.log('BlogApp found, calling handleCategoryChange');
        window.blogApp.handleCategoryChange(category);
    } else {
        console.error('BlogApp not found in window.blogApp');
    }
};

window.searchByTag = function(tag) {
    if (window.blogApp) {
        window.blogApp.handleSearch(tag);
    }
};

// Wrapper function for global loadRealStatistics access
window.loadRealStatistics = function() {
    if (window.blogApp && typeof window.blogApp.loadRealStatistics === 'function') {
        window.blogApp.loadRealStatistics();
    } else {
        console.error('BlogApp not initialized or loadRealStatistics method not found');
    }
};

// Initialize everything when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing BlogApp...');

    // Initialize the blog app
    window.blogApp = new BlogApp();
    console.log('BlogApp initialized:', window.blogApp);

    // Statistics will be loaded by BlogApp.init() method
});

// Additional CSS for enhanced features
const enhancedStyles = `
    .featured-post {
        background: white;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: var(--shadow);
        transition: var(--transition);
        cursor: pointer;
    }
    
    .featured-post:hover {
        transform: translateY(-4px);
        box-shadow: var(--shadow-lg);
    }
    
    .featured-thumbnail {
        position: relative;
        height: 200px;
        overflow: hidden;
    }
    
    .featured-thumbnail img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    
    .placeholder-thumbnail {
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #9ca3af;
    }
    
    .featured-overlay {
        position: absolute;
        top: 1rem;
        right: 1rem;
    }
    
    .featured-badge {
        background: var(--primary-color);
        color: white;
        padding: 0.25rem 0.75rem;
        border-radius: 1rem;
        font-size: 0.75rem;
        font-weight: 500;
    }
    
    .featured-content {
        padding: 1.5rem;
    }
    
    .featured-title {
        font-size: 1.25rem;
        font-weight: 600;
        margin-bottom: 0.5rem;
        color: #1f2937;
        line-height: 1.4;
    }
    
    .featured-summary {
        color: #6b7280;
        font-size: 0.875rem;
        line-height: 1.5;
        margin-bottom: 1rem;
    }
    
    .featured-meta {
        display: flex;
        align-items: center;
        gap: 1rem;
        font-size: 0.75rem;
        color: #9ca3af;
    }
    
    .content-header {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        margin-bottom: 0.75rem;
    }
    
    .content-category {
        background: var(--primary-color);
        color: white;
        padding: 0.25rem 0.5rem;
        border-radius: 0.375rem;
        font-size: 0.75rem;
        font-weight: 500;
    }
    
    .content-summary {
        color: #6b7280;
        font-size: 0.875rem;
        line-height: 1.5;
        margin-bottom: 1rem;
    }
    
    .content-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 0.25rem;
        margin-bottom: 1rem;
    }
    
    .content-tag {
        background: #f3f4f6;
        color: #6b7280;
        padding: 0.125rem 0.5rem;
        border-radius: 0.75rem;
        font-size: 0.75rem;
    }
    
    .content-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    
    .content-meta {
        display: flex;
        align-items: center;
        gap: 1rem;
        font-size: 0.75rem;
        color: #9ca3af;
    }
    
    .content-actions {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }
    
    .like-btn {
        background: none;
        border: none;
        color: #9ca3af;
        cursor: pointer;
        display: flex;
        align-items: center;
        gap: 0.25rem;
        font-size: 0.875rem;
        transition: var(--transition);
    }
    
    .like-btn:hover {
        color: #dc2626;
    }
    
    .like-btn.liked {
        color: #dc2626;
    }
`;

// Inject enhanced styles
const styleElement = document.createElement('style');
styleElement.textContent = enhancedStyles;
document.head.appendChild(styleElement);