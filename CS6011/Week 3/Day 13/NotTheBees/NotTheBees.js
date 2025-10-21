let beeimg = new Image(); // Alternatively document.createElement('img');
beeimg.src = "bee-left1.png"; // This should be the file name of the bee image.

let bee = {x: 200, y: 100, speed: Math.random()*5+5};
bee.img = beeimg;

// let bee2 = {x: 220, y: 110, speed: Math.random()*5+5};
// bee2.img = beeimg;

// let bee3 = {x: 180, y: 85, speed: Math.random()*5+5};
// bee3.img = beeimg;

let myCanvas = document.querySelector("canvas");
myCanvas.style.border = "2px solid green";
let ctx = myCanvas.getContext('2d');

let mouseX = bee.x;
let mouseY = bee.y;

function recordMousePos(event) {
  const rect = myCanvas.getBoundingClientRect(); // Finds the canvas's size and correctly adjusts coordinates
  mouseX = event.clientX - rect.left; // X axis
  mouseY = event.clientY - rect.top; // Y axis
}


// Causes the bees to follow the mouse
function updateBees() {
    let dx = mouseX - bee.x;
	if(Math.abs(dx) < bee.speed){
		bee.x += dx;
	}
	else if(bee.x < mouseX) {
		bee.x += bee.speed;
	}
	else {
		bee.x -= bee.speed;
	}

    let dy = mouseY - bee.y;
	if(Math.abs(dy) < bee.speed){
		bee.y += dy;
	}
	else if(bee.y < mouseY) {
		bee.y += bee.speed;
	}
	else {
		bee.y -= bee.speed;
	}
}

// Render the bee and request next frame
function draw() {
  ctx.clearRect(0, 0, myCanvas.width, myCanvas.height);
  ctx.drawImage(beeimg, bee.x, bee.y, 50, 50);
  updateBees();
  requestAnimationFrame(draw);
}

beeimg.onload = () => {
  draw();
};

myCanvas.addEventListener("mousemove", recordMousePos);

