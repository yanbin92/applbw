package com.chinausky.lanbowan.controller.activity.maintenance;

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
import com.chinausky.lanbowan.view.adapter.recyclerview.MaintenanceHistoryRvAdapter;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RepairInfo;
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
 * Created by succlz123 on 15/8/28.
 */
public class MaintenanceHistoryActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, MaintenanceHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.recycle_view_maintenance_history)
    android.support.v7.widget.RecyclerView mRecyclerView;

    @Bind(R.id.maintenance_num_tv)
    TextView mRepairNumTV;
    public static final int REPAIR_REQUEST_CODE = 2;

    private MaintenanceHistoryRvAdapter mAdapter;
    private int mPagerNum = 1;
    private boolean canLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_history);
        ButterKnife.bind(this);
        mRightBtn.setBackgroundResource(R.drawable.add);
        mTitleTv.setText("维修历史 ");

        final MyLinearLayoutManager manager = new MyLinearLayoutManager(MaintenanceHistoryActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new MaintenanceHistoryRvAdapter();
        mAdapter.setOnClickLister(new MaintenanceHistoryRvAdapter.onClickLister() {
            @Override
            public void onClick(RepairInfo repairInfo) {

                MaintenanceInfoActivity.startActivity(MaintenanceHistoryActivity.this, REPAIR_REQUEST_CODE, repairInfo);
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

                    String token = register.getToken();
                    if (canLoadMore) {
                        getRepairInfo(token, String.valueOf(mPagerNum));
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

        String token = register.getToken();

        Call<Integer> call = Api.getRepairNum().onResult("Bearer " + token);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                String repairNum = response.body().toString();
                mRepairNumTV.setText(repairNum);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        getRepairInfo(token, "1");
    }

    public void onRefresh() {

        final Register register = MyApplication.getInstance().getRegister();

        if (register.getToken() == null) {
            return;
        }

        String token = register.getToken();

        Call<List<RepairInfo>> call1 = Api.getRepairInfo().onResult("Bearer " + token, "1");

        call1.enqueue(new Callback<List<RepairInfo>>() {
            @Override
            public void onResponse(Response<List<RepairInfo>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                mAdapter.reFreshRepairInfoList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getRepairInfo(String token, final String pageNum) {

        Call<List<RepairInfo>> call1 = Api.getRepairInfo().onResult("Bearer " + token, pageNum);

        call1.enqueue(new Callback<List<RepairInfo>>() {
            @Override
            public void onResponse(Response<List<RepairInfo>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }

                if (pageNum.equals("1")) {
                    if (mAdapter.getRepairInfoList() != null && mAdapter.getRepairInfoList().size() == 0) {
                        mAdapter.setRepairInfoList(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (response.body().size() > 0) {
                        mAdapter.setRepairInfoList(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }


                if (response.body().size() == 10) {
                    mPagerNum++;
                } else if (!pageNum.equals("1")) {
                    GlobalUtils.showToastShort(MaintenanceHistoryActivity.this, "没有更多记录");
                    canLoadMore = false;
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REPAIR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onRefresh();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_right_btn)
    void onClickToolbarRightBtn() {
        MaintenanceServiceActivity.startActivity(MaintenanceHistoryActivity.this, REPAIR_REQUEST_CODE);
    }


}
