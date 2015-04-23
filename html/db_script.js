var canvas;
var ctx;
var xColor = "blue";
var yColor = "green";
var zColor = "red";
var lapse = 0;
    var prevX = 400;
    var prevY = 400;
    var prevZ = 400;

function init(){

    canvas = document.getElementById('plotCanvas');
    ctx = canvas.getContext("2d");

    var xhr=new XMLHttpRequest();
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var data=JSON.parse(xhr.responseText);
            plotData(data);
            //console.log(data);
        }
    };
    
    xhr.open("GET", "http://127.0.0.1:8080/feeds/1/", true);
    xhr.send();

}

function plotData(data){
    console.log(data.length);
    for (var i = 0; i < data.length; i++){
        if (i > 80){
            break;
        }
        //console.log(data[i].x);
        //console.log(data[i].y);
        //console.log(data[i].z);
        plotData2(data[i]);
    }
}

function plotData2(data){
    var event = data;
    var x = (event.x * 8) + 160;
    var y = (event.y * 8) + 160;
    var z = (event.z * 8) + 160;
    var newLapse = lapse + 10;

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