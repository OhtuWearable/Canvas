package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * emulates HTML5 canvas functionality on android.canvas
 */
public class CanvasElement extends Activity {

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private WatchViewStub stub;
    private float lastX;
    private float lastY;

    public String fillStyle;

    /**
     * Intializes canvas for drawing, sets last point to (0,0) and color to black rgb(0,0,0)
     *
     * @param width
     * @param height
     * @param stub
     */
    public CanvasElement(int width, int height, WatchViewStub stub){
        this.stub = stub;
        paint = new Paint();
        fillStyle = "rgb(0,0,0)";
        parseColors(fillStyle);
        
        lastX = 0;
        lastY = 0;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(0xffffffff);

    }

    /**
     * Draws filled rectangle
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillRect(int x, int y, int width, int height){
        parseColors(this.fillStyle);
        canvas.drawRect(x, y, x+width, y+height, paint);
        LinearLayout ll = (LinearLayout) stub.findViewById(R.id.canvas);
        ll.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    /**
     * Draws line from last point to point given as parameters
     *
     * @param x
     * @param y
     */
    public void lineTo(int x, int y){
        paint.setColor(Color.parseColor("#000000"));
        //ToDo: implement line drawing here

        canvas.drawLine(lastX, lastY, x, y, paint);
        lastX = x;
        lastY = y;
        Log.d("Canvas Element", "drawing line from: " + lastX + ", " + lastY +"," + " to: " + x +", " + y);
    }

    /**
     * parses colors from color string
     *
     * @param colors colors as a string formatted 'rgb(rrr,ggg,bbb)' or '#rrggbb'
     */
    private void parseColors(String colors){
        String pattern = "#([A-F0-9]||[a-f0-9]){6}";
        if (colors.matches(pattern)){
            paint.setColor(Color.parseColor(colors));
            return;
        }
        pattern = "rgb\\(\\d\\d?\\d?,\\d\\d?\\d?,\\d\\d?\\d?\\)";
        if (!colors.matches(pattern)) return;

        colors = colors.substring(4);

        int r = Integer.parseInt(colors.substring(0,colors.indexOf(',')));
        if (r > 255) return;

        colors = colors.substring(colors.indexOf(',') + 1);

        int g = Integer.parseInt(colors.substring(0,colors.indexOf(',')));
        if (g > 255) return;

        int b = Integer.parseInt(colors.substring(colors.indexOf(',') + 1, colors.length()-1));
        if (b > 255) return;

        paint.setColor(Color.argb(255, r, g, b));

    }

}
