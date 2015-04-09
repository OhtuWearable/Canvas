package com.ohtu.wearable.canvas;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.InputStream;

/**
 * Wrapper class to wrap calls from JNI (duktape) to CanvasElement
 *
 */
public class DuktapeWrapper {
    static {
        System.loadLibrary("jsModule");
    }

    private Context ctx;
    private String canvasScript;
    private static WatchViewStub stub;
    private static CanvasElement canvasElement;

    public native void runScript(String canvas, String script);

    /**
     * Creates new android.canvas sized to fill screen
     *
     * @param stub
     */
    public DuktapeWrapper(WatchViewStub stub){
        this.ctx = stub.getContext();
        this.stub = stub;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) stub.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int canvasWidth = metrics.widthPixels;
        int canvasHeight = metrics.heightPixels;

        Log.d("Canvas", "width: " + canvasWidth + " - height: " + canvasHeight);

        this.canvasElement = new CanvasElement(canvasWidth, canvasHeight, stub);
    }

    /**
     * Executes JS given as parameter if loading of canvas.js is successful and returns true,
     * otherwise returns false
     *
     * @param script, javascript to execute
     * @return true if success
     */
    public boolean execJS(String script){
         try {
            AssetManager am = ctx.getAssets();
            InputStream is = am.open("canvas_script/canvas.js");

            byte[] b = new byte[is.available()];
            is.read(b);
            canvasScript = new String(b);

        } catch (Exception e) {
            canvasScript = "";
            Log.e("DW","Error: can't read file.");
            return false;
        }
        Log.d("wrapper", canvasScript);
        Log.d("wrapper", script);
        runScript(canvasScript, script);
        return true;
    }

    /**
     * Calls canvasElement.fillRect() function, call this from JNI
     *
     * @param fillStyle
     * @param strx
     * @param stry
     * @param strwidth
     * @param strheight
     * @return
     */
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

    public static String moveTo(String strx, String stry){
        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        //canvasElement.moveTo(x, y);
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
