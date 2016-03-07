package com.chinausky.lanbowan.controller.activity.start;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.activity.main.MainActivity;
import com.chinausky.lanbowan.controller.activity.login.LoginActivity;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.GetMyResidenceInfo;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.SignInInfo;
import com.chinausky.lanbowan.model.database.LbwDao;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.evideo.voip.EvideoVoipFunctions;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/12/1.
 */
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences sharedPreferences = getSharedPreferences("isLoading", Activity.MODE_PRIVATE);
        final boolean name = sharedPreferences.getBoolean("isLoading", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (name) {
                    go();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }

    private void go() {
        Register register = MyApplication.getInstance().getRegister();

        if (register != null) {
            if (!register.getMobileNumber().isEmpty() && !register.getPassword().isEmpty()) {
                login(register);
            }
        } else {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(final Register register) {
        final String username = register.getMobileNumber();
        final String password = register.getPassword();

        Call<SignInInfo> call = Api.signInApi().onResult("password", username, password);
        call.enqueue(new Callback<SignInInfo>() {
            @Override
            public void onResponse(Response<SignInInfo> response, Retrofit retrofit) {
                if (response.body() != null) {
                    GlobalUtils.showToastShort(StartActivity.this, "登陆成功");
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);

                    //通过application传递数据 不好吧
                    LbwDao lbwDao = MyApplication.getInstance().lbwDao;
                    register.setToken(response.body().getAccess_token());
                    if (lbwDao != null) {
                        lbwDao.updateRegister(register);
                    }

                    // 获得与用户房间绑定的对应的梯口机房间账号
                    Call<GetMyResidenceInfo> myResidenceInfoCall = Api.getMyResidenceApi().onResult("Bearer " + response.body().getAccess_token());
                    //异步请求这个API
                    myResidenceInfoCall.enqueue(new Callback<GetMyResidenceInfo>() {
                        @Override
                        public void onResponse(Response<GetMyResidenceInfo> response, Retrofit retrofit) {
                            if (response.body() == null) {
                                return;
                            }
                            Log.d("MyResidenceInfo", response.toString());
                            String account = response.body().getStarnetAccount();
                            String password = response.body().getStarnetPassword();
                            String server = response.body().getStarnetServer();
                            String port = response.body().getStarnetServerPort();
                            EvideoVoipFunctions.getInstance().register(account, "", password, server, Integer.valueOf(port));
                            GlobalUtils.showToastShort(StartActivity.this, "房间注册成功");
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GlobalUtils.showToastShort(StartActivity.this, "账号或者密码错误");
            }
        });
    }
}
