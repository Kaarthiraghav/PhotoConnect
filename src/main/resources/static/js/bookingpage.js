const hourlyRate = 150;

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
        document.getElementById('eventDate').setAttribute('min', today);

        // Form submission
        document.getElementById('bookingForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Show success message
            const successMessage = document.getElementById('successMessage');
            successMessage.classList.add('show');
            
            // Scroll to top
            window.scrollTo({ top: 0, behavior: 'smooth' });
            
            // Reset form after 2 seconds
            setTimeout(() => {
                this.reset();
                updateSummary();
                successMessage.classList.remove('show');
            }, 3000);
        });

        // Initialize summary
        updateSummary();