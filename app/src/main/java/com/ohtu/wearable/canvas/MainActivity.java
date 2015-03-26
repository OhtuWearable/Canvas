package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

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
                loadAndRunScript(stub);
            }
        });
    }

    //loads script from file and calls duktape wrapper to execute script
    private void loadAndRunScript(WatchViewStub stub){
        DuktapeWrapper wrapper = new DuktapeWrapper(stub);

        try {
            AssetManager am = stub.getContext().getAssets();
            InputStream is = am.open("script.js");

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
