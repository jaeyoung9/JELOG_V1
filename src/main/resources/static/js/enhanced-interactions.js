/**
 * Enhanced Interactions for Jelog
 * Improves button responsiveness, animations, and user experience
 */

// Global state
let currentCategory = '';
let currentPage = 0;
let isLoading = false;

// Initialize enhanced interactions
document.addEventListener('DOMContentLoaded', function() {
    initializeEnhancedInteractions();
    setupHeroSearch();
    setupCategoryFilters();
    setupContentCards();
    loadInitialStats();
});

/**
 * Initialize enhanced interactions
 */
function initializeEnhancedInteractions() {
    // Add touch feedback for buttons
    addTouchFeedback();
    
    // Setup intersection observer for animations
    setupIntersectionObserver();
    
    // Add keyboard navigation
    setupKeyboardNavigation();
    
    // Add smooth scrolling
    setupSmoothScrolling();
    
    // Setup search autocomplete
    setupSearchAutocomplete();
}

/**
 * Add touch feedback for interactive elements
 */
function addTouchFeedback() {
    const interactiveElements = document.querySelectorAll(
        'button, .category-btn, .view-btn, .nav-link, .mobile-nav-link, .content-page'
    );
    
    interactiveElements.forEach(element => {
        // Add touch start feedback
        element.addEventListener('touchstart', function() {
            this.classList.add('touching');
        }, { passive: true });
        
        // Remove touch feedback
        element.addEventListener('touchend', function() {
            this.classList.remove('touching');
        }, { passive: true });
        
        element.addEventListener('touchcancel', function() {
            this.classList.remove('touching');
        }, { passive: true });
    });
}

/**
 * Setup intersection observer for scroll animations
 */
function setupIntersectionObserver() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    // Observe content cards and sections
    const observeElements = document.querySelectorAll(
        '.content-page, .featured-posts, .tags-section, .content-toolbar'
    );
    
    observeElements.forEach(element => {
        observer.observe(element);
    });
}

/**
 * Setup hero search functionality
 */
function setupHeroSearch() {
    const heroSearchInput = document.getElementById('main-search-input');
    const heroSearchButton = document.querySelector('.hero-search-button');
    const suggestionsContainer = document.getElementById('searchSuggestions');
    
    if (!heroSearchInput) return;
    
    // Enter key search
    heroSearchInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            performHeroSearch();
        }
    });
    
    // Input event for suggestions
    heroSearchInput.addEventListener('input', debounce(function() {
        const query = this.value.trim();
        if (query.length >= 2) {
            showSearchSuggestions(query);
        } else {
            hideSearchSuggestions();
        }
    }, 300));
    
    // Focus and blur events
    heroSearchInput.addEventListener('focus', function() {
        this.parentElement.classList.add('focused');
    });
    
    heroSearchInput.addEventListener('blur', function() {
        this.parentElement.classList.remove('focused');
        // Delay hiding suggestions to allow click
        setTimeout(hideSearchSuggestions, 200);
    });
    
    // Click outside to hide suggestions
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.hero-search')) {
            hideSearchSuggestions();
        }
    });
}

/**
 * Perform hero search
 */
function performHeroSearch() {
    const searchInput = document.getElementById('main-search-input');
    const query = searchInput ? searchInput.value.trim() : '';
    
    if (!query) {
        showNotification('검색어를 입력해주세요.', 'warning');
        return;
    }
    
    // Add loading state
    const searchButton = document.querySelector('.hero-search-button');
    if (searchButton) {
        searchButton.classList.add('loading');
        searchButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
    }
    
    // Perform search
    if (typeof searchPosts === 'function') {
        searchPosts(query);
    } else {
        // Fallback to URL-based search
        window.location.href = `/search?q=${encodeURIComponent(query)}`;
    }
    
    // Hide suggestions
    hideSearchSuggestions();
    
    // Track search
    trackEvent('search', { query: query });
}

/**
 * Show search suggestions
 */
function showSearchSuggestions(query) {
    const suggestionsContainer = document.getElementById('searchSuggestions');
    if (!suggestionsContainer) return;
    
    // Mock suggestions - replace with actual API call
    const suggestions = [
        `${query} JavaScript`,
        `${query} React`,
        `${query} Node.js`,
        `${query} Python`,
        `${query} 개발`
    ].filter(suggestion => suggestion.toLowerCase().includes(query.toLowerCase()));
    
    if (suggestions.length > 0) {
        suggestionsContainer.innerHTML = suggestions.map(suggestion => 
            `<div class="suggestion-item" onclick="selectSuggestion('${suggestion}')">${suggestion}</div>`
        ).join('');
        
        suggestionsContainer.style.display = 'block';
    } else {
        hideSearchSuggestions();
    }
}

/**
 * Hide search suggestions
 */
