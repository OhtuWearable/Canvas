/**
* draws pink rectangle that fills canvas
*/

var canvas = document.getElementById('myCanvas');
var cwidth = canvas.width;
var cheight = canvas.height;
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#ff69b4";
ctx.fillRect(0,0, cwidth, cheight);
