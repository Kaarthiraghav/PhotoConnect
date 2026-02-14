document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('form');
  if (!form) return;

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const name = document.getElementById('name')?.value || '';
    const email = document.getElementById('email')?.value || '';
    const phone = document.getElementById('phone')?.value || '';
    const message = document.getElementById('message')?.value || '';

    if (!name || !email) {
      alert('Please enter your name and email.');
      return;
    }

    if (!message) {
      alert('Please enter a message.');
      return;
    }

    // In a real application, this would send data to the backend
    // For now, we'll show a success message
    alert('Thanks! Your message has been received. We will get back to you soon.');
    
    // Optional: Send email via mailto
    const mailto = `mailto:info@photoconnectapp.com?subject=Contact%20Request%20from%20${encodeURIComponent(name)}&body=${encodeURIComponent(
      `Name: ${name}\nEmail: ${email}\nPhone: ${phone}\n\nMessage:\n${message}`
    )}`;
    
    // Reset form
    form.reset();
    
    // Optionally open email client
    // window.location.href = mailto;
  });
});
