let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleOpen
ws.onmessage = handleMsg;
const messageInput = document.getElementById('messageBox');
let chatMessages = [];
const page = document.getElementById('fullPage');

function handleKeyPress( ke ) {
    if(ke.key === "Enter" || ke.target === btn) {
        let user = userName.value;
        let chatRoom = room.value;
        if(userName.value === "") {
            alert("Please enter a username");
            userName.value = "TestUsername";
            userName.select();
            return;
        }

        if(ke.target === userName) {
            room.focus();
        }


        else if(ke.target === room || ke.target === btn) {
            if(room.value === "") {
                alert("Please enter a Room Name");
                room.value = "RoomName";
                room.select();
                return;
            }

            ws.send("join " + user + " " + chatRoom);

            hideInputs();

            createRoom();

        }
        else if(ke.target === messageBox || ke.target === send) {
            let message = messageInput.value.trim();
            if (message === "") return;
            ws.send("message " + message);
            messageInput.value = "";
        }
        else if(ke.target === leave) {
            ws.send("leave " + user + " " + chatRoom);
        }
    }
}

function updateChat() {
    const canvas = document.getElementById("myCanvas");
    if (!canvas) {
        console.warn("Canvas element not found!");
        return;
    }
    const ctx = canvas.getContext("2d");

    // Clear entire canvas to start fresh
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw background rectangle
    ctx.fillStyle = "lightgreen";
    ctx.fillRect(50, 50, 1497, 777);

    // Draw border rectangle
    ctx.strokeStyle = "green";
    ctx.lineWidth = 3;
    ctx.strokeRect(50, 50, 1497, 777);

    // Draw chat text
    ctx.fillStyle = "black";
    ctx.font = "16px Arial";
    let lineHeight = 20;
    chatMessages.slice(-40).forEach((msg, i) => {
        ctx.fillText(msg, 60, 80 + i * lineHeight);
    });
}

userName.addEventListener('keypress',handleKeyPress);
room.addEventListener('keypress',handleKeyPress);
messageBox.addEventListener('keypress',handleKeyPress);

function hideInputs() {
    page.style.display = 'none';
    btn.style.display = 'none';
    send.style.display = 'block';
    leave.style.display = 'block';
}

function createRoom() {
    const canvas = document.createElement("canvas");
    canvas.width = 1550;
    canvas.height = 900;
    canvas.id = "myCanvas";

    document.body.appendChild(canvas);

    const ctx = canvas.getContext("2d");

    ctx.strokeStyle = "green";
    ctx.lineWidth = 3;
    ctx.strokeRect(50, 50, 1497, 777);

    ctx.fillStyle = "lightgreen";
    ctx.fillRect(50, 50, 1497, 777);

    ctx.stroke();

    messageInput.style.display = 'block';

    messageInput.style.backgroundColor = "lightgreen";
    messageInput.style.outlineColor = "green";

    messageInput.style.position = 'absolute'; // Or 'relative', 'fixed'
    messageInput.style.bottom = '40px';
    messageInput.style.left = '56px';
    messageInput.style.width = "1408px";
    messageInput.style.height = "100px"
}

function handleJoinBtn() {
    let ke = {};
    ke.key = "Enter"
    ke.target = btn;
    handleKeyPress(ke);
}

function handleLeaveBtn() {
    let ke = {};
    ke.key = "Enter"
    ke.target = leave;
    handleKeyPress(ke);

    page.style.display = 'block';
    btn.style.display = 'block';
    send.style.display = 'none';
    leave.style.display = 'none';
    myCanvas.style.display = 'none';
    messageInput.style.display = 'none';
}

function handleSendBtn() {
    let message = messageInput.value.trim();
    if (message === "") return;

    ws.send("message " + message);
    messageInput.value = "";
}

function handleOpen() {
    messageInput.style.display = 'none';
    send.style.display = 'none';
    leave.style.display = 'none';
}

function handleMsg(m) {
    let msg = JSON.parse(m.data);
    console.log(msg.room);
    let text = msg.user + ": " + msg.message;
    chatMessages.push(text);
    updateChat();
}

let btn = document.createElement("button");
btn.textContent = "Enter";
btn.onclick = handleJoinBtn;
document.body.appendChild(btn);

let send = document.createElement("button");
send.textContent = "Send";
send.style.position = "fixed";
send.style.width = "86px";
send.style.height = "108px";
send.style.bottom = "41px";
send.style.right = "103px";
send.onclick = handleSendBtn;
document.body.appendChild(send);

let leave = document.createElement("button");
leave.textContent = "Leave Room";
leave.onclick = handleLeaveBtn;
document.body.appendChild(leave);