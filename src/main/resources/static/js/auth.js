import { registerUser, loginUser } from '/js/api.js';

document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signupForm');
    const signinForm = document.getElementById('signinForm');

    if (signupForm) {
        signupForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('signupName').value.trim();
            const email = document.getElementById('signupEmail').value.trim();
            const password = document.getElementById('signupPassword').value;

            try {
                const res = await registerUser({ username, email, password });
                if (res && res.message) {
                    alert('Registered: ' + res.message);
                    window.location.href = '/pages/signin.html';
                } else if (res && res.error) {
                    alert('Error: ' + (res.message || res.error));
                } else {
                    alert('Registered successfully');
                    window.location.href = '/pages/signin.html';
                }
            } catch (err) {
                console.error(err);
                alert('Registration failed');
            }
        });
    }

    if (signinForm) {
        signinForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('signinEmail').value.trim();
            const password = document.getElementById('signinPassword').value;

            try {
                const res = await loginUser({ email, password });
                if (res && res.token) {
                    localStorage.setItem('token', res.token);
                    alert('Logged in');
                    window.location.href = '/';
                } else if (res && res.error) {
                    alert('Login failed: ' + (res.message || res.error));
                } else {
                    alert('Login response received');
                    window.location.href = '/';
                }
            } catch (err) {
                console.error(err);
                alert('Login failed');
            }
        });
    }
});
