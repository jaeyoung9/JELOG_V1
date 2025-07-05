/**
 * Modern Header JavaScript
 * Handles responsive navigation, search, and mobile menu
 */

// Global state
let isMobileMenuOpen = false;
let isSearchOpen = false;

// DOM Elements
const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
const mobileMenu = document.getElementById('mobileMenu');
const mobileMenuOverlay = document.getElementById('mobile-menu-overlay');
const mobileSearch = document.getElementById('mobileSearch');
const mobileSearchInput = document.getElementById('mobileSearchInput');

// Initialize header functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeHeader();
    setupEventListeners();
    updateAuthDisplay();
});

/**
 * Initialize header components
 */
function initializeHeader() {
    // Add scroll effect to header
    window.addEventListener('scroll', handleHeaderScroll);
    
    // Setup dropdown menus for desktop
    setupDropdownMenus();
    
    // Setup keyboard navigation
    setupKeyboardNavigation();
    
    // Initialize category filters
    initializeCategoryFilters();
}

/**
 * Setup event listeners
 */
function setupEventListeners() {
    // Mobile menu overlay click
    if (mobileMenuOverlay) {
        mobileMenuOverlay.addEventListener('click', closeMobileMenu);
    }
    
    // Escape key handling
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            if (isMobileMenuOpen) closeMobileMenu();
            if (isSearchOpen) closeSearch();
        }
    });
    
    // Search input handling
    if (mobileSearchInput) {
        mobileSearchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }
    
    // Resize handling
    window.addEventListener('resize', handleResize);
}

/**
 * Handle header scroll effects
 */
function handleHeaderScroll() {
    const header = document.querySelector('.modern-header');
    if (!header) return;
    
    const scrollY = window.scrollY;
    
    if (scrollY > 10) {
        header.classList.add('scrolled');
    } else {
        header.classList.remove('scrolled');
    }
}

/**
 * Setup dropdown menu functionality
 */
function setupDropdownMenus() {
    const dropdowns = document.querySelectorAll('.nav-dropdown');
    
    dropdowns.forEach(dropdown => {
        const toggle = dropdown.querySelector('.dropdown-toggle');
        const menu = dropdown.querySelector('.dropdown-menu');
        
        if (!toggle || !menu) return;
        
        // Click handling for mobile/touch devices
        toggle.addEventListener('click', function(e) {
            if (window.innerWidth <= 768) {
                e.preventDefault();
                toggleDropdown(dropdown);
            }
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!dropdown.contains(e.target)) {
                closeDropdown(dropdown);
            }
        });
    });
}

/**
 * Toggle dropdown menu
 */
function toggleDropdown(dropdown) {
    const menu = dropdown.querySelector('.dropdown-menu');
    const isOpen = dropdown.classList.contains('open');
    
    // Close all other dropdowns
    document.querySelectorAll('.nav-dropdown.open').forEach(d => {
        if (d !== dropdown) closeDropdown(d);
    });
    
    if (isOpen) {
        closeDropdown(dropdown);
    } else {
        openDropdown(dropdown);
    }
}

/**
 * Open dropdown menu
 */
function openDropdown(dropdown) {
    dropdown.classList.add('open');
    const menu = dropdown.querySelector('.dropdown-menu');
    if (menu) {
        menu.style.display = 'block';
    }
}

/**
 * Close dropdown menu
 */
function closeDropdown(dropdown) {
    dropdown.classList.remove('open');
    const menu = dropdown.querySelector('.dropdown-menu');
    if (menu) {
        menu.style.display = 'none';
    }
}

/**
 * Toggle mobile menu
 */
function toggleMobileMenu() {
    if (isMobileMenuOpen) {
        closeMobileMenu();
    } else {
        openMobileMenu();
    }
}

/**
 * Open mobile menu
 */
function openMobileMenu() {
    isMobileMenuOpen = true;
    
    if (mobileMenu) {
        mobileMenu.classList.add('active');
    }
    
    if (mobileMenuOverlay) {
        mobileMenuOverlay.classList.add('active');
    }
    
    if (mobileMenuBtn) {
        mobileMenuBtn.classList.add('active');
    }
    
    // Prevent body scroll
    document.body.style.overflow = 'hidden';
    
    // Add animation delay for menu items
    const menuItems = document.querySelectorAll('.mobile-nav-link');
    menuItems.forEach((item, index) => {
        item.style.animationDelay = `${index * 0.1}s`;
        item.classList.add('slide-in-left');
    });
}

/**
 * Close mobile menu
 */
function closeMobileMenu() {
    isMobileMenuOpen = false;
    
    if (mobileMenu) {
        mobileMenu.classList.remove('active');
    }
    
    if (mobileMenuOverlay) {
        mobileMenuOverlay.classList.remove('active');
    }
    
    if (mobileMenuBtn) {
        mobileMenuBtn.classList.remove('active');
    }
    
    // Restore body scroll
    document.body.style.overflow = '';
    
    // Remove animation classes
    const menuItems = document.querySelectorAll('.mobile-nav-link');
    menuItems.forEach(item => {
        item.classList.remove('slide-in-left');
        item.style.animationDelay = '';
    });
}

/**
 * Toggle search
 */
function toggleSearch() {
    if (isSearchOpen) {
        closeSearch();
    } else {
        openSearch();
    }
}

/**
 * Open search
 */
