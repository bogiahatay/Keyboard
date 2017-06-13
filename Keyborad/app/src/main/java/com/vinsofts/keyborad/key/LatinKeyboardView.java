/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vinsofts.keyborad.key;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.utils.DeviceUtils;
import com.vinsofts.keyborad.utils.GlobalFunction;
import com.vinsofts.keyborad.utils.MLog;

import java.util.List;

public class LatinKeyboardView extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    // TODO: Move this into android.inputmethodservice.Keyboard
    static final int KEYCODE_LANGUAGE_SWITCH = -101;


    private Context context;
    public boolean isOnecaps = false;
    public boolean isNumber = false;
    private PopupWindow popup;
    private TextView tvTitle;
    private int width;
    private RectF rect;
    private Path path;
    private boolean pressed = false;
    private int defaultMargin;
    private Paint paint;
    private TextPaint paintText;
    public int keyPress = -9999;

    public LatinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = GlobalFunction.getWidth(context);
        this.context = context;
        init();
    }


    public LatinKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        width = GlobalFunction.getWidth(context);
        this.context = context;
        init();

    }

    private void init() {
        defaultMargin = DeviceUtils.convertDpToPixel(context, 20);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(width / 35);
        paint.setColor(Color.GRAY);

        paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(width / 23);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.BLACK);


        rect = new RectF(0, 0, 0, 0);
        path = new Path();

        View custom = LayoutInflater.from(context).inflate(R.layout.popup_layout_more, new FrameLayout(context));
        tvTitle = (TextView) custom.findViewById(R.id.tv_title);
        popup = new PopupWindow(context);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setContentView(custom);
    }

    private void showPopup(Key key) {
        popup.dismiss();
        if (key.label != null) {
            tvTitle.setText(key.popupCharacters);
            popup.showAtLocation(this, Gravity.NO_GRAVITY, key.x, key.y);
        }
    }


    @Override
    protected boolean onLongPress(Key key) {
        MLog.e("onLongPress");
//        if (key.codes[0] == Keyboard.KEYCODE_CANCEL) {
//            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
//            return true;
//        /*} else if (key.codes[0] == 113) {
//
//            return true; */
//        } else {
//            //Log.d("LatinKeyboardView", "KEY: " + key.codes[0]);
//        }
        return super.onLongPress(key);
    }

    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        final LatinKeyboard keyboard = (LatinKeyboard) getKeyboard();
        //keyboard.setSpaceIcon(getResources().getDrawable(subtype.getIconResId()));
        invalidateAllKeys();
    }


    @Override
    public boolean onTouchEvent(MotionEvent me) {
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                this.invalidate();
                break;
        }
        return super.onTouchEvent(me);

    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);


        List<Key> keys = getKeyboard().getKeys();
        boolean extraRow = GlobalFunction.getExtraRow(context);
        for (Key key : keys) {
            if (key.label != null) {
                if (key.label.equals("q")) {
                    setText(canvas, extraRow ? "~" : "1", key, defaultMargin, paint);
                } else if (key.label.equals("w")) {
                    setText(canvas, extraRow ? "`" : "2", key, defaultMargin, paint);
                } else if (key.label.equals("e")) {
                    setText(canvas, extraRow ? "|" : "3", key, defaultMargin, paint);
                } else if (key.label.equals("r")) {
                    setText(canvas, extraRow ? "•" : "4", key, defaultMargin, paint);
                } else if (key.label.equals("t")) {
                    setText(canvas, extraRow ? "√" : "5", key, defaultMargin, paint);
                } else if (key.label.equals("y")) {
                    setText(canvas, extraRow ? "π" : "6", key, defaultMargin, paint);
                } else if (key.label.equals("u")) {
                    setText(canvas, extraRow ? "÷" : "7", key, defaultMargin, paint);
                } else if (key.label.equals("i")) {
                    setText(canvas, extraRow ? "×" : "8", key, defaultMargin, paint);
                } else if (key.label.equals("o")) {
                    setText(canvas, extraRow ? "¶" : "9", key, defaultMargin, paint);
                } else if (key.label.equals("p")) {
                    setText(canvas, extraRow ? "∆" : "0", key, defaultMargin, paint);
                } else if (key.label.equals("a")) {
                    setText(canvas, "@", key, defaultMargin, paint);
                } else if (key.label.equals("s")) {
                    setText(canvas, "#", key, defaultMargin, paint);
                } else if (key.label.equals("d")) {
                    setText(canvas, "$", key, defaultMargin, paint);
                } else if (key.label.equals("f")) {
                    setText(canvas, "%", key, defaultMargin, paint);
                } else if (key.label.equals("g")) {
                    setText(canvas, "&", key, defaultMargin, paint);
                } else if (key.label.equals("h")) {
                    setText(canvas, "-", key, defaultMargin, paint);
                } else if (key.label.equals("j")) {
                    setText(canvas, "+", key, defaultMargin, paint);
                } else if (key.label.equals("k")) {
                    setText(canvas, "(", key, defaultMargin, paint);
                } else if (key.label.equals("l")) {
                    setText(canvas, ")", key, defaultMargin, paint);
                } else if (key.label.equals("z")) {
                    setText(canvas, "*", key, defaultMargin, paint);
                } else if (key.label.equals("x")) {
                    setText(canvas, "\"", key, defaultMargin, paint);
                } else if (key.label.equals("c")) {
                    setText(canvas, "'", key, defaultMargin, paint);
                } else if (key.label.equals("v")) {
                    setText(canvas, ":", key, defaultMargin, paint);
                } else if (key.label.equals("b")) {
                    setText(canvas, ";", key, defaultMargin, paint);
                } else if (key.label.equals("n")) {
                    setText(canvas, "!", key, defaultMargin, paint);
                } else if (key.label.equals("m")) {
                    setText(canvas, "?", key, defaultMargin, paint);
                }
            }
//            if (key.label != null) {
//                paintText.setTextSize(60);
//                Drawable dr = null;
//                paintText.setColor(Color.BLACK);
//                dr = ContextCompat.getDrawable(context, R.drawable.btn_keyboard_key_ios8);
//                dr.setBounds(key.x, key.y + defaultMargin, key.x + key.width, key.y + key.height + defaultMargin);
//                dr.draw(canvas);
//
//                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + 18 + defaultMargin, paintText);
//            }
            if (key.codes[0] == -5 || key.codes[0] == 95 || key.codes[0] == 47 ||
                    key.codes[0] == -1 || key.codes[0] == -2 ||
                    (key.codes[0] == 46 && !isNumber) || (key.codes[0] == 45 && isNumber) ||
                    key.codes[0] == 10 || key.codes[0] == 44 || key.codes[0] == 60 ||
                    key.codes[0] == 62 || key.codes[0] == 8230) {
                Drawable dr = null;
                MLog.e("pressed : " + pressed);
                if (key.codes[0] == 10 && key.icon == null) {
                    if (pressed && keyPress != 0 && keyPress == key.codes[0]) {
                        paintText.setColor(Color.BLACK);
                        dr = ContextCompat.getDrawable(context, R.drawable.btn_keyboard_key_light_normal_ios8);
                    } else {
                        paintText.setColor(Color.WHITE);
                        dr = ContextCompat.getDrawable(context, R.drawable.btn_keyboard_key_ios8_action);
                    }
                } else {
                    if (pressed && keyPress != 0&& keyPress == key.codes[0]) {
                        paintText.setColor(Color.BLACK);
                        dr = ContextCompat.getDrawable(context, R.drawable.btn_keyboard_key_light_normal_ios8);
                    } else {
                        paintText.setColor(Color.BLACK);
                        dr = ContextCompat.getDrawable(context, R.drawable.btn_keyboard_key_ios8_dark);
                    }
                }
                dr.setBounds(key.x, key.y + defaultMargin, key.x + key.width, key.y + key.height + defaultMargin);
                dr.draw(canvas);

                if (key.label != null) {
                    canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + width / 50 + defaultMargin, paintText);
                }

                if (key.icon != null) {
                    if (key.codes[0] == -1) {
                        if (isShifted()) {


//                            if (isOnecaps){
//                                key.icon = ContextCompat.getDrawable(context, R.drawable.sym_keyboard_shift_locked_ios8_full);
//                            }else {
//                            }
                            key.icon = ContextCompat.getDrawable(context, R.drawable.sym_keyboard_shift_locked_ios8);
                        } else {
                            key.icon = ContextCompat.getDrawable(context, R.drawable.sym_keyboard_shift_ios8);
                        }
                    }
                    key.icon.setBounds(
                            key.x + key.width / 2 - key.icon.getIntrinsicWidth() / 2,
                            key.y + key.height / 2 - key.icon.getIntrinsicHeight() / 2 + defaultMargin,
                            key.x + key.icon.getIntrinsicWidth() + key.width / 2 - key.icon.getIntrinsicWidth() / 2,
                            key.y + key.icon.getIntrinsicHeight() + key.height / 2 - key.icon.getIntrinsicHeight() / 2 + defaultMargin);
                    key.icon.draw(canvas);
                }

            }
        }

    }

    private void setText(Canvas canvas, String s, Key key, int defaultMargin, Paint paint) {
        canvas.drawText(s, key.x + (key.width - width / 40), key.y + width / 25 + defaultMargin, paint);
    }
}
