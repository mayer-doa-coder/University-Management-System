// ========================================
// University Management System
// Main JavaScript Application
// ========================================

// Get auth credentials from session storage
function getAuthHeader() {
    const credentials = sessionStorage.getItem('authCredentials');
    if (!credentials) {
        window.location.href = 'index.html';
        return null;
    }
    return 'Basic ' + credentials;
}

// Check if user is authenticated
function checkAuth() {
    const credentials = sessionStorage.getItem('authCredentials');
    if (!credentials) {
        window.location.href = 'index.html';
        return false;
    }

    // Update user info in header
    const username = sessionStorage.getItem('username') || 'User';
    const role = sessionStorage.getItem('role') || 'USER';

    const userAvatar = document.getElementById('userAvatar');
    const userName = document.getElementById('userName');
    const userRole = document.getElementById('userRole');

    if (userAvatar) userAvatar.textContent = username.charAt(0).toUpperCase();
    if (userName) userName.textContent = username.charAt(0).toUpperCase() + username.slice(1);
    if (userRole) userRole.textContent = role === 'TEACHER' ? 'Teacher' : 'Student';

    return true;
}

// Logout function
function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

// API request helper
async function apiRequest(url, method = 'GET', body = null) {
    const authHeader = getAuthHeader();
    if (!authHeader) return null;

    const options = {
        method: method,
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    };

    if (body && method !== 'GET') {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

    if (response.status === 401) {
        logout();
        return null;
    }

    if (response.status === 204 || response.status === 403) {
        return { status: response.status };
    }

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

// Check if user is teacher
function isTeacher() {
    return sessionStorage.getItem('role') === 'TEACHER';
}

// Show alert message
function showAlert(container, message, type = 'danger') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `toast toast-${type}`;
    alertDiv.innerHTML = `${type === 'success' ? '✓' : '⚠'} ${message}`;

    container.innerHTML = '';
    container.appendChild(alertDiv);

    setTimeout(() => alertDiv.remove(), 4000);
}

// Modal functions - works with both old and new modal classes
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        modal.classList.add('open');
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
        modal.classList.remove('open');
    }
}

// Close modal when clicking outside
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay') || e.target.classList.contains('modal-wrap')) {
        e.target.classList.remove('active');
        e.target.classList.remove('open');
    }
});

// Format date
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
