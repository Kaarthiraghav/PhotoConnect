const hourlyRate = 150;

function getQueryParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
}

async function getCurrentUser() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    const res = await fetch('/api/users/me', {
        headers: { Authorization: `Bearer ${token}` }
    });
    if (!res.ok) return null;
    return res.json();
}

        // Update summary as form is filled
        document.getElementById('duration').addEventListener('input', updateSummary);
        document.getElementById('eventDate').addEventListener('change', updateSummary);
        document.getElementById('eventTime').addEventListener('change', updateSummary);
        document.getElementById('venue').addEventListener('input', updateSummary);

        function updateSummary() {
            const duration = document.getElementById('duration').value || 0;
            const eventDate = document.getElementById('eventDate').value;
            const eventTime = document.getElementById('eventTime').value;
            const venue = document.getElementById('venue').value;

            // Update summary display
            document.getElementById('summaryDuration').textContent = duration ? `${duration} hours` : '- hours';
            document.getElementById('summaryDate').textContent = eventDate ? formatDate(eventDate) : 'Not selected';
            document.getElementById('summaryTime').textContent = eventTime ? formatTime(eventTime) : 'Not selected';
            document.getElementById('summaryVenue').textContent = venue || 'Not specified';

            // Calculate costs
            const subtotal = hourlyRate * duration;
            const serviceFee = subtotal * 0.10;
            const total = subtotal + serviceFee;

            document.getElementById('subtotal').textContent = `$${subtotal}`;
            document.getElementById('serviceFee').textContent = `$${serviceFee.toFixed(2)}`;
            document.getElementById('totalAmount').textContent = `$${total.toFixed(2)}`;
        }

        function formatDate(dateString) {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US', { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric' });
        }

        function formatTime(timeString) {
            const [hours, minutes] = timeString.split(':');
            const hour = parseInt(hours);
            const ampm = hour >= 12 ? 'PM' : 'AM';
            const displayHour = hour % 12 || 12;
            return `${displayHour}:${minutes} ${ampm}`;
        }

        // Set minimum date to today
        const today = new Date().toISOString().split('T')[0];
        const eventDateInput = document.getElementById('eventDate');
        eventDateInput.setAttribute('min', today);
        const prefillDate = getQueryParam('date');
        if (prefillDate) {
            eventDateInput.value = prefillDate;
        }

        // Form submission
        document.getElementById('bookingForm').addEventListener('submit', async function(e) {
            e.preventDefault();

            const token = localStorage.getItem('token');
            if (!token) {
                window.location.href = '/pages/signin.html';
                return;
            }

            const photographerId = getQueryParam('photographerId');
            if (!photographerId) {
                alert('Missing photographer. Please choose a photographer first.');
                window.location.href = '/pages/photographers.html';
                return;
            }

            const eventDate = document.getElementById('eventDate').value;
            const eventTime = document.getElementById('eventTime').value;
            if (!eventDate || !eventTime) {
                alert('Please select event date and time.');
                return;
            }

            try {
                const user = await getCurrentUser();
                if (!user || !user.id) {
                    alert('Unable to identify user. Please sign in again.');
                    localStorage.removeItem('token');
                    window.location.href = '/pages/signin.html';
                    return;
                }

                const eventDateTime = `${eventDate}T${eventTime}:00`;
                const res = await fetch(`/api/bookings?clientId=${encodeURIComponent(user.id)}&photographerId=${encodeURIComponent(photographerId)}&eventDate=${encodeURIComponent(eventDateTime)}`,
                    { method: 'POST', headers: { Authorization: `Bearer ${token}` } }
                );

                if (!res.ok) throw new Error('Booking failed');

                const successMessage = document.getElementById('successMessage');
                successMessage.classList.add('show');
                window.scrollTo({ top: 0, behavior: 'smooth' });
                setTimeout(() => {
                    this.reset();
                    updateSummary();
                    successMessage.classList.remove('show');
                }, 3000);
            } catch (err) {
                console.error(err);
                alert('Booking failed. Please try again.');
            }
        });

        // Initialize summary
        updateSummary();