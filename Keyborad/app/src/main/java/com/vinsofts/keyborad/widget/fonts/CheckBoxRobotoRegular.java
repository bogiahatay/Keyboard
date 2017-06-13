package com.vinsofts.keyborad.widget.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

public class CheckBoxRobotoRegular extends AppCompatCheckBox {

    public CheckBoxRobotoRegular(Context context) {
        super(context);
        init(context);
    }

    public CheckBoxRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckBoxRobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        this.setTypeface(face);
    }

}
