package com.chinausky.lanbowan.evideo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipManager;
import com.evideo.voip.EvideoVoipPreferences;
import com.evideo.voip.core.EvideoVoipCore;
import com.evideo.voip.core.EvideoVoipCore.EcCalibratorStatus;
import com.evideo.voip.core.EvideoVoipCoreException;
import com.evideo.voip.core.EvideoVoipCoreListenerBase;

import com.chinausky.lanbowan.R;

public class SettingActivity extends FragmentActivity implements OnClickListener {

    private EvideoVoipPreferences mPres;

    private EditText mEchoDelay;
    private EvideoVoipCoreListenerBase mListener;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting);

        mPres = EvideoVoipPreferences.instance();

        findViewById(R.id.settingExit).setOnClickListener(this);
        findViewById(R.id.settingSave).setOnClickListener(this);

        mListener = new EvideoVoipCoreListenerBase(){
            @Override
            public void ecCalibrationStatus(EvideoVoipCore lc, final EcCalibratorStatus status, final int delayMs, Object data) {
                EvideoVoipManager.getInstance().routeAudioToReceiver();

                CheckBox echoCheck = (CheckBox) findViewById(R.id.settingEchoEnable);
                if (mEchoDelay == null)
                    mEchoDelay = (EditText) findViewById(R.id.settingEchoDelayms);

                mEchoDelay.setText(String.valueOf(EvideoVoipFunctions
                        .setEchoCalibration(status, delayMs)));
                echoCheck.setChecked(true);
            }
        };

        CheckBox echoCheck = (CheckBox) findViewById(R.id.settingEchoEnable);
        echoCheck.setChecked(mPres.isEchoCancellationEnabled());
        echoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPres.setEchoCancellation(isChecked);

                if (isChecked)
                    try {
                        EvideoVoipManager.getInstance().startEcCalibration(mListener);
                        if (mEchoDelay == null)
                            mEchoDelay = (EditText) findViewById(R.id.settingEchoDelayms);
                        mEchoDelay.setText("calibrating...");
                    } catch (EvideoVoipCoreException e) {
                        e.printStackTrace();
                    }
            }
        });

        mEchoDelay = (EditText) findViewById(R.id.settingEchoDelayms);
        mEchoDelay.setText(String.valueOf(mPres.getEchoCalibration()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.settingExit == id) {
            finish();
        } else if (R.id.settingSave == id) {
            int delayms = Integer.parseInt(mEchoDelay.getEditableText().toString());
            mPres.setEchoCalibration(delayms);
        }
    }

}
