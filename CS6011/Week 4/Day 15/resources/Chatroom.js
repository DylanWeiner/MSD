let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleOpen
ws.onmsg = handleMsg;

function handleJoinBtn() {
    let ke = {};
    ke.key = "Enter"
    ke.target = btn;
    handleKeyPress(ke);
}

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

            // fetch("http://localhost:8000/calculate?x=" + x + "&y=" + y)
            //     .then(
            //         response => {
            //             if(!response.ok) {
            //             throw new Error("server had an issue");
            //         }
            //             return response.text();
            //         }
            //     )
            //     .then(
            //         text => {
            //             resultInput.value = text;
            //         }
            //     )
            //     .catch(
            //         error => {console.log("error: ", error); }
            //     );
                
                }
        }
    }

userName.addEventListener('keypress',handleKeyPress);
room.addEventListener('keypress',handleKeyPress);

function hideInputs() {
    style.display = none;
}

function handleOpen() {
    // ws.send("join Dylan LivingRoom");
}

function handleMsg(m) {
    let msg = JSON.parse(m.data);
    console.log(msg.room);
}

let btn = document.createElement("button");
btn.textContent = "Enter";
btn.onclick = handleJoinBtn;
document.body.appendChild(btn);