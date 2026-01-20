const { Stomp } = window;

let stompClient = null;
let currentRoomId = null;

function appendMessage(msg, me = false) {
  const container = document.getElementById('messages');
  if (!container) return;
  const el = document.createElement('div');
  el.className = 'message' + (me ? ' me' : '');
  const who = document.createElement('div');
  who.textContent = msg.senderId || 'Unknown';
  who.style.fontSize = '12px';
  who.style.opacity = '0.8';
  const text = document.createElement('div');
  text.textContent = msg.content || msg.message || '';
  el.appendChild(who);
  el.appendChild(text);
  container.appendChild(el);
  container.scrollTop = container.scrollHeight;
}

async function loadHistory(roomId) {
  try {
    const res = await fetch(`/api/chat/history/${roomId}`);
    if (!res.ok) throw new Error('history fetch failed');
    const msgs = await res.json();
    const container = document.getElementById('messages');
    container.innerHTML = '';
    msgs.forEach(m => appendMessage(m, false));
  } catch (e) {
    console.warn('Could not load history', e);
  }
}

function connect(roomId) {
  const socket = new SockJS('/ws-chat');
  stompClient = Stomp.over(socket);
  stompClient.configure({ debug: false });
  stompClient.connect({}, frame => {
    console.log('Connected to STOMP', frame);
    const sub = `/topic/chat/${roomId}`;
    stompClient.subscribe(sub, message => {
      try {
        const payload = JSON.parse(message.body);
        appendMessage(payload, false);
      } catch (e) {
        console.error('invalid message', e, message.body);
      }
    });
  }, err => {
    console.error('STOMP connect error', err);
  });
}

function disconnect() {
  if (stompClient) {
    stompClient.disconnect();
    stompClient = null;
  }
}

async function joinRoom() {
  const bookingId = document.getElementById('bookingId').value.trim();
  const userId = document.getElementById('userId').value.trim();
  if (!bookingId) {
    alert('Enter booking ID');
    return;
  }
  if (!userId) {
    alert('Enter your user id');
    return;
  }
  if (stompClient) {
    disconnect();
  }
  currentRoomId = bookingId;
  document.getElementById('currentRoom').textContent = bookingId;
  await loadHistory(bookingId);
  connect(bookingId);
}

function sendMessage() {
  const input = document.getElementById('messageInput');
  const userId = document.getElementById('userId').value.trim();
  const text = input.value.trim();
  if (!currentRoomId) { alert('Join a room first'); return; }
  if (!userId) { alert('Please enter your user ID'); return; }
  if (!text) return;
  const payload = { senderId: userId, content: text };
  try {
    stompClient.send(`/app/chat/${currentRoomId}`, {}, JSON.stringify(payload));
    appendMessage(payload, true);
    input.value = '';
  } catch (e) {
    console.error('send failed', e);
    alert('Failed to send');
  }
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('joinBtn').addEventListener('click', joinRoom);
  document.getElementById('sendBtn').addEventListener('click', sendMessage);
  document.getElementById('messageInput').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') sendMessage();
  });
});
