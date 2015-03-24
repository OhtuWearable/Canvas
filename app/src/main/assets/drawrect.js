/**
* draws pink rectangle that fills canvas
*/

var canvas = document.getElementById('myCanvas');
var cwidth = 150;
var cheight = 150;
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#ff69b4";
ctx.fillRect(20,20, cwidth, cheight);
