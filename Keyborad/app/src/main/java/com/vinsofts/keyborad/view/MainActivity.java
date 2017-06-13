package com.vinsofts.keyborad.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.base.BaseActivity;
import com.vinsofts.keyborad.utils.DeviceUtils;
import com.vinsofts.keyborad.utils.GlobalFunction;
import com.vinsofts.keyborad.utils.IntentUtils;
import com.vinsofts.keyborad.utils.MLog;
import com.vinsofts.keyborad.widget.HeartFlyAnimation;
import com.vinsofts.keyborad.widget.ItemGift;
import com.vinsofts.keyborad.widget.fonts.TextViewRobotoBold;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_step)
    TextViewRobotoBold tvStep;
    @BindView(R.id.v_step_one)
    LinearLayout vStepOne;
    @BindView(R.id.v_step_two)
    LinearLayout vStepTwo;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initReceiver();
        GlobalFunction.putWidth(mActivity, DeviceUtils.getScreenWidth(mActivity));
        GlobalFunction.putHeight(mActivity, DeviceUtils.getScreenHeight(mActivity));
        System.out.println(Math.ceil((double) ('g' - 8 * 22)));
//        new CountDownTimer(60000, 500) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
//
//                HeartFlyAnimation.addScreenIdol(rootView);
//                ItemGift itemGift = new ItemGift(mActivity, R.drawable.ic_x, "Ha");
//                rootView.addView(itemGift);
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkKeyBoard();
    }

    private void checkKeyBoard() {
        if (checkEnableMyKey()) {
            if (checkIsSelectMyKey()) {
                startActivity(SettingsActivity.class);
                finish();
            } else {
                tvStep.setText(R.string.step_two);
                vStepOne.setVisibility(View.GONE);
                vStepTwo.setVisibility(View.VISIBLE);
            }
        } else {
            tvStep.setText(R.string.step_one);
            vStepOne.setVisibility(View.VISIBLE);
            vStepTwo.setVisibility(View.GONE);
        }
    }

    private boolean checkEnableMyKey() {
        String myKey = mActivity.getPackageName() + "/.key.SoftKeyboard";
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list = imm.getEnabledInputMethodList();
        for (int i = 0; i < list.size(); i++) {
            MLog.e(list.get(i).getPackageName());
            MLog.e(list.get(i).getId());
            MLog.e(list.get(i).getServiceInfo().toString());
            MLog.e(list.get(i).getServiceName());
            MLog.e(list.get(i).getSettingsActivity());
            if (list.get(i).getId().equalsIgnoreCase(myKey)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIsSelectMyKey() {
        String defaultKey = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        String myKey = mActivity.getPackageName() + "/.key.SoftKeyboard";
        if (defaultKey.equalsIgnoreCase(myKey)) {
            return true;
        } else {
            return false;
        }
    }

    private void initReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_INPUT_METHOD_CHANGED)) {
                    if (checkIsSelectMyKey()) {
                        startActivity(SettingsActivity.class);
                        finish();
                    } else {
                        tvStep.setText(R.string.step_two);
                        vStepOne.setVisibility(View.GONE);
                        vStepTwo.setVisibility(View.VISIBLE);
                    }

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MLog.e("onActivityResult");
        switch (requestCode) {
            case 0:
                checkKeyBoard();
                break;
        }

    }


    @OnClick({R.id.btn_enable, R.id.btn_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_enable:
                IntentUtils.showEnableKeyboard(mActivity, 0);
                break;
            case R.id.btn_choose:
                IntentUtils.showChooseKeyboard(mActivity);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
