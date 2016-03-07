package com.chinausky.lanbowan.controller.activity.help;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EstateCenterActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, EstateCenterActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_center);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
