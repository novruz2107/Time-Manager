package com.antech.timemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Novruz Engineer on 4/22/2018.
 */

public class Ring extends View {
    private RectF mDrawable;
    private RectF center;
    private Paint mPaint;
    private Paint center_paint;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private float angle;
    private CountDownTimer pomodoroTimer;
    private CountDownTimer shortBreakTimer;
    private CountDownTimer longBreakTimer;
    private long targetTimeInMilliSeconds;
    private long shortTime;
    private long longTime;
    int flag = 0;
    boolean onOff;

    public Ring (Context context){
        super(context, null);
        setWillNotDraw(false);
        init();
    }

    public Ring(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        center_paint = new Paint(Paint.ANTI_ALIAS_FLAG);


        onOff=true;



        mPaint.setColor(Color.argb(255, 115, 242, 49));
        paint1.setColor(Color.GRAY);
        paint2.setColor(Color.GRAY);
        paint3.setColor(Color.GRAY);
        center_paint.setColor(Color.argb(255, 100, 172, 196));
        init();



    }


    void init(){
        SharedPreferences prefs = getContext().getSharedPreferences("Files", Context.MODE_PRIVATE);
        targetTimeInMilliSeconds=(prefs.getInt("number1", 25))*60*1000;
        shortTime = (prefs.getInt("number2", 5))*60*1000;
        longTime = (prefs.getInt("number3", 12))*60*1000;


        pomodoroTimer = new CountDownTimer(targetTimeInMilliSeconds, 1000){
            public void onTick (long milliSecondsUntilDone){

                if(flag%3==0 && onOff){
                    paint1.setColor(Color.GREEN);
                }else if(flag%3==0 && !onOff){
                    paint1.setColor(Color.GRAY);
                }if(flag%3==1 && onOff){
                    paint2.setColor(Color.GREEN); paint1.setColor(Color.GREEN);
                }else if(flag%3==1 && !onOff){
                    paint2.setColor(Color.GRAY); paint1.setColor(Color.GREEN);
                }if(flag%3==2 && onOff){
                    paint3.setColor(Color.GREEN); paint1.setColor(Color.GREEN); paint2.setColor(Color.GREEN);
                }else if(flag%3==2 && !onOff){
                    paint3.setColor(Color.GRAY); paint1.setColor(Color.GREEN); paint2.setColor(Color.GREEN);
                }



                if(MainActivity.isWorking) {
                    setAngle((Long.valueOf(milliSecondsUntilDone).floatValue()) / ((Long.valueOf(targetTimeInMilliSeconds)).floatValue()) * 360);
                    if(onOff){
                        onOff=false;
                    }else{
                        onOff=true;
                    }
                    invalidate();
                }else{
                    pomodoroTimer.cancel();
                }
            }

            public void onFinish(){
                if(flag%3==0){
                    paint1.setColor(Color.GREEN);
                }else if(flag%3==1){
                    paint1.setColor(Color.GREEN); paint2.setColor(Color.GREEN);
                }else if(flag%3==2){
                    paint1.setColor(Color.GREEN); paint2.setColor(Color.GREEN); paint3.setColor(Color.GREEN);
                }
                flag++;
                center_paint.setColor(Color.argb(255, 204, 255, 204));
                if(flag % 3 !=0) {
                    shortBreakTimer.start();
                }
                else{
                    longBreakTimer.start();
                }

            }
        }.start();
        shortBreakTimer = new CountDownTimer(shortTime, 1000) {
            @Override
            public void onTick(long milliSecondsUntilDone) {
                setAngle ((Long.valueOf(milliSecondsUntilDone).floatValue())/((Long.valueOf(shortTime)).floatValue())*360);
                invalidate();
            }

            @Override
            public void onFinish() {
                center_paint.setColor(Color.argb(255, 208, 208, 208));
                pomodoroTimer.start();

            }
        };

        longBreakTimer = new CountDownTimer(longTime, 1000) {
            @Override
            public void onTick(long milliSecondsUntilDone) {
                setAngle ((Long.valueOf(milliSecondsUntilDone).floatValue())/((Long.valueOf(longTime)).floatValue())*360);
                invalidate();
            }

            @Override
            public void onFinish() {
                center_paint.setColor(Color.argb(255, 208, 208, 208));
                pomodoroTimer.start();

            }
        };
    }

    protected void dispatchDraw (Canvas canvas){
        float x = MainActivity.textView.getX()-40;
        float y = MainActivity.textView.getY()-((MainActivity.textView.getWidth()+65)/2)+MainActivity.textView.getHeight()/2;
        float width = x+MainActivity.textView.getWidth()+80;
        float height = y+MainActivity.textView.getWidth()+80;
        canvas.drawArc(x, y, width, height, -90, angle, true, mPaint);
        canvas.drawArc(x+10, y+10, width-10, height-10, 0, 360, true, center_paint);
        canvas.drawRect((MainActivity.textView.getX()+MainActivity.textView.getWidth()/2)-40, MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, 20+(MainActivity.textView.getX()+MainActivity.textView.getWidth()/2)-40, 7+MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, paint1);
        canvas.drawRect((MainActivity.textView.getX()+MainActivity.textView.getWidth()/2), MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, 20+(MainActivity.textView.getX()+MainActivity.textView.getWidth()/2), 7+MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, paint2);
        canvas.drawRect((MainActivity.textView.getX()+MainActivity.textView.getWidth()/2)+40, MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, 20+(MainActivity.textView.getX()+MainActivity.textView.getWidth()/2)+40, 7+MainActivity.textView.getY()+MainActivity.textView.getHeight()+200, paint3);
    }

    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }

    public float getAngle() {
        return angle;
    }
}
