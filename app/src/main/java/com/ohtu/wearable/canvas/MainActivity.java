package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.InputStream;

public class MainActivity extends Activity {

    public static DuktapeWrapper wrapper=new DuktapeWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //downloadAndRunScript(stub);
                loadAndRunScript(stub);
            }
        });
    }

    //loads script from file and calls duktape wrapper to execute script
    private void loadAndRunScript(WatchViewStub stub){

        DuktapeWrapper.ctx = stub.getContext();
        DuktapeWrapper.stub = stub;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) stub.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int canvasWidth = metrics.widthPixels;
        int canvasHeight = metrics.heightPixels;

        Log.d("Canvas", "width: " + canvasWidth + " - height: " + canvasHeight);

        DuktapeWrapper.canvasElement = new CanvasElement(canvasWidth, canvasHeight, stub);


        try {
            AssetManager am = stub.getContext().getAssets();
            InputStream is = am.open("script.js");

            byte[] b = new byte[is.available()];
            is.read(b);
            String script = new String(b);

            is = am.open("canvas_script/canvas.js");

            b = new byte[is.available()];
            is.read(b);
            String canvasScript = new String(b);

            MainActivity.wrapper.execJS(canvasScript, script);
            //Log.d("MA", script);
        } catch (Exception e) {
            Log.e("MA", "Error: can't read file.");

        }

    }

    /*
    //doesn't work on watch, no direct internet connection
    private void downloadAndRunScript(WatchViewStub stub){

        DuktapeWrapper wrapper = new DuktapeWrapper(stub);
        DownloadActivity dl = new DownloadActivity();
        dl.setWrapper(wrapper);
        dl.execute("https://raw.githubusercontent.com/OhtuWearable/Canvas/master/app/src/main/assets/script.js");

    }
    */
}
