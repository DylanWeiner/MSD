import './App.css';
import InputWidget from "./InputWidget";
import { useRef, useEffect } from "react";

function ChatPage({chatRoomName, allMessages, message }) {
    const messageRef = useRef('');
    const usernameRef = useRef('');
    const roomRef = useRef('');
    const canvasRef = useRef(null);

    useEffect(() => {
        const canvas = canvasRef.current;
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
            ctx.fillStyle = msg.color || "black";
            ctx.fillText(msg.text, 60, 60 + i * lineHeight);
        });
    }, [allMessages]);

    return (
        <div>
            <p>{chatRoomName}</p>
            <button
                id="leaveButton"
                onClick={(e) => {
                    message(e, usernameRef.current, roomRef.current,"leave");
                }}
            >
                Leave
            </button>
            <canvas id="canvas" ref={canvasRef} width={1550} height={900}></canvas>
            <textarea
                id="messageBox"
                onChange={(e) => (messageRef.current = e.target.value)}
                onKeyPress={(e) => {
                    if (e.key === "Enter") message(e, usernameRef.current, roomRef.current, messageRef.current);
                }}
            ></textarea>
            <button
                id="sendButton"
                onClick={(e) => {
                    message(e, usernameRef.current, roomRef.current, messageRef.current);
                }}
            >
                Send
            </button>

        </div>
    );
}

export default ChatPage;