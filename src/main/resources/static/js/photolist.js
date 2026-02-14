
        let photographers = [];
        let currentPage = 1;
        let filteredPhotographers = [];

        // Fetch photographers from API
        async function loadPhotographers() {
            try {
                const response = await fetch('/api/photographers');
                if (!response.ok) throw new Error('Failed to load photographers');
                photographers = await response.json();
                filteredPhotographers = [...photographers];
                displayPhotographers();
            } catch (error) {
                console.error('Error loading photographers:', error);
                // Show error message to user
                document.getElementById('photographerGrid').innerHTML = '<p style="color:red;">Failed to load photographers. Please try again later.</p>';
            }
        }

        // Generate photographer cards
        function displayPhotographers() {
            const grid = document.getElementById('photographerGrid');
            grid.innerHTML = '';

            if (filteredPhotographers.length === 0) {
                grid.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 40px;">No photographers found matching your criteria.</p>';
                document.getElementById('resultCount').textContent = '0';
                return;
            }

            filteredPhotographers.forEach((photographer) => {
                const card = document.createElement('div');
                card.className = 'photographer-card';
                card.setAttribute('role', 'button');
                card.setAttribute('tabindex', '0');
                card.dataset.photographerId = String(photographer.id);
                card.innerHTML = `
                    <div class="card-image" style="background-image: url('${photographer.profilePhoto || '/images/default-avatar.svg'}'); background-size: cover; background-position: center;"></div>
                    <div class="card-content">
                        <div class="photographer-name">${photographer.name}</div>
                        <div class="photographer-location">📍 ${photographer.location || 'Not specified'}</div>
                
                        <div class="card-footer">
                            <div class="rating">
                                <span class="stars">★</span>
                                <span>${photographer.averageRating.toFixed(1)} (${photographer.reviewCount})</span>
                            </div>
                            <div class="rate">$${photographer.hourlyRate || 0}<span>/hr</span></div>
                        </div>
                    </div>
                `;
                card.addEventListener('click', () => {
                    window.location.href = `/pages/photographer_profile.html?id=${card.dataset.photographerId}`;
                });
                card.addEventListener('keydown', (e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault();
                        card.click();
                    }
                });
                grid.appendChild(card);
            });

            document.getElementById('resultCount').textContent = filteredPhotographers.length;
        }

        // Apply filters
        function applyFilters() {
            const location = document.getElementById('location').value.toLowerCase();
            const maxRate = document.getElementById('rate').value;
            

            filteredPhotographers = photographers.filter(p => {
                const matchLocation = !location || (p.location && p.location.toLowerCase().includes(location));
                const matchRate = !maxRate || p.hourlyRate <= parseInt(maxRate);
                
                return matchLocation && matchRate;
            });

            displayPhotographers();
        }

        // Reset filters
        function resetFilters() {
            document.getElementById('location').value = '';
            document.getElementById('rate').value = '';
            document.getElementById('date').value = '';
          
            filteredPhotographers = [...photographers];
            displayPhotographers();
        }

        // Sort photographers
        function sortPhotographers() {
            const sortBy = document.getElementById('sort').value;
            
            switch(sortBy) {
                case 'rating':
                    filteredPhotographers.sort((a, b) => (b.averageRating || 0) - (a.averageRating || 0));
                    break;
                case 'price-low':
                    filteredPhotographers.sort((a, b) => (a.hourlyRate || 0) - (b.hourlyRate || 0));
                    break;
                case 'price-high':
                    filteredPhotographers.sort((a, b) => (b.hourlyRate || 0) - (a.hourlyRate || 0));
                    break;
                default:
                    filteredPhotographers.sort((a, b) => b.reviews - a.reviews);
            }
            
            displayPhotographers();
        }

        // Pagination functions
        function changePage(direction) {
            if (direction === 'prev' && currentPage > 1) {
                currentPage--;
            } else if (direction === 'next' && currentPage < 4) {
                currentPage++;
            }
            updatePagination();
        }

        function goToPage(page) {
            currentPage = page;
            updatePagination();
        }

        function updatePagination() {
            document.querySelectorAll('.page-btn').forEach((btn, index) => {
                if (index > 0 && index < 5) {
                    btn.classList.toggle('active', index === currentPage);
                }
            });
            document.getElementById('prevBtn').disabled = currentPage === 1;
            document.getElementById('nextBtn').disabled = currentPage === 4;
        }

        // Initialize
        displayPhotographers();
   