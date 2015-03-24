/**
* draws pink rectangle that fills canvas
*/

var canvas = document.getElementById('myCanvas');
var cwidth = 10;
var cheight = 10;
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#ffffff";
ctx.fillRect(150, 150, cwidth, cheight);
