let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleOpen;
ws.onmessage = handleMsg;

const messageInput = document.getElementById('messageBox');
const u = document.getElementById('userName');
const r = document.getElementById('room');
const page = document.getElementById('fullPage');

let chatMessages = [];
let joinMessages = [];
let leaveMessages = [];
let allMessages = [];
let userHasJoined = false;

let userColors = {}; // Store username → color mapping
ws.binaryType = "arraybuffer";


// Handle key presses
function handleKeyPress(ke) {
    if (ke.key === "Enter" || ke.target === btn) {
        let user = userName.value;
        let chatRoom = room.value;

        if (userName.value === "") {
            alert("Please enter a username");
            userName.value = "TestUsername";
            userName.select();
            return;
        }

        if (ke.target === userName) {
            room.focus();
        } else if (ke.target === room || ke.target === btn) {
            if (room.value === "") {
                alert("Please enter a Room Name");
                room.value = "RoomName";
                room.select();
                return;
            }

            if (!userHasJoined) {
                allMessages.push(user + " has joined " + chatRoom + ".");
                updateChat();
                userHasJoined = true;
            }

            let selectedColor = document.getElementById("ColorSelect").value || "black";
            userColors[user] = selectedColor;

            ws.send("join " + user + " " + chatRoom + " " + selectedColor);
            console.log("test duplicate 1");
            hideInputs();
            createRoom();
            updateChat();
        } else if (ke.target === messageBox || ke.target === send) {
            let message = messageInput.value.trim();
            if (message === "") return;

            let user = userName.value;
            let chatRoom = room.value;
            let color = userColors[user] || "black";

            ws.send("message " + user + " " + chatRoom + " " + message + " " + color);
            console.log("test duplicate 1");
            messageInput.value = "";
        } else if (ke.target === leave) {
            ws.send("leave " + user + " " + chatRoom);
            console.log("test duplicate 1");
            allMessages.push(user + " has left " + chatRoom + ".");
            updateChat();
            removeCanvas();
            showInputs();
            userHasJoined = false;
        }
    }
}

// Draw chat messages
function updateChat() {
    if (!userHasJoined) return;
    const canvas = document.getElementById("myCanvas");
    if (!canvas) return;
    const ctx = canvas.getContext("2d");

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#C9F5D9";
    ctx.fillRect(50, 50, 1497, 777);
    ctx.strokeStyle = "#3CD184";
    ctx.lineWidth = 3;
    ctx.strokeRect(50, 50, 1497, 777);

    ctx.textAlign = "left";
    ctx.textBaseline = "top";
    ctx.font = "16px Arial";

    const lineHeight = 22;

    allMessages.slice(-40).forEach((msg, i) => {
        ctx.fillStyle = typeof msg === "string" ? "black" : msg.color;
        ctx.fillText(typeof msg === "string" ? msg : msg.text, 60, 60 + i * lineHeight);
    });
}

userName.addEventListener('keypress', handleKeyPress);
room.addEventListener('keypress', handleKeyPress);
messageBox.addEventListener('keypress', handleKeyPress);

// UI Helpers
function hideInputs() {
    if (userHasJoined) {
        page.style.display = 'none';
        btn.style.display = 'none';
        send.style.display = 'block';
        leave.style.display = 'block';
        colorDropdown.style.display = 'block';
        colorSubmit.style.display = 'block';
    }
}

function showInputs() {
    if (!userHasJoined) {
        page.style.display = 'block';
        btn.style.display = 'block';
        send.style.display = 'none';
        leave.style.display = 'none';
        messageInput.style.display = 'none';
        colorDropdown.style.display = 'none';
        colorSubmit.style.display = 'none';
    }
}

function createRoom() {
    if (!userHasJoined) return;
    const canvas = document.createElement("canvas");
    canvas.width = 1550;
    canvas.height = 900;
    canvas.id = "myCanvas";
    document.body.appendChild(canvas);

    const ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#3CD184";
    ctx.lineWidth = 3;
    ctx.strokeRect(50, 50, 1497, 777);
    ctx.fillStyle = "#C9F5D9";
    ctx.fillRect(50, 50, 1497, 777);

    messageInput.style.display = 'block';
    messageInput.style.backgroundColor = "#C9F5D9";
    messageInput.style.outlineColor = "#3CD184";
    messageInput.style.position = 'absolute';
    messageInput.style.bottom = '40px';
    messageInput.style.left = '56px';
    messageInput.style.width = "1408px";
    messageInput.style.height = "100px";
}

function handleJoinBtn() {
    let ke = {};
    ke.key = "Enter";
    ke.target = btn;
    handleKeyPress(ke);
}

function removeCanvas() {
    const canvas = document.getElementById("myCanvas");
    if (canvas) canvas.remove();
}

