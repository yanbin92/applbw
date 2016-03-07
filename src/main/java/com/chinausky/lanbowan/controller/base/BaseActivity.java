package com.chinausky.lanbowan.controller.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chinausky.lanbowan.MyApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by fashi on 2015/7/15.
 */
public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = MyApplication.getRefWatcher(this);
		refWatcher.watch(this);
	}
}
