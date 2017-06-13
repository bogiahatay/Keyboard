package com.vinsofts.keyborad.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.base.BaseActivity;
import com.vinsofts.keyborad.utils.AppUtils;
import com.vinsofts.keyborad.utils.GlobalFunction;
import com.vinsofts.keyborad.widget.TestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    View tv_title;
    @BindView(R.id.switch_sound)
    Switch switchSound;
    @BindView(R.id.switch_vibrate)
    Switch switchVibrate;
    @BindView(R.id.switch_extra_row)
    Switch switchExtraRow;
    @BindView(R.id.switch_popup)
    Switch switchPopup;
    @BindView(R.id.edt_demo)
    EditText edtDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        isHideKeyboard = false;

        boolean vibrate = GlobalFunction.getVibrate(mActivity);
        boolean sounds = GlobalFunction.getSounds(mActivity);
        boolean extraRow = GlobalFunction.getExtraRow(mActivity);
        boolean popup = GlobalFunction.getPopup(mActivity);
        switchVibrate.setChecked(vibrate);
        switchSound.setChecked(sounds);
        switchExtraRow.setChecked(extraRow);
        switchPopup.setChecked(popup);

        switchVibrate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalFunction.putVibrate(mActivity, isChecked);

        });
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalFunction.putSounds(mActivity, isChecked);
        });
        switchExtraRow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalFunction.putExtraRow(mActivity, isChecked);
        });
        switchPopup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalFunction.putPopup(mActivity, isChecked);
        });
        edtDemo.requestFocus();
        AppUtils.showKeyboard(mActivity, edtDemo);
    }

    @OnClick({R.id.btn_sound, R.id.btn_vibrate, R.id.btn_extra_row, R.id.btn_popup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sound:
                break;
            case R.id.btn_vibrate:
                break;
        }
    }


}
