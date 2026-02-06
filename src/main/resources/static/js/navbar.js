fetch("/components/navbar.html")
  .then((res) => res.text())
  .then((html) => {
    document.getElementById("navbar").innerHTML = html;
    // After injecting markup, render right side depending on auth state
    const navRight = document.getElementById("nav-right");
    const token = localStorage.getItem("token");
    
    if (token) {
      // Logged-in UI: bell + avatar + logout
      const avatarUrl =
        localStorage.getItem("avatarUrl") || "/images/default-avatar.png";
      navRight.innerHTML = `
        <div class="nav-controls">
          <button class="icon-btn bell" aria-label="Notifications">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M15 17H9a3 3 0 0 0 6 0z" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/><path d="M18 8a6 6 0 10-12 0c0 7-3 7-3 7h18s-3 0-3-7" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/></svg>
            <span class="notif-badge">1</span>
          </button>
          <button class="avatar-btn" aria-label="Open dashboard">
            <span class="avatar-wrap">
              <img src="${avatarUrl}" alt="User avatar" class="avatar" />
            </span>
          </button>
          <button class="logout-btn" aria-label="Log Out">Log Out</button>
        </div>`;
    } else {
      // Not logged-in: keep Sign In button behavior
      navRight.innerHTML = `<button class="signin-btn" aria-label="Sign In">Sign In</button>`;
    }

    // Add event listeners for Sign In button
    const signInBtn = document.querySelector(".signin-btn");
    if (signInBtn) {
      signInBtn.addEventListener("click", () => {
        window.location.href = "/pages/signin.html";
      });
    }

    const avatarBtn = document.querySelector(".avatar-btn");
    if (avatarBtn) {
      avatarBtn.addEventListener("click", async () => {
        try {
          const res = await fetch("/api/users/me", {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (!res.ok) throw new Error("Failed to load user");
          const user = await res.json();
          const role = (user.role || "").toUpperCase();
          if (role.includes("ADMIN")) {
            window.location.href = "/pages/dashboards/admin.html";
          } else if (role.includes("PHOTOGRAPHER")) {
            window.location.href = "/pages/dashboards/photographer.html";
          } else {
            window.location.href = "/pages/dashboards/client.html";
          }
        } catch (err) {
          console.error("Failed to redirect to dashboard", err);
          window.location.href = "/pages/dashboards/client.html";
        }
      });
    }

    const logoutBtn = document.querySelector(".logout-btn");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("token");
        localStorage.removeItem("avatarUrl");
        window.location.href = "/index.html";
      });
    }
  })
  .catch((err) => {
    console.error("Failed to load navbar:", err);
  });
