package com.chinausky.lanbowan.controller.activity.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.view.custom.ClickLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, HelpActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.ll_convenient_phone)
    ClickLinearLayout mConvenientPhone;

    @Bind(R.id.ll_property_certificate)
    ClickLinearLayout mPropertyCertificate;

    @Bind(R.id.ll_provide_house)
    ClickLinearLayout mProvideHouse;

    @Bind(R.id.ll_rent_phone)
    ClickLinearLayout mRentPhone;

    @Bind(R.id.ll_common_question)
    ClickLinearLayout mCommonQuestion;

    @Bind(R.id.ll_user_know)
    ClickLinearLayout mUserKnow;

    @Bind(R.id.help_estate_center_tv)
    TextView mHelpEstateCenterTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        mTitleTv.setText("帮助中心");

        mConvenientPhone.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                EstateCenterActivity.startActivity(HelpActivity.this);
            }
        });

        mPropertyCertificate.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                OwnerKnowsActivity.startActivity(HelpActivity.this);
            }
        });

        mProvideHouse.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                RoomDeliverActivity.startActivity(HelpActivity.this);

            }
        });

        mRentPhone.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                OwnerFeedbackActivity.startActivity(HelpActivity.this);
            }
        });

        mCommonQuestion.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                ConvenienceGuideActivity.startActivity(HelpActivity.this);
            }
        });

        mUserKnow.setClickListener(new ClickLinearLayout.ClickListener() {
            @Override
            public void onClick() {
                PropertyPermitsActivity.startActivity(HelpActivity.this);
            }
        });
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