function openSearch() {
    isSearchOpen = true;
    
    if (mobileSearch) {
        mobileSearch.classList.add('active');
        
        // Focus on search input with delay
        setTimeout(() => {
            if (mobileSearchInput) {
                mobileSearchInput.focus();
            }
        }, 300);
    }
}

/**
 * Close search
 */
function closeSearch() {
    isSearchOpen = false;
    
    if (mobileSearch) {
        mobileSearch.classList.remove('active');
    }
    
    if (mobileSearchInput) {
        mobileSearchInput.blur();
    }
}

/**
 * Perform search
 */
function performSearch() {
    const query = mobileSearchInput ? mobileSearchInput.value.trim() : '';
    
    if (query) {
        // Use existing search functionality
        if (typeof searchPosts === 'function') {
            searchPosts(query);
        } else {
            // Fallback to URL-based search
            const searchUrl = `/api/search/posts?keyword=${encodeURIComponent(query)}`;
            window.location.href = searchUrl;
        }
        
        closeSearch();
    }
}

/**
 * Filter by category
 */
function filterByCategory(category) {
    console.log('Filtering by category:', category);
    
    // Close mobile menu if open
    if (isMobileMenuOpen) {
        closeMobileMenu();
    }
    
    // Use existing category filter functionality
    if (typeof DataToURL === 'function') {
        DataToURL(category);
    } else {
        // Fallback to URL-based filtering
        const filterUrl = `/?category=${encodeURIComponent(category)}`;
        window.location.href = filterUrl;
    }
    
    // Update active navigation
    updateActiveNavigation(category);
}

/**
 * Update active navigation state
 */
function updateActiveNavigation(activeCategory = '') {
    // Remove active class from all nav links
    document.querySelectorAll('.nav-link, .mobile-nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Add active class to current category
    if (activeCategory) {
        const categoryLinks = document.querySelectorAll(`[onclick*="${activeCategory}"]`);
        categoryLinks.forEach(link => {
            link.classList.add('active');
        });
    } else {
        // Home page active
        document.querySelectorAll('[href="/"], [href=""]').forEach(link => {
            link.classList.add('active');
        });
    }
}

/**
 * Initialize category filters from URL
 */
function initializeCategoryFilters() {
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');
    
    if (category) {
        updateActiveNavigation(category);
    }
}

/**
 * Setup keyboard navigation
 */
function setupKeyboardNavigation() {
    // Tab navigation for dropdown menus
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Tab') {
            // Close dropdowns when tabbing away
            setTimeout(() => {
                const focusedElement = document.activeElement;
                const openDropdowns = document.querySelectorAll('.nav-dropdown.open');
                
                openDropdowns.forEach(dropdown => {
                    if (!dropdown.contains(focusedElement)) {
                        closeDropdown(dropdown);
                    }
                });
            }, 10);
        }
    });
}

/**
 * Update authentication display
 */
function updateAuthDisplay() {
    // Check if user is logged in (using existing cookie functionality)
    const isLoggedIn = typeof getCookieValue === 'function' && getCookieValue('JY-ACCESS-TOKEN');
    
    const authBtn = document.getElementById('signIn');
    const writingLinks = document.querySelectorAll('#header-nav-writing, #mobile-nav-writing');
    
    if (isLoggedIn) {
        if (authBtn) authBtn.textContent = '로그아웃';
        writingLinks.forEach(link => link.style.display = 'block');
    } else {
        if (authBtn) authBtn.textContent = '로그인';
        writingLinks.forEach(link => link.style.display = 'none');
    }
}

/**
 * Handle window resize
 */
function handleResize() {
    // Close mobile menu on resize to desktop
    if (window.innerWidth > 768 && isMobileMenuOpen) {
        closeMobileMenu();
    }
    
    // Close search on resize
    if (isSearchOpen) {
        closeSearch();
    }
    
    // Close all dropdowns on resize
    document.querySelectorAll('.nav-dropdown.open').forEach(closeDropdown);
}

/**
 * Add smooth scrolling for anchor links
 */
function initializeSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Show loading state for navigation
 */
function showNavigationLoading(element) {
    if (element) {
        element.classList.add('loading');
        element.style.pointerEvents = 'none';
    }
}

/**
 * Hide loading state for navigation
 */
function hideNavigationLoading(element) {
    if (element) {
        element.classList.remove('loading');
        element.style.pointerEvents = '';
    }
}

/**
 * Add touch gesture support for mobile menu
 */
function initializeTouchGestures() {
    let startX = 0;
    let startY = 0;
    
    document.addEventListener('touchstart', function(e) {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
    });
    
    document.addEventListener('touchmove', function(e) {
        if (!isMobileMenuOpen) return;
        
        const currentX = e.touches[0].clientX;
        const currentY = e.touches[0].clientY;
        const diffX = startX - currentX;
        const diffY = startY - currentY;
        
        // Swipe left to close menu (if swiping more horizontally than vertically)
        if (Math.abs(diffX) > Math.abs(diffY) && diffX > 50) {
            closeMobileMenu();
        }
    });
}

// Initialize touch gestures when DOM is ready
document.addEventListener('DOMContentLoaded', initializeTouchGestures);

// Add scroll event listener for header effects
window.addEventListener('scroll', handleHeaderScroll, { passive: true });

// Expose functions to global scope for backward compatibility
window.toggleMobileMenu = toggleMobileMenu;
window.toggleSearch = toggleSearch;
window.performSearch = performSearch;
window.filterByCategory = filterByCategory;