package com.chinausky.lanbowan;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.chinausky.lanbowan.evideo.EvideoMainActivity;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.database.LbwDao;
import com.evideo.voip.EvideoVoipApplication;
import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by fashi on 2015/7/6.
 */
public class MyApplication extends EvideoVoipApplication {
    public static final String APP_ID = "2882303761517424043";
    public static final String APP_KEY = "5151742499043";
    public static final String TAG = "com.chinausky.lanbowan";

    private Handler mMainHandler = new Handler();

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public LbwDao lbwDao;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //查看内存泄露
        refWatcher = LeakCanary.install(this);
        Fresco.initialize(sInstance);

        lbwDao = new LbwDao(this);

        CrashReport.initCrashReport(this, "900012643", false);

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                while (!EvideoVoipService.isReady()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("waiting thread sleep() has been interrupted");
                    }
                }
                EvideoVoipFunctions.setActivityToLaunchOnIncomingReceived(EvideoMainActivity.class);
            }
        });

        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);


    }

    // 数据库
    public Register getRegister() {
        if (lbwDao == null) {
            return null;
        }

        final List<Register> registers = lbwDao.queryAllForRegister();
        if (registers.size() > 0) {
            return registers.get(0);
        }
        return null;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


}
