package com.pkovinski.balls;

import android.content.Context;

//     Graphics     //
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

//     Android View //
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

//     Log Message //

//     Timer       //

//     Array List  //
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyPanel extends View implements OnTouchListener {

    //      Margins      //
    final float[] margin_ten   = {10, 10, 10, 10};
    final float[] margin_five  = {5, 5, 5, 5};
    final float[] margin_zero  = {0, 0, 0, 0};

    //       Scale       //
    public static final double[] content_scale = {1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.5, 2.75, 3.00};
    public double scale;

    //  Timing Sequence  //
    public long time = 0;

    //  Screen dimension //
    public float width;
    public float height;

    //    Frame List     //
    ArrayList<Integer> frame_list;
    ArrayList<String>  frame_name;
    float[][] frame_boundary;

    public MyPanel(Context context)
    {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);

        //  Initialize frame variables  //
        this.frame_list     = new ArrayList<>();
        this.frame_name     = new ArrayList<>();
        this.frame_boundary = new float[10][4] ;


        // Start system timer //
        Timer t = new Timer();
        TimerTask taskLog = new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        };
        t.schedule(taskLog, 0, 1);

    }

    public void createFrame(String frame_name, Canvas canvas, int frameId, float x, float y, float width, float height, float[] margins ,  int color, int StrokeWidth)
    {
        Paint  p = new Paint();

        float left   = margins[0];
        float top    = margins[1];
        float right  = margins[2];
        float bottom = margins[3];

        p.setColor(color);
        p.setStrokeWidth(StrokeWidth);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Cap.ROUND);
        canvas.drawRect(x + left, y + top , width - right, height - bottom, p);
        this.addFrame(frameId, frame_name, new float[] {x + left + StrokeWidth, y + top + StrokeWidth, width - right - StrokeWidth, height - bottom - StrokeWidth});
    }

    public void createElasticFrame(String frame_name, Canvas canvas, int frameId, float x, float y, float width, float height, float[] margins, int color, int StrokeWidth)
    {

        Paint p = new Paint();

        // Margins //
        float left   = margins[0];
        float top    = margins[1];
        float right  = margins[2];
        float bottom = margins[3];

        // Screen dimension //
        float s_width  = this.width;
        float s_height = this.height;

        float start_x = (x / 100) * s_width + left;
        float start_y = (y / 100) * s_height + top;
        float end_x   = (width / 100)  * this.width - right - StrokeWidth;
        float end_y   = (height / 100) * this.height - bottom - StrokeWidth;


        p.setColor(color);
        p.setStrokeWidth(StrokeWidth * (float)this.getContentScale());
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Cap.ROUND);
        canvas.drawRect(start_x, start_y, end_x, end_y, p);
        this.addFrame(frameId, frame_name, new float[] {start_x + StrokeWidth, start_y + StrokeWidth, end_x - StrokeWidth, end_y - StrokeWidth});
    }

    private void addFrame(int frameId, String frameName, float[] frameBoundary)
    {
        if(!this.frame_list.contains(frameId))
        {
            this.frame_list.add(frameId);
        }

        if(!this.frame_name.contains(frameName))
        {
            this.frame_name.add(frameName);
        }

        this.frame_boundary[frameId] = frameBoundary;
        Log.i("Scale", this.getContentScale() + " MyPanel.java");
        scale = this.getContentScale();
    }

    public double getContentScale()
    {
        double scale = 1;
        // Scale for FullHD resolution screen //
        if(this.width <= 800 || this.height <= 1100)
        {
            scale = content_scale[0];
        }
        else if(this.width <= 1080.1 || this.height <= 1920.1)
        {
            scale = content_scale[2];
        }else if(this.width <= 1440.1 || this.height <= 2560.1)
        {
            scale = content_scale[4];
        }
        return scale;
    }

    public double getContentScale2(double width, double height)
    {
        double scale = 1;
        if(width <= 800 || height <= 1100)
        {
            scale = content_scale[0];
        }
        else if(this.width <= 1080.1 || this.height <= 1920.1)
        {
            scale = content_scale[2];
        }else if(this.width <= 1440.1 || this.height <= 2560.1)
        {
            scale = content_scale[4];
        }
        return scale;
    }

    public void writePanelName(Canvas canvas, float[] frame_boundary,  String msg, int color, int textSize, float scale,int moveCornerX,  int moveCornerY)
    {
        Paint p = new Paint();
        p.setColor(color);
        p.setTextSize(textSize * scale);
        canvas.drawText(msg, frame_boundary[0] + moveCornerX * scale, (frame_boundary[1] + moveCornerY * scale), p);
    }

    public void drawCoordinateLine(Canvas canvas, float[] frame_boundary)
    {

    }

    public void writePanelInfo(Canvas canvas, int frameId)
    {
        Paint p = new Paint();
        p.setTextSize(45);
        p.setColor(Color.RED);
        String msg = "";
        msg += "Frame Id:   " + Integer.toString(frameId);
        float[] boundary = this.frame_boundary[frameId];
        canvas.drawText(msg, 20, 200, p);
        msg = "LEFT  : " + boundary[0] + " ";
        canvas.drawText(msg, 25, 250, p);
        msg = "TOP   : " + boundary[1] + " ";
        canvas.drawText(msg, 25, 350, p);
        msg = "RIGHT : " + boundary[2] + " ";
        canvas.drawText(msg, 25, 450, p);
        msg = "BOTTOM: " + boundary[3] + " ";
        canvas.drawText(msg, 25, 550, p);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        return true;
    }
}
