package com.vinsofts.keyborad.widget.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextViewRobotoLight extends AppCompatTextView {

    public TextViewRobotoLight(Context context) {
        super(context);
        init(context);
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        this.setTypeface(face);
    }

}
