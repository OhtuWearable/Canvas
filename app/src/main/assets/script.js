
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

function init() {
    consoleLog("inited");
    canvas = document.getElementById('plotCanvas');
    ctx = canvas.getContext("2d");
    //setInterval(getData, 1);
    prevX = 160;
    prevY = 160;
    prevZ = 160;
    lapse = 0;

    //if we make ~ > 130 calls to getData, program crashes to java.util.concurrent.RejectedExecutionException
    for (var i = 0; i < 130; i++){
        getData();
    }
}

function getData() {
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            //alert("Connected!");
            if (arrayX.length == 320) {
                lapse = 0;
                plotData2(xmlhttp.responseText);
            } else {
                plotData(xmlhttp.responseText);
            }
        } else {
            //alert("Not connected!");
        }
    };
    //xmlhttp.open("GET", "http://128.214.138.76:8080/feeds/1", true);
    xmlhttp.open("GET", "http://localhost:8080/feeds/1", true);
    xmlhttp.send();
}

function plotData(data) {
    //console.log(data);
    /*if (lapse == 320) {
        console.log(arrayX);
        console.log(arrayY);
        console.log(arrayZ);
    }*/

    var event = JSON.parse(data);
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;
    var newLapse = lapse + 1;

    arrayX.push(x);
    arrayY.push(y);
    arrayZ.push(z);

    ctx.beginPath();
    ctx.moveTo(lapse, prevX);
    ctx.lineTo(newLapse, x);
    ctx.strokeStyle = xColor;
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(lapse, prevY);
    ctx.lineTo(newLapse, y);
    ctx.strokeStyle = yColor;
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(lapse, prevZ);
    ctx.lineTo(newLapse, z);
    ctx.strokeStyle = zColor;
    ctx.stroke();

    prevX = x;
    prevY = y;
    prevZ = z;
    lapse = newLapse;
}

function plotData2(data) {
    arrayX.splice(0, 1);
    arrayY.splice(0, 1);
    arrayZ.splice(0, 1);

    var event = JSON.parse(data);
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;

    arrayX.push(x);
    arrayY.push(y);
    arrayZ.push(z);

    canvas = document.getElementById('plotCanvas');
    ctx = canvas.getContext("2d");
    ctx.clearRect(0,0,320,320);
    for (var i = 0; i < 320; i++) {
        plotData3(arrayX[i], arrayY[i], arrayZ[i]);
    }
}
/*
function plotData3(x, y, z) {
    var newLapse = lapse + 1;
    
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
} */

function plotData3(x, y, z) {
    var newLapse = lapse + 1;
    //r = 50;
    ctx.beginPath();
    ctx.moveTo(lapse,prevX);
    ctx.lineTo(newLapse,x);
    //ctx.arcTo(lapse, prevX, newLapse,x,r);

    ctx.strokeStyle=xColor;
    ctx.stroke();
    
    ctx.beginPath();
    ctx.moveTo(lapse,prevY);
    ctx.lineTo(newLapse,y);
    //ctx.arcTo(lapse, prevY, newLapse,y, r);

    ctx.strokeStyle=yColor;
    ctx.stroke();
    
    ctx.beginPath();
    ctx.moveTo(lapse,prevZ);
    ctx.lineTo(newLapse, z);
    //ctx.arcTo(lapse, prevZ, newLapse, z, r);

    ctx.strokeStyle=zColor;
    ctx.stroke();
    
    prevX = x;
    prevY = y;
    prevZ = z;
    lapse = newLapse;
}

init();