package com.chinausky.lanbowan.evideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.GetMyResidenceInfo;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.evideo.call.InCallActivity;
import com.chinausky.lanbowan.evideo.call.IncomingCallActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipManager;
import com.evideo.voip.EvideoVoipPreferences;
import com.evideo.voip.core.EvideoVoipCall;
import com.evideo.voip.core.EvideoVoipCall.State;
import com.evideo.voip.core.EvideoVoipChatMessage;
import com.evideo.voip.core.EvideoVoipChatRoom;
import com.evideo.voip.core.EvideoVoipCore;
import com.evideo.voip.core.EvideoVoipCore.RegistrationState;
import com.evideo.voip.core.EvideoVoipCoreException;
import com.evideo.voip.core.EvideoVoipCoreListenerBase;
import com.evideo.voip.core.EvideoVoipProxyConfig;
import com.evideo.voip.core.Reason;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EvideoMainActivity extends FragmentActivity implements
        View.OnClickListener {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, EvideoMainActivity.class);
        activity.startActivity(intent);
    }


    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    public static final String TAG = EvideoMainActivity.class.getCanonicalName();

    private TextView mState;
    private TextView mCurSipNum;
    private AddressText mAddress;
    private EvideoVoipCoreListenerBase mListener;

    public static RegistrationState mRegistrationState = RegistrationState.RegistrationNone;

    private Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        if (!EvideoVoipManager.isInstanciated()) {
            Log.e(TAG, "No service running: avoid crash by starting the launcher, "
                    + this.getClass().getName());
            finish();
            return;
        }

        initView(savedInstanceState);
        onEvideoVoipManagerInstanciated();
    }

    private void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_evideo_main);
        ButterKnife.bind(this);
        mTitleTv.setText("门禁卡");
//        mAddress = (AddressText) findViewById(R.id.inputAddressText);

//        findViewById(R.id.accountManagerButton).setOnClickListener(this);
//        findViewById(R.id.backButton).setOnClickListener(this);
//        findViewById(R.id.settingButton).setOnClickListener(this);
        findViewById(R.id.callButton).setOnClickListener(this);
//        findViewById(R.id.unlockButton).setOnClickListener(this);

        mState = (TextView) findViewById(R.id.registrationStateTextView);
