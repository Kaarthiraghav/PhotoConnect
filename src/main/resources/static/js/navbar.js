fetch("/components/navbar.html")
  .then((res) => res.text())
  .then((html) => {
    document.getElementById("navbar").innerHTML = html;
    // After injecting markup, render right side depending on auth state
    try {
      const navRight = document.getElementById("nav-right");
      const token =
        localStorage.getItem("authToken") || localStorage.getItem("token");
      if (token) {
        // Logged-in UI: bell + avatar
        const avatarUrl =
          localStorage.getItem("avatarUrl") || "/images/default-avatar.png";
        navRight.innerHTML = `
          <div class="nav-controls">
            <button class="icon-btn bell" aria-label="Notifications">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M15 17H9a3 3 0 0 0 6 0z" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/><path d="M18 8a6 6 0 10-12 0c0 7-3 7-3 7h18s-3 0-3-7" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/></svg>
              <span class="notif-badge">1</span>
            </button>
            <div class="avatar-wrap">
              <img src="${avatarUrl}" alt="User avatar" class="avatar" />
            </div>
          </div>`;
      } else {
        // Not logged-in: keep Sign In button behavior
        navRight.innerHTML = `<button class="signin-btn" aria-label="Sign In">Sign In</button>`;
      }

function renderNavRight() {
  const right = document.getElementById("navRight");
  if (!right) return;

  const token = localStorage.getItem("token");
  
  if (token) {
    right.innerHTML = `
      <button class="icon-btn" id="notifBtn" aria-label="Notifications">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M18 8a6 6 0 10-12 0c0 7-3 8-3 8h18s-3-1-3-8"/>
          <path d="M13.73 21a2 2 0 01-3.46 0"/>
        </svg>
        <span class="badge">1</span>
      </button>
      <div class="user-menu">
        <img class="avatar" src="https://i.pravatar.cc/64?img=12" alt="User" id="userAvatar"/>
        <button id="logoutBtn" class="logout-btn">Logout</button>
      </div>
    `;
    
    document.getElementById('logoutBtn').addEventListener('click', () => {
        localStorage.removeItem('token');
        window.location.href = '/pages/signin.html';
    });

  } else {
    right.innerHTML = `<a href="/pages/signin.html" class="signin-btn" aria-label="Sign In">Sign In</a>`;
  }
}
