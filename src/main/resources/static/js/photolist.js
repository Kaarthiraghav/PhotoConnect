
        // Sample photographer data
        const photographers = [
            { name: "Clara Photography", location: "Colombo", rating: 4.9, reviews: 127, rate: 150 },
            { name: "Sarah Miller", location: "Kandy", rating: 4.8, reviews: 98, rate: 120 },
            { name: "Michael Chen", location: "Colombo", rating: 5.0, reviews: 156, rate: 200 },
            { name: "Emma Davis", location: "Colombo", rating: 4.7, reviews: 89, rate: 130 },
            { name: "James Wilson", location: "Galle",  rating: 4.9, reviews: 112, rate: 180 },
            { name: "Olivia Brown", location: "Kandy",  rating: 4.8, reviews: 134, rate: 160 },
            { name: "David Lee", location: "NuwaraEliya",  rating: 4.9, reviews: 145, rate: 140 },
            { name: "Sophia Garcia", location: "Ella",  rating: 4.7, reviews: 76, rate: 110 },
            { name: "Daniel Martinez", location: "Galle", rating: 5.0, reviews: 189, rate: 190 },
           
        ];

        let currentPage = 1;
        let filteredPhotographers = [...photographers];

        // Generate photographer cards
        function displayPhotographers() {
            const grid = document.getElementById('photographerGrid');
            grid.innerHTML = '';

            filteredPhotographers.forEach(photographer => {
                const card = document.createElement('div');
                card.className = 'photographer-card';
                card.innerHTML = `
                    <div class="card-image"></div>
                    <div class="card-content">
                        <div class="photographer-name">${photographer.name}</div>
                        <div class="photographer-location">📍 ${photographer.location}</div>
                
                        <div class="card-footer">
                            <div class="rating">
                                <span class="stars">★</span>
                                <span>${photographer.rating} (${photographer.reviews})</span>
                            </div>
                            <div class="rate">$${photographer.rate}<span>/hr</span></div>
                        </div>
                    </div>
                `;
                grid.appendChild(card);
            });

            document.getElementById('resultCount').textContent = filteredPhotographers.length;
        }

        // Apply filters
        function applyFilters() {
            const location = document.getElementById('location').value.toLowerCase();
            const maxRate = document.getElementById('rate').value;
            

            filteredPhotographers = photographers.filter(p => {
                const matchLocation = !location || p.location.toLowerCase().includes(location);
                const matchRate = !maxRate || p.rate <= parseInt(maxRate);
                
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
                    filteredPhotographers.sort((a, b) => b.rating - a.rating);
                    break;
                case 'price-low':
                    filteredPhotographers.sort((a, b) => a.rate - b.rate);
                    break;
                case 'price-high':
                    filteredPhotographers.sort((a, b) => b.rate - a.rate);
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
   