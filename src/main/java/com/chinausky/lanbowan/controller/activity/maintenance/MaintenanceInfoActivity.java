package com.chinausky.lanbowan.controller.activity.maintenance;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RepairInfo;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.controller.activity.photo.HeeActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
public class MaintenanceInfoActivity extends AppCompatActivity {

    public static void startActivity(AppCompatActivity activity, int requestCode, RepairInfo repairInfo) {
        Intent intent = new Intent(activity, MaintenanceInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("repairInfo", repairInfo);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;
    @Bind(R.id.toolbar_title)
    TextView mTitleTv;
    @Bind(R.id.toolbar_right_btn)
    ImageView mToolbarRightBtn;
    @Bind(R.id.repairType_tv)
    TextView mRepairTypeTv;
    @Bind(R.id.repairStatus_tv)
    TextView mRepairStatusTv;
    @Bind(R.id.repairMessage_tv)
    TextView mRepairMessageTv;
    @Bind(R.id.iv_maintain_photo1)
    SimpleDraweeView mIvMaintainPhoto1;
    @Bind(R.id.iv_maintain_photo2)
    SimpleDraweeView mIvMaintainPhoto2;
    @Bind(R.id.iv_maintain_photo3)
    SimpleDraweeView mIvMaintainPhoto3;

    @Bind(R.id.btn_delete_repair)
    Button mButton;
    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;
    @Bind(R.id.applyDate_tv)
    TextView mApplyDateTv;
    @Bind(R.id.repairDate_tv)
    TextView mRepairDateTv;
    //    @Bind(R.id.recycle_view_visitor_history)
//    android.support.v7.widget.RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainteance_info);
        ButterKnife.bind(this);
        mTitleTv.setText("维修详情");

//        MyLinearLayoutManager manager = new MyLinearLayoutManager(VisitorHistoryActivity.this);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
////        VisitorHistoryRvAdapter adapter = new VisitorHistoryRvAdapter();
//        adapter.setOnClickLister(new VisitorHistoryRvAdapter.onClickLister() {
//            @Override
//            public void onClick() {
//
//            }
//        });
//        mRecyclerView.setAdapter(adapter);


        final RepairInfo repairInfo = getIntent().getExtras().getParcelable("repairInfo");

        if (repairInfo == null) {
            return;
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (repairInfo.getApplyDate() != null) {
            try {
                Date dt = sdf1.parse(repairInfo.getApplyDate());
                mApplyDateTv.setText(sdf2.format(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (repairInfo.getRepairDate() != null) {
            try {
                Date dt = sdf1.parse(repairInfo.getRepairDate());
                mRepairDateTv.setText(sdf2.format(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (repairInfo.getRepairType() != null) {
            mRepairTypeTv.setText(repairInfo.getRepairType());
        }

        if (repairInfo.getRepairStatus() != null) {
            mRepairStatusTv.setText(repairInfo.getRepairStatus());
        }

        if (repairInfo.getRepairMessage() != null) {
            mRepairMessageTv.setText(repairInfo.getRepairMessage());
        }

        if (repairInfo.getUrlPaths().size() > 0) {

            if (repairInfo.getUrlPaths().size() == 1) {
                mIvMaintainPhoto1.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl()));
                mIvMaintainPhoto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl());
                        startActivity(intent);
                    }
                });
            }

            if (repairInfo.getUrlPaths().size() == 2) {
                mIvMaintainPhoto1.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl()));
                mIvMaintainPhoto2.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(1).getRelativeUrl()));

                mIvMaintainPhoto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl());
                        startActivity(intent);
                    }
                });

                mIvMaintainPhoto2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(1).getRelativeUrl());
                        startActivity(intent);
                    }
                });
            }

            if (repairInfo.getUrlPaths().size() == 3) {
                mIvMaintainPhoto1.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl()));
                mIvMaintainPhoto2.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(1).getRelativeUrl()));
                mIvMaintainPhoto3.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(2).getRelativeUrl()));

                mIvMaintainPhoto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl());
                        startActivity(intent);
                    }
                });
                mIvMaintainPhoto2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(1).getRelativeUrl());
                        startActivity(intent);
                    }
                });
                mIvMaintainPhoto3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MaintenanceInfoActivity.this, HeeActivity.class);
                        intent.putExtra("url", "http://" + repairInfo.getUrlPaths().get(2).getRelativeUrl());
                        startActivity(intent);
                    }
                });
            }
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MaintenanceInfoActivity.this)
                        .setTitle("警告")
                        .setMessage("确定要删除这个记录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                if (repairInfo.getRepairId() == 0) {
//                                    return;
//                                }

                                final Register register = MyApplication.getInstance().getRegister();

                                if (register.getToken() == null) {
                                    return;
                                }

                                HashMap map = new HashMap();
                                map.put("myRepairId", repairInfo.getRepairId());

                                Call<RequestMessage> call = Api.deleteRepairInfo().onResult("Bearer " + register.getToken(), map);
                                call.enqueue(new Callback<RequestMessage>() {
                                    @Override
                                    public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {

                                        if (response.body() == null) {
                                            return;
                                        }

                                        GlobalUtils.showToastShort(MaintenanceInfoActivity.this, "记录已删除");

                                        MaintenanceInfoActivity.this.setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
