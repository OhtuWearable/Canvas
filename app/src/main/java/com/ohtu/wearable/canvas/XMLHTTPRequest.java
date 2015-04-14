package com.ohtu.wearable.canvas;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sjsaarin on 5.2.2015.
 * <p/>
 * Downloads message from web server and displays it on screen
 */
public class XMLHTTPRequest extends AsyncTask<String, Void, String> {

    private String url;
    private String method;
    private long contextPointer;
    private String reqID;
    private int state;
    private int status;
    private HashMap<String, String> headers;
    private String username;
    private String password;
    private String responseHeaders;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    @Override
    protected String doInBackground(String... urls) {
        return exec();
    }

    private String execCallback(String callback){
        String runRet= MainActivity.wrapper.runScriptOnContext(this.contextPointer, "if(xmlHttpRequests[\"" + reqID + "\"]."+callback+"!=null){xmlHttpRequests[\"" + reqID + "\"]."+callback+"();}");
        Log.d("EXEC", runRet);
        return runRet;
    }

    @Override
    protected void onPostExecute(String result) {
        MainActivity.wrapper.runScriptOnContext(this.contextPointer, "xmlHttpRequests[\"" + reqID + "\"].readyState=4;");
        MainActivity.wrapper.runScriptOnContext(this.contextPointer, "xmlHttpRequests[\"" + reqID + "\"].status=" + this.status + ";");
        MainActivity.wrapper.runScriptOnContext(this.contextPointer, "xmlHttpRequests[\"" + reqID + "\"].responseText=\'" + result + "\';");
        MainActivity.wrapper.runScriptOnContext(this.contextPointer, "xmlHttpRequests[\"" + reqID + "\"].responseHeaders=Duktape.dec(\'jx\', \'" + this.responseHeaders + "\');");
        Log.d("RESPONSE", this.responseHeaders);
        Log.d("XMLHTTPREQUEST-RESULT", reqID + ": " + this.execCallback("onreadystatechange"));
    }

    private String exec() {
        switch (method) {
            case "GET":
                return this.getData("GET");
            case "SLEEP": {
                try {
                    Thread.currentThread();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.getData("GET");
            }
            case "POST":
                return this.postData("POST");
            case "PUT":
                return this.postData("PUT");
            case "DELETE":
                return this.getData("DELETE");
            default:
                return "";
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("ABORT", reqID + " ABORTED");
        //this.execCallback("onabort");
    }

    public void setContextPointer(long contextPointer) {
        this.contextPointer = contextPointer;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    private String headersToJSON(Map<String, List<String>> headers) {
        String json = "";
        for (Map.Entry<String, List<String>> k : headers.entrySet()) {
            for (String v : k.getValue()) {
                if (k.getKey() != null) {
                    json += "{\"header\":\"" + k.getKey() + "\", \"value\":\"" + v + "\"},";
                }
            }
        }
        return "[" + json.substring(0, json.length() - 1) + "]";
    }

    private String getData(String method) {
        InputStream is = null;
        String retStr = "";
        //int len = 20;
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Set<String> headersStr = this.headers.keySet();
            for (String h : headersStr) {
                Log.d("HEADER", h + ":" + this.headers.get(h));
                conn.setRequestProperty(h, this.headers.get(h));
            }

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.connect();

            this.status = conn.getResponseCode();
            this.responseHeaders = this.headersToJSON(conn.getHeaderFields());
            Log.d("XMLHTTP", "HTTP response: " + this.status);
            is = conn.getInputStream();
            retStr = readISToString(is, "UTF-8");
            //String content = readToString(is, len);
            Log.d("XMLHTTP", retStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retStr;
    }

    private String postData(String method) {
        InputStream is = null;
        String retStr = "";
        //int len = 20;
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Set<String> headersStr = this.headers.keySet();
            for (String h : headersStr) {
                Log.d("HEADER", h + ":" + this.headers.get(h));
                conn.setRequestProperty(h, this.headers.get(h));
            }
            if(this.data!=null) conn.setFixedLengthStreamingMode(this.data.getBytes().length);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if(this.data!=null) {
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(this.data.getBytes());
                os.flush();
            }
            conn.connect();
            this.status = conn.getResponseCode();
            this.responseHeaders = this.headersToJSON(conn.getHeaderFields());
            Log.d("XMLHTTP", "HTTP response: " + this.status);
            is = conn.getInputStream();
            retStr = readISToString(is, "UTF-8");
            //String content = readToString(is, len);
            Log.d("XMLHTTP", retStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retStr;
    }

    /*
    private String readToString(InputStream is, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }*/

    public String readISToString(InputStream istream, String encoding) throws IOException {
        return new String(readISFully(istream), encoding);
    }

    private byte[] readISFully(InputStream istream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = istream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toByteArray();
    }

}