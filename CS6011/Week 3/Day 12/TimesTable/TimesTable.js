let myDiv = document.querySelector('#myDiv');
myDiv.style.border = '1px solid blue';
myDiv.style.padding = '20px';
let lastClicked = null;
let buttonClicked = false;

const table = document.createElement("table");
const tableBody = document.createElement("tbody");
const button = document.createElement("button");

for(let row = 1; row <= 10; row++) {
    const tr = document.createElement("tr");
    for(let col = 1; col <= 10; col++) {
        const td = document.createElement("td");
        const vals = document.createTextNode((row * col), (row * col));
        td.appendChild(vals);
        td.style.textAlign = "center";
        td.style.border = "2px solid blue";
        td.style.padding = "10px";

        td.addEventListener("mouseover", () => td.classList.add("highlight"));
        td.addEventListener("mouseleave", () => td.classList.remove("highlight"));

        td.addEventListener("click", () => {
            if (lastClicked) {
                lastClicked.classList.remove("clicked");
            }    
            td.classList.add("clicked");
            lastClicked = td;
        });

        tr.appendChild(td);
    }
    tableBody.append(tr);
}

table.append(tableBody);

table.style.border = "2px solid blue";
table.style.padding = "10px";

button.style.background = "red";
button.style.border = "8px solid darkred";
button.style.padding = "10px";

table.style.marginLeft = "auto";
table.style.marginRight = "auto";
myBody.style.backgroundColor = "white";

button.style.display = "block";
button.append("Chaos (Epilepsy Warning)");
button.style.marginLeft = "auto";
button.style.marginRight = "auto";
button.style.width = "20%";

function getRandomColor() {
  let r = Math.floor(Math.random() * 256);
  let g = Math.floor(Math.random() * 256);
  let b = Math.floor(Math.random() * 256);
  return `rgb(${r},${g},${b})`;
}

function colorChange() {
    myBody.style.backgroundColor = getRandomColor();
}


 button.addEventListener("click", () => {
            if (!buttonClicked) {
                buttonClicked = true;
                intervalId = setInterval(colorChange, 100);
            } 
            else {
                clearInterval(intervalId);
                myBody.style.backgroundColor = "white";
                buttonClicked = true;
            }
        });

myDiv.appendChild(table);
myDiv.appendChild(button);

