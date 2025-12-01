fetch("/components/navbar.html")
  .then((res) => res.text())
  .then((html) => {
    document.getElementById("navbar").innerHTML = html;
    renderNavRight();
  });

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
