/**
* Implementation of HTML5 canvas (only fillRect() function for now)
*/

function Canvas(width, height) {
    this.width = width;
    this.height = height;
    this.fillStyle = "#FFFFFF";
}

Canvas.prototype.getContext = function(ctx){
    return this;
};

Canvas.prototype.fillRect  = function (x, y, width, height){
    //ToDo: implement call to JNI->JAVA CanvasElement class fillRect() method
    print('fillrect');
};

function Document(){
};

Document.prototype.getElementById = function(id){
    //ToDo: replace hardcoded values with real screen width/height from device
    return new Canvas(100, 100);
};

var document = new Document();