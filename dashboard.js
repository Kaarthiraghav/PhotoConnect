// Common Dashboard Functionality

// Admin Dashboard Functions
const adminFunctions = {
    pendingRequests: [
        { name: 'Sarah Mitchell', type: 'Wedding Photography', date: '2 hours ago' },
        { name: 'Mike Johnson', type: 'Portrait Photography', date: '5 hours ago' },
        { name: 'Emma Davis', type: 'Event Photography', date: '1 day ago' },
        { name: 'Alex Chen', type: 'Product Photography', date: '1 day ago' }
    ],

    reports: [
        'User Activity Report',
        'Photo Upload Statistics',
        'Payment Analytics'
    ],

    renderRequests: function() {
        const requestsList = document.getElementById('requestsList');
        if (!requestsList) return;
        
        requestsList.innerHTML = this.pendingRequests.map(request => `
            <div class="request-item">
                <div class="request-info">
                    <div class="request-name">${request.name}</div>
                    <div class="request-type">${request.type}</div>
                    <div class="request-date">${request.date}</div>
                </div>
                <div class="request-actions">
                    <button class="btn btn-approve" onclick="adminFunctions.approveRequest('${request.name}')">
                        <i class="fas fa-check-circle"></i>
                        Approve
                    </button>
                    <button class="btn btn-reject" onclick="adminFunctions.rejectRequest('${request.name}')">
                        <i class="fas fa-times-circle"></i>
                        Reject
                    </button>
                </div>
            </div>
        `).join('');
    },

    renderReports: function() {
        const reportsGrid = document.getElementById('reportsGrid');
        if (!reportsGrid) return;
        
        reportsGrid.innerHTML = this.reports.map(report => `
            <div class="report-item" onclick="adminFunctions.viewReport('${report}')">
                <div class="report-name">${report}</div>
                <span class="report-link">View →</span>
            </div>
        `).join('');
    },

    approveRequest: function(name) {
        alert(`Approved request from ${name}`);
    },

    rejectRequest: function(name) {
        alert(`Rejected request from ${name}`);
    },

    viewReport: function(reportName) {
        alert(`Opening ${reportName}`);
    },

    init: function() {
        this.renderRequests();
        this.renderReports();
    }
};

// Photographer Dashboard Functions
const photographerFunctions = {
    init: function() {
        // Edit Profile button
        const editProfileBtn = document.querySelector('.profile-content .btn-primary');
        if (editProfileBtn) {
            editProfileBtn.addEventListener('click', () => {
                alert('Opening profile editor...');
            });
        }

        // Open Chat button
        const openChatBtn = document.querySelector('.chat-content .btn-primary');
        if (openChatBtn) {
            openChatBtn.addEventListener('click', () => {
                alert('Opening chat interface...');
            });
        }

        // Update Availability button
        const updateAvailabilityBtn = document.querySelector('.chat-content .btn-secondary');
        if (updateAvailabilityBtn) {
            updateAvailabilityBtn.addEventListener('click', () => {
                alert('Opening availability settings...');
            });
        }
    }
};

// Client Dashboard Functions
const clientFunctions = {
    events: [
        { name: 'Wedding Album', status: 'Ready', statusClass: 'status-ready' },
        { name: 'Birthday Party', status: 'Processing', statusClass: 'status-processing' },
        { name: 'Corporate Event', status: 'Scheduled', statusClass: 'status-scheduled' }
    ],

    renderEvents: function() {
        const eventsList = document.getElementById('eventsList');
        if (!eventsList) return;
        
        eventsList.innerHTML = this.events.map(event => `
            <div class="event-item">
                <div class="event-content">
                    <span class="event-name">${event.name}</span>
                    <span class="event-status ${event.statusClass}">${event.status}</span>
                </div>
            </div>
        `).join('');
    },

    init: function() {
        this.renderEvents();

        // Message Photographer button
        const messageBtn = document.querySelector('.chat-content .btn-primary');
        if (messageBtn) {
            messageBtn.addEventListener('click', () => {
                alert('Opening chat with photographer...');
            });
        }

        // View Event Pictures button
        const viewPicturesBtn = document.querySelector('.chat-content .btn-secondary');
        if (viewPicturesBtn) {
            viewPicturesBtn.addEventListener('click', () => {
                alert('Opening event pictures gallery...');
            });
        }
    }
};

// Initialize appropriate dashboard on page load
document.addEventListener('DOMContentLoaded', () => {
    // Detect which dashboard is loaded based on page content
    if (document.getElementById('requestsList')) {
        // Admin Dashboard
        adminFunctions.init();
    } else if (document.querySelector('.profile-content')) {
        // Photographer Dashboard
        photographerFunctions.init();
    } else if (document.getElementById('eventsList')) {
        // Client Dashboard
        clientFunctions.init();
    }
});