package com.chinausky.lanbowan.aipuwaton;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.view.adapter.recyclerview.CameraRvAdapter;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.GetMyCamerasMessage;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.platform.net.NetSdk;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CameraActivity extends BaseActivity {

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.bagetu_rv)
    RecyclerView mRecyclerView;

    private CameraRvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bagetu);
        ButterKnife.bind(this);
        mTitleTv.setText("视频监控");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new CameraRvAdapter.MyDecoration());
        mAdapter = new CameraRvAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new CameraRvAdapter.onClickListener() {
            @Override
            public void onClick(String ip, int port, String username, String password) {
                new MyTask(ip, port, username, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        final Register register = MyApplication.getInstance().getRegister();
        if (register.getToken() == null) {
            return;
        }

        Call<List<GetMyCamerasMessage>> call = Api.getMyCameras().onResult("Bearer " + register.getToken());

        call.enqueue(new Callback<List<GetMyCamerasMessage>>() {
            @Override
            public void onResponse(Response<List<GetMyCamerasMessage>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                List<GetMyCamerasMessage> cameras = response.body();
                mAdapter.setCameras(cameras);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    public void dealLoginRes(int res) {
        switch (res) {
            case 0:
                //登陆成功
                startAiPuWaTonActivity();
                break;
            case -10004:
                GlobalUtils.showToastShort(this, "获取数据失败");
                break;
            case -1:
                GlobalUtils.showToastShort(this, "网络错误");
                break;
            case -10002:
                GlobalUtils.showToastShort(this, "发送数据失败");
                break;
            case -10003:
                GlobalUtils.showToastShort(this, "登录超时");
                break;
            case 421:
            case 422:
            case 423:
            case 425:
            case 531:
                GlobalUtils.showToastShort(this, "用户不存在");
                break;
            case 424:
                GlobalUtils.showToastShort(this, "密码无效");
                break;
            case 426:
                GlobalUtils.showToastShort(this, "用户被锁定");
                break;
            case 427:
                GlobalUtils.showToastShort(this, "用户已经登录");
                break;
            case -1000:
                GlobalUtils.showToastShort(this, "系统忙");
                break;
            default:
                GlobalUtils.showToastShort(this, "未知错误");
                break;
        }
    }

    public void startAiPuWaTonActivity() {
        Intent in = new Intent(this, AiPuWaTonActivity.class);
//        in.setClassName(getApplicationContext(), "com.example.demoandroid.MainActivity");
        startActivity(in);
    }

    private class MyTask extends AsyncTask<Object, Object, Integer> {
        private String ip;
        private int port;
        private String username;
        private String password;

        public MyTask(String ip, int port, String username, String password) {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Object... params) {
            int res = NetSdk.getInstance().login(ip, port, username, password);

            return res;
        }

        @Override
        protected void onPostExecute(Integer res) {
            dealLoginRes(res);
        }
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
