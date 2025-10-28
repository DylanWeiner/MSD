import logo from './logo.svg';
import './App.css';
import App from './App.js'
import {useRef, useState} from "react";

function InputArea({setValue, addFn}) {

    const InputRef = useRef('');

  return (
    <p>
    <textarea
        id ="task"
        type="text"
        placeholder="Enter an item"
        onChange={(e)=>setValue(e.target.value)}
        onKeyPress={(e)=>{if(e.key==='Enter')addFn(e)}}
    />
    <button id="btn" onClick={(e)=>{ addFn(e);}}>Add</button>
    </p>
   );
}

export default InputArea;
