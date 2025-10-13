function main() {
    console.log("running main");
	console.log("body: ", document.body)

    document.body.style.background = "lightgreen";
	

	let allLines = document.getElementsByTagName('li');
	let li1 = allLines[0];
    let li2 = allLines[1];
    let li3 = allLines[2];
    let li4 = allLines[3];
    let li5 = allLines[4];
    let li6 = allLines[5];
    let li7 = allLines[6];
    let li8 = allLines[7];

    let allHeaders = document.getElementsByTagName('p');
    let p = allHeaders[0];

    console.log("p: ", p);

    p.textContent = "Dylan's Web Page";
    p.style.color = "blue";
    p.style.textAlign = "center";
    p.style.fontSize = 24;
	
	console.log("li: ", li1);
    console.log("li: ", li2);
    console.log("li: ", li3);
    console.log("li: ", li4);
    console.log("li: ", li5);
    console.log("li: ", li6);
    console.log("li: ", li7);
    console.log("li: ", li8);
	
	li1.textContent = "My name is Dylan";
	li1.style.color = "blue";
    li1.style.textAlign = "center";
    li1.style.fontSize = 20;

    li2.textContent = "I am 25 years old";
	li2.style.color = "blue";
    li2.style.textAlign = "center";
    li2.style.fontSize = 20;

    li3.textContent = "I am originally from Las Vegas";
	li3.style.color = "blue";
    li3.style.textAlign = "center";
    li3.style.fontSize = 20;

    li4.textContent = "I enjoy exercising in my free time.";
	li4.style.color = "blue";
    li4.style.textAlign = "center";
    li4.style.fontSize = 20;
    
    li5.textContent = "I play video games, I prefer watching to playing horror but I'm trying to play more horror games to celebrate the upcoming holiday.";
	li5.style.color = "blue";
    li5.style.textAlign = "center";
    li5.style.fontSize = 20;
    
    li6.textContent = "I have a cat, though sometimes I suspect he's more of a dog.";
	li6.style.color = "blue";
    li6.style.textAlign = "center";
    li6.style.fontSize = 20;

    li7.textContent = "My favorite games of all time are Undertale and Outer Wilds.";
	li7.style.color = "blue";
    li7.style.textAlign = "center";
    li7.style.fontSize = 20;
    
    li8.textContent = "I like all kinds of music, and love any recommendations.";
	li8.style.color = "blue";
    li8.style.textAlign = "center";
    li8.style.fontSize = 20;
};

window.onload = main;