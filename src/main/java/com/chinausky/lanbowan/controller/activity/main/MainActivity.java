package com.chinausky.lanbowan.controller.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.controller.activity.me.MeActivity;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.controller.fragment.HomeFragment;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import java.io.BufferedOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private long exitTime = 0;

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLeftBtn.setVisibility(View.GONE);
        mRightBtn.setBackgroundResource(R.drawable.personal_gray);

        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction transaction = fragment.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();

        transaction.replace(R.id.main_fragment, homeFragment).addToBackStack("main").commit();

        //小米更新
        XiaomiUpdateAgent.update(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                GlobalUtils.showToastShort(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.toolbar_right_btn)
    void onClickToolbarRightBtn() {
        Intent intent = new Intent(this, MeActivity.class);
        startActivity(intent);
    }
}
