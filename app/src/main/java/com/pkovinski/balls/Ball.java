package com.pkovinski.balls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class Ball{

    // All Ball List //
    double[][][] ball_data;
    Ball[] balls;

    // Ball properties //
    private double ball_id;
    private double radius;
    private double speed;
    private double angle;
    private double[] positions;
    private double[] velocity;
    private double mass;
    private double[] next;

    // Paint //
    private Paint paint;

    // Content frame dimension //
    private float left;
    private float top;
    private float right;
    private float bottom;

    public Ball(double id, double[] initialPosition, double[] velocity, double[] next, double angle, double speed, double mass, double radius, Paint paint)
    {
        super();
        this.setId(id);
        this.setPositions(initialPosition);
        this.setVelocity(velocity);
        this.setNext(next);
        this.setAngle(angle);
        this.setSpeed(speed);
        this.setMass(mass);
        this.setRadius(radius);
        this.setPaint(paint);
    }

    public void setFrameBoundary(float[] frame_boundary)
    {
        this.left   = frame_boundary[0];
        this.top    = frame_boundary[1];
        this.right  = frame_boundary[2];
        this.bottom = frame_boundary[3];
    }

    public void setPositions(double[] positions)
    {
        this.positions = positions;
    }

    public double[] getPositions()
    {
        return this.positions;
    }

    public void setVelocity(double[] velocity)
    {
        this.velocity = velocity;
    }

    public double[] getVelocity()
    {
        return this.velocity;
    }

    public void setNext(double[] next)
    {
        this.next = next;
    }

    public double[] getNext()
    {
        return this.next;
    }

    public void setId(double id)
    {
        this.ball_id = id;
    }

    public double getId()
    {
        return this.ball_id;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    public double getAngle()
    {
        return this.angle;
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    public double getMass()
    {
        return this.mass;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public double[][][] getBallListData()
    {
        return this.ball_data;
    }

    public void setBallListData(double[][][] ball_data)
    {
        this.ball_data = ball_data;
    }

    public void setBalls(Ball[] balls)
    {
        this.balls = balls;
    }

    public Ball[] getBalls()
    {
        return this.balls;
    }

    public void Rendering(Canvas canvas, double scale)
    {
        double[] next = this.getNext();
        // Update ball scale //
             // SCALE!!!!!!!!!!!!!!!! //
                // Updating code //
        // Update velocity //
        this.updateVelocity();
        float posX = (float) ( (next[0]));
        float posY = (float) ( (next[1]));
        float rad  = (float) (  this.getRadius() );
        // Update next //
        this.updateNext();
        //this.paintBallInfo(canvas,  this.getBallInfo()[7], new float[] {50, 500});
        //Log.i("pos", Float.toString(posX) + " " + Float.toString(posY));

        // Draw green inner ball //
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        canvas.drawCircle(posX, posY, rad, this.paint);
        canvas.drawCircle(posX, posY, (float)(this.radius * 0.75), p);
        this.collideWall();
        this.drawDirectionalVector(canvas);
        this.collide();
        Log.i("bs", scale + "");
    }

    private void updateNext()
    {
        for(int i = 0; i <= this.balls.length - 1; i ++)
        {
            Ball ball = this.balls[i];
            double[] newPos = {
                    ball.getPositions()[0] + ball.getVelocity()[0],
                    ball.getPositions()[1] + ball.getVelocity()[1]
            };
            ball.setNext(new double[] { newPos[0], newPos[1] } );
            this.updatePosition(newPos, i);
        }
    }

    private void updateVelocity()
    {
       for(int i = 0 ; i <= this.balls.length - 1; i ++)
       {
           Ball ball = this.balls[i];
           double ang       = ball.getAngle();
           // angle to radian = Angle * PI / 180
           double radians   = Math.toRadians(ang);
           double velocityX = Math.cos(radians) * ball.speed;
           double velocityY = Math.sin(radians) * ball.speed;
           ball.setVelocity(new double[] {velocityX, velocityY});
       }
    }

    private void collideWall()
    {
        Ball[] balls = this.balls;
        Ball   ball;

        float left   = this.left;
        float top    = this.top;
        float right  = this.right;
        float bottom = this.bottom;

        for(int i = 0 ; i <= balls.length - 1; i++)
        {
            ball = balls[i];
            double[] pos = ball.getPositions();
            double[] vlo = ball.getVelocity();
            double[] nxt = ball.getNext();
            double   rad = ball.getRadius();
            double   ang = ball.getAngle();
            if( ( nxt[0] + rad ) > right)
            {
                double[] new_velocity = {vlo[0] * -1, vlo[1]};
                ball.setVelocity(new_velocity);
                double[] new_next     = {right - rad, nxt[1]};
                ball.setNext(new_next);
                ball.setAngle(180 - ang);
                Log.i("Bounce", "GO LEFT " + new_next[0] + ", " + new_velocity[0]);
            }
            else if ( ( nxt[0] - rad ) < left)
            {
                double[] new_velocity = {vlo[0] * -1, vlo[1]};
                ball.setVelocity(new_velocity);
                double[] new_next     = {left + rad, nxt[1]};
                ball.setNext(new_next);
                ball.setAngle(180 - ang);
                Log.i("Bounce", "GO RIGHT " + new_next[0] + ", " + new_velocity[0]);
            }
            else if ( ( nxt[1] + rad ) > bottom)
            {
                double[] new_velocity = {vlo[0], vlo[1] * -1};
                ball.setVelocity(new_velocity);
                double[] new_next     = {nxt[0], bottom - rad};
                ball.setNext(new_next);
                ball.setAngle(360 - ang);
                Log.i("Bounce", "GO UP " + new_next[0] + ", " + new_velocity[0]);
            }
            else if ( (nxt[1] - rad ) < top)
            {
                double[] new_velocity = {vlo[0], vlo[1] * -1};
                ball.setVelocity(new_velocity);
                double[] new_next     = {nxt[0], top + rad};
                ball.setNext(new_next);
                ball.setAngle(360 - ang);
                Log.i("Bounce", "GO DOWN " + new_next[0] + ", " + new_velocity[0]);
            }
        }
    }

    private void updatePosition(double[] positions, int indx)
    {
        Ball ball = this.balls[indx];
        ball.setPositions(new double[] {
                positions[0], positions[1]
        });
    }


    private void collide()
    {
        Ball ball;
        Ball testBall;
        for(int i = 0 ; i <= this.balls.length - 1; i ++)
        {
            ball = this.balls[i];
            for(int j = i + 1 ; j <= this.balls.length - 1; j ++)
            {
                testBall = this.balls[j];
                if(hitTestCircle(ball, testBall))
                {
                    Log.i("Hit", "Balls: " + ball.getId() + " AND, " + testBall.getId());
                    //this.collideBalls(ball, testBall);
                    //this.stupidBounce(ball, testBall);
                }
            }
        }
    }

    private void drawDirectionalVector(Canvas canvas)
    {
        Paint p = new Paint();
        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(10);
        for(int i = 0 ; i <= this.balls.length - 1; i++)
        {
            Ball ball = this.balls[i];
            double   degree       = ball.getAngle();
            double   radian       = degree * Math.PI / 180;
            double[] pos          = ball.getPositions();
            double[] endLinePos   = {pos[0] + Math.cos(radian) * ball.getRadius(),
                                     pos[1] + Math.sin(radian) * ball.getRadius()};
            canvas.drawLine((float)pos[0], (float)pos[1], (float)endLinePos[0], (float)endLinePos[1], p);
        }
    }

    private void stupidBounce(Ball ball_1, Ball ball_2)
    {
        double dx                       =   ball_1.getNext()[0] - ball_2.getNext()[0];
        double dy                       =   ball_1.getNext()[1] - ball_2.getNext()[1];
        double collisionAngle   =   Math.atan2(dy, dx);
        //Log.i("COL", " " + collisionAngle);
        //ball_1.setSpeed(0);
        //ball_2.setSpeed(0);

        //double   b1preSpd    = Math.pow(ball_1.getVelocity()[0], 2) + Math.pow(ball_1.getVelocity()[1], 2);
        //double   b2preSpd    = Math.pow(ball_2.getVelocity()[0], 2) + Math.pow(ball_2.getVelocity()[1], 2);
        //double   b1Spd       = Math.sqrt(b1preSpd);
        //double   b2spd       = Math.sqrt(b2preSpd);


        //double   caAndHalfPi   = collisionAngle + Math.PI / 2;
        //double[] b1Velo        = {Math.cos(caAndHalfPi), Math.sin(caAndHalfPi)};
        //double[] b2Velo        = {Math.cos(caAndHalfPi), Math.sin(caAndHalfPi)};

        double   b1Direction   = Math.atan2(ball_1.getVelocity()[1], ball_1.getVelocity()[0]);
        double   b2Direction   = Math.atan2(ball_2.getVelocity()[0], ball_2.getVelocity()[1]);
        ball_1.setAngle(180 - b1Direction);
        ball_2.setAngle(b2Direction);
        Log.i("b2a", " " + b2Direction + " " + b1Direction);
    }

    private void collideBalls(Ball ball_1, Ball ball_2)
    {
        double dx                 =   ball_1.getNext()[0] - ball_2.getNext()[0];
        double dy                 =   ball_1.getNext()[1] - ball_2.getNext()[1];
        double collisionAngle     =   Math.atan2(dy, dx);


        //     Calculate the Velocity Vectors   //
        double b1vx               =   ball_1.getVelocity()[0] * ball_1.getVelocity()[0];
        double b1vy               =   ball_1.getVelocity()[1] * ball_1.getVelocity()[1];
        double spd_1              =   Math.sqrt(b1vx + b1vy);

        double b2vx               =   ball_2.getVelocity()[0] * ball_2.getVelocity()[0];
        double b2vy               =   ball_2.getVelocity()[1] * ball_2.getVelocity()[1];
        double spd_2              =   Math.sqrt(b2vx + b2vy);

        double direction_1        =   Math.atan2(ball_1.getVelocity()[1], ball_1.getVelocity()[0]);
        double direction_2        =   Math.atan2(ball_2.getVelocity()[1], ball_2.getVelocity()[0]);

        double velocityx_1        =   spd_1 * Math.cos(direction_1 - collisionAngle);
        double velocityy_1        =   spd_1 * Math.sin(direction_1 - collisionAngle);
        double velocityx_2        =   spd_2 * Math.cos(direction_2 - collisionAngle);
        double velocityy_2        =   spd_2 * Math.sin(direction_2 - collisionAngle);

        double final_velocityx_1  =   ((ball_1.getMass() - ball_2.getMass()) * velocityx_1 + (ball_2.getMass() + ball_2.getMass()) * velocityx_2) / (ball_1.getMass() + ball_2.getMass());
        double final_velocityx_2  =   ((ball_1.getMass() + ball_2.getMass()) * velocityx_1 + (ball_2.getMass() - ball_2.getMass()) * velocityx_2) / (ball_1.getMass() + ball_2.getMass());
        double final_velocityy_1  =   velocityy_1;
        double final_velocityy_2  =   velocityy_2;

        double b1_velocityx       =   Math.cos(collisionAngle) * final_velocityx_1 + Math.cos(collisionAngle + Math.PI / 2) * final_velocityy_1;
        double b1_velocityy       =   Math.sin(collisionAngle) * final_velocityx_1 + Math.sin(collisionAngle + Math.PI / 2) * final_velocityy_1;
        double b2_velocityx       =   Math.cos(collisionAngle) * final_velocityx_2 + Math.cos(collisionAngle + Math.PI / 2) * final_velocityy_2;
        double b2_velocityy       =   Math.sin(collisionAngle) * final_velocityy_2 + Math.cos(collisionAngle + Math.PI / 2) * final_velocityy_2;

        double[] b1Next           =  {ball_1.getNext()[0] + b1_velocityx, ball_1.getNext()[1] + b1_velocityy};
        double[] b2Next           =  {ball_2.getNext()[0] + b2_velocityx, ball_2.getNext()[1] + b2_velocityy};

        ball_1.setNext(b1Next);
        ball_2.setNext(b2Next);
    }

    private boolean hitTestCircle(Ball ball_1, Ball ball_2)
    {
        boolean ret_val = false;
        double dx = ball_1.getNext()[0] - ball_2.getNext()[0];
        double dy = ball_1.getNext()[1] - ball_2.getNext()[1];
        double distance = (dx * dx +  dy * dy);
        double radius = ball_1.getRadius() + ball_2.getRadius();
        if(distance <=  radius * radius)
        {
            // Ball overlapped //
            ret_val = true;
        }
        // Ball not overlapped //
        return ret_val;
    }

    public void getBallResponse(float[] touch_positions)
    {
        float touchX = touch_positions[0];
        float touchY = touch_positions[1];
        for(int i = 0 ; i <= this.balls.length - 1 ; i ++)
        {
            Ball ball = this.balls[i];
            double rad = ball.getRadius();
            if(touchX <= rad || touchX >= rad)
            {
                if(touchY <= rad || touchY >= rad)
                {
                    Log.i("Touch", "BALL!");
                }
            }
        }
    }

    public void paintBallInfo(Canvas canvas, String str, float[] coordinate)
    {
        Paint p = new Paint();
        p.setTextSize(40);
        p.setColor(Color.RED);
        canvas.drawText(str, coordinate[0],coordinate[1], p);
    }

    public String[] getBallInfo()
    {
        String[] msg = new String[20];
        msg[0] = "Ball Id: " + Double.toString(this.getId());
        msg[1] = "Frame Boundary: "
                + "L>"   + Float.toString(this.left)
                + ", T>" + Float.toString(this.top)
                + ", R>" + Float.toString(this.right)
                + ", B>" + Float.toString(this.bottom);
        msg[2] = "Speed :" + Double.toString(this.getSpeed());
        msg[3] = "Mass :" + Double.toString(this.getMass());
        msg[4] = "Angle :" + Double.toString(this.getAngle());
        msg[5] = "Radius :" + Double.toString(this.getRadius());
        double[] pos = this.getPositions();
        msg[6] = "Positions :" + " X: " + Double.toString(pos[0]) + " ,Y: " + Double.toString(pos[1]);
        double[] next = this.getNext();
        msg[7] = "Next :" + " NexX: " + Double.toString(next[0]) + " ,Y:" + Double.toString(next[1]);
        return msg;
    }

}
