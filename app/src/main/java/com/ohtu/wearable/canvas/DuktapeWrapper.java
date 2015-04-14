package com.ohtu.wearable.canvas;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Wrapper class to wrap calls from JNI (duktape) to CanvasElement
 *
 */
public class DuktapeWrapper {

    public static Context ctx;
    public static WatchViewStub stub;
    public static CanvasElement canvasElement;

    static {
        System.loadLibrary("jsModule");
    }

    public static HashMap<String, AsyncTask> requests=new HashMap<>();

    public native void runScript(String canvas, String script);

    public native String runScriptOnContext(long contextPointer, String script);

    public void execJS(String lib, String script){
        runScript(lib, script);
        Log.d("SCRIPT", script);
    }

    public static String performJavaHttpAbort(String reqID){
        if(DuktapeWrapper.requests.get(reqID)!=null){
            if(!DuktapeWrapper.requests.get(reqID).isCancelled()){
                DuktapeWrapper.requests.get(reqID).cancel(true);
            }
        }
        return "";
    }

    public static String performJavaHttpRequest(String method,
                                                String url,
                                                String data,
                                                String reqid,
                                                long contextPointer,
                                                String headers,
                                                String username,
                                                String password,
                                                boolean async) {
        Log.d("DUKTAPE", "NEW REQUEST: "+reqid+" "+method+" "+data);
        switch (method) {
            case "GET":
                DuktapeWrapper.newRequest(url, method, data, contextPointer, reqid, headers, username, password, async);
                break;
            case "SLEEP":
                DuktapeWrapper.newRequest(url, method, data, contextPointer, reqid, headers, username, password, async);
                break;
            case "POST":
                DuktapeWrapper.newRequest(url, method, data, contextPointer, reqid, headers, username, password, async);
                break;
            case "PUT":
                DuktapeWrapper.newRequest(url, method, data, contextPointer, reqid, headers, username, password, async);
                break;
            case "DELETE":
                DuktapeWrapper.newRequest(url, method, data, contextPointer, reqid, headers, username, password, async);
                break;
            default:
                break;
        }
        return "";
    }

    private static HashMap<String, String> headersToMap(String headers){
        HashMap<String, String> map=new HashMap<>();
        if(headers.contains("#")){
            String[] splitted=headers.split("#");
            for(int i=0; i<splitted.length; i++){
                if(splitted[i].contains(":")) {
                    String[] header = splitted[i].split(":");
                    map.put(header[0], header[1]);
                }
            }
        }
        return map;
    }

    public static void newRequest(String url,
                                  String method,
                                  String data,
                                  long contextPointer,
                                  String reqID,
                                  String headers,
                                  String username,
                                  String password,
                                  boolean async) {
        XMLHTTPRequest req = new XMLHTTPRequest();
        req.setMethod(method);
        req.setUrl(url);
        req.setHeaders(DuktapeWrapper.headersToMap(headers));
        req.setUsername(username);
        req.setPassword(password);
        req.setContextPointer(contextPointer);
        req.setReqID(reqID);
        req.setData(data);
        DuktapeWrapper.requests.put(reqID, req);
        req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public static String fillRect(String fillStyle, String strx, String stry, String strwidth, String strheight){

        Log.d("Draw", "drawCanvas " + fillStyle + " " + strx + " " + stry + " " + strwidth + " " + strheight);

        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        int width = Integer.parseInt(strwidth);
        int height = Integer.parseInt(strheight);


        canvasElement.fillStyle=fillStyle;
        canvasElement.fillRect(x, y, width, height);

        return "rectangle drawn";
    }

    public static String clearRect(String strx, String stry, String strwidth, String strheight){

        Log.d("Clear", "clearRect " + strx + " " + stry + " " + strwidth + " " + strheight);

        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        int width = Integer.parseInt(strwidth);
        int height = Integer.parseInt(strheight);

        canvasElement.clearRect(x, y, width, height);

        return "rectangle drawn";
    }

    public static String moveTo(String strx, String stry){
        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        canvasElement.moveTo(x, y);
        return "moved to: " + strx + ", " + stry;

    }

    /**
     * Calls canvasElement.beginPath()
     *
     * @return
     */
    public static String beginPath(){
        canvasElement.beginPath();
        return "path started";
    }

    /**
     * Calls canvasElement.lineTo() function, call this from JNI
     *
     * @param strx
     * @param stry
     * @return
     */
    public static String lineTo(String strx, String stry){

        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);

        canvasElement.lineTo(x, y);

        return "line drawn";
    }

    public static String stroke(String strokeStyle) {

        canvasElement.stroke(strokeStyle);

        return "stroke done";
    }

    /**
     * Calls canvasElement.getWidth() function
     *
     * @return canvas width
     */
    public static int getWidth(){
        //return canvasElement.getWidth();
        return 1;
    }

    /**
     * Calls canvasElement.getHeight function
     *
     * @return canvas height
     */
    public static int getHeight(){
        //return canvasElement.getHeight();
        return 1;
    }


}
