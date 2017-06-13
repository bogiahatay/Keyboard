package com.vinsofts.keyborad.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vinsofts.keyborad.R;

public class GodImageView extends AppCompatImageView {

    private float radius = 0.1f;
    private Path path;
    private RectF rect;

    private String type = "";
    private boolean topcrop = false;
    private Paint mPaint;

    public GodImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GodImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        rect = new RectF(0, 0, 0, 0);
        path = new Path();

//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.BLUE);
//        mPaint.setStrokeWidth(10);
//        mPaint.setStyle(Paint.Style.FILL);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GodImageView);
        type = a.getString(R.styleable.GodImageView_gtv_type);
//        strokeColor = a.getColor(R.styleable.GodImageView_gtv_border_color, Color.GRAY);
        radius = a.getDimensionPixelSize(R.styleable.GodImageView_gtv_border_width, 1);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        rect.top = 0;
//        rect.left = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();

//        canvas.drawRoundRect(rect, radius, radius, mPaint);
//
//        rect.right = getWidth() - 2;
//        rect.bottom = getHeight() - 2;
//        rect.top = 2;
//        rect.left = 2;

        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);

        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!type.isEmpty()) {
            if (type.equalsIgnoreCase("1")) {
                int width = getMeasuredWidth();
                setMeasuredDimension(width, width);
            }
            if (type.equalsIgnoreCase("2")) {
                int width = getMeasuredWidth();
                int height = Math.round(width * 9 / 16);
                setMeasuredDimension(width, height);
            }
            if (type.equalsIgnoreCase("3")) {
                int width = getMeasuredWidth();
                int height = Math.round(width * 3 / 4);
                setMeasuredDimension(width, height);
            }
        }
    }
}