//        mCurSipNum = (TextView) findViewById(R.id.currentSipNumTextView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        int stateID = SampleUtils.switchRegistrationState(RegistrationState.RegistrationNone);
        if (EvideoVoipFunctions.getEVCore().getDefaultProxyConfig() != null) {
            stateID = SampleUtils.switchRegistrationState(EvideoVoipFunctions.getEVCore()
                    .getDefaultProxyConfig().getState());
        }
        mState.setText(stateID);

        //check the current call and then resume call activity.
        EvideoVoipFunctions.getInstance().resumeCurrentCall(this,
                IncomingCallActivity.class, InCallActivity.class);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvideoVoipFunctions.removeEVCoreListener(mListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view) {
        int id = view.getId();
//        if (R.id.accountManagerButton == id) {
//            startActivity(new Intent(this, AccountManageActivity.class));
//        }
//        else if (R.id.backButton == id) {
//            finish();
//        } else
        if (R.id.callButton == id) {
            final Register register = MyApplication.getInstance().getRegister();

            if (register.getToken() == null) {
                return;
            }

            String token = register.getToken();
            Call<GetMyResidenceInfo> myResidenceInfoCall = Api.getMyResidenceApi().onResult("Bearer " + token);
            myResidenceInfoCall.enqueue(new Callback<GetMyResidenceInfo>() {
                @Override
                public void onResponse(Response<GetMyResidenceInfo> response, Retrofit retrofit) {
                    String callNum = response.body().getStarnetElevatorAccount();
                    if (callNum != null && !callNum.isEmpty())
                        callNum(callNum);
                    else
                        Toast.makeText(EvideoMainActivity.this, R.string.home_input_hint, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
//        else if (R.id.settingButton == id) {
//            startActivity(new Intent(this, SettingActivity.class));
//        }
    }

    private void callNum(String num) {
        Log.d(TAG, "call num = " + num);
        if (!TextUtils.isEmpty(num))
            EvideoVoipFunctions.getInstance().makeCall(num, "", true);
    }

    protected void onEvideoVoipManagerInstanciated() {
//        EvideoVoipFunctions.setActivityToLaunchOnIncomingReceived(EvideoMainActivity.class);
        mListener = new EvideoVoipCoreListenerBase() {

            @Override
            public void registrationState(EvideoVoipCore evCore,
                                          EvideoVoipProxyConfig proxy,
                                          EvideoVoipCore.RegistrationState state, String smessage) {
                mRegistrationState = state;
                final int stateID = SampleUtils.switchRegistrationState(state);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mState.setText(stateID);
                    }
                });
            }

            @Override
            public void messageReceived(EvideoVoipCore evCore, EvideoVoipChatRoom cr,
                                        EvideoVoipChatMessage message) {
                Log.d(TAG, "recv message : " + message.getText());
                super.messageReceived(evCore, cr, message);
            }

            @Override
            public void callState(EvideoVoipCore evCore, EvideoVoipCall call,
                                  EvideoVoipCall.State state, String message) {
                Log.d(TAG, "call state = " + state.toString() + ", message = "
                        + message + ", reason = "
                        + call.getErrorInfo().getReason());

                if (state == State.IncomingReceived) {
                    if (evCore.getCurrentCall() != null &&
                            evCore.getCurrentCall() != call/*IncomingCallActivity.isInstanciated()
                            || InCallActivity.isInstanciated()*/) {
                        evCore.terminateCall(call);
                        return;
                    } else {
                        startActivity(new Intent(EvideoMainActivity.this,
                                IncomingCallActivity.class));
                    }

                }

                if (evCore.getCurrentCall() != null &&
                        evCore.getCurrentCall() != call) {
                    Log.d(TAG, "Callstate is not from current call, exit.");
                    return;
                }

                if (state == State.OutgoingInit) // received
                {
                    startActivity(new Intent(EvideoMainActivity.this,
                            InCallActivity.class));
                } else if (state == State.CallEnd || state == State.Error
                        || state == State.CallReleased) {
                    if (!InCallActivity.isInstanciated()
                            && !IncomingCallActivity.isInstanciated()) {

                        if (call.getErrorInfo().getReason() == Reason.Busy
                                || call.getErrorInfo().getReason() == Reason.Declined) {
                            Log.d(TAG, "call reason : " +
                                    getResources().getString(R.string.call_incall_prompt_device_busy));
                            Toast.makeText(getApplicationContext(),
                                    R.string.call_incall_prompt_device_busy,
                                    Toast.LENGTH_SHORT).show();
                        } else if (call.getErrorInfo().getReason() == Reason.NotFound
                                || call.getErrorInfo().getReason() == Reason.IOError
                                || call.getErrorInfo().getReason() == Reason.NoResponse) {
                            Log.d(TAG, "call reason : "
                                    + getResources().getString(R.string.call_incall_prompt_device_not_found));
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.call_incall_prompt_device_not_found,
                                    Toast.LENGTH_SHORT).show();
                        } else if (call.getErrorInfo().getReason() == Reason.None) {
                            Log.d(TAG, "incall ended normally.");
                        } else {
                            Log.d(TAG, "call reason : "
                                    + call.getErrorInfo().getReason().toString());
                            Toast.makeText(getApplicationContext(),
                                    call.getErrorInfo().getReason().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    Log.d(TAG, "Callstate finish current call.");
                    try {
                        if (InCallActivity.isInstanciated())
                            InCallActivity.instance().finish();
                        if (IncomingCallActivity.isInstanciated())
                            IncomingCallActivity.instance().finish();
                    } catch (IllegalArgumentException e) {
                        Log.d(TAG, "CallActivity finish error.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void ecCalibrationStatus(EvideoVoipCore evCore, final EvideoVoipCore.EcCalibratorStatus status, final int delayMs, Object data) {
                EvideoVoipFunctions.setEchoCalibration(status, delayMs);
            }
        };

        EvideoVoipFunctions.addEVCoreListener(mListener);

        try {
            if (EvideoVoipPreferences.instance().getEchoCalibration() < 0
                    && !EvideoVoipPreferences.instance().isEchoCancellationEnabled()) {
                EvideoVoipManager.getInstance().startEcCalibration(mListener);
            }
        } catch (EvideoVoipCoreException e1) {
            e1.printStackTrace();
        }
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
