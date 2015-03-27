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
    jni_fill_rect(this.fillStyle, x.toString(), y.toString(), width.toString(), height.toString());
    return "rectangle drawn";
};

Canvas.prototype.beginPath = function (){
    //implement this
}

Canvas.prototype.moveTo = function (x, y){
    //implement this
}

Canvas.prototype.stroke = function (){
    //implement this
}

Canvas.prototype.lineTo = function (x,y){
    jni_line_to(this.fillStyle, x.toString(), y.toString());
    return "line drawn";
};

function Document(){
};

Document.prototype.getElementById = function(id){
    //ToDo: replace hardcoded values with real screen width/height from device?
    return new Canvas(320, 320);
};

var document = new Document();