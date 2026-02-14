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
        localStorage.getItem("avatarUrl") || "/images/default-avatar.svg";
      navRight.innerHTML = `
        <div class="nav-controls">
          <button class="icon-btn bell" aria-label="Notifications">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M15 17H9a3 3 0 0 0 6 0z" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/><path d="M18 8a6 6 0 10-12 0c0 7-3 7-3 7h18s-3 0-3-7" stroke="#000" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/></svg>
            <span class="notif-badge" style="display:none;">0</span>
          </button>
          <button class="avatar-btn" aria-label="Open dashboard">
            <span class="avatar-wrap">
              <img src="${avatarUrl}" alt="User avatar" class="avatar" />
            </span>
          </button>
          <button class="logout-btn" aria-label="Log Out">Log Out</button>
        </div>`;
      
      // Load notification count
      loadNotificationCount(token);
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

    // Add bell notification handler
    const bellBtn = document.querySelector(".bell");
    if (bellBtn) {
      bellBtn.addEventListener("click", () => {
        showNotifications(token);
      });
    }
  })
  .catch((err) => {
    console.error("Failed to load navbar:", err);
  });

// Load notification count
async function loadNotificationCount(token) {
  try {
    const res = await fetch("/api/notifications/unread-count", {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!res.ok) return;
    const count = await res.json();
    const badge = document.querySelector(".notif-badge");
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? "inline-flex" : "none";
    }
  } catch (err) {
    console.error("Failed to load notification count:", err);
  }
}

// Show notifications dropdown
async function showNotifications(token) {
  try {
    const res = await fetch("/api/notifications", {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!res.ok) throw new Error("Failed to load notifications");
    const notifications = await res.json();
    
    // Create dropdown
    let dropdown = document.querySelector(".notif-dropdown");
    if (dropdown) {
      dropdown.remove();
      return; // Toggle off
    }
    
    dropdown = document.createElement("div");
    dropdown.className = "notif-dropdown";
    dropdown.innerHTML = `
      <div class="notif-header">
        <h3>Notifications</h3>
        <button class="mark-all-read">Mark all as read</button>
      </div>
      <div class="notif-list">
        ${notifications.length === 0 
          ? '<p class="no-notifs">No notifications</p>' 
          : notifications.slice(0, 10).map(n => `
            <div class="notif-item ${n.read ? 'read' : 'unread'}" data-id="${n.id}">
              <div class="notif-icon">${getNotifIcon(n.type)}</div>
              <div class="notif-content">
                <p class="notif-title">${n.title}</p>
                <p class="notif-message">${n.message}</p>
                <span class="notif-time">${formatTime(n.createdAt)}</span>
              </div>
            </div>
          `).join('')
        }
      </div>
    `;
    
    document.body.appendChild(dropdown);
    
    // Position dropdown
    const bellBtn = document.querySelector(".bell");
    const rect = bellBtn.getBoundingClientRect();
    dropdown.style.top = `${rect.bottom + 8}px`;
    dropdown.style.right = `${window.innerWidth - rect.right}px`;
    
    // Add click handlers
    dropdown.querySelector(".mark-all-read")?.addEventListener("click", async () => {
      try {
        await fetch("/api/notifications/mark-all-read", {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` },
        });
        loadNotificationCount(token);
        dropdown.remove();
      } catch (err) {
        console.error("Failed to mark all as read:", err);
      }
    });
    
    // Mark individual as read on click
    dropdown.querySelectorAll(".notif-item.unread").forEach(item => {
      item.addEventListener("click", async () => {
        const id = item.dataset.id;
        try {
          await fetch(`/api/notifications/${id}/read`, {
            method: "PUT",
            headers: { Authorization: `Bearer ${token}` },
          });
          item.classList.remove("unread");
          item.classList.add("read");
          loadNotificationCount(token);
        } catch (err) {
          console.error("Failed to mark as read:", err);
        }
      });
    });
    
    // Close on outside click
    setTimeout(() => {
      document.addEventListener("click", function closeDropdown(e) {
        if (!dropdown.contains(e.target) && !bellBtn.contains(e.target)) {
          dropdown.remove();
          document.removeEventListener("click", closeDropdown);
        }
      });
    }, 100);
    
  } catch (err) {
    console.error("Failed to show notifications:", err);
    alert("Failed to load notifications");
  }
}

function getNotifIcon(type) {
  const icons = {
    BOOKING: '📅',
    MESSAGE: '💬',
    PAYMENT: '💳',
    REVIEW: '⭐',
    SYSTEM: '🔔'
  };
  return icons[type] || '📢';
}

function formatTime(timestamp) {
  const date = new Date(timestamp);
  const now = new Date();
  const diff = now - date;
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  
  if (days > 0) return `${days}d ago`;
  if (hours > 0) return `${hours}h ago`;
  if (minutes > 0) return `${minutes}m ago`;
  return 'Just now';
}
