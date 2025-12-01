
document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');

    if (!token) {
        // Not logged in, redirect to signin
        window.location.href = '/pages/signin.html';
        return;
    }

    try {
        const response = await fetch('/api/users/me', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const user = await response.json();
            
            // Update profile with user data
            const nameElement = document.getElementById('photographer-name');
            if (nameElement) nameElement.textContent = user.username;

            // Since we don't have other fields in User model yet, we leave defaults
            // or we could fetch extended profile if it existed.
            
        } else {
            // Token might be invalid
            localStorage.removeItem('token');
            window.location.href = '/pages/signin.html';
        }
    } catch (error) {
        console.error('Error fetching profile:', error);
    }
});
