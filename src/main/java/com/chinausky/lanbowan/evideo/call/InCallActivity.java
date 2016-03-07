package com.chinausky.lanbowan.evideo.call;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinausky.lanbowan.evideo.Constants;
import com.chinausky.lanbowan.evideo.SampleUtils;
import com.evideo.voip.EvideoVoipConstants;
import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipManager;
import com.evideo.voip.EvideoVoipPreferences;
import com.evideo.voip.EvideoVoipUtils;
import com.evideo.voip.core.EvideoVoipAddress;
import com.evideo.voip.core.EvideoVoipCall;
import com.evideo.voip.core.EvideoVoipCall.State;
import com.evideo.voip.core.EvideoVoipCallParams;
import com.evideo.voip.core.EvideoVoipChatMessage;
import com.evideo.voip.core.EvideoVoipChatRoom;
import com.evideo.voip.core.EvideoVoipCore;
import com.evideo.voip.core.EvideoVoipCoreListenerBase;
import com.evideo.voip.core.EvideoVoipInfoMessage;

import com.chinausky.lanbowan.R;

public class InCallActivity extends FragmentActivity implements OnClickListener {

    private final static String TAG = InCallActivity.class.getCanonicalName();

    private final static int SECONDS_BEFORE_DENYING_CALL_UPDATE = 30000;

    private static InCallActivity instance;

    private Handler mHandler = new Handler();
    private TextView video, micro, speaker;
    private Button hangupBtn, unlock, snapshot;
    private String mDisplayName;
    private AudioCallFragment audioCallFragment;
    private VideoCallFragment videoCallFragment;
    private boolean isSpeakerEnabled = true, isMicMuted = false, isVideoEnabled;
    private CountDownTimer timer;
    private AcceptCallUpdateDialog callUpdateDialog;
    private boolean isVideoCallPaused = false;

    private TableLayout callsList;
    private RelativeLayout pictureView;
    private LayoutInflater inflater;
    private ViewGroup container;

    private EvideoVoipCoreListenerBase mListener;
    private TimeoutRunnable mTimeoutRunnable = null;

    public static InCallActivity instance() {
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_call_calling);

        isVideoEnabled = getIntent().getExtras() != null
                && getIntent().getExtras().getBoolean(EvideoVoipFunctions.EXTRA_CALL_VIDEOENABLED);

        if (EvideoVoipFunctions.getEVCore() == null || EvideoVoipPreferences.instance() == null)
            return;

        EvideoVoipFunctions.getInstance().enableSpeaker(true);
        isSpeakerEnabled = EvideoVoipFunctions.getInstance().isSpeakerEnabled();

