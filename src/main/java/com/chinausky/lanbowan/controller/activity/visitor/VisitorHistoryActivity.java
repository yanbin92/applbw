package com.chinausky.lanbowan.controller.activity.visitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.view.adapter.recyclerview.VisitorHistoryRvAdapter;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.VisitorInfo;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.view.custom.MyLinearLayoutManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/8/27.
 */
public class VisitorHistoryActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, VisitorHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.visitor_num_tv)
    TextView mVisitorNumTv;

    @Bind(R.id.recycle_view_visitor_history)
    android.support.v7.widget.RecyclerView mRecyclerView;

    private VisitorHistoryRvAdapter mAdapter;
    private int mPagerNum = 1;
    private boolean canLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_visitor_history);
        ButterKnife.bind(this);
        mRightBtn.setBackgroundResource(R.drawable.add);
        mTitleTv.setText("访客历史");

        final MyLinearLayoutManager manager = new MyLinearLayoutManager(VisitorHistoryActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VisitorHistoryRvAdapter();
        mAdapter.setOnClickLister(new VisitorHistoryRvAdapter.onClickLister() {
            @Override
            public void onClick(VisitorInfo visitorInfo) {


                VisitorInfoActivity.startActivity(VisitorHistoryActivity.this, visitorInfo);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                        && manager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {

                    final Register register = MyApplication.getInstance().getRegister();

                    if (register.getToken() == null) {
                        return;
                    }
                    if (canLoadMore) {
                        getRegisterInfo(register, String.valueOf(mPagerNum));
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Register register = MyApplication.getInstance().getRegister();

        if (register.getToken() == null) {
            return;
        }

        Call<Integer> call = Api.getVisitorNum().onResult("Bearer " + register.getToken());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                String visitorNum = response.body().toString();
                mVisitorNumTv.setText(visitorNum);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        getRegisterInfo(register, "1");
    }

    public void onRefresh() {
        final Register register = MyApplication.getInstance().getRegister();
        if (register.getToken() == null) {
            return;
        }
        String token = register.getToken();

        Call<List<VisitorInfo>> call1 = Api.getVisitorInfo().onResult("Bearer " + token, "1");
        call1.enqueue(new Callback<List<VisitorInfo>>() {
            @Override
            public void onResponse(Response<List<VisitorInfo>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                mAdapter.reFreshVisitorInfoList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getRegisterInfo(Register register, final String pageNum) {
        Call<List<VisitorInfo>> call1 = Api.getVisitorInfo().onResult("Bearer " + register.getToken(), pageNum);

        call1.enqueue(new Callback<List<VisitorInfo>>() {
            @Override
            public void onResponse(Response<List<VisitorInfo>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                if (pageNum.equals("1")) {
                    if (mAdapter.mVisitorInfoList != null && mAdapter.mVisitorInfoList.size() == 0) {
                        mAdapter.setVisitorInfoList(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (response.body().size() > 0) {
                        mAdapter.setVisitorInfoList(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }


                if (response.body().size() == 10) {
                    mPagerNum++;
                } else if (!pageNum.equals("1")) {
                    GlobalUtils.showToastShort(VisitorHistoryActivity.this, "没有更多记录");
                    canLoadMore=false;
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VisitorInfoActivity.VISITOR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onRefresh();
            }else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_right_btn)
    void onClickToolbarRightBtn() {

        VisitorRegistrationActivity.startActivity(this);
    }


}
