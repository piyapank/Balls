package com.pkovinski.balls;

import android.content.Context;

        // Graphics //
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// Android View //
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

       // Array List //
import java.util.ArrayList;

// Log //
import android.util.Log;

public class MainContent extends MyPanel implements OnTouchListener
{
    // Props (Entity List) //
    Ball[] balls;
    double[][][] ball_data;
    double content_scale;

    public MainContent(Context context) {
        super(context);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Pre-defined entity characteristic //
        ball_data = new double[][][] {
                { {1.23 }, {300, 300}, {0, 0}, {0, 0}, {25}, {10}, {10}, {60} },
                { {1.232}, {800, 300}, {0, 0}, {0, 0}, {95}, {10}, {10}, {60} },
                { {4.56},  {450, 450}, {0, 0}, {0, 0}, {12}, {1},  {7} , {30} },
                { {7,89},  {600, 600}, {0, 0}, {0, 0}, {96}, {3},  {15}, {90} },
                { {0}   ,  {250, 700}, {0, 0}, {0, 0}, {35}, {5},  {13}, {60} },
                { {12},    {500, 250}, {0, 0}, {0, 0}, {32}, {7},  {6},  {25} },
                { {121},   {600, 450}, {0, 0}, {0, 0}, {36}, {6},  {8},  {19} },
                { {1221},  {240, 290}, {0, 0}, {0, 0}, {12}, {8},  {9},  {44} },
                { {12221}, {700, 150}, {0, 0}, {0, 0}, {22}, {2},  {4},  {34} }
        };

        // Create entity array //
        balls = new Ball[ball_data.length];
        for(int i = 0; i <= ball_data.length - 1; i ++)
        {
          double      id = ball_data[i][0][0];
          double[]   pos = ball_data[i][1];
          double[]   vlo = ball_data[i][2];
          double[]   nxt = ball_data[i][3];
          double     ang = ball_data[i][4][0];
          double     spd = ball_data[i][5][0];
          double     mas = ball_data[i][6][0];
          double     rad = ball_data[i][7][0];
          balls[i] = new Ball(id, pos, vlo, nxt, ang, spd, mas, rad, paint);
        }
    }

    public void onDraw(Canvas canvas)
    {
        this.width  = getWidth();
        this.height = getHeight();
        //updateContentScale(getContentScale());
        myDraw(canvas);
    }

    public void myDraw(Canvas canvas)
    {
        this.drawInfo(canvas);
        this.drawMainContent(canvas);
        this.drawAds(canvas);
        invalidate();
    }

    public void updateContentScale(double scale)
    {
        for(int i = 0; i <= balls.length - 1; i++)
        {
            double rad = balls[i].getRadius();
            balls[i].setRadius(rad * scale);
        }
    }

    public void drawInfo(Canvas canvas)
    {
        int frame_id = 1;
        createElasticFrame("SysTimer", canvas, frame_id, 1, 1, 100, 5, this.margin_five, Color.TRANSPARENT, 5);
        float[] frame_boundary = this.frame_boundary[frame_id];
        String txt = "The time is: " + this.time;
        writePanelName(canvas, frame_boundary, txt, Color.WHITE, 40, (float) getContentScale(), 0, 25);
    }

    public void drawMainContent(Canvas canvas)
    {
        int frame_id = 2;
        createElasticFrame("MainContents", canvas, frame_id, 1, 5, 100, 90, this.margin_five, Color.WHITE, 5);
        float[] frame_boundary = this.frame_boundary[frame_id];
        writePanelName(canvas, frame_boundary, "Main Content", Color.WHITE, 40, (float) getContentScale(),0 , 40);
        // Function to draw contents //
        //this.writePanelInfo(canvas, frame_id);
        this.drawBall(canvas, frame_boundary);
    }

    public void drawBall(Canvas canvas, float[] frame_boundary)
    {
        // Paint paint = new Paint();
        // String[] b_info = ball.getBallInfo();
        // Log.i("b_info", b_info[1]);
        // paint.setColor(Color.WHITE);

        // Solution 1 //
        for(int i = 0; i <= this.balls.length - 1; i ++)
        {
            this.balls[i].setBalls(this.balls);    // OR  this.balls[i].setBallListData(this.ball_data);
            this.balls[i].setFrameBoundary(frame_boundary);
            this.balls[i].Rendering(canvas, scale);
            this.balls = this.balls[i].getBalls(); // OR  this.ball_data = this.balls[i].getBallListData();
        }

        /*Ball ball = new Ball(frame_boundary);
        ball.setId(1.123);
        ball.setPaint(p);
        ball.setRadius(60);
        ball.Rendering(canvas);*/

    }

    public void drawAds(Canvas canvas)
    {
        // Draw ads function //
        int frame_id = 3;
        createElasticFrame("AdsContainer", canvas, frame_id, 1, 90, 100,100,  this.margin_five, Color.WHITE, 5);
        float[] frame_boundary = this.frame_boundary[frame_id];
        writePanelName(canvas, frame_boundary, "ADS Panel", Color.WHITE, 40, (float) getContentScale(), 0, 40);
    }

    public boolean getBallResponse(float[] touch_positions)
    {
        float touchX = touch_positions[0];
        float touchY = touch_positions[1];
        double scale = this.getContentScale();
        for(int i = 0 ; i <= this.balls.length - 1 ; i ++)
        {
            Ball ball = this.balls[i];
            double rad = ball.getRadius();
            double[] pos = ball.getPositions();
            if(touchX <= (pos[0] + rad) * scale && touchX >= (pos[0] - rad) * scale)
            {
                if(touchY <= (pos[1] + rad) * scale && touchY >= (pos[1] - rad) * scale)
                {
                    Log.i("Touch", "Ball Id:" + ball.getId());
                    this.selectBall = ball.getId();
                    return true;
                }
            }
        }
        return false;
    }

    // Control class //
    public void drawTapPoint(Canvas canvas)
    {
        Paint p = new Paint();
        p.setColor(Color.RED);
        Log.i("TAP", "d");
        for(int i = 0 ; i <= this.tapPoint.length - 1; i ++)
        {
            float posX = this.tapPoint[i][0];
            float posY = this.tapPoint[i][1];
            float rad  = this.tapPoint[i][2];
            canvas.drawCircle(posX, posY, rad, p);
        }
    }

    // controlling entity //
    double selectBall;
    float[][] tapPoint;

    public void moveBall(float[] touch_positions)
    {
        /*if(this.getBallResponse(touch_positions))
        {
            double newX = (double) touch_positions[0];
            double newY = (double) touch_positions[1];

            for(int i = 0; i <= this.balls.length - 1; i ++)
            {
                //this.balls[i].setPositions(new double[] {newX, newY});
                Log.i("Touch", "New Pos: " + newX + ", " + newY);
                if(this.balls[i].getId() == this.selectBall)
                {
                    this.balls[i].setPositions(new double[] {newX, newY});
                }
            }
        }*/
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        float x =  event.getX();
        float y =  event.getY();
        float[] touchPoint = {x, y};
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //Log.i("Touch", "HERE");
            this.getBallResponse(touchPoint);
            //addTapPoint(touchPoint,10);
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            this.moveBall(touchPoint);
        }
        return true;
    }
}
