import logo from './logo.svg';
import './App.css';
import InputArea from './InputArea.js';
import {useRef, useState} from "react";

function ToDoList({list, deletedItem}) {



  return (
  <p>
    <ul style={{ listStyleType: 'none', padding: 0, margin: 0 }}>
             {list.map((name, index) =>  <li key={index} className={list.completed ? 'completed': ''}>
             <input type="checkbox" onClick={() => deletedItem(index)}/>
                             <span>{name}</span>
                             </li>)}
    </ul>
  </p>
   );
}

export default ToDoList;
