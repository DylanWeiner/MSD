function main() {
    console.log("running main");
	console.log("body: ", document.body)

    document.body.style.background = "lightgreen";

    // let list = document.getElementById('listed');

    for(let i = 0; i < 7; i++) {
        let line = document.createElement(`li`);
        line.textContent = "Element " + i;
        document.body.appendChild(line);
    }
}

window.onload = main;