        if (findViewById(R.id.fragmentContainer) != null) {
            initUI();

            if (EvideoVoipFunctions.getEVCore().getCallsNb() > 0) {
                EvideoVoipCall call = EvideoVoipFunctions.getEVCore().getCalls()[0];

                if (EvideoVoipUtils.isCallEstablished(call)) {
                    isVideoEnabled = call.getCurrentParamsCopy().getVideoEnabled()
                            && !call.getRemoteParams().isLowBandwidthEnabled();
                    enableAndRefreshInCallActions();
                }
            } else {
//                Log.e(TAG, "Couldn't find running call");
                finish();
                return;
            }

            if (savedInstanceState != null) {
                // Fragment already created, no need to create it again (else it
                // will generate a memory leak with duplicated fragments)
                isSpeakerEnabled = savedInstanceState.getBoolean("Speaker");
                isMicMuted = savedInstanceState.getBoolean("Mic");
                isVideoCallPaused = savedInstanceState.getBoolean("VideoCallPaused");
                refreshInCallActions();
                return;
            }

            Fragment callFragment;
            if (isVideoEnabled) {
                callFragment = new VideoCallFragment();
                videoCallFragment = (VideoCallFragment) callFragment;
                pictureView.setVisibility(View.GONE);
            } else {
                callFragment = new AudioCallFragment();
                audioCallFragment = (AudioCallFragment) callFragment;
                pictureView.setVisibility(View.VISIBLE);
            }
            callFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, callFragment)
                    .commitAllowingStateLoss();
        }

        mTimeoutRunnable = new TimeoutRunnable();
        mHandler.postDelayed(mTimeoutRunnable, 180000);
        instance = this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Speaker", isSpeakerEnabled);
        outState.putBoolean("Mic", isMicMuted);
        outState.putBoolean("VideoCallPaused", isVideoCallPaused);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        mListener = new EvideoVoipCoreListenerBase() {
            @Override
            public void messageReceived(EvideoVoipCore evCore, EvideoVoipChatRoom cr, EvideoVoipChatMessage message) {
//                Log.d(TAG, "recv message : " + message.getText());
            }

            @Override
            public void infoReceived(EvideoVoipCore evCore, EvideoVoipCall call,
                                     EvideoVoipInfoMessage info) {
//                Log.d(TAG, "info.getContent() : " + info.getContent().getDataAsString());
                final int result = EvideoVoipFunctions.getInstance().getUnlockResult(info.getContent().getDataAsString());
                if (result != EvideoVoipConstants.RESULT_UNLOCK_INVALID) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != EvideoVoipConstants.RESULT_UNLOCK_FAILED)
                                Toast.makeText(InCallActivity.instance(), R.string.call_incall_prompt_unlock_success, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(InCallActivity.instance(), R.string.call_incall_prompt_unlock_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                super.infoReceived(evCore, call, info);
            }

            @Override
            public void callState(EvideoVoipCore evCore, EvideoVoipCall call, EvideoVoipCall.State state, String message) {
                if (EvideoVoipFunctions.getEVCore().getCallsNb() == 0) {
//                    Log.d(TAG, "incall call ended.");
                    finish();
                    return;
                }

                if (state == State.StreamsRunning) {
                    boolean isVideoEnabledInCall = call.getCurrentParamsCopy().getVideoEnabled();
                    if (isVideoEnabledInCall != isVideoEnabled) {
                        isVideoEnabled = isVideoEnabledInCall;
                        switchVideo(isVideoEnabled, false);
                    }

                    EvideoVoipFunctions.getInstance().enableSpeaker(isSpeakerEnabled);
                    isMicMuted = EvideoVoipFunctions.getInstance().isMicMuted();
                    enableAndRefreshInCallActions();
                }
                refreshInCallActions();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshCallList(getResources());
                    }
                });

                if (state == State.CallUpdatedByRemote) {
                    // If the correspondent proposes video while audio call
                    boolean isVideoEnabled = EvideoVoipPreferences.instance()
                            .isVideoEnabled();
                    if (!isVideoEnabled) {
                        acceptCallUpdate(false);
                        return;
                    }

                    boolean remoteVideo = call.getRemoteParams().getVideoEnabled();
                    boolean localVideo = call.getCurrentParamsCopy().getVideoEnabled();
                    if (remoteVideo && !localVideo) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                showAcceptCallUpdateDialog();

                                timer = new CountDownTimer(
                                        SECONDS_BEFORE_DENYING_CALL_UPDATE, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        acceptCallUpdate(false);
                                    }
                                }.start();
                            }
                        });
                    }
                }
            }

            @Override
            public void callVideoTimeout(EvideoVoipCore lc, EvideoVoipCall call) {
                super.callVideoTimeout(lc, call);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(InCallActivity.this, "视频超时", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        EvideoVoipFunctions.addEVCoreListener(mListener);
        refreshCallList(getResources());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EvideoVoipFunctions.removeEVCoreListener(mListener);
    }

    @Override
    protected void onDestroy() {
        if (mTimeoutRunnable != null) {
            mHandler.removeCallbacks(mTimeoutRunnable);
        }
        mHandler = null;
//        Log.d(TAG, "onDestroy");
        unbindDrawables(findViewById(R.id.topLayout));
        instance = null;
        super.onDestroy();
        System.gc();
    }

    private void initUI() {
        inflater = LayoutInflater.from(this);
        container = (ViewGroup) findViewById(R.id.topLayout);
        callsList = (TableLayout) findViewById(R.id.callsInfoTableLayout);
        callsList.setVisibility(View.VISIBLE);

        pictureView = (RelativeLayout) findViewById(R.id.incallPortraitRelativeLayout);

        video = (TextView) findViewById(R.id.incallVideoTextView);
        video.setOnClickListener(this);
        video.setEnabled(false);
        micro = (TextView) findViewById(R.id.incallMicroMuteTextView);
        micro.setOnClickListener(this);
        speaker = (TextView) findViewById(R.id.incallSpeakerTextView);
        speaker.setOnClickListener(this);
        unlock = (Button) findViewById(R.id.incallUnlockButton);
        unlock.setOnClickListener(this);
        snapshot = (Button) findViewById(R.id.incallSnapshotButton);
        snapshot.setOnClickListener(this);

        hangupBtn = (Button) findViewById(R.id.incallRejectButton);
        hangupBtn.setOnClickListener(this);

        findViewById(R.id.callsInfoTableLayout).setOnClickListener(this);

        speaker.setVisibility(View.VISIBLE);
    }

    private void refreshInCallActions() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!EvideoVoipPreferences.instance().isVideoEnabled()) {
                    video.setEnabled(false);
                } else {
                    if (isVideoEnabled) {
                        Drawable videoOnDrawable = getResources().getDrawable(R.drawable.call_video_on);
                        videoOnDrawable.setBounds(0, 0, videoOnDrawable.getMinimumWidth(), videoOnDrawable.getMinimumHeight());
                        video.setCompoundDrawables(null, videoOnDrawable, null, null);
                    } else {
                        Drawable videoOffDrawable = getResources().getDrawable(R.drawable.call_video_off);
                        videoOffDrawable.setBounds(0, 0, videoOffDrawable.getMinimumWidth(), videoOffDrawable.getMinimumHeight());
                        video.setCompoundDrawables(null, videoOffDrawable, null, null);
                    }
                }

                try {
                    if (isSpeakerEnabled) {
                        Drawable speakerOnDrawable = getResources().getDrawable(R.drawable.call_speaker_on);
                        speakerOnDrawable.setBounds(0, 0, speakerOnDrawable.getMinimumWidth(), speakerOnDrawable.getMinimumHeight());
                        speaker.setCompoundDrawables(null, speakerOnDrawable, null, null);
                    } else {
                        Drawable speakerOffDrawable = getResources().getDrawable(R.drawable.call_speaker_off);
                        speakerOffDrawable.setBounds(0, 0, speakerOffDrawable.getMinimumWidth(), speakerOffDrawable.getMinimumHeight());
                        speaker.setCompoundDrawables(null, speakerOffDrawable, null, null);
                    }
                } catch (NullPointerException npe) {
                    Log.e(TAG, "Bluetooth: Audio routes menu disabled on tablets for now (4)");
                }

                if (isMicMuted) {
                    Drawable microOnDrawable = getResources().getDrawable(R.drawable.call_micro_on);
                    microOnDrawable.setBounds(0, 0, microOnDrawable.getMinimumWidth(), microOnDrawable.getMinimumHeight());
                    micro.setCompoundDrawables(null, microOnDrawable, null, null);
                } else {
                    Drawable microOffDrawable = getResources().getDrawable(R.drawable.call_micro_off);
                    microOffDrawable.setBounds(0, 0, microOffDrawable.getMinimumWidth(), microOffDrawable.getMinimumHeight());
                    micro.setCompoundDrawables(null, microOffDrawable, null, null);
                }
            }
        });
    }

    private void enableAndRefreshInCallActions() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                video.setEnabled(true);
                micro.setEnabled(true);
                speaker.setEnabled(true);

                refreshInCallActions();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.incallVideoTextView) {
            isVideoEnabled = !isVideoEnabled;
            switchVideo(isVideoEnabled, true);
        } else if (id == R.id.incallMicroMuteTextView) {
            toggleMicro();
        } else if (id == R.id.incallSpeakerTextView) {
            toggleSpeaker();
        } else if (id == R.id.incallUnlockButton) {
            EvideoVoipFunctions.getInstance().unlock();
        } else if (id == R.id.incallSnapshotButton) {
            if (!isVideoEnabled) {
                return;
            }
            String filePath = SampleUtils.getSdCardWorkPath() + Constants.SNAPSHOT_SUB_PATH;
            String fileName = String.format(Constants.SNAPSHOT_FILENAME_FORMAT,
                    "snapshot",
                    SampleUtils.formatCurrentTimeFilename(System.currentTimeMillis()));

            EvideoVoipCall call = (EvideoVoipCall) EvideoVoipFunctions.getEVCore().getCurrentCall();
            if (call != null) {
                fileName = String.format(Constants.SNAPSHOT_FILENAME_FORMAT,
                        call.getRemoteAddress().getDisplayName(),
                        SampleUtils.formatCurrentTimeFilename(System.currentTimeMillis()));
            }
            String savePath = EvideoVoipFunctions.getInstance().takeVideoSnapshot(filePath, fileName);
            if (savePath != null) {
                Toast.makeText(this,
                        getResources().getString(R.string.snapshot_saveto) + savePath, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, R.string.snapshot_failed, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.incallRejectButton) {
            EvideoVoipFunctions.getInstance().hangUp();
            this.finish();
        }
    }

    private void switchVideo(final boolean displayVideo,
                             final boolean isInitiator) {
        final EvideoVoipCall call;
        if (EvideoVoipFunctions.getEVCore().getCallsNb() > 0) {
            call = EvideoVoipFunctions.getEVCore().getCalls()[0];
            if (call == null) {
                return;
            }
        } else {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!displayVideo) {
                    if (isInitiator) {
                        EvideoVoipCallParams params = call.getCurrentParamsCopy();
                        params.setVideoEnabled(false);
                        EvideoVoipFunctions.getEVCore().updateCall(call, params);
                    }
                    showAudioView();
                } else {
                    if (EvideoVoipFunctions.getEVCore().getCallsNb() > 0) {
                        EvideoVoipManager.getInstance().addVideo();
                        showVideoView();
                    } else {
                    }
                }
            }
        });
    }

    private void showAudioView() {
        Drawable videoOffDrawable = getResources().getDrawable(R.drawable.call_video_off);
        videoOffDrawable.setBounds(0, 0, videoOffDrawable.getMinimumWidth(), videoOffDrawable.getMinimumHeight());
        video.setCompoundDrawables(null, videoOffDrawable, null, null);

//        Log.d(TAG, "showAudioView");
        pictureView.setVisibility(View.VISIBLE);
        replaceFragmentVideoByAudio();
    }

    private void showVideoView() {
        isSpeakerEnabled = true;
//        Log.d(TAG, "showVideoView");
        pictureView.setVisibility(View.GONE);
        EvideoVoipFunctions.getInstance().enableSpeaker(isSpeakerEnabled);

        Drawable speakerOnDrawable = getResources().getDrawable(R.drawable.call_speaker_on);
        speakerOnDrawable.setBounds(0, 0, speakerOnDrawable.getMinimumWidth(), speakerOnDrawable.getMinimumHeight());
        speaker.setCompoundDrawables(null, speakerOnDrawable, null, null);

        Drawable videoOnDrawable = getResources().getDrawable(R.drawable.call_video_on);
        videoOnDrawable.setBounds(0, 0, videoOnDrawable.getMinimumWidth(), videoOnDrawable.getMinimumHeight());
        video.setCompoundDrawables(null, videoOnDrawable, null, null);

        replaceFragmentAudioByVideo();
    }

    private void replaceFragmentVideoByAudio() {
        audioCallFragment = new AudioCallFragment();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, audioCallFragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

    private void replaceFragmentAudioByVideo() {
        videoCallFragment = new VideoCallFragment();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, videoCallFragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

    private void toggleMicro() {
        isMicMuted = !isMicMuted;
        EvideoVoipFunctions.getInstance().muteMic(isMicMuted);
        if (isMicMuted) {
            Drawable microOnDrawable = getResources().getDrawable(R.drawable.call_micro_on);
            microOnDrawable.setBounds(0, 0, microOnDrawable.getMinimumWidth(), microOnDrawable.getMinimumHeight());
            micro.setCompoundDrawables(null, microOnDrawable, null, null);
        } else {
            Drawable microOffDrawable = getResources().getDrawable(R.drawable.call_micro_off);
            microOffDrawable.setBounds(0, 0, microOffDrawable.getMinimumWidth(), microOffDrawable.getMinimumHeight());
            micro.setCompoundDrawables(null, microOffDrawable, null, null);
        }
    }

    private void toggleSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled;
        EvideoVoipFunctions.getInstance().enableSpeaker(isSpeakerEnabled);
        if (isSpeakerEnabled) {
            Drawable speakerOnDrawable = getResources().getDrawable(R.drawable.call_speaker_on);
            speakerOnDrawable.setBounds(0, 0, speakerOnDrawable.getMinimumWidth(), speakerOnDrawable.getMinimumHeight());
            speaker.setCompoundDrawables(null, speakerOnDrawable, null, null);
        } else {
            Drawable speakerOffDrawable = getResources().getDrawable(R.drawable.call_speaker_off);
            speakerOffDrawable.setBounds(0, 0, speakerOffDrawable.getMinimumWidth(), speakerOffDrawable.getMinimumHeight());
            speaker.setCompoundDrawables(null, speakerOffDrawable, null, null);
        }
    }

    private void acceptCallUpdate(boolean accept) {
        if (timer != null) {
            timer.cancel();
        }

        if (callUpdateDialog != null) {
            callUpdateDialog.dismissAllowingStateLoss();
        }

        EvideoVoipCall call = EvideoVoipFunctions.getEVCore().getCurrentCall();
        if (call == null) {
            return;
        }

        EvideoVoipCallParams params = call.getCurrentParamsCopy();
        if (accept) {
            params.setVideoEnabled(true);
            EvideoVoipFunctions.getEVCore().enableVideo(true, true);
        }

        EvideoVoipFunctions.getEVCore().updateCall(call, params);
    }

    private void showAcceptCallUpdateDialog() {
        FragmentManager fm = getSupportFragmentManager();
        callUpdateDialog = new AcceptCallUpdateDialog();
        callUpdateDialog.show(fm, "Accept Call Update Dialog");
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ImageView) {
            view.setOnClickListener(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (EvideoVoipUtils.onKeyVolumeAdjust(keyCode))
            return true;
        if (EvideoVoipUtils.onKeyBackGoHome(this, keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    public void bindAudioFragment(AudioCallFragment fragment) {
        audioCallFragment = fragment;
    }

    public void bindVideoFragment(VideoCallFragment fragment) {
        videoCallFragment = fragment;
    }

    @SuppressLint("ValidFragment")
    class AcceptCallUpdateDialog extends DialogFragment {

        public AcceptCallUpdateDialog() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.call_accept_update_dialog,
                    container);

            getDialog().setTitle(R.string.call_incall_prompt_videocall_request);

            Button yes = (Button) view.findViewById(R.id.yes);
            yes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "Call Update Accepted");
                    acceptCallUpdate(true);
                }
            });

            Button no = (Button) view.findViewById(R.id.no);
            no.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "Call Update Denied");
                    acceptCallUpdate(false);
                }
            });

            return view;
        }
    }

    private void displayCall(Resources resources, EvideoVoipCall call, int index) {
        String sipUri = call.getRemoteAddress().asStringUriOnly();

        // Control Row
        RelativeLayout callView = (RelativeLayout) inflater.inflate(
                R.layout.call_active_control_row, container, false);
        callView.setId(index + 1);

        EvideoVoipAddress remoteAddr = call.getRemoteAddress();
        String displayName = "楼道梯口机";
//        if (TextUtils.isEmpty(remoteAddr.getUserName()))
//            displayName = "sip:" + remoteAddr.getUserName() + "@" + remoteAddr.getDomain();
//        else {
//            displayName = TextUtils.isEmpty(remoteAddr.getDisplayName()) ?
//                    remoteAddr.getUserName() : remoteAddr.getDisplayName();
//        }
        setContactName(callView, displayName, sipUri, resources);
        displayCallStatusIconAndReturnCallPaused(callView, call);
        registerCallDurationTimer(callView, call);
        callsList.addView(callView);
    }

    private void setContactName(RelativeLayout callView, String displayName,
                                String sipUri, Resources resources) {
        TextView contact = (TextView) callView
                .findViewById(R.id.nameOrNumberTextView);

        mDisplayName = displayName;
        contact.setText(mDisplayName);
    }

    private boolean displayCallStatusIconAndReturnCallPaused(
            RelativeLayout callView, EvideoVoipCall call) {
        boolean isCallPaused;
        TextView callState = (TextView) callView.findViewById(R.id.callStatusTextView);
        callState.setTag(call);
        callState.setTextColor(getResources().getColor(R.color.white));
        callState.setOnClickListener(this);
        callState.setVisibility(View.GONE);

        if (call.getState() == State.Paused
                || call.getState() == State.PausedByRemote
                || call.getState() == State.Pausing) {
            callState.setText(R.string.call_incall_prompt_hold);
            isCallPaused = true;
        } else if (call.getState() == State.OutgoingInit
                || call.getState() == State.OutgoingProgress
                || call.getState() == State.OutgoingRinging) {
            callState.setText(R.string.call_incall_prompt_dialing);
            callState.setVisibility(View.VISIBLE);
            isCallPaused = false;
        } else {
            callState.setText(R.string.call_incall_prompt_talking);
            isCallPaused = false;
        }

        return isCallPaused;
    }

    private void registerCallDurationTimer(View v, EvideoVoipCall call) {
        int callDuration = call.getDuration();
        if (callDuration == 0 && call.getState() != State.StreamsRunning) {
            return;
        }

        Chronometer timer = (Chronometer) v.findViewById(R.id.callDurationChronometer);
        if (timer == null) {
            throw new IllegalArgumentException("no callee_duration view found");
        }

        timer.setVisibility(View.VISIBLE);
        timer.setBase(SystemClock.elapsedRealtime() - 1000 * callDuration);
        timer.start();
    }

    public void refreshCallList(Resources resources) {
        if (callsList == null) {
            return;
        }
        callsList.removeAllViews();
        int index = 0;

        if (EvideoVoipFunctions.getEVCore().getCallsNb() == 0) {
            finish();
            return;
        }
        for (EvideoVoipCall call : EvideoVoipFunctions.getEVCore().getCalls()) {
            displayCall(resources, call, index);
            index++;
        }
        callsList.invalidate();
    }

    private class TimeoutRunnable implements Runnable {

        @Override
        public void run() {
            EvideoVoipFunctions.getInstance().hangUp();
            InCallActivity.this.finish();
        }

    }
}
