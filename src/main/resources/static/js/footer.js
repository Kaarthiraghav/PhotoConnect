document.addEventListener("DOMContentLoaded", function () {
    const footerContainer = document.getElementById("footer");
    if (footerContainer) {
        footerContainer.innerHTML = `
            <footer class="footer">
                <div>
                    <h3>PhotoConnect</h3>
                    <p>Find the perfect photographer for your special moments.</p>
                </div>
                <div>
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="/index.html">Home</a></li>
                        <li><a href="/pages/photographers.html">Photographers</a></li>
                        <li><a href="/pages/about_us.html">About Us</a></li>
                        <li><a href="/pages/contact_us.html">Contact Us</a></li>
                    </ul>
                </div>
                <div>
                    <h3>Follow Us</h3>
                    <ul>
                        <li><a href="https://facebook.com" target="_blank" rel="noopener">Facebook</a></li>
                        <li><a href="https://twitter.com" target="_blank" rel="noopener">Twitter</a></li>
                        <li><a href="https://instagram.com" target="_blank" rel="noopener">Instagram</a></li>
                    </ul>
                </div>
                <div>
                    <h3>Support</h3>
                    <ul>
                        <li><a href="/pages/contact_us.html">FAQ</a></li>
                        <li><a href="/pages/contact_us.html">Privacy Policy</a></li>
                        <li><a href="/pages/contact_us.html">Terms of Service</a></li>
                    </ul>
                </div>
            </footer>
        `;
    }
});
