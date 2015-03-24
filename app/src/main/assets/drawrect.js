/**
* draws rectangle to canvas
*/

var canvas = document.getElementById('myCanvas');
var cwidth = 100;
var cheight = 20;
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#ffffff";
ctx.fillRect(150, 150, cwidth, cheight);

canvas = document.getElementById('myCanvas');
var ctx = canvas.getContext("2d");
ctx.fillStyle = "rgb(100,2,3)";
ctx.fillRect(110, 110, 30, 100);
