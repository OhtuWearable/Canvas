package com.ohtu.wearable.canvas;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.wearable.view.WatchViewStub;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Emulates HTML5 canvas functionality on android.canvas
 * Methods implemented: fillRect, clearRect, beginPath, lineTo, stroke
 */
public class CanvasElement extends Activity {

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private WatchViewStub stub;
    private float lastX;
    private float lastY;
    private Queue<int []> path;

    public String fillStyle;
    public String strokeStyle;

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
     * draws "clear" (=rectangle filled with background color) rectangle
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void clearRect(int x, int y, int width, int height){
        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawRect(x, y, x+width, y+height, paint);
        LinearLayout ll = (LinearLayout) stub.findViewById(R.id.canvas);
        ll.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    /**
     *  Starts new path
     */
    public void beginPath(){
        path = new LinkedList<>();
        lastX = 0;
        lastY = 0;
    }

    /**
     * Adds line from last point to point given as parameters to path
     *
     * @param x
     * @param y
     */
    public void lineTo(int x, int y){
        //1: draw line
        int point[] = {1, x, y};
        path.add(point);
    }

    /**
     * Moves drawing point to coordinates given as parameters
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        int point[] = {0, x, y};
        path.add(point);
    }

    /**
     * Draws the path
     *
     * @param strokeStyle
     */
    public void stroke(String strokeStyle){
        paint.setColor(Color.parseColor(strokeStyle));
        while (!path.isEmpty()){
            int point[] = path.remove();
            if (point[0] == 1) {
                canvas.drawLine(lastX, lastY, point[1], point[2], paint);
            }
            lastX = point[1];
            lastY = point[2];
        }

        LinearLayout ll = (LinearLayout) stub.findViewById(R.id.canvas);
        ll.setBackgroundDrawable(new BitmapDrawable(bitmap));

    }

    /**
     * parses colors from color string
     *
     * @param colors colors as a string formatted 'rgb(rrr,ggg,bbb)' or '#rrggbb' or color in colorStrings list
     */
    private void parseColors(String colors){
        List<String> colorStrings = new ArrayList<>(Arrays.asList("red", "blue", "green", "black", "white", "gray", "cyan", "magenta", "yellow", "lightgray", "darkgray", "grey", "lightgrey", "darkgrey", "aqua", "fuschia", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"));



        String pattern = "#([A-F0-9]||[a-f0-9]){6}";
        if (colors.matches(pattern) || colorStrings.contains(colors)){
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
