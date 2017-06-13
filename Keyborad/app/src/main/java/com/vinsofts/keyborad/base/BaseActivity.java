package com.vinsofts.keyborad.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.utils.AppUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ha on 3/13/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected BaseActivity mActivity;
    protected SweetAlertDialog progressDialog;
    protected boolean anim = true;
    protected boolean isHideKeyboard = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
//        Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(this));
    }


    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void startActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void startActivityNotAnim(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void startActivityNotAnim(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); ;
    }
    public void startActivityResult(Class<?> clz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, requestCode, bundle);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishing() && anim) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog(boolean show) {
        if (progressDialog == null) {
            progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            progressDialog.setTitleText("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }





    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isHideKeyboard) {
            return super.dispatchTouchEvent(ev);
        }
        boolean handleReturn = super.dispatchTouchEvent(ev);
        View view = getCurrentFocus();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (view instanceof EditText) {
            View innerView = getCurrentFocus();
            if (ev.getAction() == MotionEvent.ACTION_UP && !getLocationOnScreen(innerView).contains(x, y)) {
                AppUtils.hideSoftKeyboard(mActivity);
            }
        }
        return handleReturn;
    }


    protected Rect getLocationOnScreen(View mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    public void showToast(int id) {
        Toast.makeText(mActivity, id, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String mes) {
        Toast.makeText(mActivity, mes, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(int id) {
        Snackbar.make(mActivity.getWindow().getDecorView(), id, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(String mes) {
        Snackbar.make(mActivity.getWindow().getDecorView(), mes, Snackbar.LENGTH_SHORT).show();
    }

    public int getColorRes(int id) {
        return ContextCompat.getColor(mActivity, id);
    }

    public Drawable getDrawableRes(int id) {
        return ContextCompat.getDrawable(mActivity, id);
    }


}
