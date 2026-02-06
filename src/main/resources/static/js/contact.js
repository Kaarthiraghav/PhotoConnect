document.addEventListener('DOMContentLoaded', () => {
  const submitBtn = document.querySelector('.submit-btn');
  if (!submitBtn) return;

  submitBtn.addEventListener('click', () => {
    const name = document.querySelector('.input-field input[type="text"]')?.value || '';
    const email = document.querySelector('.input-field-2 input[type="email"]')?.value || '';
    const message = document.querySelector('.textarea-field .textarea')?.textContent || '';

    if (!name || !email) {
      alert('Please enter your name and email.');
      return;
    }

    alert('Thanks! Your message has been recorded.');
    const mailto = `mailto:info@photoconnectapp.com?subject=Contact%20Request&body=${encodeURIComponent(
      `Name: ${name}\nEmail: ${email}\nMessage: ${message}`
    )}`;
    window.location.href = mailto;
  });
});
