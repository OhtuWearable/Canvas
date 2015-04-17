
var canvas;
var ctx;

canvas = document.getElementById('plotCanvas');
ctx = canvas.getContext("2d");

var xhr=new XMLHttpRequest();

var x=1;
var y=1;

xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
        ctx.beginPath();
        ctx.moveTo(0,0);
        ctx.lineTo(x,y);
        ctx.strokeStyle="blue";
        ctx.stroke();
        x++;
        y++;
        if(x<200){
            xhr.open("GET", "http://127.0.0.1:8080/feeds", true);
            xhr.send();
        }
    }
};
xhr.open("GET", "http://127.0.0.1:8080/feeds", true);
xhr.send();
