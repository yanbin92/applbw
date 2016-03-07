package com.chinausky.lanbowan.aipuwaton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.platform.media.RealplayController;
import com.platform.media.TalkController;
import com.platform.net.NetSdk;
import com.platform.protocol.monitor.response.cms.OrgBean;
import com.platform.protocol.monitor.response.cms.TalkBean;
import com.platform.protocol.monitor.response.cms.VideoBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AiPuWaTonActivity extends BaseActivity implements OnClickListener, OnWorkerListener {

    @Bind(R.id.qiujiu)
    SimpleDraweeView mQiujuImg;
    //private MyHandler _hander = new MyHandler();

    private EditText textDeviceid;
    private EditText textChannel;
    private EditText textPathCapture;
    private SurfaceView surfaceView;

    SurfaceHolder surfaceHolder;

    private AlertDialog alertDialog;

    //设备信息
    private OrgBean orgBean;

    //保存实时监视的播放信息
    private VideoBean videoBean;

    //语音对讲的信息
    private TalkBean talkBean;

    //实时监视控制器
    private RealplayController realplayController;

    //语音对讲控制器
    private TalkController talkController;
    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aipuwaton);
        ButterKnife.bind(this);
        mTitleTv.setText("视频监控");

        APP.Init(this);
        //APP.RegHandler(R.layout.activity_main, _hander);

        textDeviceid = (EditText) findViewById(R.id.editTextDeviceid);
        textChannel = (EditText) findViewById(R.id.editTextChannel);
        textPathCapture = (EditText) findViewById(R.id.editTextPathCapture);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        findViewById(R.id.buttonLogout).setOnClickListener(this);
        findViewById(R.id.buttonStartRealplay).setOnClickListener(this);
        findViewById(R.id.buttonStopRealplay).setOnClickListener(this);
        findViewById(R.id.buttonStartTalk).setOnClickListener(this);
        findViewById(R.id.buttonStopTalk).setOnClickListener(this);
        findViewById(R.id.buttonStartAudio).setOnClickListener(this);
        findViewById(R.id.buttonStopAudio).setOnClickListener(this);
        findViewById(R.id.buttonCapture).setOnClickListener(this);

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("操作结果");
        alertDialog.setButton("确定", new DialogInterface.OnClickListener() {
            //添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定按钮的响应事件
                dialog.dismiss();
            }
        });

        orgBean = new OrgBean();
        videoBean = new VideoBean();
        talkBean = new TalkBean();
        realplayController = null;
        talkController = null;
        APP.ShowWorking(this, "打开视频中...", 2);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(AiPuWaTonActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(AiPuWaTonActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();

            if (realplayController != null) {
                //realplayController.setSurface(surfaceView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonLogout:
                logout();
                break;
            case R.id.buttonStartRealplay:
                startRealplay();
                break;
            case R.id.buttonStopRealplay:
                stopRealplay();
                break;
            case R.id.buttonStartTalk:
                startTalk();
                break;
            case R.id.buttonStopTalk:
                stopTalk();
                break;
            case R.id.buttonStartAudio:
                startAudio();
                break;
            case R.id.buttonStopAudio:
                stopAudio();
                break;
            case R.id.buttonCapture:
                capture();
                break;
            default:
                break;
        }
    }

    public Object OnDoInBackground(int what, int arg1, int arg2, Object obj) {
        int res = 0;
        switch (what) {
            case 1:
                res = doLogout();
                break;    //登出
            case 2:
                res = doStartRealplay();
                break;
            case 3:
                res = doStopRealplay();
                break;
            case 4:
                res = doStartTalk();
                break;
            case 5:
                res = doStopTalk();
                break;
            case 6:
                res = doStartAudio();
                break;
            case 7:
                res = doStopAudio();
                break;
            case 8:
                res = doCapture();
                break;
            default:
                break;
        }


        return res;
    }

    public void OnPostExecute(int what, int arg1, int arg2, Object obj, Object ret) {
        Integer res = (Integer) ret;
//
//        switch (what) {
//            case 1:
//                dealLogoutRes(res);
//                break;
//            case 2:
//                dealStartRealplayRes(res);
//                break;
//            case 3:
//                dealStopRealplayRes(res);
//                break;
//            case 4:
//                dealStartTalkRes(res);
//                break;
//            case 5:
//                dealStopTalkRes(res);
//                break;
//            case 6:
//                dealStartAudio(res);
//                break;
//            case 7:
//                dealStopAudio(res);
//                break;
//            case 8:
//                dealCapture(res);
//                break;
//            default:
//                break;
//        }

    }


    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AiPuWaTonActivity.this);
        builder.setMessage("确定要登出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        APP.ShowWorking(AiPuWaTonActivity.this, "登出中...", 1);
                    }
                });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void startRealplay() {
        APP.ShowWorking(this, "打开视频中...", 2);
    }

    private void stopRealplay() {
        APP.ShowWorking(this, "关闭视频中...", 3);
    }

    private void startTalk() {
        APP.ShowWorking(this, "打开对讲中...", 4);
    }

    private void stopTalk() {
        APP.ShowWorking(this, "关闭对讲中...", 5);
    }

    private void startAudio() {
        APP.ShowWorking(this, "打开声音中...", 6);
    }

    private void stopAudio() {
        APP.ShowWorking(this, "关闭声音中...", 7);
    }

    public int doLogout() {
        int res = NetSdk.getInstance().logout();

        return res;
    }

    public int doStartRealplay() {
        String deviceid = textDeviceid.getText().toString();
        String channel = textChannel.getText().toString();
        String cameraid = deviceid + "$" + channel;
        int res = NetSdk.getInstance().getRealplayInfo(videoBean, cameraid, 1, 2);

        if (res == 0) {
            realplayController = new RealplayController();
            realplayController.play(surfaceView, videoBean.getUrl());
        }

        return res;
    }

    public int doStopRealplay() {
        if (realplayController != null) {
            realplayController.stop();
            realplayController = null;
        }

        int res = NetSdk.getInstance().stopVideo(videoBean.getSession());

        return res;
    }

    public int doStartTalk() {
        String deviceid = textDeviceid.getText().toString();
        int res = NetSdk.getInstance().getTalkInfo(talkBean, deviceid, 2);

        if (res == 0) {
            talkController = new TalkController();

            //暂时不支持双通道
            talkController.startTalk(talkBean.getUrl(), 1, talkBean.getAuSampleRate(), talkBean.getAudiobit(), talkBean.getStreamtype(), talkBean.getAutiotype());
        }

        return res;
    }

    public int doStopTalk() {
        if (talkController != null) {
            talkController.stopTalk();
            talkController = null;
        }

        String deviceid = textDeviceid.getText().toString();
        int res = NetSdk.getInstance().stopTalk(deviceid, talkBean.getSession());

        return res;
    }

    public int doStartAudio() {
        int res = -1;
        if (realplayController != null) {
            res = realplayController.startAudio();
        }

        return res;
    }

    public int doStopAudio() {
        int res = -1;
        if (realplayController != null) {
            res = realplayController.stopAudio();
        }

        return res;
    }

    public void dealLogoutRes(int res) {
        switch (res) {
            case 0: {
                exitDialog();
                break;
            }
            default:
                alert("登出失败");
                break;
        }
    }

    public void dealStartRealplayRes(int res) {
        switch (res) {
            case 0:
                alert("打开实时视频成功");
                break;
            default:
                alert("打开实时视频失败");
                break;
        }
    }

    public void dealStopRealplayRes(int res) {
        switch (res) {
            case 0:
                alert("关闭实时视频成功");
                break;
            default:
                alert("关闭实时视频失败");
                break;
        }
    }

    public void dealStartTalkRes(int res) {
        switch (res) {
            case 0:
                alert("打开对讲成功");
                break;
            default:
                alert("打开对讲失败");
                break;
        }
    }

    public void dealStopTalkRes(int res) {
        switch (res) {
            case 0:
                alert("关闭对讲成功");
                break;
            default:
                alert("关闭对讲失败");
                break;
        }
    }

    public void dealStartAudio(int res) {

    }

    public void dealStopAudio(int res) {

    }

    public void capture() {
        APP.ShowWorking(this, "抓图中...", 8);
    }

    public int doCapture() {
        String fileName = textPathCapture.getText().toString();
        int res = realplayController.capture(fileName);

        return res;
    }

    public void dealCapture(int res) {
        switch (res) {
            case 0:
                alert("抓图成功");
                break;
            default:
                alert("抓图失败");
                break;
        }
    }

    private void alert(String text) {
        alertDialog.setMessage(text);//设置显示的内容
        alertDialog.show();
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            logout();
//
//            return true;
//        }
//
//        return true;
//    }

    protected void exitDialog() {
        RealplayController.unInit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
//        APP.ShowWorking(AiPuWaTonActivity.this, "登出中...", 1);
        super.onBackPressed();
    }

    @OnClick(R.id.qiujiu)
    void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "13681828506");
        intent.setData(data);
        startActivity(intent);
    }
}
