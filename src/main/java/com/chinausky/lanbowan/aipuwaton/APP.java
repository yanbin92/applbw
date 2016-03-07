package com.chinausky.lanbowan.aipuwaton;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;

public class APP {
    static Dlg_Worker _woker = null;
    static HashMap<Integer, Handler> _handles = new HashMap<Integer, Handler>();
    static Context curContext;

    public static boolean Init(Context context) {
        _woker = new Dlg_Worker(context);
        //_woker = new Dlg_Worker(MainActivity.Instance);
        curContext = context;

        return true;
    }

    public static boolean RegHandler(int viewId, Handler handler) {
        if (handler == null) {
            return false;
        }
        _handles.put(viewId, handler);

        return true;
    }

    public static boolean SendMessage(int viewId, Message msg) {
        if (!_handles.containsKey(viewId)) {
            return false;
        }

        return _handles.get(viewId).sendMessage(msg);
    }

    public static boolean SendMessage(int viewId, int what, int arg1) {
        return SendMessage(viewId, what, arg1, 0, null);
    }

    public static boolean SendMessage(int viewId, int what, Object obj) {
        return SendMessage(viewId, what, 0, 0, obj);
    }

    public static boolean SendMessage(int viewId, int what, int arg1, Object obj) {
        return SendMessage(viewId, what, arg1, 0, obj);
    }

    public static boolean SendMessage(int viewId, int what, int arg1, int arg2, Object obj) {
        Message msg = new Message();
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        msg.what = what;
        return SendMessage(viewId, msg);
    }

    public static Context GetActivity() {
        return curContext;
    }

    static public void ShowError(String text) {
        ShowToast(text, Toast.LENGTH_LONG);
    }

    static public void ShowToast(String text) {
        ShowToast(text, Toast.LENGTH_SHORT);
    }

    static public void ShowToast(String text, int time) {
        Toast.makeText(APP.GetActivity(), text, time).show();
    }

    public static void ShowWorking(OnWorkerListener user, String text, int what, int arg1, int arg2, Object obj) {
        _woker.Show(user, text, what, arg1, arg2, obj);
    }

    public static void ShowWorking(OnWorkerListener user, String text) {
        _woker.Show(user, text);
    }

    public static void ShowWorking(OnWorkerListener user, String text, int what) {
        _woker.Show(user, text, what, 0, 0, null);
    }


}


