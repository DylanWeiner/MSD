let beeimg = new Image(); // Alternatively document.createElement('img');
beeimg.src = "bee.png"; // This should be the file name of the bee image.

let bee1 = {x: 15, y: 25, speed: Math.random()*5+5};
let bee2 = {x: 250, y: 600, speed: Math.random()*5+5};
let bee3 = {x: 150, y: 425, speed: Math.random()*5+5};
let bee4 = {x: 630, y: 120, speed: Math.random()*5+5};
let bee5 = {x: 510, y: 20, speed: Math.random()*5+5};

let bees = [bee1, bee2, bee3, bee4, bee5]; // 1024 x 1024

let myCanvas = document.querySelector("canvas");
myCanvas.style.border = "2px solid green";
let ctx = myCanvas.getContext('2d');

let mouseX = 0;
let mouseY = 0;
let score = 0;
let collision = false;
let header = document.getElementById('title');

function recordMousePos(event) {
  const rect = myCanvas.getBoundingClientRect(); // Finds the canvas's size and correctly adjusts coordinates
  mouseX = event.clientX - rect.left; // X axis
  mouseY = event.clientY - rect.top; // Y axis
}


// Causes the bees to follow the mouse
function updateBees() {
   for(let i =0; i < bees.length; i++){
        let dx = mouseX - bees[i].x;
        if(Math.abs(dx) < bees[i].speed){
            bees[i].x += dx;
        }
        else if(bees[i].x < mouseX) {
            bees[i].x += bees[i].speed;
        }
        else {
            bees[i].x -= bees[i].speed;
        }

        let dy = mouseY - bees[i].y;
        if(Math.abs(dy) < bees[i].speed){
            bees[i].y += dy;
        }
        else if(bees[i].y < mouseY) {
            bees[i].y += bees[i].speed;
        }
        else {
            bees[i].y -= bees[i].speed;
        }
    }
}

function draw() {
  ctx.clearRect(0, 0, myCanvas.width, myCanvas.height);
  for(let bee of bees){
      bee.img = beeimg;
    ctx.drawImage(bee.img, bee.x, bee.y, 50, 50);
  }
  if(isAnimating){
    updateBees();
    header.textContent = 'Protect the Honey! Your Score is: '
    header.append(score);
    collisionDetected();
    animationId = requestAnimationFrame(draw);

    if(!collision) {
      setTimeout(() => {
        score += 10;
      }, 100);
     }

    if(collision) {
      alert("Game Over: You lose!\n Your Score was " + score);
      score = 0;
    }
  }
}

function beeStop() {
  isAnimating = false;
  if(animationId){
    cancelAnimationFrame(animationId);
  }
}

function beeStart() {
  if(!isAnimating){
    isAnimating = true;
    draw();
  }

  // Hide the default cursor on the whole page
  document.body.style.cursor = 'none';

  // Create an image element dynamically
  const customCursor = document.createElement('img');
  customCursor.src = 'honey.png'; // 2048 x 2048
  customCursor.style.position = 'absolute';
  customCursor.style.width = '40px';   // customize size
  customCursor.style.height = '40px';
  customCursor.style.pointerEvents = 'none'; // so it doesn't block mouse events
  customCursor.style.zIndex = '9999';
  customCursor.style.userSelect = 'none';

  // Add the image to the document body
  document.body.appendChild(customCursor);

  // Update image position to follow mouse
  document.addEventListener('mousemove', (event) => {
    // Position custom cursor centered on mouse pointer
    customCursor.style.left = (event.clientX - customCursor.width / 2) + 'px';
    customCursor.style.top = (event.clientY - customCursor.height / 2) + 'px';
  });
}

function collisionDetected() {
    for (let bee of bees) {
    if (bee.x >= mouseX - 10 &&
        bee.y >= mouseY - 10 &&
        bee.x <= mouseX &&
        bee.y <= mouseY) {
        collision = true;
        return;
    }
}
  if(bees[1].x >= mouseX-10 &&
      bees[1].y >= mouseY-10 &&
      bees[1].x <= mouseX &&
      bees[1].y <= mouseY ) {
      collision = true;
  }
  if(bees[2].x >= mouseX-10 &&
      bees[2].y >= mouseY-10 &&
      bees[2].x <= mouseX &&
      bees[2].y <= mouseY ) {
      collision = true;
  }
  if(bees[3].x >= mouseX-10 &&
      bees[3].y >= mouseY-10 &&
      bees[3].x <= mouseX &&
      bees[3].y <= mouseY ) {
      collision = true;
  }
  if(bees[4].x >= mouseX-10 &&
      bees[4].y >= mouseY-10 &&
      bees[4].x <= mouseX &&
      bees[4].y <= mouseY ) {
      collision = true;
  }
}

beeimg.onload = () => {
  isAnimating = false;
  draw();
};

myCanvas.addEventListener('mousemove', recordMousePos);
myCanvas.addEventListener('mouseleave', beeStop);
myCanvas.addEventListener('mouseenter', beeStart);