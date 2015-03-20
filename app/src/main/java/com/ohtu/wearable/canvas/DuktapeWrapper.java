package com.ohtu.wearable.canvas;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
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

    public native void runScript(String xmlhttprequest, String script);

    public DuktapeWrapper(Context ctx){
        try {
            AssetManager am = ctx.getAssets();
            InputStream is = am.open("canvas.js");

            byte[] b = new byte[is.available()];
            is.read(b);
            canvasScript = new String(b);
            Log.d("DW", canvasScript);
        } catch (Exception e) {
            canvasScript = "";
            Log.e("DW","Error: can't read file.");
        }
    }

    public void execJS(String script){

    }
}
