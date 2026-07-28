import logo from './logo.svg';
import './App.css';
import InputArea from './InputArea.js';
import ToDoList from './ToDoList.js';
import {useRef, useState} from "react";

function App() {
    const [tasks, setTasks] = useState([]);
    const [inputValue, setInputValue] = useState('');

    console.log("calling from app" + inputValue);

    function addInput(event) {
                       if(tasks.length===0)
                        setTasks([inputValue]);
                        else
                         setTasks([ ...tasks,inputValue]);
                }

    function deleteItem(index) {
        setTasks(tasks => [
            ...tasks.slice(0, index),
            ...tasks.slice(index + 1)
          ]);
    }


  return (
    <div className="App">
      <header className="App-header">
        <h1>
          To-Do List
        </h1>
            <InputArea setValue = {setInputValue} addFn={addInput}></InputArea>

       <ToDoList list = {tasks} deletedItem = {deleteItem}></ToDoList>
     </header>
   </div>
   );
}

export default App;
