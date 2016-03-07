package com.chinausky.lanbowan.aipuwaton;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

interface OnWorkerListener {
    Object OnDoInBackground(int what, int arg1, int arg2, Object obj);

    void OnPostExecute(int what, int arg1, int arg2, Object obj, Object ret);
}


public class Dlg_Worker {
    OnWorkerListener _user;
    MyTask _task;
    MyHandler _handler = new MyHandler();

    AlertDialog alert = null;

    public Dlg_Worker(Context context) {

//        alert = new AlertDialog.Builder(context).create();
//        alert.setTitle("系统提示");
//        alert.setMessage("处理中...");//设置显示的内容
//        alert.setButton("确定", new DialogInterface.OnClickListener() {
//            //添加确定按钮
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //确定按钮的响应事件
//                dialog.dismiss();
//            }
//        });
    }

    public void Show(OnWorkerListener user, String text) {
        Show(user, text, 0, 0, 0, null);
    }

    public void Show(OnWorkerListener user, String text, int what, int arg1, int arg2, Object obj) {
        if (_task != null) {
            return;
        }
        _user = user;
        _text = text;
        _task = new MyTask();
        _task.execute(what, arg1, arg2, obj);
    }

    String _text;

    public void UpdateText(String text) {
        _text = text;
        _handler.sendEmptyMessage(100);
    }

    protected class MyTask extends AsyncTask {
        int what = 0, arg1 = 0, arg2 = 0;
        Object obj = null;

        @Override
        protected void onPreExecute() {
//            alert.show();
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            what = (Integer) arg0[0];
            arg1 = (Integer) arg0[1];
            arg2 = (Integer) arg0[2];
            obj = arg0[3];

            return _user.OnDoInBackground(what, arg1, arg2, obj);
        }

        @Override
        protected void onPostExecute(Object ret) {
            _user.OnPostExecute(what, arg1, arg2, obj, ret);
            _task = null;
//            alert.dismiss();
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }
}
