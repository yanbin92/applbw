package com.chinausky.lanbowan.controller.base;

import android.support.v4.app.Fragment;

import com.chinausky.lanbowan.MyApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Created by fashi on 2015/7/15.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
