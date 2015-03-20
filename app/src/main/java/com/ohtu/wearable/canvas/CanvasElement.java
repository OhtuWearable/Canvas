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
 * Created by sjsaarin on 20.3.2015.
 */
public class CanvasElement extends Activity {

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private int r;
    private int g;
    private int b;
    private String color;
    private WatchViewStub stub;

    public String fillStyle;

    public CanvasElement(int width, int height, WatchViewStub stub){
        this.stub = stub;
        paint = new Paint();
        fillStyle = "rgb(0,0,0)";
        parseColors(fillStyle);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
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
        canvas.drawRect(x, y, width, height, paint);
        LinearLayout ll = (LinearLayout) stub.findViewById(R.id.canvas);
        ll.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    /**
     * parses colors from color string
     * @param colors colors as a string formatted 'rgb(rrr,ggg,bbb)'
     */
    private void parseColors(String colors){
        String pattern = "rgb\\(\\d\\d?\\d?,\\d\\d?\\d?,\\d\\d?\\d?\\)";
        if (!colors.matches(pattern)) return;

        colors = colors.substring(4);

        int r = Integer.parseInt(colors.substring(0,colors.indexOf(',')));
        if (r > 255) return;

        colors = colors.substring(colors.indexOf(',') + 1);

        int g = Integer.parseInt(colors.substring(0,colors.indexOf(',')));
        if (g > 255) return;

        int b = Integer.parseInt(colors.substring(colors.indexOf(',') + 1, colors.length()-1));
        if (b > 255) return;

        this.r = r;
        this.g = g;
        this.b = b;

        paint.setColor(Color.argb(255, r, g, b));

    }
}
