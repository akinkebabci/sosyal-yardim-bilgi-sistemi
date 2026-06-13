/**
 * Main Application JavaScript
 * Utility functions ve form helpers
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('SAIS Application initialized');
});

// Utility functions can be added here

// Smooth scroll to top
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// Show loading indicator
function showLoading(message = 'Yükleniyor...') {
    // This can be integrated with PrimeFaces BlockUI
    console.log('Loading:', message);
}

// Hide loading indicator
function hideLoading() {
    console.log('Loading complete');
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('tr-TR', {
        style: 'currency',
        currency: 'TRY'
    }).format(amount);
}

// Format date
function formatDate(date) {
    return new Intl.DateTimeFormat('tr-TR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(new Date(date));
}

// Confirm dialog helper
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// Copy to clipboard
async function copyToClipboard(text) {
    try {
        await navigator.clipboard.writeText(text);
        showNotification('Panoya kopyalandı', 'success');
    } catch (err) {
        console.error('Kopyalama hatası:', err);
    }
}

// Show notification (can be replaced with PrimeFaces growl)
function showNotification(message, type = 'info') {
    console.log(`[${type.toUpperCase()}] ${message}`);
    // Integrate with PrimeFaces growl if available
}

// Print current page
function printPage() {
    window.print();
}

// Export data (placeholder)
function exportData(format = 'excel') {
    console.log('Exporting data as:', format);
    // Implement actual export logic
}

// Theme switcher (if needed)
function toggleTheme() {
    const body = document.body;
    body.classList.toggle('dark-theme');
    const isDark = body.classList.contains('dark-theme');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
}

// Initialize theme on load
document.addEventListener('DOMContentLoaded', function() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-theme');
    }
});

