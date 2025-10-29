import './App.css';
import {useRef} from "react";

function InputWidget({login}) {

    const usernameRef = useRef('');
    const roomRef = useRef('');

    return (
        <div>
            <p> Chatroom  </p>
            <p>
                <label>Username</label>
                <input
                    id="userName"
                    onChange={(e)=>usernameRef.current=(e.target.value)}
                    onKeyPress={(e)=>{if(e.key==='Enter') document.getElementById("room").focus()}}

                ></input>
            </p>
            <br></br>
            <p>
                <label>Room</label>
                <input
                    id="room"
                    onChange={(e)=>roomRef.current=(e.target.value)}
                    onKeyPress={(e)=>{if(e.key==='Enter')login(e, usernameRef.current, roomRef.current)}}
                ></input>
            </p>
            <button id="joinButton" onClick={(e)=>{ login(e, usernameRef.current, roomRef.current);}}>Enter</button>
        </div>
    );
}

export default InputWidget;
