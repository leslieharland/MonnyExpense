package com.example.leslie.monnyfree.common;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


public class ProgressSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    public ProgressSeekBar (Context context) {
        super(context);
    }

    public ProgressSeekBar (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProgressSeekBar (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        Paint paint = new Paint();
        int xPos = this.getWidth() / 2;
        int yPos = (int) ((this.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        c.drawText(""+this.getProgress() + "%", xPos, yPos, paint);
    }
}