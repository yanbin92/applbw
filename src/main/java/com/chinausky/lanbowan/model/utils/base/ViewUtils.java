package com.chinausky.lanbowan.model.utils.base;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 * Created by fashi on 2015/7/7.
 */
public class ViewUtils {

    /**
     * 显示Toolbar 默认标题
     *
     * @param context
     * @param toolbar
     */
    public static void setToolbar(AppCompatActivity context, Toolbar toolbar, Boolean WithHomeButton) {
        context.setSupportActionBar(toolbar);
        if (WithHomeButton) {
            ActionBar actionBar = context.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    /**
     * 显示Toolbar 添加自定义标题
     *
     * @param context
     * @param toolbar
     */
    public static void setToolbar(AppCompatActivity context, Toolbar toolbar, Boolean WithHomeButton, String title) {
        if (title != null) {
            toolbar.setTitle(title);
        }
        context.setSupportActionBar(toolbar);
        if (WithHomeButton) {
            ActionBar actionBar = context.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

//    public static void setDrawerToggle(Activity context, DrawerLayout drawerLayout, Toolbar toolbar) {
//        ActionBarDrawerToggle drawerToggle =
//                new ActionBarDrawerToggle(context, drawerLayout, toolbar, R.string.open, R.string.close) {
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
//                        super.onDrawerOpened(drawerView);
//                    }
//
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                        super.onDrawerClosed(drawerView);
//                    }
//                };
//        drawerToggle.syncState();
//        drawerLayout.setDrawerListener(drawerToggle);
//        drawerLayout.setScrimColor(MyApplication.getInstance()
//                .getApplicationContext().getResources().getColor(R.color.shadow_white));
//    }
}
