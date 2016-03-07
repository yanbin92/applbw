package com.chinausky.lanbowan.view.adapter.other;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Woody on 2015/8/19.
 */
public class BannerAdapter extends PagerAdapter {

    private List<ImageView> mImageViewList;

    public BannerAdapter(List<ImageView> mImageViewList) {
        super();
        this.mImageViewList = mImageViewList;
    }

    /**
     * 该方法将返回所包含的 Item总个数。为了实现一种循环滚动的效果，返回了基本整型的最大值，这样就会创建很多的Item,
     * 其实这并非是真正的无限循环。
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * 判断出去的view是否等于进来的view 如果为true直接复用
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    /**
     * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来，就是position，
     * 因为mImageViewList只有3条数据，而position将会取到很大的值，
     * 所以使用取余数的方法来获取每一条数据项。
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
     //Warning：不要在这里调用removeView
    }

    /**
     * 创建一个view
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % mImageViewList.size();
        if (position < 0) {
            position = mImageViewList.size() + position;
        }
        ImageView imageView = mImageViewList.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = imageView.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }
}
