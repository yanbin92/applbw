package com.chinausky.lanbowan.controller.activity.Estate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.activity.myparking.MyParkingActivity;
import com.chinausky.lanbowan.controller.activity.visitor.VisitorHistoryActivity;
import com.chinausky.lanbowan.controller.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnerServiceActivity extends BaseActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, OwnerServiceActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.car_place)
    LinearLayout mCarPlace;

//    @Bind(R.id.maintain_service)
//    LinearLayout mMaintainService;

    @Bind(R.id.visitor)
    LinearLayout mVisitor;

//    @Bind(R.id.door_card)
//    LinearLayout mDoorCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_service);
        ButterKnife.bind(this);

        mTitleTv.setText("业主服务");

        mCarPlace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyParkingActivity.startActivity(OwnerServiceActivity.this);
            }
        });

        mVisitor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VisitorHistoryActivity.startActivity(OwnerServiceActivity.this);
            }
        });

//        mMaintainService.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                MaintenanceHistoryActivity.startActivity(OwnerServiceActivity.this);
//            }
//        });
//
//        mDoorCard.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                EvideoMainActivity.startActivity(OwnerServiceActivity.this);
//            }
//        });
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
