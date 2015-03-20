package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                CanvasElement ce = new CanvasElement(100, 100, stub);
                ce.fillStyle="rgb(23,231,12)";
                ce.fillRect(20, 20, 40, 40);
                DuktapeWrapper wrapper = new DuktapeWrapper(stub.getContext());
            }
        });
    }
}
