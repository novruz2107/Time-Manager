package com.antech.timemanagement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Novruz Engineer on 3/31/2018.
 */

public class CustomClock extends View {
    private final float x;
    private final float y;
    private final int r=90;
    private int minutes;
    private int seconds;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomClock(Context context, float x, float y, int minutes, int seconds) {
        super(context);
        this.x = x;
        this.y = y;
        this.minutes=minutes;
        this.seconds=seconds;
        this.setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, r, mPaint);
        float sec=(float)seconds;
        float min=(float)minutes;
        mPaint.setColor(0xFF0000FF);
        canvas.drawLine(x, y, (float)(x+r*Math.cos(Math.toRadians((min / 60.0f * 360.0f)-90f))), (float)(y+r*Math.sin(Math.toRadians((min / 60.0f * 360.0f)-90f))), mPaint);
        canvas.save();
        mPaint.setColor(0xFFA2BC13);
        canvas.drawLine(x, y, (float)(x+(r+10)*Math.cos(Math.toRadians((sec / 60.0f * 360.0f)-90f))), (float)(y+(r+15)*Math.sin(Math.toRadians((sec / 60.0f * 360.0f)-90f))), mPaint);
        canvas.save();
    }
}