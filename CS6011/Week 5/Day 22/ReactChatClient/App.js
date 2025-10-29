import InputWidget from './InputWidget.js';
import {useEffect, useRef, useState} from "react";
import ChatPage from "./ChatPage";
import {View} from "react-native";

function App() {
    const [home, setHome] = useState(1);
    const [value, setValue] = useState("black")
    let ws = useRef(null);

    let currentUser = useRef('')
    let currentRoom  = useRef('')

    function handleOpen() {
        console.log("websocket connection is on");
    }


    useEffect(()=> {
        ws.current = new WebSocket("ws://localhost:8080");
        ws.current.onopen = handleOpen;
        ws.current.onmessage = handleMsg;
        ws.current.binaryType = "arraybuffer";
    },[]);
    // ws.current.onmessage = (event) => {
    //     handleMsg(event);
    // }; Proposed Change

    let chatMessages = [];
    let joinMessages = [];
    let leaveMessages = [];
    const [allMessages, setAllMessages] = useState([]);
    const [userHasJoined, setUserHasJoined] = useState(false);

    let userColors = {}; // Store username → color mapping

    function showChatRoom() {
        setHome(0);
    }

    function showHomePage() {
        setHome(1);
    }

    function handleMsg(m) {
        let text;
        if(m.data instanceof Blob) {
            text =  m.data.text();
        } else if(m.data instanceof ArrayBuffer) {
            text = new TextDecoder("utf-8").decode(m.data);
        } else {
            text = m.data;
        }
        console.log("got this", text);
        if (!document.getElementById("#canvas")) {
            // createRoom();
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

        if (messageText ) {
            console.log("set messages for all messages box");
            setAllMessages(prevMessages => {
                const exists = prevMessages.some(m => m.text === messageText);
                if (exists) return prevMessages;
                return [...prevMessages, { text: messageText, color: msg.color || 'black' }];
            });
        }
    }

    // Handle key presses
    function handleLogin(ke, user, rm) {
        console.log("test 1 " + ke);
        if (ke.key === "e" || ke.target.id === "joinButton") {
            console.log(ke.key + "test 2");
            if (user === "") {
                alert("Please enter a username");
                user = "TestUsername";
                user.select();
                return;
            }

            if (ke.target.id === "userName") {
                // "room".focus();
                console.log("testing")
            } else if (ke.target.id === "room" || ke.target.id === "joinButton") {
                if (rm === "") {
                    alert("Please enter a Room Name");
                    rm = "RoomName";
                    rm.select();
                    return;
                }
                console.log(user + " has joined " + rm + ".");

                if (!userHasJoined) {
                    console.log("test 3");
                    setAllMessages(prev => [...prev, {text: user + " has joined " + rm + ".", color: "black"}]);
                    setUserHasJoined(true);

                    // ws.send("join " + user + " " + chatRoom + " " + selectedColor);

                    ws.current.send("join " + user + " " + rm + " " + 'black');
                    console.log("join " + user + " " + rm + " " + 'black');

                    currentRoom.current=rm;
                    currentUser.current=user;

                    showChatRoom();

                }
            }
        }
    }

    function handleMessageSend(ke, user, rm, message) {
        // Handle sending message on Enter key in textarea or button click
        if (
            (ke.type === "keypress" && ke.key === "Enter" && ke.target.id === "messageBox") ||
            (ke.type === "click" && ke.target.id === "sendButton")
        ) {
            ke.preventDefault();
            if (!message || message.trim() === "") return;

            let color = userColors[user] || "black";

            ws.current.send(`message ${currentUser.current} ${currentRoom.current} ${message} ${color}`);
            console.log("Message sent:", message);

            // Clear the message box after sending
            const messageBox = document.getElementById("messageBox");
            if(messageBox) {
                messageBox.value = "";
            }
        }
        // Handle leave button click
        else if (message==='leave') {
            ws.current.send(`leave ${currentUser.current} ${currentRoom.current}`);
            console.log(`${currentUser.current} has left ${currentRoom.current}`);
            showHomePage();
            setUserHasJoined(false);
        }
    }

    return (
        <View style={{flex: 1, backgroundColor: "#A9EED1"}}>
            {home === 1 ? (
                <InputWidget login={handleLogin}/>
            ) : (
                <ChatPage
                    chatRoomName={currentRoom.current}
                    allMessages={allMessages}
                    message={handleMessageSend}
                />
            )}
        </View>
    );
}

export default App;