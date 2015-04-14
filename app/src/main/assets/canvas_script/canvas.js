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

Canvas.prototype.clearRect  = function (x, y, width, height){
    jni_clear_rect(x.toString(), y.toString(), width.toString(), height.toString());
    return "rectangle cleared";
};

Canvas.prototype.beginPath = function (){
    jni_begin_path();
    return "path started";
}

Canvas.prototype.moveTo = function (x, y){
    var xInt = parseInt(x);
    var yInt = parseInt(y);
    jni_move_to(xInt.toString(), yInt.toString());
    return "moved";
}

Canvas.prototype.stroke = function (){
    jni_stroke(this.strokeStyle);
    return "path drawn";
}

Canvas.prototype.lineTo = function (x,y){
    var xInt = parseInt(x);
    var yInt = parseInt(y);
    jni_line_to(xInt.toString(), yInt.toString());
    return "line added";
};

function Document(){
};

Document.prototype.getElementById = function(id){
    //ToDo: replace hardcoded values with real screen width/height from device?
    return new Canvas();
};

var document = new Document();


var reqCounter=0;
var xmlHttpRequests=new Object();

function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}

function isMethod(method){
  if(contains(["SLEEP","CONNECT", "DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE", "TRACK"], method) || contains(["sleep", "connect", "delete", "get", "head", "options", "post", "put", "trace", "track"], method)){
    return true;
  }
  return false;
}

function isUrl(s) {
   var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
   return regexp.test(s);
}





function isContentType(h, v){
    if(contains(["Accept-Charset","Accept-Features","Accept-Encoding","Accept-Language","Accept-Ranges","Access-Control-Allow-Credentials","Access-Control-Allow-Origin","Access-Control-Allow-Methods","Access-Control-Allow-Headers","Access-Control-Max-Age","Access-Control-Expose-Headers","Access-Control-Request-Method","Access-Control-Request-Headers","Age","Allow","Alternates","Authorization","Cache-Control","Connection","Content-Encoding","Content-Language","Content-Length","Content-Location","Content-MD5","Content-Range","Content-Security-Policy","Content-Type","Cookie","DNT","Date","ETag","Expect","Expires","From","Host","If-Match","If-Modified-Since","If-None-Match","If-Range","If-Unmodified-Since","Last-Event-ID","Last-Modified","Link","Location","Max-Forwards","Negotiate","Origin","Pragma","Proxy-Authenticate","Proxy-Authorization","Range","Referer","Retry-After","Sec-Websocket-Extensions","Sec-Websocket-Key","Sec-Websocket-Origin","Sec-Websocket-Protocol","Sec-Websocket-Version","Server","Set-Cookie","Set-Cookie2","Strict-Transport-Security","TCN","TE","Trailer","Transfer-Encoding","Upgrade","User-Agent","Variant-Vary","Vary","Via","Warning","WWW-Authenticate","X-Content-Duration","X-Content-Security-Policy","X-DNSPrefetch-Control","X-Frame-Options","X-Requested-With"], h)){
        return true;
    }
    return false;
}

/* Hooks for using XMLHttpRequest from JSOO */
var location = {};
location["host"] = "127.0.0.1:8080";
location["hostname"] = "127.0.0.1";
location["protocol"] = "http:";
location["href"] = "http://127.0.0.1/applications/";
location["origin"] = "http://127.0.0.1:8080";
location["pathname"] = "/applications/";
location["port"] = "8080"; //return empty if 80 or 443
location["hash"] = "";
location["search"] = "";

//Math.imul emulation from Mozilla dev
//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/imul
Math.imul = Math.imul || function(a, b) {
  var ah  = (a >>> 16) & 0xffff;
  var al = a & 0xffff;
  var bh  = (b >>> 16) & 0xffff;
  var bl = b & 0xffff;
  // the shift by 0 fixes the sign on the high part
  // the final |0 converts the unsigned value into a signed value
  return ((al * bl) + (((ah * bl + al * bh) << 16) >>> 0)|0);
};

