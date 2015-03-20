package com.ohtu.wearable.canvas;

/**
 * Created by sjsaarin on 20.3.2015.
 */
public class DuktapeWrapper {
    static {
        System.loadLibrary("jsModule");
    }

    public native void runScript(String xmlhttprequest, String script);
}