function hideSearchSuggestions() {
    const suggestionsContainer = document.getElementById('searchSuggestions');
    if (suggestionsContainer) {
        suggestionsContainer.style.display = 'none';
    }
}

/**
 * Select search suggestion
 */
function selectSuggestion(suggestion) {
    const searchInput = document.getElementById('main-search-input');
    if (searchInput) {
        searchInput.value = suggestion;
        performHeroSearch();
    }
}

/**
 * Setup category filters with enhanced interactions
 */
function setupCategoryFilters() {
    const categoryButtons = document.querySelectorAll('.category-btn');
    
    categoryButtons.forEach(button => {
        button.addEventListener('click', function() {
            const category = this.dataset.category || '';
            setActiveCategory(this, category);
        });
        
        // Add keyboard support
        button.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });
}

/**
 * Set active category
 */
function setActiveCategory(activeButton, category) {
    // Update visual state
    document.querySelectorAll('.category-btn').forEach(btn => {
        btn.classList.remove('active');
        btn.setAttribute('aria-pressed', 'false');
    });
    
    activeButton.classList.add('active');
    activeButton.setAttribute('aria-pressed', 'true');
    
    // Update global state
    currentCategory = category;
    currentPage = 0;
    
    // Show loading state
    showContentLoading();
    
    // Filter content
    filterContentByCategory(category);
    
    // Track category selection
    trackEvent('category_filter', { category: category || 'all' });
}

/**
 * Filter content by category
 */
function filterContentByCategory(category) {
    if (typeof DataToURL === 'function') {
        DataToURL(category);
    } else {
        // Fallback implementation
        loadContentByCategory(category);
    }
}

/**
 * Load content by category (fallback)
 */
function loadContentByCategory(category) {
    // Map frontend category to backend enum title
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
    
    const enumTitle = category ? (categoryTitleMapping[category] || category) : '';
    console.log('Loading category:', category, '-> Enum title:', enumTitle);
    
    const apiUrl = '/api/public/mains/?' + new URLSearchParams({
        page: currentPage.toString(),
        size: '8',
        Categories: enumTitle
    });
    
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            console.log('Category filter response:', data);
            if (data.result === 'SUCCESS' && data.payload && data.payload.data) {
                const pageResponse = data.payload.data;
                displayContent(pageResponse.content || []);
            } else {
                displayContent([]);
            }
            hideContentLoading();
        })
        .catch(error => {
            console.error('Error loading content:', error);
            showNotification('콘텐츠를 불러오는데 실패했습니다.', 'error');
            hideContentLoading();
        });
}

/**
 * Display content with animations
 */
function displayContent(contentArray) {
    const contentList = document.getElementById('contentList');
    if (!contentList) return;
    
    contentList.innerHTML = '';
    
    contentArray.forEach((content, index) => {
        const contentElement = createContentCard(content);
        contentElement.style.animationDelay = `${index * 0.1}s`;
        contentList.appendChild(contentElement);
    });
    
    // Update content count
    updateContentCount(contentArray.length);
}

/**
 * Create content card element
 */
