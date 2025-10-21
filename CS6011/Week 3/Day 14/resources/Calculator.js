let xInput = document.querySelector('#x_value');
let yInput = document.querySelector('#y_value');
let resultInput = document.querySelector('#result_value');
let wsResultInput = document.querySelector('#ws_result_value');

function handleCalcBtn() {
    let ke = {};
    ke.key = "Enter"
    ke.target = btn;
    handleKeyPress(ke);
}

function handleKeyPress( ke ) {
    if(ke.key === "Enter" || ke.target === btn) {
        let x = Number(xInput.value);
        let y = Number(yInput.value);
        if(xInput.value === "" || isNaN(x)) {
            alert("Please enter a number");
            xInput.value = "<Enter a Number>";
            xInput.select();
            return;
        }

        if(ke.target === xInput) {
            yInput.focus();
        }
        
        else if(ke.target === yInput || ke.target === btn) {
            if(yInput.value === "" || isNaN(y)) {
                alert("Please enter a number");
                yInput.value = "<Enter a Number>";
                yInput.select();
                return;
            }

            ws.send("" + x + " " + y);

            fetch("http://localhost:8000/calculate?x=" + x + "&y=" + y)
                .then(
                    response => {
                        if(!response.ok) {
                        throw new Error("server had an issue");
                    }
                        return response.text();
                    }
                )
                .then(
                    text => {
                        resultInput.value = text;
                    }
                )
                .catch(
                    error => {console.log("error: ", error); }
                );
                
                }
        }
    }

xInput.addEventListener('keypress',handleKeyPress);
yInput.addEventListener('keypress',handleKeyPress);

let btn = document.createElement("button");
btn.textContent = "Calculate";
btn.onclick = handleCalcBtn;
document.body.appendChild(btn);

function handleWsOpen() {
    console.log('ws connected');

}

function handleWsMessage(msg) {
    console.log("server sent us a message via the WS: ", msg.data);
    wsResultInput.value = msg.data;
}

let ws = new WebSocket("ws://localhost:8000");
ws.onopen = handleWsOpen;
ws.onmessage = handleWsMessage;