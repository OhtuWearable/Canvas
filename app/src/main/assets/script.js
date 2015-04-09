/**
* draws some rectangles and lines to canvas
*/

var canvas = document.getElementById('myCanvas');
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#ff69b4";
ctx.fillRect(150, 150, 100, 20);

ctx.fillStyle = "rgb(100,2,3)";
ctx.fillRect(110, 110, 30, 100);

ctx.beginPath();
ctx.moveTo(320,80);
ctx.lineTo(130,140);
ctx.strokeStyle="blue";
ctx.lineTo(250,0);
ctx.stroke();
