
document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const photographerId = params.get('id');

    // Load photographer profile from API
    if (photographerId) {
        try {
            const response = await fetch(`/api/photographers/${photographerId}`);
            if (!response.ok) throw new Error('Failed to load photographer');
            
            const photographer = await response.json();
            
            // Update profile with photographer data
            const nameElement = document.getElementById('photographer-name');
            if (nameElement) nameElement.textContent = photographer.name;
            
            const emailElement = document.getElementById('photographer-email');
            if (emailElement) emailElement.textContent = photographer.email;
            
            const ratingElement = document.getElementById('photographer-rating');
            if (ratingElement) ratingElement.textContent = `${(photographer.averageRating || 0).toFixed(1)} (${photographer.reviewCount || 0} reviews)`;
            
            const rateElement = document.getElementById('photographer-rate');
            if (rateElement) rateElement.textContent = `$${photographer.hourlyRate || 0}/hour`;
            
            const locationElement = document.getElementById('photographer-location');
            if (locationElement) locationElement.textContent = photographer.location || 'Not specified';
            
            const photoElement = document.getElementById('photographer-photo');
            if (photoElement) photoElement.style.backgroundImage = `url('${photographer.profilePhoto || '/images/default-avatar.png'}')`;
            
        } catch (error) {
            console.error('Error loading photographer profile:', error);
            document.body.innerHTML = '<p style="color: red; text-align: center; padding: 40px;">Failed to load photographer profile. Please try again later.</p>';
        }
    }

    // Book Now button handler
    const bookNowBtn = document.getElementById('book-now');
    if (bookNowBtn) {
        bookNowBtn.addEventListener('click', () => {
            const date = document.getElementById('booking-date')?.value;
            if (!photographerId) {
                alert('Please select a photographer first.');
                return;
            }
            const url = new URL('/pages/bookingpage.html', window.location.origin);
            url.searchParams.set('photographerId', photographerId);
            if (date) url.searchParams.set('date', date);
            window.location.href = url.toString();
        });
    }
});
