package com.vinsofts.keyborad.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.widget.fonts.TextViewRobotoRegular;

public class CircularTextView extends TextViewRobotoRegular {
    private float strokeWidth;
    private int strokeColor, solidColor;

    public CircularTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getDimensionPixelSize(R.styleable.CircularTextView_ctv_border_width, 1);

        strokeColor = a.getColor(R.styleable.CircularTextView_ctv_border_color, Color.GRAY);

        solidColor = a.getColor(R.styleable.CircularTextView_ctv_fill_color, Color.WHITE);

        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius - strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp * scale;

    }

    public void setStrokeColor(String color) {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color) {
        solidColor = Color.parseColor(color);

    }
//    textViewCircle.setSolidColor("#ffffff");
//    textViewCircle.setStrokeWidth(1);
//    textViewCircle.setStrokeColor("#9E9E9E");
}