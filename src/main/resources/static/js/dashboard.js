// Common Dashboard Functionality

// Helper function to get token
function getAuthToken() {
    return localStorage.getItem('token');
}

// Helper function to get current user
async function getCurrentUser() {
    const token = getAuthToken();
    if (!token) return null;
    
    try {
        const response = await fetch('/api/users/me', {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (response.ok) return await response.json();
    } catch (error) {
        console.error('Error fetching user:', error);
    }
    return null;
}

// Admin Dashboard Functions
const adminFunctions = {
    pendingRequests: [],

    loadRequests: async function() {
        const token = getAuthToken();
        if (!token) return;
        
        try {
            const response = await fetch('/api/photographers', {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (response.ok) {
                this.pendingRequests = await response.json();
            }
        } catch (error) {
            console.error('Error loading photographer requests:', error);
        }
    },

    reports: [
        'User Activity Report',
        'Photo Upload Statistics',
        'Payment Analytics'
    ],

    renderRequests: function() {
        const requestsList = document.getElementById('requestsList');
        if (!requestsList) return;
        
        if (this.pendingRequests.length === 0) {
            requestsList.innerHTML = '<p style="text-align: center; padding: 20px;">No pending requests</p>';
            return;
        }
        
        requestsList.innerHTML = this.pendingRequests.map((request, idx) => `
            <div class="request-item" data-id="${idx}">
                <div class="request-info">
                    <div class="request-name">${request.name}</div>
                    <div class="request-type">Photographer Registration</div>
                    <div class="request-date">${request.email}</div>
                </div>
                <div class="request-actions">
                    <button class="btn btn-approve" onclick="adminFunctions.approveRequest(${idx})">
                        <i class="fas fa-check-circle"></i>
                        Approve
                    </button>
                    <button class="btn btn-reject" onclick="adminFunctions.rejectRequest(${idx})">
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

    approveRequest: function(idx) {
        if (idx >= 0 && idx < this.pendingRequests.length) {
            const request = this.pendingRequests[idx];
            this.pendingRequests.splice(idx, 1);
            this.renderRequests();
            alert(`Approved request from ${request.name}`);
        }
    },

    rejectRequest: function(idx) {
        if (idx >= 0 && idx < this.pendingRequests.length) {
            const request = this.pendingRequests[idx];
            this.pendingRequests.splice(idx, 1);
            this.renderRequests();
            alert(`Rejected request from ${request.name}`);
        }
    },

    viewReport: function(reportName) {
        window.location.href = `/pages/dashboards/admin.html#reports`;
        alert(`Opening ${reportName}`);
    },

    init: async function() {
        await this.loadRequests();
        this.renderRequests();
        this.renderReports();
    }
};

// Photographer Dashboard Functions
const photographerFunctions = {
    bookings: [],

    loadBookings: async function() {
        const token = getAuthToken();
        const user = await getCurrentUser();
        if (!token || !user) return;
        
        try {
            const response = await fetch(`/api/bookings/photographer/${user.id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (response.ok) {
                this.bookings = await response.json();
            }
        } catch (error) {
            console.error('Error loading bookings:', error);
        }
    },

    renderBookings: function() {
        const bookingsList = document.getElementById('bookingsList');
        if (!bookingsList) return;
        
        if (this.bookings.length === 0) {
            bookingsList.innerHTML = '<p style="text-align: center; padding: 20px;">No bookings yet</p>';
            return;
        }
        
        bookingsList.innerHTML = this.bookings.map(booking => `
            <div class="booking-item">
                <div class="booking-info">
                    <div class="booking-client">Client: ${booking.clientId}</div>
                    <div class="booking-date">Date: ${new Date(booking.eventDate).toLocaleDateString()}</div>
                    <div class="booking-status">${booking.status}</div>
                </div>
            </div>
        `).join('');
    },

    init: async function() {
        await this.loadBookings();
        this.renderBookings();
        
        // Edit Profile button
        const editProfileBtn = document.querySelector('.profile-content .btn-primary');
        if (editProfileBtn) {
            editProfileBtn.addEventListener('click', () => {
                window.location.href = '/pages/phreq.html';
            });
        }

        // Open Chat button
        const openChatBtn = document.querySelector('.chat-content .btn-primary');
        if (openChatBtn) {
            openChatBtn.addEventListener('click', () => {
                window.location.href = '/pages/chat.html';
            });
        }

        // Update Availability button
        const updateAvailabilityBtn = document.querySelector('.chat-content .btn-secondary');
        if (updateAvailabilityBtn) {
            updateAvailabilityBtn.addEventListener('click', () => {
                window.location.href = '/pages/photographer_profile.html';
            });
        }
    }
};

// Client Dashboard Functions
const clientFunctions = {
    bookings: [],

    loadBookings: async function() {
        const token = getAuthToken();
        const user = await getCurrentUser();
        if (!token || !user) return;
        
        try {
            const response = await fetch(`/api/bookings/client/${user.id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (response.ok) {
                this.bookings = await response.json();
            }
        } catch (error) {
            console.error('Error loading bookings:', error);
        }
    },

    renderBookings: function() {
        const eventsList = document.getElementById('eventsList');
        if (!eventsList) return;
        
        if (this.bookings.length === 0) {
            eventsList.innerHTML = '<p style="text-align: center; padding: 20px;">No bookings yet</p>';
            return;
        }
        
        eventsList.innerHTML = this.bookings.map(booking => `
            <div class="event-item">
                <div class="event-content">
                    <span class="event-name">Booking #${booking.id}</span>
                    <span class="event-status status-${booking.status.toLowerCase()}">${booking.status}</span>
                </div>
                <div class="event-date">${new Date(booking.eventDate).toLocaleDateString()}</div>
            </div>
        `).join('');
    },

    init: async function() {
        await this.loadBookings();
        this.renderBookings();

        // Message Photographer button
        const messageBtn = document.querySelector('.chat-content .btn-primary');
        if (messageBtn) {
            messageBtn.addEventListener('click', () => {
                window.location.href = '/pages/chat.html';
            });
        }

        // View Event Pictures button
        const viewPicturesBtn = document.querySelector('.chat-content .btn-secondary');
        if (viewPicturesBtn) {
            viewPicturesBtn.addEventListener('click', () => {
                window.location.href = '/pages/photographer_profile.html';
            });
        }
    }
};

// Initialize appropriate dashboard on page load
document.addEventListener('DOMContentLoaded', async () => {
    // Detect which dashboard is loaded based on page content
    if (document.getElementById('requestsList')) {
        // Admin Dashboard
        await adminFunctions.init();
    } else if (document.querySelector('.profile-content')) {
        // Photographer Dashboard
        await photographerFunctions.init();
    } else if (document.getElementById('eventsList')) {
        // Client Dashboard
        await clientFunctions.init();
    }
});
