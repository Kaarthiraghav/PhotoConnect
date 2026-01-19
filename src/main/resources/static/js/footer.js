
document.addEventListener("DOMContentLoaded", function () {
    const footer = document.getElementById("footer");
    if (footer) {
        footer.innerHTML = `
            <footer class="footer-container">
                <div class="footer-content">
                    <p>&copy; ${new Date().getFullYear()} PhotoConnect. All rights reserved.</p>
                    <div class="social-links">
                        <a href="#"><i class="fab fa-facebook-f"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                    </div>
                </div>
            </footer>
        `;
    }
});
