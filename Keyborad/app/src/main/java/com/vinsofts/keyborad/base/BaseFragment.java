package com.vinsofts.keyborad.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.vinsofts.keyborad.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ha on 3/13/2017.
 */

public class BaseFragment extends Fragment {
    protected Activity mActivity;
    private SweetAlertDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    protected void startActivity(Class<?> clz) {
        Intent intent = new Intent(mActivity, clz);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    protected void startActivityResult(Class<?> clz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(mActivity, clz);
        if (bundle != null) intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, requestCode, bundle);
        mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    protected void startActivityResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(mActivity, clz);
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void showProgressDialog(boolean show) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showProgressDialog(show);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void showSnackBar(int id) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showSnackBar(id);
        }
    }

    public void showSnackBar(String mes) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showSnackBar(mes);
        }
    }


    public void showToast(int id) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showToast(id);
        }
    }

    public void showToast(String mes) {
        ((BaseActivity) mActivity).showToast(mes);
    }
}
