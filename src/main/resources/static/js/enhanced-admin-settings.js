/**
 * Enhanced Admin Settings Management
 * Advanced functionality for system configuration
 */

class AdminSettingsManager {
    constructor() {
        this.settings = {};
        this.originalSettings = {};
        this.hasUnsavedChanges = false;
        
        this.init();
    }
    
    init() {
        console.log('Initializing Admin Settings Manager...');
        
        this.loadSettings();
        this.bindEvents();
        this.setupAutoSave();
    }
    
    bindEvents() {
        // Tab switching
        document.querySelectorAll('.nav-tab').forEach(tab => {
            tab.addEventListener('click', (e) => {
                e.preventDefault();
                const targetTab = e.target.closest('.nav-tab').getAttribute('href').substring(1);
                this.switchTab(targetTab);
            });
        });
        
        // Form change detection
        document.addEventListener('change', (e) => {
            if (e.target.matches('input, select, textarea')) {
                this.markAsChanged();
            }
        });
        
        // Prevent accidental navigation
        window.addEventListener('beforeunload', (e) => {
            if (this.hasUnsavedChanges) {
                e.preventDefault();
                e.returnValue = '저장되지 않은 변경사항이 있습니다. 정말 나가시겠습니까?';
                return e.returnValue;
            }
        });
        
        // Keyboard shortcuts
        document.addEventListener('keydown', (e) => {
            if ((e.ctrlKey || e.metaKey) && e.key === 's') {
                e.preventDefault();
                this.saveSettings();
            }
        });
    }
    
    async loadSettings() {
        try {
            const response = await fetch('/api/republic/settings');
            const data = await response.json();
            
            if (data.result === 'SUCCESS') {
                this.settings = data.payload.data || {};
                this.originalSettings = { ...this.settings };
                this.applySettings();
            } else {
                // Load default settings
                this.loadDefaultSettings();
            }
        } catch (error) {
            console.error('Error loading settings:', error);
            this.loadDefaultSettings();
        }
    }
    
    loadDefaultSettings() {
        // Default settings for demonstration
        this.settings = {
            siteTitle: 'Jelog - 개발자 블로그',
            siteDescription: '개발자를 위한 기술 블로그입니다. 다양한 프로그래밍 언어와 개발 도구에 대한 실무 경험을 공유합니다.',
            postsPerPage: 10,
            commentsEnabled: true,
            seoEnabled: true,
            maintenanceMode: false,
            forceHttps: true,
            maxLoginAttempts: 5,
            sessionTimeout: 60,
            twoFactorAuth: false,
            cacheEnabled: true,
            imageCompression: true,
            cdnEnabled: false,
            autoBackup: true,
            backupInterval: 'daily'
        };
        
        this.originalSettings = { ...this.settings };
        this.applySettings();
    }
    
    applySettings() {
        // Apply settings to form elements
        Object.keys(this.settings).forEach(key => {
            const element = document.getElementById(key);
            if (element) {
                if (element.type === 'checkbox') {
                    element.checked = this.settings[key];
                    const toggle = element.closest('.toggle-switch');
                    if (toggle) {
                        toggle.classList.toggle('active', this.settings[key]);
                    }
                } else {
                    element.value = this.settings[key];
                }
            }
        });
        
        // Update maintenance banner
        const maintenanceBanner = document.getElementById('maintenanceBanner');
        if (maintenanceBanner) {
            maintenanceBanner.classList.toggle('active', this.settings.maintenanceMode);
        }
        
        this.hasUnsavedChanges = false;
    }
    
    switchTab(tabName) {
        // Update tab navigation
        document.querySelectorAll('.nav-tab').forEach(tab => {
            tab.classList.remove('active');
        });
        document.querySelector(`[href="#${tabName}"]`).classList.add('active');
        
        // Update tab content
        document.querySelectorAll('.tab-content').forEach(content => {
            content.classList.remove('active');
        });
        document.getElementById(tabName).classList.add('active');
        
        // Load tab-specific data
        this.loadTabData(tabName);
    }
    
