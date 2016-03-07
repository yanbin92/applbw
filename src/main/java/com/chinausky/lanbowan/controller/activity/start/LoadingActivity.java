package com.chinausky.lanbowan.controller.activity.start;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.GetMyResidenceInfo;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.SignInInfo;
import com.chinausky.lanbowan.model.database.LbwDao;
import com.chinausky.lanbowan.controller.activity.main.MainActivity;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.controller.activity.login.LoginActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.evideo.voip.EvideoVoipFunctions;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoadingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private int[] imgs = {R.drawable.loading_page1, R.drawable.loading_page2};
    private ArrayList<View> mViews;
    private LoadingAdapter loadingAdapter;

    @Bind(R.id.loading_viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);


        SharedPreferences sharedPreferences = getSharedPreferences("isLoading", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoading", true);
        editor.commit();

        setViewPager();
    }

    private void setViewPager() {
        mViews = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            SimpleDraweeView draweeView1 = new SimpleDraweeView(LoadingActivity.this);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            Uri uri1 = Uri.parse("res://com.chinausky.lanbowan/" + imgs[i]);
            draweeView1.setImageURI(uri1);
            draweeView1.setHierarchy(hierarchy);
            mViews.add(draweeView1);
        }

        View view = LayoutInflater.from(LoadingActivity.this).inflate(R.layout.loading_content_view, null);
        Uri uri2 = Uri.parse("res:///" + R.drawable.loading_page3);
        SimpleDraweeView draweeView2 = (SimpleDraweeView) view.findViewById(R.id.iv_loading_content);
        draweeView2.setImageURI(uri2);
        mViews.add(view);

        Button startBtn = (Button) view.findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });

        loadingAdapter = new LoadingAdapter(mViews);
        mViewPager.setAdapter(loadingAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void go() {
        Register register = MyApplication.getInstance().getRegister();

        if (register != null) {
            if (!register.getMobileNumber().isEmpty() && !register.getPassword().isEmpty()) {
                login(register);
            }
        } else {
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class LoadingAdapter extends PagerAdapter {
        private ArrayList<View> mViews;

        public LoadingAdapter(ArrayList<View> views) {
            mViews = views;
        }

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            if (mViews != null) {
                return mViews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 负责销毁item对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        /**
         * 构建item对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position), 0);
            return mViews.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    private void login(final Register register) {
        final String username = register.getMobileNumber();
        final String password = register.getPassword();

        Call<SignInInfo> call = Api.signInApi().onResult("password", username, password);
        call.enqueue(new Callback<SignInInfo>() {
            @Override
            public void onResponse(Response<SignInInfo> response, Retrofit retrofit) {
                if (response.body() != null) {
                    GlobalUtils.showToastShort(LoadingActivity.this, "登陆成功");
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);

                    LbwDao lbwDao = MyApplication.getInstance().lbwDao;
                    register.setToken(response.body().getAccess_token());
                    if (lbwDao != null) {
                        lbwDao.updateRegister(register);
                    }

                    // 获得与用户房间绑定的对应的梯口机房间账号 异步请求这个API
                    Call<GetMyResidenceInfo> myResidenceInfoCall = Api.getMyResidenceApi().onResult("Bearer " + response.body().getAccess_token());
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
                            GlobalUtils.showToastShort(LoadingActivity.this, "房间注册成功");
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
                GlobalUtils.showToastShort(LoadingActivity.this, "账号或者密码错误");
            }
        });
    }

}
