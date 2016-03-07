package com.chinausky.lanbowan.controller.activity.myparking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.view.adapter.recyclerview.MyParkingRvAdapter;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.GetMyCarInfo;
import com.chinausky.lanbowan.model.bean.GetMyParkingCostMessage;
import com.chinausky.lanbowan.model.bean.PostMyCar;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.controller.fragment.dialog.MyParkingPayDialogFragment;
import com.chinausky.lanbowan.view.custom.DividerItemDecoration;
import com.chinausky.lanbowan.view.custom.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/8/5.
 */
public class MyParkingActivity extends BaseActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, MyParkingActivity.class);
        activity.startActivity(intent);
    }

    private MyParkingRvAdapter mAdapter;

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.recycle_view_may_parking)
    android.support.v7.widget.RecyclerView mRecyclerView;

    private ArrayList<GetMyParkingCostMessage> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parking);
        ButterKnife.bind(this);
        mTitleTv.setText("我的车位");
        mRightBtn.setBackgroundResource(R.drawable.add);

        MyLinearLayoutManager manager = new MyLinearLayoutManager(MyParkingActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MyParkingRvAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyParkingActivity.this, null));


        final Register register = MyApplication.getInstance().getRegister();

        mAdapter.setOnClickLister(new MyParkingRvAdapter.OnClickLister() {
            @Override
            public void onClick(int i) {
                mAdapter.setData(i);
            }
        });
        mAdapter.setOnDeleteClickLister(new MyParkingRvAdapter.OnDeleteClickLister() {
            @Override
            public void onClick(final HashMap ownerCarId, String plateNumber) {
                new AlertDialog.Builder(MyParkingActivity.this)
                        .setTitle("删除车牌")
                        .setMessage("车牌号  " + plateNumber)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (register.getToken() != null) {
                                    deleteMyCar(ownerCarId, register.getToken());
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        mAdapter.setPayClickLister(new MyParkingRvAdapter.OnPayClickLister() {

            @Override
            public void onClick() {
                MyParkingPayDialogFragment.startFragment(MyParkingActivity.this, list);
            }
        });

        mRightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MyParkingActivity.this);
                editText.setHint("例如:沪A12345");
                int margin = GlobalUtils.dip2pix(MyParkingActivity.this, 15);

                new AlertDialog.Builder(MyParkingActivity.this)
                        .setTitle("请输入要添加的车牌号")
                        .setView(editText, margin, margin, margin, 0)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PostMyCar myCarInfo = new PostMyCar();
                                String plateNumber = editText.getText().toString();

                                if (plateNumber != null && GlobalUtils.isCarNum(plateNumber) && register.getToken() != null) {
                                    myCarInfo.setPlateNumber(plateNumber);
                                    postMyCar(myCarInfo, register.getToken());
                                } else {
                                    GlobalUtils.showToastShort(MyParkingActivity.this, "输入的车牌号码不合法");
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        mRecyclerView.setVisibility(View.INVISIBLE);
        if (register.getToken() != null) {
            getMyCar(register.getToken());
            getMyParkingCost(register.getToken());
        }
    }

    private void getMyCar(String token) {

        Call<List<GetMyCarInfo>> call = Api.getMyCarApi().onResult("Bearer " + token);
        call.enqueue(new Callback<List<GetMyCarInfo>>() {
            @Override
            public void onResponse(Response<List<GetMyCarInfo>> response, Retrofit retrofit) {
                if (response.body() != null) {
                    mAdapter.setMyCarInfoList(response.body());
                    mAdapter.notifyDataSetChanged();
                }

                if (mRecyclerView != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GlobalUtils.showToastShort(MyParkingActivity.this, "查询失败,请检查网络是否正常");
                if (MyParkingActivity.this != null && MyParkingActivity.this.isDestroyed()) {
                    return;
                }
                if (mRecyclerView != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void deleteMyCar(HashMap ownerCarId, final String token) {
        retrofit.Call<RequestMessage> call = Api.deleteMyCarApi().onResult("Bearer " + token, ownerCarId);
        call.enqueue(new Callback<RequestMessage>() {
            @Override
            public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(),
                        "车牌删除成功");

                mAdapter.ChangeWithData();
                getMyCar(token);
            }

            @Override
            public void onFailure(Throwable t) {
                GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(),
                        "车牌删除失败,请检查网络连接");
            }
        });
    }

    private void postMyCar(PostMyCar myCarInfo, final String token) {
        Call<RequestMessage> call = Api.postMyCarApi().onResult("Bearer " + token, myCarInfo);
        call.enqueue(new Callback<RequestMessage>() {
            @Override
            public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(),
                        response.body().getMessage());

                mAdapter.ChangeWithData();
                getMyCar(token);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getMyParkingCost(final String token) {
        Call<List<GetMyParkingCostMessage>> call = Api.getMyParkingCost().onResult("Bearer " + token);
        call.enqueue(new Callback<List<GetMyParkingCostMessage>>() {
            @Override
            public void onResponse(Response<List<GetMyParkingCostMessage>> response, Retrofit retrofit) {
                list = (ArrayList) response.body();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_right_btn)
    void onClickToolbarRightBtn() {

    }
}
