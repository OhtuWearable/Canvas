/**
* Implementation of HTML5 canvas (only fillRect() function for now)
*/

function Canvas() {
    this.fillStyle = "#000000";
    this.strokeStyle = "#000000";
}

Canvas.prototype.getContext = function(ctx){
    return this;
};

Canvas.prototype.fillRect  = function (x, y, width, height){
    jni_fill_rect(this.fillStyle, x.toString(), y.toString(), width.toString(), height.toString());
    return "rectangle drawn";
};

Canvas.prototype.beginPath = function (){
    jni_begin_path();
    return "path started";
}

Canvas.prototype.moveTo = function (x, y){
    //implement this
    return "moved";
}

Canvas.prototype.stroke = function (){
    jni_stroke(this.strokeStyle);
    return "path drawn";
}

Canvas.prototype.lineTo = function (x,y){
    jni_line_to(x.toString(), y.toString());
    return "line added";
};

function Document(){
};

Document.prototype.getElementById = function(id){
    //ToDo: replace hardcoded values with real screen width/height from device?
    return new Canvas();
};

var document = new Document();