var XMLHttpRequest = function() {

  	var self = this;

	var reqid="req"+reqCounter;
	xmlHttpRequests[reqid]=self;
	reqCounter++;

	// Constants
	this.UNSENT = 0;
  	this.OPENED = 1;
  	this.HEADERS_RECEIVED = 2;
  	this.LOADING = 3;
  	this.DONE = 4;

  	//Set the ready state variable
  	var readyState = this.UNSENT; //readonly

  	// default ready state change handler in case one is not set or is set late
  	this.onreadystatechange = null;

  	//Request functions and attributes
  	var request;

  	// check http://www.w3.org/TR/2014/WD-XMLHttpRequest-20140130/
  	this.open = function(method, url, async, username, password) {
	  if(isMethod(method) && isUrl(url)){
	    this.abort(); //First abort any current task
	    this.settings = {
		    "method": method,
		    "url": url.toString(),
		    "async": (typeof async !== "boolean" ? true : async),
		    "username": username || null,
		    "password": password || null
	    };
	    if(this.readyState!=this.OPENED){
	        this.readyState=this.OPENED;
	        if(this.onreadystatechange!=null){
	            this.onreadystatechange();
	        }
	    }
	  }else{
        throw new Error('TypeError');
	  }
    };

    this.requestHeaders="";

    this.setRequestHeader = function(header, value) {
        if(this.readyState==this.OPENED){
            if(isContentType(header, value)){
                this.requestHeaders+=header+":"+value+"#";
            }else{
                throw new Error('TypeError');
            }
        }else{
            throw new Error('InvalidStateError');
        }
    };

    this.sendFlag=false;

    this.send = function(data) {
        if(this.readyState==this.OPENED || this.sendFlag==false){
            var hasData = typeof data !== 'undefined';
            //Now I will try to use some Java method to call the thing
            this.sendFlag=true;
            var resp = native_xmlhttprequest(reqid,
                this.settings.method,
                this.settings.url,
                data,
                this.requestHeaders,
                this.settings.username,
                this.settings.password,
                this.settings.async);

		}else{
		    throw new Error('InvalidStateError');
		}
    };
    this.abort = function() {
        if((this.readyState==this.OPENED && this.sendFlag==false) || this.readyState==this.UNSENT || this.readyState==this.DONE){
            this.readyState==this.UNSENT;
        }else{
            this.readyState=this.DONE;
            this.sendFlag=false;
            native_abort(reqid);
        }
    };

    //Response functions and attributes
    var response;
  	this.responseText = "";
  	this.responseXML = "";
  	this.status = null;
  	this.statusText = null;

  	this.responseHeaders=null;

  	this.getResponseHeader = function(header) {
  	    if(this.readyState==this.OPENED || this.readyState==this.UNSENT){
  	        return null;
  	    }else{
  	        var value="";
  	        if(this.responseHeaders!=null){
                for(var i=0; i<this.responseHeaders.length; i++){
                    if(this.responseHeaders[i].header===header){
                            if(value.length==0){
                                 value=this.responseHeaders[i].value;
                            }else{
                                 value+=", "+this.responseHeaders[i].value;
                            }
                     }
                }
  	        }
  	        return value;
  	    }
  	}
  	this.getAllResponseHeaders = function() {
  	    if(this.readyState==this.OPENED || this.readyState==this.UNSENT){
      	        return "";
      	}else{
            var allHeaders="";
            if(this.responseHeaders!=null){
                            for(var i=0; i<this.responseHeaders.length; i++){
                                        if(allHeaders.length==0){
                                             allHeaders=this.responseHeaders[i].header+": "+this.responseHeaders[i].value;
                                        }else{
                                             allHeaders+="\n"+this.responseHeaders[i].header+": "+this.responseHeaders[i].value;
                                        }

                            }
            }
            return allHeaders;
      	}
  	}
  	this.overrideMimeType = function(mime) {
  	    if(this.readyState==this.LOADING || this.readyState==this.DONE){
  	        throw new Error('InvalidStateError');
  	    }else{

  	    }
  	}
}