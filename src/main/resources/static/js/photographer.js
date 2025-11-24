document.addEventListener("DOMContentLoaded", () => {
  // Mock photographer data (replace with real API calls later)
  const photographer = {
    id: 1,
    name: "John Doe",
    avatar: "/images/default-avatar.png",
    rate: "$60 / hr",
    location: "Colombo, LK",
    description:
      "Experienced portrait and landscape photographer. Loves natural light and candid moments.",
    gallery: [
      "/images/sample1.jpg",
      "/images/sample2.jpg",
      "/images/sample3.jpg",
      "/images/sample4.jpg",
      "/images/sample5.jpg",
      "/images/sample6.jpg",
    ],
  };

  // Populate UI
  const nameEl = document.getElementById("photographer-name");
  const avatarEl = document.getElementById("photographer-avatar");
  const rateEl = document.getElementById("photographer-rate");
  const locEl = document.getElementById("photographer-location");
  const descEl = document.getElementById("photographer-desc");
  const galleryEl = document.getElementById("past-work");

  if (nameEl) nameEl.textContent = photographer.name;
  if (avatarEl) avatarEl.src = photographer.avatar;
  if (rateEl) rateEl.textContent = photographer.rate;
  if (locEl) locEl.textContent = photographer.location;
  if (descEl) descEl.textContent = photographer.description;

  // Render gallery
  if (galleryEl) {
    photographer.gallery.forEach((src) => {
      const item = document.createElement("div");
      item.className = "panel-item";
      const img = document.createElement("img");
      img.src = src;
      img.alt = "Past work";
      item.appendChild(img);
      galleryEl.appendChild(item);
    });
  }

  // Book now behavior
  const bookBtn = document.getElementById("book-now");
  const dateInput = document.getElementById("booking-date");
  if (bookBtn) {
    bookBtn.addEventListener("click", () => {
      const token =
        localStorage.getItem("authToken") || localStorage.getItem("token");
      if (!token) {
        window.location.href = "/pages/signin.html";
        return;
      }
      const date = dateInput ? dateInput.value : null;
      if (!date) {
        alert("Please select a date before booking.");
        return;
      }

      // Placeholder: send request to backend when API available
      // fetch('/api/requests', { method: 'POST', ... })
      alert(`Request sent to ${photographer.name} for ${date}`);
    });
  }
});
