package com.chinausky.lanbowan.evideo.call;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipManager;
import com.evideo.voip.EvideoVoipUtils;
import com.evideo.voip.core.EvideoVoipAddress;
import com.evideo.voip.core.EvideoVoipCall;
import com.evideo.voip.core.EvideoVoipCall.State;
import com.evideo.voip.core.EvideoVoipCore;
import com.evideo.voip.core.EvideoVoipCoreListenerBase;

import com.chinausky.lanbowan.R;

public class IncomingCallActivity extends FragmentActivity implements OnClickListener {

    private final static String TAG = IncomingCallActivity.class.getCanonicalName();
    private static IncomingCallActivity instance;

    private TextView mNameView, mNameViewMiddle;
    private String mDisplayName;
    private ImageView mPictureView;
    private EvideoVoipCall mCall;

    private EvideoVoipCoreListenerBase mListener;
    private Handler mHandler = new Handler();

    public static IncomingCallActivity instance() {
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_call_incoming);

        findViewById(R.id.incomingInfo).setVisibility(View.GONE);
        mNameView = (TextView) findViewById(R.id.nameOrNumberTextView);
        mNameViewMiddle = (TextView) findViewById(R.id.incomingNameTextView);
        mPictureView = (ImageView) findViewById(R.id.incomingPortraitImageView);

        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        findViewById(R.id.incomingVideoAnswerButton).setOnClickListener(this);
        findViewById(R.id.incomingAudioAnswerButton).setOnClickListener(this);
        findViewById(R.id.incomingRejectButton).setOnClickListener(this);

        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mListener = new EvideoVoipCoreListenerBase(){

            @Override
            public void callState(EvideoVoipCore evCore, EvideoVoipCall call, EvideoVoipCall.State state, String message) {
                Log.d(TAG, "call state = " + state.toString() + ", message = " + message );
                if (call == mCall && State.CallEnd == state) {
                    Log.d(TAG, "incoming call ended.");
                    finish();
                }
            }
        };

        EvideoVoipFunctions.addEVCoreListener(mListener);

        // Only one call ringing at a time is allowed
        if (EvideoVoipFunctions.getEVCore() != null) {
            mCall = EvideoVoipFunctions.getEVCore().getCurrentCall();
        }
        if (mCall == null || mCall.getState() != State.IncomingReceived) {
            Log.e(TAG, "Couldn't find incoming call");
            finish();
            return;
        }
        EvideoVoipAddress address = mCall.getRemoteAddress();

        // To be done after findUriPictureOfContactAndSetDisplayName called
        mDisplayName = address.getDisplayName();
        String sipUri = address.asStringUriOnly();
        if (TextUtils.isEmpty(mDisplayName))
            mDisplayName = EvideoVoipUtils.getUsernameFromAddress(sipUri);

        mNameView.setText(mDisplayName);
        mNameViewMiddle.setText(mDisplayName);

        mPictureView.setImageResource(R.drawable.ic_call_default_portrait);
    }

    @Override
    protected void onPause() {
        EvideoVoipFunctions.removeEVCoreListener(mListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (EvideoVoipManager.isInstanciated()
                && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
            EvideoVoipFunctions.getInstance().hangUp(mCall);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.incomingVideoAnswerButton:
                EvideoVoipFunctions.getInstance().answer(IncomingCallActivity.this, InCallActivity.class, true);
                finish();
                break;
            case R.id.incomingAudioAnswerButton:
                EvideoVoipFunctions.getInstance().answer(IncomingCallActivity.this, InCallActivity.class, false);
                finish();
                break;
            case R.id.incomingRejectButton:
                EvideoVoipFunctions.getInstance().hangUp();
                finish();
                break;
        }
    }
}
