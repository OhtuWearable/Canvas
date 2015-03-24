package com.ohtu.wearable.canvas;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

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

    public native void runScript(String canvas, String script);

    public DuktapeWrapper(WatchViewStub stub){
        this.ctx = stub.getContext();
        this.stub = stub;
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
            Log.d("wrapper", canvasScript);
            Log.d("wrapper", script);
            runScript(canvasScript, script);
            //Log.d("DW", canvasScript);
        } catch (Exception e) {
            canvasScript = "";
            Log.e("DW","Error: can't read file.");
            return false;
        }
        return true;
    }

    public static String drawCanvas(String fillStyle, String strx, String stry, String strwidth, String strheight){

        Log.d("Draw", "drawCanvas " + fillStyle + " " + strx + " " + stry + " " + strwidth + " " + strheight);

        int x = Integer.parseInt(strx);
        int y = Integer.parseInt(stry);
        int width = Integer.parseInt(strwidth);
        int height = Integer.parseInt(strheight);

        Log.d("Draw", "drawCanvas " + fillStyle + " " + strx + " " + stry + " " + strwidth + " " + strheight);

        CanvasElement ce = new CanvasElement(width, height, stub);
        ce.fillStyle=fillStyle;
        ce.fillRect(x, y, width, height);

        return "canvas drawn";
    }
}
