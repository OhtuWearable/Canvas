package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                /*
                CanvasElement ce = new CanvasElement(100, 100, stub);
                //ce.fillStyle="rgb(23,231,12)";
                ce.fillStyle="#ffffff";
                ce.fillRect(20, 20, 40, 40);*/
                loadAndRunScript(stub);
            }
        });

    }

    private void loadAndRunScript(WatchViewStub stub){
        DuktapeWrapper wrapper = new DuktapeWrapper(stub);

        try {
            AssetManager am = stub.getContext().getAssets();
            InputStream is = am.open("drawrect.js");

            byte[] b = new byte[is.available()];
            is.read(b);
            String script = new String(b);
            wrapper.execJS(script);
            //Log.d("MA", script);
        } catch (Exception e) {
            Log.e("MA", "Error: can't read file.");

        }

    }
}
