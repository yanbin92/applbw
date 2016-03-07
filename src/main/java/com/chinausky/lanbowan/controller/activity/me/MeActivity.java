package com.chinausky.lanbowan.controller.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.OwnerInfo;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.database.LbwDao;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.controller.activity.login.LoginActivity;
import com.chinausky.lanbowan.model.api.Api;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fashi on 2015/7/10.
 */
public class MeActivity extends BaseActivity {

    @Bind(R.id.iv_my_head)
    ImageView mIvMyHead;
    @Bind(R.id.tv_my_name)
    TextView mTvMyName;
    @Bind(R.id.tv_my_address)
    TextView mTvMyAddress;
    @Bind(R.id.iv_QR_code)
    ImageView mIvQRCode;
    @Bind(R.id.rl_personal_name)
    RelativeLayout mRlPersonalName;
//    @Bind(R.id.ll_apply_to_be_host)
//    LinearLayout mLlApplyToBeHost;
//    @Bind(R.id.ll_my_activity)
    LinearLayout mLlMyActivity;
    @Bind(R.id.settings)
    ImageView mSettings;
    @Bind(R.id.ll_settings)
    LinearLayout mLlSettings;
    @Bind(R.id.ll_zhuxiao)
    LinearLayout mLLZhuXiao;

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        mTitleTv.setText("我的");

        final Register register = MyApplication.getInstance().getRegister();

        if (register != null || register.getToken() != null) {

            Call<OwnerInfo> call = Api.getOwnerInfo().onResult("Bearer " + register.getToken());
            call.enqueue(new Callback<OwnerInfo>() {
                @Override
                public void onResponse(Response<OwnerInfo> response, Retrofit retrofit) {
                    if (response.body() != null) {

                        if (MeActivity.this == null | MeActivity.this.isFinishing()) {
                            return;
                        }

                        OwnerInfo ownerInfo = response.body();
                        mTvMyName.setText("" + ownerInfo.getUserName());
                        mTvMyAddress.setText(ownerInfo.getCommonAddress());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        mLLZhuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LbwDao lbwDao = MyApplication.getInstance().lbwDao;

                if (lbwDao != null) {
                    lbwDao.deleteAllRegister();
                }

                Intent intent = new Intent(MeActivity.this, LoginActivity.class);
                startActivity(intent);
                MeActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarRightBtn() {
        onBackPressed();
    }
}
