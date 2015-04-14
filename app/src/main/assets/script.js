var canvas;
var ctx;
var xColor = "blue";
var yColor = "green";
var zColor = "red";
var prevX;
var prevY;
var prevZ;
var lapse;
var arrayX = [];
var arrayY = [];
var arrayZ = [];


function init(){
    canvas = document.getElementById('plotCanvas');
    ctx = canvas.getContext("2d");
    getData();
    prevX = 160;
    prevY = 160;
    prevZ = 160;
    lapse = 0;
}

function getData() {
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            //alert("Connected!");
            plotData(Duktape.dec('jx',xmlhttp.responseText));
            xmlhttp.send();
        } else {
            //alert("Not connected!");
        }
    };
    xmlhttp.open("GET", "http://127.0.0.1:8080/feeds/1", true);
    xmlhttp.send();
}

function plotData(data) {
    console.log(data);

    arrayX.push(x);
    arrayY.push(y);
    arrayZ.push(z);
    if (lapse==320) {
        console.log(arrayX);
        console.log(arrayY);
        console.log(arrayZ);
    }
    var event = JSON.parse(data);
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;
    var newLapse = lapse + 10;


    ctx.beginPath();
    ctx.moveTo(lapse,prevX);
    ctx.lineTo(newLapse,x);
    ctx.strokeStyle=xColor;
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(lapse,prevY);
    ctx.lineTo(newLapse,y);
    ctx.strokeStyle=yColor;
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(lapse,prevZ);
    ctx.lineTo(newLapse, z);
    ctx.strokeStyle=zColor;
    ctx.stroke();

    prevX = x;
    prevY = y;
    prevZ = z;
    lapse = newLapse;
}

init();