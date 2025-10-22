let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleOpen
ws.onmessage = handleMsg;
const messageInput = document.getElementById('messageBox');
let chatMessages = [];
let joinMessages = [];
let leaveMessages = [];
let allMessages = [];
const page = document.getElementById('fullPage');
let userHasJoined = false;

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

            if (!userHasJoined) {
                allMessages.push(user + " has joined " + chatRoom + ".");
                updateChat();
                userHasJoined = true;
            }
            ws.send("join " + user + " " + chatRoom);
            hideInputs();
            createRoom();
            updateChat();
        }
        else if(ke.target === messageBox || ke.target === send) {
            let message = messageInput.value.trim();
            if (message === "") return;
            ws.send("message " + message);
            messageInput.value = "";
        }
        else if(ke.target === leave) {
            ws.send("leave " + user + " " + chatRoom);
            allMessages.push(user + " has left " + chatRoom + ".");
            updateChat();
            removeCanvas();
            showInputs();
            userHasJoined = false;
        }
    }
}

function updateChat() {
    const canvas = document.getElementById("myCanvas");
    if (!canvas) return;
    const ctx = canvas.getContext("2d");

    // Clear everything
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw background and border
    ctx.fillStyle = "lightgreen";
    ctx.fillRect(50, 50, 1497, 777);
    ctx.strokeStyle = "green";
    ctx.lineWidth = 3;
    ctx.strokeRect(50, 50, 1497, 777);

    // Draw all text (join + main chat + leave)
    ctx.textAlign = "left";
    ctx.textBaseline = "top";
    ctx.fillStyle = "black";
    ctx.font = "16px Arial";


    const lineHeight = 22;
    allMessages.slice(-40).forEach((msg, i) => {
        ctx.fillText(msg, 60, 60 + i * lineHeight);
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

function showInputs() {
    page.style.display = 'block';
    btn.style.display = 'block';
    send.style.display = 'none';
    leave.style.display = 'none';
    messageInput.style.display = 'none';
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
    messageInput.style.height = "100px";

    canvas.style.display = 'block';
    canvas.style.zIndex = '10';
}

function handleJoinBtn() {
    let ke = {};
    ke.key = "Enter"
    ke.target = btn;
    handleKeyPress(ke);
}

function removeCanvas() {
    const canvas = document.getElementById("myCanvas");
    if (canvas) {
        canvas.remove(); // Completely removes element from DOM
    }
}

function handleLeaveBtn() {
    let user = userName.value;
    let chatRoom = room.value;
    ws.send("leave " + user + " " + chatRoom);

    setTimeout(() => {
        allMessages = [];
        chatMessages = [];
        joinMessages = [];
        leaveMessages = [];
        updateChat();
        removeCanvas();
        showInputs();
    }, 100);
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
    console.log("Received raw message:", m.data); // Debug log
    let msg = JSON.parse(m.data);
    if (!document.getElementById("myCanvas")) {
        createRoom();
    }

    let messageText = "";
    if (msg.type === "join") {
        messageText = msg.user + " has joined " + msg.room + ".";
    } else if (msg.type === "leave") {
        messageText = msg.user + " has left " + msg.room + ".";
    } else if (msg.user && msg.message) {
        messageText = msg.user + ": " + msg.message;
    }

    if (messageText && !allMessages.includes(messageText)) {
        allMessages.push(messageText);
        updateChat();
    }
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