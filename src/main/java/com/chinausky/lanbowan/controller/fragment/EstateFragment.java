package com.chinausky.lanbowan.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by fashi on 2015/7/10.
 */
public class EstateFragment extends BaseFragment {

//    @Bind(R.id.chewei)
//    FrameLayout chewei;
//    @Bind(R.id.dengji)
//    FrameLayout dengji;
//    @Bind(R.id.weixiu)
//    FrameLayout weixiu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estate, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}