    loadTabData(tabName) {
        switch (tabName) {
            case 'system':
                this.loadSystemStats();
                this.loadSystemLogs();
                break;
            case 'backup':
                this.loadBackupList();
                break;
        }
    }
    
    toggleSetting(settingName) {
        const toggle = event.target.closest('.toggle-switch');
        const isActive = toggle.classList.contains('active');
        
        toggle.classList.toggle('active');
        this.settings[settingName] = !isActive;
        this.markAsChanged();
        
        // Special handling for maintenance mode
        if (settingName === 'maintenanceMode') {
            const maintenanceBanner = document.getElementById('maintenanceBanner');
            if (maintenanceBanner) {
                maintenanceBanner.classList.toggle('active', this.settings[settingName]);
            }
            
            if (this.settings[settingName]) {
                this.showConfirmation(
                    '유지보수 모드를 활성화하시겠습니까?',
                    '사용자들이 사이트에 접근할 수 없게 됩니다.',
                    () => {
                        this.showNotification('유지보수 모드가 활성화되었습니다.', 'warning');
                    },
                    () => {
                        toggle.classList.remove('active');
                        this.settings[settingName] = false;
                        maintenanceBanner.classList.remove('active');
                    }
                );
            }
        }
        
        // Special handling for 2FA
        if (settingName === 'twoFactorAuth' && this.settings[settingName]) {
            this.showNotification('2단계 인증을 설정하려면 개별적으로 구성해야 합니다.', 'info');
        }
    }
    
    async saveSettings() {
        try {
            this.showLoading('설정을 저장하는 중...');
            
            // Collect current form values
            const formData = this.collectFormData();
            
            const response = await fetch('/api/republic/settings', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'JY-ACCESS-TOKEN': this.getAuthToken()
                },
                body: JSON.stringify(formData)
            });
            
