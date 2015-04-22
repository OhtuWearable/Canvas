var canvas;
var ctx;
var xColor = "blue";
var yColor = "green";
var zColor = "red";


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
    var prevX = 400;
    var prevY = 400;
    var prevZ = 400;
    
    for (var i = 0; i < data.length; i++){
        console.log(data[i].x);
        console.log(data[i].y);
        console.log(data[i].z);
    }
}