function createContentCard(content) {
    const card = document.createElement('div');
    card.className = 'content-card fade-in';
    card.onclick = () => navigateToPost(content.contentId);
    
    const imageUrl = content.contentThumbnail || '/static/images/default-thumbnail.jpg';
    const excerpt = content.contentSummary || content.contentBody?.substring(0, 150) + '...' || '';
    const publishDate = new Date(content.inDate).toLocaleDateString('ko-KR');
    
    card.innerHTML = `
        <img class="content-card-image" src="${imageUrl}" alt="${content.contentTitle}" loading="lazy">
        <div class="content-card-body">
            <h3 class="content-card-title">${content.contentTitle}</h3>
            <p class="content-card-excerpt">${excerpt}</p>
            <div class="content-card-meta">
                <div class="content-card-date">
                    <i class="fas fa-calendar-alt"></i>
                    <span>${publishDate}</span>
                </div>
                <div class="content-card-stats">
                    <div class="content-card-stat">
                        <i class="fas fa-eye"></i>
                        <span>${content.views || 0}</span>
                    </div>
                    <div class="content-card-stat">
                        <i class="fas fa-heart"></i>
                        <span>${content.likes || 0}</span>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    return card;
}

/**
 * Navigate to post
 */
function navigateToPost(contentId) {
    // Add loading feedback
    showNotification('게시물을 불러오는 중...', 'info');
    
    // Navigate to post
    window.location.href = `/api/view/public/mains/relay/${contentId}/`;
    
    // Track post view
    trackEvent('post_view', { contentId: contentId });
}

/**
 * Setup content cards interactions
 */
function setupContentCards() {
    // Add hover effects and click handlers will be added dynamically
    document.addEventListener('mouseover', function(e) {
        if (e.target.closest('.content-card')) {
            e.target.closest('.content-card').classList.add('hovered');
        }
    });
    
    document.addEventListener('mouseout', function(e) {
        if (e.target.closest('.content-card')) {
            e.target.closest('.content-card').classList.remove('hovered');
        }
    });
}

/**
 * Show content loading state
 */
function showContentLoading() {
    const loadingState = document.getElementById('loadingState');
    const contentList = document.getElementById('contentList');
    
    if (loadingState) loadingState.style.display = 'block';
    if (contentList) contentList.style.opacity = '0.5';
    
    isLoading = true;
}

/**
 * Hide content loading state
 */
function hideContentLoading() {
    const loadingState = document.getElementById('loadingState');
    const contentList = document.getElementById('contentList');
    
    if (loadingState) loadingState.style.display = 'none';
    if (contentList) contentList.style.opacity = '1';
    
    isLoading = false;
}

/**
 * Update content count
 */
function updateContentCount(count) {
    const contentCountElement = document.getElementById('contentCount');
    if (contentCountElement) {
        contentCountElement.textContent = count;
    }
}

/**
 * Load initial statistics
 */
function loadInitialStats() {
    // Mock stats - replace with actual API calls
    updateHeroStats({
        totalPosts: 42,
        totalViews: 1250
    });
}

/**
 * Update hero statistics
 */
function updateHeroStats(stats) {
    const totalPostsElement = document.getElementById('totalPosts');
    const totalViewsElement = document.getElementById('totalViews');
    
    if (totalPostsElement && stats.totalPosts) {
        animateNumber(totalPostsElement, stats.totalPosts);
    }
    
    if (totalViewsElement && stats.totalViews) {
        animateNumber(totalViewsElement, stats.totalViews);
    }
}

/**
 * Animate number counter
 */
function animateNumber(element, target) {
    const duration = 2000;
    const start = 0;
    const increment = target / (duration / 16);
    let current = start;
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            current = target;
            clearInterval(timer);
        }
        element.textContent = Math.floor(current).toLocaleString();
    }, 16);
}

/**
 * Setup keyboard navigation
 */
function setupKeyboardNavigation() {
    document.addEventListener('keydown', function(e) {
        // Escape key
        if (e.key === 'Escape') {
            hideSearchSuggestions();
            // Remove focus from search input
            const searchInput = document.getElementById('main-search-input');
            if (searchInput === document.activeElement) {
                searchInput.blur();
            }
        }
        
        // Search shortcut (Ctrl/Cmd + K)
        if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
            e.preventDefault();
            const searchInput = document.getElementById('main-search-input');
            if (searchInput) {
                searchInput.focus();
                searchInput.select();
            }
        }
    });
}

/**
 * Setup smooth scrolling
 */
function setupSmoothScrolling() {
    // Smooth scroll to content when category is selected
    function scrollToContent() {
        const contentSection = document.querySelector('.content-container');
        if (contentSection) {
            contentSection.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    }
    
    // Add to category filter functions
    window.scrollToContent = scrollToContent;
}

/**
 * Setup search autocomplete
 */
function setupSearchAutocomplete() {
    // This would integrate with a real search API
    // For now, it's implemented in the hero search function
}

/**
 * Show notification
 */
function showNotification(message, type = 'info') {
    // Use toastr if available, otherwise create simple notification
    if (typeof toastr !== 'undefined') {
        toastr[type](message);
    } else {
        console.log(`${type.toUpperCase()}: ${message}`);
    }
}

/**
 * Track events (analytics)
 */
function trackEvent(event, data = {}) {
    // This would integrate with analytics service
    console.log('Event tracked:', event, data);
}

/**
 * Debounce function
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Reset search and filters
 */
function resetSearch() {
    const searchInput = document.getElementById('main-search-input');
    if (searchInput) {
        searchInput.value = '';
    }
    
    // Reset category to all
    const allCategoryBtn = document.querySelector('.category-btn[data-category=""]');
    if (allCategoryBtn) {
        setActiveCategory(allCategoryBtn, '');
    }
}

/**
 * Scroll to top function
 */
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
    
    // Track scroll to top
    trackEvent('scroll_to_top');
}

/**
 * Show privacy policy (placeholder)
 */
function showPrivacyPolicy() {
    // This would open a modal or redirect to privacy policy page
    alert('개인정보처리방침 페이지로 이동합니다.');
}

// Expose functions to global scope
window.performHeroSearch = performHeroSearch;
window.filterByCategory = function(category) {
    const categoryBtn = document.querySelector(`.category-btn[data-category="${category}"]`);
    if (categoryBtn) {
        setActiveCategory(categoryBtn, category);
    }
};
window.resetSearch = resetSearch;
window.selectSuggestion = selectSuggestion;
window.scrollToTop = scrollToTop;
window.showPrivacyPolicy = showPrivacyPolicy;