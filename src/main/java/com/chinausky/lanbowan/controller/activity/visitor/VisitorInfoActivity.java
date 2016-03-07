package com.chinausky.lanbowan.controller.activity.visitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.model.bean.VisitorInfo;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Created by succlz123 on 15/8/27.
 */
public class VisitorInfoActivity extends AppCompatActivity {

    public static final int VISITOR_REQUEST_CODE = 1;

    public static void startActivity(AppCompatActivity activity, VisitorInfo visitorInfo) {
        Intent intent = new Intent(activity, VisitorInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("visitorInfo", visitorInfo);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, VISITOR_REQUEST_CODE);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;
    @Bind(R.id.toolbar_title)
    TextView mTitleTv;
    @Bind(R.id.toolbar_right_btn)
    ImageView mToolbarRightBtn;
    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;
    @Bind(R.id.arrive_time)
    TextView mArriveTime;
    @Bind(R.id.visitor_info)
    LinearLayout mVisitorInfo;
    @Bind(R.id.arriveDateTimestamp)
    TextView mArriveDateTimestamp;
    @Bind(R.id.leaveDateTimestamp)
    TextView mLeaveDateTimestamp;
    @Bind(R.id.remark)
    TextView mRemark;
    @Bind(R.id.btn_delete)
    Button mDeleteBtn;
    //    @Bind(R.id.recycle_view_visitor_history)
//    android.support.v7.widget.RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_info);
        ButterKnife.bind(this);
        mTitleTv.setText("访客详情");

        final VisitorInfo visitorInfo = getIntent().getExtras().getParcelable("visitorInfo");

        if (visitorInfo == null) {
            return;
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (visitorInfo.getArriveDate() != null) {
            try {
                Date arriveDt = sdf1.parse(visitorInfo.getArriveDate());

                mArriveTime.setText(sdf2.format(arriveDt));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (visitorInfo.getArriveDateTimestamp() != null) {
            try {
                Date dt = sdf1.parse(visitorInfo.getArriveDateTimestamp());

                mArriveDateTimestamp.setText(sdf2.format(dt));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (visitorInfo.getLeaveDateTimestamp() != null) {

            try {
                Date dt = sdf1.parse(visitorInfo.getLeaveDateTimestamp());

                mLeaveDateTimestamp.setText(sdf2.format(dt));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (visitorInfo.getRemark() != null) {
            mRemark.setText(visitorInfo.getRemark());
        }

        List<VisitorInfo.VisitorsEntity> visitors = visitorInfo.getVisitors();

        for (VisitorInfo.VisitorsEntity visitorsEntity : visitors) {
            String name = visitorsEntity.getVisitorName();
            String phone = visitorsEntity.getMobileNumber();
            String carNum = visitorsEntity.getPlateNumber();

            if (!TextUtils.isEmpty(name) | !TextUtils.isEmpty(phone) | !TextUtils.isEmpty(carNum)) {


                LinearLayout linearLayout = new LinearLayout(VisitorInfoActivity.this);
                LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                linearLayout.setLayoutParams(llParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView nameView = new TextView(VisitorInfoActivity.this);
                TextView phoneView = new TextView(VisitorInfoActivity.this);
                TextView carNumView = new TextView(VisitorInfoActivity.this);

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT);

                tvParams.weight = 1;

                nameView.setLayoutParams(tvParams);
                phoneView.setLayoutParams(tvParams);
                phoneView.setGravity(Gravity.CENTER);
                carNumView.setLayoutParams(tvParams);
                carNumView.setGravity(Gravity.CENTER);

                if (name != null) {
                    nameView.setText(name);
                }
                if (phone != null) {
                    phoneView.setText(phone);

                }
                if (carNum != null) {
                    carNumView.setText(carNum);
                }

                linearLayout.addView(nameView);
                linearLayout.addView(phoneView);
                linearLayout.addView(carNumView);

                mVisitorInfo.addView(linearLayout);
            }
        }

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(VisitorInfoActivity.this)
                        .setTitle("警告")
                        .setMessage("确定要删除这个记录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (visitorInfo.getBookingId() == null) {
                                    return;
                                }

                                HashMap map = new HashMap();
                                map.put("bookingId", visitorInfo.getBookingId());

                                final Register register = MyApplication.getInstance().getRegister();

                                if (register.getToken() == null) {
                                    return;
                                }

                                Call<RequestMessage> call = Api.deleteVisitorInfo().onResult("Bearer " + register.getToken(), map);

                                call.enqueue(new Callback<RequestMessage>() {
                                    @Override
                                    public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                                        if (response.body() == null) {
                                            return;
                                        }
                                        GlobalUtils.showToastShort(VisitorInfoActivity.this, "记录已删除");
                                        VisitorInfoActivity.this.setResult(RESULT_OK);
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
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
