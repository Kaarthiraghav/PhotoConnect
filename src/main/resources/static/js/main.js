import { getTopPhotographers } from "/js/api.js";

function injectSkeletons() {
  const container = document.getElementById("top-photographers");
  if (!container) return;
  container.innerHTML = Array.from({ length: 3 })
    .map(() => '<div class="skeleton-card" aria-hidden="true"></div>')
    .join("");
}

async function loadTopPhotographers() {
  const container = document.getElementById("top-photographers");
  injectSkeletons();
  try {
    const data = await getTopPhotographers();
    const topThree = data.slice(0, 3);
    const medalClass = ["gold", "silver", "bronze"];
    container.innerHTML = topThree
      .map(
        (p, i) => `
          <div class="ranking-card" tabindex="0" aria-label="${p.name} ranked ${
          i + 1
        }">
            <div class="medal ${medalClass[i]}">${
          i === 0 ? "1st" : i === 1 ? "2nd" : "3rd"
        }</div>
            <div class="ranking-avatar">
              ${
                p.profilePhoto
                  ? `<img src="${p.profilePhoto}" alt="${p.name}" style="width:100%;height:100%;object-fit:cover;"/>`
                  : '<span aria-hidden="true">👤</span>'
              }
            </div>
            <p class="name">${p.name}</p>
            <p class="events">${p.totalEvents} events</p>
          </div>`
      )
      .join("");
  } catch (e) {
    const sample = [
      { name: "John Doe", totalEvents: 42 },
      { name: "Jane Doe", totalEvents: 35 },
      { name: "Alex Doe", totalEvents: 30 },
    ];
    const medalClass = ["gold", "silver", "bronze"];
    container.innerHTML = sample
      .map(
        (p, i) => `
      <div class="ranking-card" tabindex="0" aria-label="${
        p.name
      } sample ranked ${i + 1}">
        <div class="medal ${medalClass[i]}">${
          i === 0 ? "1st" : i === 1 ? "2nd" : "3rd"
        }</div>
        <div class="ranking-avatar"><span aria-hidden="true">👤</span></div>
        <p class="name">${p.name}</p>
        <p class="events">${p.totalEvents} events</p>
      </div>`
      )
      .join("");
  }
}

document
  .querySelector(".hero-btn")
  ?.addEventListener("click", () => {
    window.location.href = "/pages/photographers.html";
  });

loadTopPhotographers();
