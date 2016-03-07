package com.chinausky.lanbowan.controller.activity.myparking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.activity.visitor.VisitorHistoryActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyParkingHomeActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, MyParkingHomeActivity.class);
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
        setContentView(R.layout.activity_owner_service);
        ButterKnife.bind(this);
        mTitleTv.setText("我的车牌");
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_right_btn)
    void onClickToolbarRightBtn() {
    }

    @OnClick(R.id.car_place)
    void onClickCarPlace() {
        MyParkingActivity.startActivity(this);
    }

    @OnClick(R.id.visitor)
    void onClickVisitor() {
        VisitorHistoryActivity.startActivity(this);
    }
}
