export async function getTopPhotographers() {
    const res = await fetch("/api/photographers/top");
    return res.json();
}

export async function registerUser(payload) {
    const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    return res.json();
}

export async function loginUser(payload) {
    const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    return res.json();
}

export async function getPhotographers() {
    const res = await fetch('/api/photographers');
    return res.json();
}