            if (response.ok) {
                this.settings = { ...formData };
                this.originalSettings = { ...this.settings };
                this.hasUnsavedChanges = false;
                
                this.showNotification('설정이 성공적으로 저장되었습니다.', 'success');
                
                // Apply immediate effects
                this.applySettingsEffects();
            } else {
                throw new Error('Settings save failed');
            }
        } catch (error) {
            console.error('Error saving settings:', error);
            this.showNotification('설정 저장에 실패했습니다.', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    collectFormData() {
        const formData = {};
        
        // Text inputs and selects
        document.querySelectorAll('input[type="text"], input[type="number"], input[type="email"], select, textarea').forEach(element => {
            if (element.id) {
                formData[element.id] = element.value;
            }
        });
        
        // Toggle switches
        document.querySelectorAll('.toggle-switch').forEach(toggle => {
            const settingName = toggle.getAttribute('onclick')?.match(/toggleSetting\('([^']+)'\)/)?.[1];
            if (settingName) {
                formData[settingName] = toggle.classList.contains('active');
            }
        });
        
        return formData;
    }
    
    applySettingsEffects() {
        // Apply settings that have immediate effects
        if (this.settings.maintenanceMode) {
            console.log('Maintenance mode activated');
        }
        
        if (this.settings.cacheEnabled === false) {
            console.log('Cache disabled - clearing existing cache');
        }
    }
    
    resetSettings() {
        this.showConfirmation(
            '설정을 기본값으로 복원하시겠습니까?',
            '모든 사용자 정의 설정이 기본값으로 되돌아갑니다.',
            () => {
                this.loadDefaultSettings();
                this.showNotification('설정이 기본값으로 복원되었습니다.', 'info');
            }
        );
    }
    
    // System Operations
    async optimizeDatabase() {
        this.showConfirmation(
            '데이터베이스를 최적화하시겠습니까?',
            '최적화 중에는 일시적으로 성능이 저하될 수 있습니다.',
            async () => {
                try {
                    this.showLoading('데이터베이스를 최적화하는 중...');
                    
                    const response = await fetch('/api/republic/database/optimize', {
                        method: 'POST',
                        headers: {
                            'JY-ACCESS-TOKEN': this.getAuthToken()
                        }
                    });
                    
                    if (response.ok) {
                        this.showNotification('데이터베이스 최적화가 완료되었습니다.', 'success');
                    } else {
                        throw new Error('Database optimization failed');
                    }
                } catch (error) {
                    console.error('Error optimizing database:', error);
                    this.showNotification('데이터베이스 최적화에 실패했습니다.', 'error');
                } finally {
                    this.hideLoading();
                }
            }
        );
    }
    
    async clearCache() {
        this.showConfirmation(
            '모든 캐시를 삭제하시겠습니까?',
            '캐시 삭제 후 일시적으로 사이트 속도가 느려질 수 있습니다.',
            async () => {
                try {
                    this.showLoading('캐시를 삭제하는 중...');
                    
                    const response = await fetch('/api/republic/cache/clear', {
                        method: 'POST',
                        headers: {
                            'JY-ACCESS-TOKEN': this.getAuthToken()
                        }
                    });
                    
                    if (response.ok) {
                        this.showNotification('캐시가 성공적으로 삭제되었습니다.', 'success');
                    } else {
                        throw new Error('Cache clear failed');
                    }
                } catch (error) {
                    console.error('Error clearing cache:', error);
                    this.showNotification('캐시 삭제에 실패했습니다.', 'error');
                } finally {
                    this.hideLoading();
                }
            }
        );
    }
    
    // Backup Management
    async createBackup() {
        try {
            this.showLoading('백업을 생성하는 중...');
            
            const response = await fetch('/api/republic/backup/create', {
                method: 'POST',
                headers: {
                    'JY-ACCESS-TOKEN': this.getAuthToken()
                }
            });
            
            if (response.ok) {
                const result = await response.json();
                this.showNotification('백업이 성공적으로 생성되었습니다.', 'success');
                this.loadBackupList();
            } else {
                throw new Error('Backup creation failed');
            }
        } catch (error) {
            console.error('Error creating backup:', error);
            this.showNotification('백업 생성에 실패했습니다.', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async loadBackupList() {
        try {
            const response = await fetch('/api/republic/backup/list');
            const data = await response.json();
            
            if (data.result === 'SUCCESS') {
                this.renderBackupList(data.payload.data || []);
            }
        } catch (error) {
            console.error('Error loading backup list:', error);
        }
    }
    
    renderBackupList(backups) {
        const backupList = document.getElementById('backupList');
        if (!backupList) return;
        
        if (backups.length === 0) {
            backupList.innerHTML = `
                <div class="text-center text-muted p-4">
                    <i class="fas fa-inbox fa-2x mb-2"></i>
                    <p>백업 파일이 없습니다.</p>
                </div>
            `;
            return;
        }
        
        const backupsHTML = backups.map(backup => `
            <div class="backup-item">
                <div class="backup-info">
                    <h5 class="backup-name">${backup.filename}</h5>
                    <p class="backup-date">${this.formatDate(backup.createdAt)} - ${this.formatFileSize(backup.size)}</p>
                </div>
                <div class="backup-actions">
                    <button class="btn btn-sm btn-success" onclick="adminSettings.downloadBackup('${backup.filename}')">
                        <i class="fas fa-download"></i>
                    </button>
                    <button class="btn btn-sm btn-warning" onclick="adminSettings.restoreBackup('${backup.filename}')">
                        <i class="fas fa-undo"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="adminSettings.deleteBackup('${backup.filename}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `).join('');
        
        backupList.innerHTML = backupsHTML;
    }
    
    downloadBackup(filename) {
        window.open(`/api/republic/backup/download/${filename}`, '_blank');
    }
    
    restoreBackup(filename) {
        this.showConfirmation(
            '백업을 복원하시겠습니까?',
            `${filename} 백업으로 복원하면 현재 데이터가 모두 대체됩니다. 이 작업은 되돌릴 수 없습니다.`,
            async () => {
                try {
                    this.showLoading('백업을 복원하는 중...');
                    
                    const response = await fetch(`/api/republic/backup/restore/${filename}`, {
                        method: 'POST',
                        headers: {
                            'JY-ACCESS-TOKEN': this.getAuthToken()
                        }
                    });
                    
                    if (response.ok) {
                        this.showNotification('백업이 성공적으로 복원되었습니다. 페이지를 새로고침합니다.', 'success');
                        setTimeout(() => {
                            window.location.reload();
                        }, 2000);
                    } else {
                        throw new Error('Backup restore failed');
                    }
                } catch (error) {
                    console.error('Error restoring backup:', error);
                    this.showNotification('백업 복원에 실패했습니다.', 'error');
                } finally {
                    this.hideLoading();
                }
            }
        );
    }
    
    deleteBackup(filename) {
        this.showConfirmation(
            '백업 파일을 삭제하시겠습니까?',
            `${filename} 파일이 영구적으로 삭제됩니다.`,
            async () => {
                try {
                    const response = await fetch(`/api/republic/backup/delete/${filename}`, {
                        method: 'DELETE',
                        headers: {
                            'JY-ACCESS-TOKEN': this.getAuthToken()
                        }
                    });
                    
                    if (response.ok) {
                        this.showNotification('백업 파일이 삭제되었습니다.', 'success');
                        this.loadBackupList();
                    } else {
                        throw new Error('Backup delete failed');
                    }
                } catch (error) {
                    console.error('Error deleting backup:', error);
                    this.showNotification('백업 파일 삭제에 실패했습니다.', 'error');
                }
            }
        );
    }
    
    // System Monitoring
    async loadSystemStats() {
        try {
            const response = await fetch('/api/republic/system/stats');
            const data = await response.json();
            
            if (data.result === 'SUCCESS') {
                this.updateSystemStats(data.payload.data);
            }
        } catch (error) {
            console.error('Error loading system stats:', error);
            // Use mock data
            this.updateSystemStats({
                uptime: '72시간 15분',
                memoryUsage: 42,
                diskUsage: 67,
                cpuUsage: 15
            });
        }
    }
    
    updateSystemStats(stats) {
        const elements = {
            serverUptime: stats.uptime || '72시간',
            memoryUsage: (stats.memoryUsage || 42) + '%',
            diskUsage: (stats.diskUsage || 67) + '%',
            cpuUsage: (stats.cpuUsage || 15) + '%'
        };
        
        Object.keys(elements).forEach(id => {
            const element = document.getElementById(id);
            if (element) {
                element.textContent = elements[id];
            }
        });
    }
    
    async loadSystemLogs() {
        try {
            const response = await fetch('/api/republic/system/logs?limit=10');
            const data = await response.json();
            
            if (data.result === 'SUCCESS') {
                this.renderSystemLogs(data.payload.data);
            }
        } catch (error) {
            console.error('Error loading system logs:', error);
        }
    }
    
    renderSystemLogs(logs) {
        const logsContainer = document.getElementById('systemLogs');
        if (!logsContainer || !logs) return;
        
        const logsHTML = logs.map(log => `
            <div class="log-entry">
                <span class="log-time">[${this.formatLogDate(log.timestamp)}]</span>
                <span class="log-level-${log.level.toLowerCase()}">[${log.level}]</span>
                ${log.message}
            </div>
        `).join('');
        
        logsContainer.innerHTML = logsHTML;
    }
    
    // IP Blocking
    addBlockedIp() {
        const ipInput = document.getElementById('blockIpInput');
        const ip = ipInput.value.trim();
        
        if (!this.isValidIP(ip)) {
            this.showNotification('올바른 IP 주소를 입력해주세요.', 'error');
            return;
        }
        
        this.showConfirmation(
            'IP 주소를 차단하시겠습니까?',
            `${ip} 주소에서의 모든 접근이 차단됩니다.`,
            async () => {
                try {
                    const response = await fetch('/api/republic/security/block-ip', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'JY-ACCESS-TOKEN': this.getAuthToken()
                        },
                        body: JSON.stringify({ ip })
                    });
                    
                    if (response.ok) {
                        this.showNotification(`IP ${ip}가 차단되었습니다.`, 'success');
                        ipInput.value = '';
                    } else {
                        throw new Error('IP blocking failed');
                    }
                } catch (error) {
                    console.error('Error blocking IP:', error);
                    this.showNotification('IP 차단에 실패했습니다.', 'error');
                }
            }
        );
    }
    
    // Auto-save functionality
    setupAutoSave() {
        setInterval(() => {
            if (this.hasUnsavedChanges) {
                console.log('Auto-saving settings...');
                // Implement auto-save logic here
            }
        }, 30000); // Auto-save every 30 seconds
    }
    
    markAsChanged() {
        this.hasUnsavedChanges = true;
        
        // Visual indication of unsaved changes
        const saveButton = document.querySelector('.btn-primary.btn-lg');
        if (saveButton && !saveButton.classList.contains('btn-warning')) {
            saveButton.classList.remove('btn-primary');
            saveButton.classList.add('btn-warning');
            saveButton.innerHTML = '<i class="fas fa-exclamation-triangle"></i> 저장되지 않은 변경사항';
        }
    }
    
    // Utility methods
    isValidIP(ip) {
        const ipRegex = /^(\d{1,3}\.){3}\d{1,3}$/;
        if (!ipRegex.test(ip)) return false;
        
        return ip.split('.').every(octet => {
            const num = parseInt(octet);
            return num >= 0 && num <= 255;
        });
    }
    
    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
    
    formatLogDate(dateString) {
        const date = new Date(dateString);
        return date.toISOString().slice(0, 19).replace('T', ' ');
    }
    
    formatFileSize(bytes) {
        if (!bytes) return '0 B';
        const sizes = ['B', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(1024));
        return `${(bytes / Math.pow(1024, i)).toFixed(1)} ${sizes[i]}`;
    }
    
    getAuthToken() {
        return localStorage.getItem('jy-access-token') || '';
    }
    
    showLoading(message = '로딩 중...') {
        // Implementation for loading overlay
        console.log('Loading:', message);
    }
    
    hideLoading() {
        // Implementation for hiding loading overlay
        console.log('Loading complete');
    }
    
    showNotification(message, type = 'info') {
        if (typeof toastr !== 'undefined') {
            toastr[type](message);
        } else {
            console.log(`${type.toUpperCase()}: ${message}`);
        }
    }
    
    showConfirmation(title, message, onConfirm, onCancel) {
        const confirmed = confirm(`${title}\n\n${message}`);
        if (confirmed && onConfirm) {
            onConfirm();
        } else if (!confirmed && onCancel) {
            onCancel();
        }
    }
}

// Global instance
let adminSettings;

// Initialize function
function initializeSettings() {
    adminSettings = new AdminSettingsManager();
}

// Global functions for onclick handlers
function switchTab(tabName) {
    if (adminSettings) adminSettings.switchTab(tabName);
}

function toggleSetting(settingName) {
    if (adminSettings) adminSettings.toggleSetting(settingName);
}

function saveSettings() {
    if (adminSettings) adminSettings.saveSettings();
}

function resetSettings() {
    if (adminSettings) adminSettings.resetSettings();
}

function optimizeDatabase() {
    if (adminSettings) adminSettings.optimizeDatabase();
}

function clearCache() {
    if (adminSettings) adminSettings.clearCache();
}

function createBackup() {
    if (adminSettings) adminSettings.createBackup();
}

function downloadBackup(filename) {
    if (adminSettings) adminSettings.downloadBackup(filename);
}

function restoreBackup(filename) {
    if (adminSettings) adminSettings.restoreBackup(filename);
}

function deleteBackup(filename) {
    if (adminSettings) adminSettings.deleteBackup(filename);
}

function addBlockedIp() {
    if (adminSettings) adminSettings.addBlockedIp();
}

function loadSystemStats() {
    if (adminSettings) adminSettings.loadSystemStats();
}