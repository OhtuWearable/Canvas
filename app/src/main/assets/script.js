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
var r;

canvas = document.getElementById('plotCanvas');
ctx = canvas.getContext("2d");
prevX = 160;
prevY = 160;
prevZ = 160;
lapse = 0;

var xhr=new XMLHttpRequest();

var x=1;

xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
        var data=Duktape.dec('jx', xhr.responseText);
        if(data.x!=null && data.y!=null && data.z!=null){
         if (arrayX.length == 32) {
            lapse = 0;
            plotData2(data);
         } else {
            plotData(data);
         }
         }

            xhr.open("GET", "http://127.0.0.1:8080/feeds/1", true);
            xhr.send();

        x++;
    }
};
xhr.open("GET", "http://127.0.0.1:8080/feeds/1", true);
xhr.send();


function plotData(data) {
    /*if (lapse == 32) {
        console.log(arrayX);
        console.log(arrayY);
        console.log(arrayZ);
    }*/

    var event = data;
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;
    var newLapse = lapse + 10;

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

    var event = data;
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;

    arrayX.push(x);
    arrayY.push(y);
    arrayZ.push(z);

    canvas = document.getElementById('plotCanvas');
    ctx = canvas.getContext("2d");
    ctx.clearRect(0,0,320,320);
    for (var i = 0; i < 32; i++) {
        plotData3(arrayX[i], arrayY[i], arrayZ[i]);
    }
}

function plotData3(x, y, z) {
    var newLapse = lapse + 10;
    r = 50;
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