package com.ohtu.wearable.canvas;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import java.io.InputStream;

/**
 * Created by sjsaarin on 20.3.2015.
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
            InputStream is = am.open("canvas.js");

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

    public static String drawCanvas(String fillStyle, String strx, String stry, String strwidth, String strheight){

        Log.d("Draw", "drawCanvas " + fillStyle + " " + strx + " " + stry + " " + strwidth + " " + strheight);

        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        int width = Integer.parseInt(strwidth);
        int height = Integer.parseInt(strheight);


        canvasElement.fillStyle=fillStyle;
        canvasElement.fillRect(x, y, width, height);

        return "canvas drawn";
    }
}