function handleLeaveBtn() {
    let user = userName.value;
    let chatRoom = room.value;
    ws.send("leave " + user + " " + chatRoom);
    userHasJoined = false;
    setTimeout(() => {
        allMessages = [];
        chatMessages = [];
        joinMessages = [];
        leaveMessages = [];
        removeCanvas();
        showInputs();
    }, 100);
    updateChat();
}

function handleSendBtn() {
    let message = messageInput.value.trim();
    if (message === "") return;
    let user = userName.value;
    let chatRoom = room.value;
    let color = userColors[user] || "black";
    ws.send("message " + user + " " + chatRoom + " " + message + " " + color);
    messageInput.value = "";
}

function handleOpen() {
    console.log("websocket connection is on");
    messageInput.style.display = 'none';
    send.style.display = 'none';
    leave.style.display = 'none';
}

// Handle incoming messages
//function handleMsg(m) {
//    let text;
//    if (m.data instanceof Blob) {
//        m.data.text().then(t => processMsg(t));
//        return;
//    } else if (m.data instanceof ArrayBuffer) {
//        text = new TextDecoder("utf-8").decode(m.data);
//    } else {
//        text = m.data;
//    }
//    processMsg(text);
//}
//
//function processMsg(text) {
//    console.log(text);
//    if (!document.getElementById("myCanvas")) createRoom();
//
//    const msg = JSON.parse(text);
//    let messageText = "";
//
//    if (msg.type === "join") {
//        messageText = msg.user + " has joined " + msg.room + ".";
//        if (userHasJoined) {
//            allMessages.push({ text: messageText, color: msg.color });
//            updateChat();
//        }
//        return;
//    } else if (msg.type === "leave") {
//          messageText = msg.user + " has left " + msg.room + ".";
//          if (!userHasJoined) {
//              allMessages.push({ text: messageText, color: msg.color });
//              updateChat();
//          }
//          return;
//      } else if (msg.type === "message") {
//          messageText = msg.user + ": " + msg.message;
//          allMessages.push({ text: messageText, color: msg.color });
//          updateChat();
//          return;
//      } else if(msg.type === "color") {
//        messageText = msg.user + " has changed their text color to " + msg.color;
//        allMessages.push({ text: messageText, color: msg.color});
//        updateChat();
//    }
//
//    if (messageText && userHasJoined) {
//        allMessages.push({ text: messageText, color: msg.color });
//        updateChat();
//    }
//}

function handleMsg(m) {
let text;

if(m.data instanceof Blob) {
    text =  m.data.text();
} else if(m.data instanceof ArrayBuffer) {
    text = new TextDecoder("utf-8").decode(m.data);
} else {
    text = m.data;
}
console.log(text);
    if (!document.getElementById("myCanvas")) {
        createRoom();
    }
const msg = JSON.parse(text);
    let messageText = "";
    if (msg.type === "join") {
        console.log("joined a room");
        messageText = msg.user + " has joined " + msg.room + ".";
    } else if (msg.type === "leave") {
        messageText = msg.user + " has left " + msg.room + ".";
    } else if (msg.type === "message") {
        messageText = msg.user + ": " + msg.message;
    } else if(msg.type === "color") {
             messageText = msg.user + " has changed their text color to " + msg.color;
         }

    if (messageText && !allMessages.includes(messageText) && userHasJoined) {
        allMessages.push({ text: messageText, color: msg.color});
        updateChat();
    }
}


// UI setup
document.body.style.backgroundColor = "#A9EED1";

let btn = document.createElement("button");
btn.textContent = "Enter";
btn.onclick = handleJoinBtn;
document.body.appendChild(btn);
btn.style.backgroundColor = "#C9F5D9";
btn.style.outlineColor = "#3CD184";

let send = document.createElement("button");
send.textContent = "Send";
send.style.position = "fixed";
send.style.width = "88px";
send.style.height = "108px";
send.style.bottom = "40px";
send.style.right = "153px";
send.onclick = handleSendBtn;
document.body.appendChild(send);

let leave = document.createElement("button");
leave.textContent = "Leave Room";
leave.onclick = handleLeaveBtn;
leave.style.backgroundColor = "#C9F5D9";
leave.style.outlineColor = "#3CD184";
document.body.appendChild(leave);

// Color dropdown + button
let colorDropdown = document.getElementById("ColorSelect");
colorDropdown.style.display="block";
colorDropdown.style.position="absolute";
colorDropdown.style.top="10px";
colorDropdown.style.left="90px";

const colorSubmit = document.getElementById("submitButton");
colorSubmit.style.top="10px";
colorSubmit.style.left="193px";
colorSubmit.style.position = "fixed";
colorSubmit.onclick = () => {
    const user = userName.value;
    const chatRoom = room.value;
    const newColor = colorDropdown.value;
    userColors[user] = newColor;
    ws.send("color " + user + " " + chatRoom + " " + newColor);
    console.log("test ws send");
};


u.style.backgroundColor = "#C9F5D9";
u.style.outlineColor = "#3CD184";
r.style.backgroundColor = "#C9F5D9";
r.style.outlineColor = "#3CD184";