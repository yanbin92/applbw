package com.chinausky.lanbowan.view.adapter.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chinausky.lanbowan.controller.fragment.EstateFragment;
import com.chinausky.lanbowan.controller.fragment.HomeFragment;

/**
 * Created by fashi on 2015/7/10.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new EstateFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
