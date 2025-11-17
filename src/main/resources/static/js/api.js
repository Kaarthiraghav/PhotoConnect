export async function getTopPhotographers() {
    const res = await fetch("/api/photographers/top");
    return res.json();
}
