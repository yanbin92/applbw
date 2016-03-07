package com.chinausky.lanbowan.controller.activity.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConvenienceGuideActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, ConvenienceGuideActivity.class);
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
        setContentView(R.layout.activity_convenience_guide);
        ButterKnife.bind(this);
        mTitleTv.setText("帮助中心");
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
