/**
 * Enhanced Admin Posts Management
 * Advanced functionality for managing blog posts
 */

class AdminPostsManager {
    constructor() {
        this.currentPage = 0;
        this.pageSize = 10;
        this.totalPages = 0;
        this.selectedPosts = new Set();
        this.posts = [];
        this.filteredPosts = [];
        this.searchTimeout = null;
        this.postToDelete = null;
        
        this.init();
    }
    
    init() {
        console.log('Initializing Admin Posts Manager...');
        
        this.bindEvents();
        this.loadPosts();
        this.setupRealTimeUpdates();
    }
    
    bindEvents() {
        // Search functionality
        const searchInput = document.getElementById('searchInput');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                clearTimeout(this.searchTimeout);
                this.searchTimeout = setTimeout(() => {
                    this.handleSearch(e.target.value);
                }, 300);
            });
        }
        
        // Filter functionality
        const categoryFilter = document.getElementById('categoryFilter');
        const statusFilter = document.getElementById('statusFilter');
        
        if (categoryFilter) {
            categoryFilter.addEventListener('change', () => this.applyFilters());
        }
        
        if (statusFilter) {
            statusFilter.addEventListener('change', () => this.applyFilters());
        }
        
        // Select all checkbox
        const selectAllCheckbox = document.getElementById('selectAll');
        if (selectAllCheckbox) {
            selectAllCheckbox.addEventListener('change', () => this.toggleSelectAll());
        }
        
        // Keyboard shortcuts
        document.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey) {
                switch (e.key) {
                    case 'n':
                        e.preventDefault();
                        window.location.href = '/api/view/republic/posts/new';
                        break;
                    case 'r':
                        e.preventDefault();
                        this.refreshPosts();
                        break;
                    case 'a':
                        e.preventDefault();
                        this.toggleSelectAll();
                        break;
                }
            }
            
            if (e.key === 'Delete' && this.selectedPosts.size > 0) {
                this.bulkDelete();
            }
        });
    }
    
    async loadPosts() {
        try {
            this.showLoading();
            
            const response = await fetch(`/api/republic/posts?page=${this.currentPage}&size=${this.pageSize}`);
            const data = await response.json();
            
            if (data.result === 'SUCCESS' && data.payload?.data) {
                this.posts = data.payload.data.content || [];
                this.totalPages = data.payload.data.totalPages || 0;
                this.filteredPosts = [...this.posts];
                
                this.renderPosts();
                this.renderPagination();
                this.updateStats();
            } else {
                // Fallback to mock data for demonstration
                this.loadMockPosts();
            }
        } catch (error) {
            console.error('Error loading posts:', error);
            this.loadMockPosts();
        } finally {
            this.hideLoading();
        }
    }
    
    loadMockPosts() {
        // Mock data for demonstration
        this.posts = [
            {
                contentId: 1,
                contentTitle: 'Spring Boot 완벽 가이드 - 기초부터 실전까지',
                contentSummary: 'Spring Boot 프레임워크의 모든 것을 다루는 완벽한 가이드입니다. 초보자부터 숙련자까지 모두를 위한 실전 예제와 베스트 프랙티스를 포함합니다.',
                contentCategories: 'Java',
                author: 'Admin',
                publishedDate: '2024-01-15T10:00:00',
                inDate: '2024-01-15T09:30:00',
                views: 1234,
                likes: 89,
                commentCount: 15,
                status: 'published',
                contentThumbnail: '/static/images/spring-boot-thumb.jpg'
            },
            {
                contentId: 2,
                contentTitle: 'React 상태 관리 완벽 마스터하기',
                contentSummary: 'useState, useReducer, Context API, Redux까지 React의 모든 상태 관리 패턴을 실전 프로젝트와 함께 학습해보세요.',
                contentCategories: 'JavaScript',
                author: 'Admin',
                publishedDate: '2024-01-14T14:30:00',
                inDate: '2024-01-14T14:00:00',
                views: 892,
                likes: 67,
                commentCount: 22,
                status: 'published',
                contentThumbnail: '/static/images/react-thumb.jpg'
            },
            {
                contentId: 3,
                contentTitle: 'MySQL 성능 최적화 실전 가이드',
                contentSummary: '대용량 데이터베이스 환경에서의 MySQL 성능 최적화 기법과 인덱스 설계 전략을 실무 경험을 바탕으로 설명합니다.',
                contentCategories: 'Other',
                author: 'Admin',
                publishedDate: null,
                inDate: '2024-01-13T16:45:00',
                views: 0,
                likes: 0,
                commentCount: 0,
                status: 'draft',
                contentThumbnail: null
            },
            {
                contentId: 4,
                contentTitle: 'Python 웹 크롤링 자동화 프로젝트',
                contentSummary: 'Beautiful Soup과 Selenium을 활용한 웹 크롤링 프로젝트를 단계별로 구현해봅니다. 실제 사이트 크롤링 예제 포함.',
                contentCategories: 'Python',
                author: 'Admin',
                publishedDate: '2024-01-12T11:20:00',
                inDate: '2024-01-12T11:00:00',
                views: 567,
                likes: 43,
                commentCount: 8,
                status: 'published',
                contentThumbnail: '/static/images/python-thumb.jpg'
            },
            {
                contentId: 5,
                contentTitle: '보안 취약점 분석과 대응 전략',
                contentSummary: '웹 애플리케이션의 주요 보안 취약점을 식별하고 효과적인 대응 방안을 수립하는 방법을 다룹니다.',
                contentCategories: 'Security',
                author: 'Admin',
                publishedDate: null,
                inDate: '2024-01-11T09:15:00',
                views: 0,
                likes: 0,
                commentCount: 0,
                status: 'private',
                contentThumbnail: null
            }
        ];
        
        this.totalPages = 1;
        this.filteredPosts = [...this.posts];
        this.renderPosts();
        this.renderPagination();
        this.updateStats();
    }
    
    renderPosts() {
        const tbody = document.getElementById('postsTableBody');
        if (!tbody) return;
        
        if (this.filteredPosts.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center p-4">
                        <div class="text-muted">
                            <i class="fas fa-inbox fa-2x mb-2"></i>
                            <p>게시물이 없습니다.</p>
                        </div>
                    </td>
                </tr>
            `;
            return;
        }
        
        const postsHTML = this.filteredPosts.map(post => this.renderPostRow(post)).join('');
        tbody.innerHTML = postsHTML;
        
        // Update select all checkbox state
        this.updateSelectAllCheckbox();
    }
    
    renderPostRow(post) {
        const isSelected = this.selectedPosts.has(post.contentId);
        const thumbnail = post.contentThumbnail || '/static/images/default-post-thumb.jpg';
        const publishDate = post.publishedDate ? new Date(post.publishedDate).toLocaleDateString('ko-KR') : '-';
        const excerpt = post.contentSummary ? 
            (post.contentSummary.length > 100 ? post.contentSummary.substring(0, 100) + '...' : post.contentSummary) : 
            '내용 요약이 없습니다.';
        
        return `
            <tr class="${isSelected ? 'selected' : ''}" data-post-id="${post.contentId}">
                <td>
                    <input type="checkbox" 
                           class="post-checkbox" 
                           ${isSelected ? 'checked' : ''} 
                           onchange="adminPosts.togglePostSelection(${post.contentId})">
                </td>
                <td>
                    <img src="${thumbnail}" 
                         alt="${post.contentTitle}" 
                         class="post-thumbnail"
                         onerror="this.src='/static/images/default-post-thumb.jpg'">
                </td>
                <td>
                    <div>
                        <h4 class="post-title">
                            <a href="/api/view/public/mains/relay/${post.contentId}/" target="_blank">
                                ${post.contentTitle}
                            </a>
                        </h4>
                        <p class="post-excerpt">${excerpt}</p>
                    </div>
                </td>
                <td>
                    <span class="category-badge">
                        <i class="fas fa-tag"></i>
                        ${this.getCategoryName(post.contentCategories)}
                    </span>
                </td>
                <td>
                    <span class="status-badge status-${post.status}">
                        <i class="fas fa-${this.getStatusIcon(post.status)}"></i>
                        ${this.getStatusName(post.status)}
                    </span>
                </td>
                <td>
                    <div class="post-stats">
                        <div class="stat-item">
                            <i class="fas fa-eye"></i>
                            <span>${post.views || 0}</span>
                        </div>
                        <div class="stat-item">
                            <i class="fas fa-heart"></i>
                            <span>${post.likes || 0}</span>
                        </div>
                        <div class="stat-item">
                            <i class="fas fa-comment"></i>
                            <span>${post.commentCount || 0}</span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="post-meta">
                        <span>${publishDate}</span>
                        <span class="text-muted">${post.author || 'Unknown'}</span>
                    </div>
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-sm btn-primary" 
                                onclick="adminPosts.viewPost(${post.contentId})"
                                title="보기">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-warning" 
                                onclick="adminPosts.editPost(${post.contentId})"
                                title="편집">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-success" 
                                onclick="adminPosts.toggleStatus(${post.contentId}, '${post.status}')"
                                title="${post.status === 'published' ? '임시저장' : '게시'}">
                            <i class="fas fa-${post.status === 'published' ? 'eye-slash' : 'eye'}"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" 
                                onclick="adminPosts.deletePost(${post.contentId})"
                                title="삭제">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }
    
    renderPagination() {
        const pagination = document.getElementById('pagination');
        if (!pagination || this.totalPages <= 1) {
            if (pagination) pagination.innerHTML = '';
            return;
        }
        
        let paginationHTML = '';
        
        // Previous button
        paginationHTML += `
            <a href="#" class="pagination-item ${this.currentPage === 0 ? 'disabled' : ''}" 
               onclick="adminPosts.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </a>
        `;
        
        // Page numbers
        const startPage = Math.max(0, this.currentPage - 2);
        const endPage = Math.min(this.totalPages - 1, this.currentPage + 2);
        
        if (startPage > 0) {
            paginationHTML += `
                <a href="#" class="pagination-item" onclick="adminPosts.goToPage(0)">1</a>
            `;
            if (startPage > 1) {
                paginationHTML += `<span class="pagination-item disabled">...</span>`;
            }
        }
        
        for (let i = startPage; i <= endPage; i++) {
            paginationHTML += `
                <a href="#" class="pagination-item ${i === this.currentPage ? 'active' : ''}" 
                   onclick="adminPosts.goToPage(${i})">${i + 1}</a>
            `;
        }
        
        if (endPage < this.totalPages - 1) {
            if (endPage < this.totalPages - 2) {
                paginationHTML += `<span class="pagination-item disabled">...</span>`;
            }
            paginationHTML += `
                <a href="#" class="pagination-item" onclick="adminPosts.goToPage(${this.totalPages - 1})">${this.totalPages}</a>
            `;
        }
        
        // Next button
        paginationHTML += `
            <a href="#" class="pagination-item ${this.currentPage === this.totalPages - 1 ? 'disabled' : ''}" 
               onclick="adminPosts.goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </a>
        `;
        
        pagination.innerHTML = paginationHTML;
    }
    
    handleSearch(query) {
        const searchTerm = query.toLowerCase().trim();
        
        if (!searchTerm) {
            this.filteredPosts = [...this.posts];
        } else {
            this.filteredPosts = this.posts.filter(post => 
                post.contentTitle.toLowerCase().includes(searchTerm) ||
                (post.contentSummary && post.contentSummary.toLowerCase().includes(searchTerm)) ||
                (post.author && post.author.toLowerCase().includes(searchTerm))
            );
        }
        
        this.applyFilters();
    }
    
    applyFilters() {
        const categoryFilter = document.getElementById('categoryFilter').value;
        const statusFilter = document.getElementById('statusFilter').value;
        
        let filtered = [...this.posts];
        
        // Apply search filter
        const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
        if (searchTerm) {
            filtered = filtered.filter(post => 
                post.contentTitle.toLowerCase().includes(searchTerm) ||
                (post.contentSummary && post.contentSummary.toLowerCase().includes(searchTerm)) ||
                (post.author && post.author.toLowerCase().includes(searchTerm))
            );
        }
        
        // Apply category filter
        if (categoryFilter) {
            filtered = filtered.filter(post => post.contentCategories === categoryFilter);
        }
        
        // Apply status filter
        if (statusFilter) {
            filtered = filtered.filter(post => post.status === statusFilter);
        }
        
        this.filteredPosts = filtered;
        this.renderPosts();
        this.updateStats();
    }
    
    togglePostSelection(postId) {
        if (this.selectedPosts.has(postId)) {
            this.selectedPosts.delete(postId);
        } else {
            this.selectedPosts.add(postId);
        }
        
        this.updateBulkActions();
        this.updateSelectAllCheckbox();
        this.updateRowSelection(postId);
    }
    
    toggleSelectAll() {
        const selectAllCheckbox = document.getElementById('selectAll');
        
        if (selectAllCheckbox.checked) {
            // Select all visible posts
            this.filteredPosts.forEach(post => {
                this.selectedPosts.add(post.contentId);
            });
        } else {
            // Deselect all
            this.selectedPosts.clear();
        }
        
        this.updateBulkActions();
        this.renderPosts();
    }
    
    updateSelectAllCheckbox() {
        const selectAllCheckbox = document.getElementById('selectAll');
        if (!selectAllCheckbox) return;
        
        const visiblePostIds = this.filteredPosts.map(post => post.contentId);
        const selectedVisiblePosts = visiblePostIds.filter(id => this.selectedPosts.has(id));
        
        if (selectedVisiblePosts.length === 0) {
            selectAllCheckbox.checked = false;
            selectAllCheckbox.indeterminate = false;
        } else if (selectedVisiblePosts.length === visiblePostIds.length) {
            selectAllCheckbox.checked = true;
            selectAllCheckbox.indeterminate = false;
        } else {
            selectAllCheckbox.checked = false;
            selectAllCheckbox.indeterminate = true;
        }
    }
    
    updateBulkActions() {
        const bulkActions = document.getElementById('bulkActions');
        const selectedCount = document.getElementById('selectedCount');
        
        if (this.selectedPosts.size > 0) {
            bulkActions.classList.remove('hidden');
            selectedCount.textContent = this.selectedPosts.size;
        } else {
            bulkActions.classList.add('hidden');
        }
    }
    
    updateRowSelection(postId) {
        const row = document.querySelector(`tr[data-post-id="${postId}"]`);
        if (row) {
            if (this.selectedPosts.has(postId)) {
                row.classList.add('selected');
            } else {
                row.classList.remove('selected');
            }
        }
    }
    
    updateStats() {
        // Update any stats display if needed
        console.log(`Displaying ${this.filteredPosts.length} of ${this.posts.length} posts`);
    }
    
    // Post Actions
    viewPost(postId) {
        window.open(`/api/view/public/mains/relay/${postId}/`, '_blank');
    }
    
    editPost(postId) {
        window.location.href = `/api/view/republic/posts/edit/${postId}`;
    }
    
    async toggleStatus(postId, currentStatus) {
        const newStatus = currentStatus === 'published' ? 'draft' : 'published';
        
        try {
            const response = await fetch(`/api/republic/posts/${postId}/status`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'JY-ACCESS-TOKEN': this.getAuthToken()
                },
                body: JSON.stringify({ status: newStatus })
            });
            
            if (response.ok) {
                this.showNotification(`게시물 상태가 ${newStatus === 'published' ? '게시됨' : '임시저장'}으로 변경되었습니다.`, 'success');
                this.refreshPosts();
            } else {
                throw new Error('Status update failed');
            }
        } catch (error) {
            console.error('Error updating post status:', error);
            this.showNotification('상태 변경에 실패했습니다.', 'error');
        }
    }
    
    deletePost(postId) {
        this.postToDelete = postId;
        this.showModal('deleteModal');
    }
    
    async confirmDelete() {
        if (!this.postToDelete) return;
        
        try {
            const response = await fetch(`/api/republic/posts/${this.postToDelete}`, {
                method: 'DELETE',
                headers: {
                    'JY-ACCESS-TOKEN': this.getAuthToken()
                }
            });
            
            if (response.ok) {
                this.showNotification('게시물이 삭제되었습니다.', 'success');
                this.closeModal('deleteModal');
                this.refreshPosts();
            } else {
                throw new Error('Delete failed');
            }
        } catch (error) {
            console.error('Error deleting post:', error);
            this.showNotification('게시물 삭제에 실패했습니다.', 'error');
        }
        
        this.postToDelete = null;
    }
    
    // Bulk Actions
    async bulkPublish() {
        await this.bulkUpdateStatus('published');
    }
    
    async bulkDraft() {
        await this.bulkUpdateStatus('draft');
    }
    
    bulkDelete() {
        const count = this.selectedPosts.size;
        document.getElementById('bulkDeleteCount').textContent = count;
        this.showModal('bulkDeleteModal');
    }
    
    async confirmBulkDelete() {
        const postIds = Array.from(this.selectedPosts);
        
        try {
            const promises = postIds.map(id => 
                fetch(`/api/republic/posts/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'JY-ACCESS-TOKEN': this.getAuthToken()
                    }
                })
            );
            
            await Promise.all(promises);
            
            this.showNotification(`${postIds.length}개의 게시물이 삭제되었습니다.`, 'success');
            this.selectedPosts.clear();
            this.updateBulkActions();
            this.closeModal('bulkDeleteModal');
            this.refreshPosts();
        } catch (error) {
            console.error('Error bulk deleting posts:', error);
            this.showNotification('일괄 삭제에 실패했습니다.', 'error');
        }
    }
    
    async bulkUpdateStatus(status) {
        const postIds = Array.from(this.selectedPosts);
        
        try {
            const promises = postIds.map(id => 
                fetch(`/api/republic/posts/${id}/status`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'JY-ACCESS-TOKEN': this.getAuthToken()
                    },
                    body: JSON.stringify({ status })
                })
            );
            
            await Promise.all(promises);
            
            this.showNotification(`${postIds.length}개의 게시물 상태가 변경되었습니다.`, 'success');
            this.selectedPosts.clear();
            this.updateBulkActions();
            this.refreshPosts();
        } catch (error) {
            console.error('Error bulk updating status:', error);
            this.showNotification('일괄 상태 변경에 실패했습니다.', 'error');
        }
    }
    
    // Navigation
    goToPage(page) {
        if (page < 0 || page >= this.totalPages || page === this.currentPage) return;
        
        this.currentPage = page;
        this.loadPosts();
    }
    
    refreshPosts() {
        this.selectedPosts.clear();
        this.updateBulkActions();
        this.loadPosts();
    }
    
    // Real-time updates
    setupRealTimeUpdates() {
        // Poll for updates every 30 seconds
        setInterval(() => {
            if (!document.hidden) {
                this.loadPosts();
            }
        }, 30000);
        
        // Listen for visibility changes
        document.addEventListener('visibilitychange', () => {
            if (!document.hidden) {
                this.loadPosts();
            }
        });
    }
    
    // Utility methods
    getCategoryName(category) {
        const categoryMap = {
            'Java': 'Java',
            'JavaScript': 'JavaScript',
            'Python': 'Python',
            'C': 'C/C++',
            'Shell': 'Shell',
            'Security': '보안',
            'DeveloperCareerSkills': '커리어',
            'Other': '기타'
        };
        return categoryMap[category] || category;
    }
    
    getStatusName(status) {
        const statusMap = {
            'published': '게시됨',
            'draft': '임시저장',
            'private': '비공개'
        };
        return statusMap[status] || status;
    }
    
    getStatusIcon(status) {
        const iconMap = {
            'published': 'eye',
            'draft': 'edit',
            'private': 'lock'
        };
        return iconMap[status] || 'question';
    }
    
    getAuthToken() {
        return localStorage.getItem('jy-access-token') || '';
    }
    
    showLoading() {
        const tbody = document.getElementById('postsTableBody');
        if (tbody) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center p-4">
                        <div class="loading-spinner mx-auto mb-2"></div>
                        <p class="text-muted">게시물을 불러오는 중...</p>
                    </td>
                </tr>
            `;
        }
    }
    
    hideLoading() {
        // Loading will be replaced by actual content
    }
    
    showModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.add('show');
            document.body.style.overflow = 'hidden';
        }
    }
    
    closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('show');
            document.body.style.overflow = '';
        }
    }
    
    showNotification(message, type = 'info') {
        // Use toastr if available
        if (typeof toastr !== 'undefined') {
            toastr[type](message);
        } else {
            // Fallback to console
            console.log(`${type.toUpperCase()}: ${message}`);
            
            // Simple notification
            const notification = document.createElement('div');
            notification.className = `alert alert-${type}`;
            notification.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 10000;
                min-width: 300px;
                animation: slideInRight 0.3s ease;
            `;
            notification.innerHTML = `
                <div class="alert-content">
                    <div class="alert-message">${message}</div>
                </div>
            `;
            
            document.body.appendChild(notification);
            
            setTimeout(() => {
                notification.remove();
            }, 5000);
        }
    }
}

// Global instance
let adminPosts;

// Initialize function
function initializePostsManagement() {
    adminPosts = new AdminPostsManager();
}

// Global functions for onclick handlers
function refreshPosts() {
    if (adminPosts) adminPosts.refreshPosts();
}

function closeModal(modalId) {
    if (adminPosts) adminPosts.closeModal(modalId);
}

function confirmDelete() {
    if (adminPosts) adminPosts.confirmDelete();
}

function bulkPublish() {
    if (adminPosts) adminPosts.bulkPublish();
}

function bulkDraft() {
    if (adminPosts) adminPosts.bulkDraft();
}

function bulkDelete() {
    if (adminPosts) adminPosts.bulkDelete();
}

function confirmBulkDelete() {
    if (adminPosts) adminPosts.confirmBulkDelete();
}

function toggleSelectAll() {
    if (adminPosts) adminPosts.toggleSelectAll();
}

// Keyboard shortcuts info
document.addEventListener('DOMContentLoaded', () => {
    console.log('Admin Posts Manager Keyboard Shortcuts:');
    console.log('Ctrl/Cmd + N: New post');
    console.log('Ctrl/Cmd + R: Refresh');
    console.log('Ctrl/Cmd + A: Select all');
    console.log('Delete: Delete selected posts');
});