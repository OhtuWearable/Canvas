package com.ohtu.wearable.canvas;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sjsaarin on 27.3.2015.
 */
public class DownloadActivity extends AsyncTask<String, Void, String> {

    private DuktapeWrapper wrapper;

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            Log.d("DownloadActvity", e.toString());
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result){
        Log.d("DownloadActvity", result);
        if (!result.equals("error")) wrapper.execJS(result);
    }

    public void setWrapper(DuktapeWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private String downloadUrl(String mUrl) throws IOException {
        InputStream is = null;
        int len = 10;

        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int respCode = conn.getResponseCode();
            Log.d("HTTP", "HTTP response: " + respCode);

            //Convert the InputStream to string
            is = conn.getInputStream();
            String content = readToString(is, len);
            return content;

            //Close InputStream when finished
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readToString(InputStream is, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
