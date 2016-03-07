package com.chinausky.lanbowan.controller.activity.visitor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.controller.base.BaseActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.model.bean.VisitorInfo;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.view.custom.MyLinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/8/7.
 */
public class VisitorRegistrationActivity extends BaseActivity {

    public static final int VISITOR_REQUEST_CODE = 1;

    public static void startActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, VisitorRegistrationActivity.class);
        activity.startActivityForResult(intent, VISITOR_REQUEST_CODE);
    }

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.recycle_view_visitor_registration)
    android.support.v7.widget.RecyclerView mRecyclerView;

    private List mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_registration);
        ButterKnife.bind(this);
        mTitleTv.setText("访客登记");

        MyLinearLayoutManager manager = new MyLinearLayoutManager(VisitorRegistrationActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        VisitorRegistrationRvAdapter adapter = new VisitorRegistrationRvAdapter();

        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mDatas.add("123");
        }

        mRecyclerView.setAdapter(adapter);
    }

    public class VisitorRegistrationRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_TITLE = 0;
        private static final int TYPE_GUEST = 1;
        private static final int TYPE_TIME = 2;
        private static final int TYPE_POST = 3;
        private int x = 1;

        private ArrayList<VisitorInfo.VisitorsEntity> mVisitorList = new ArrayList();
        private String arriveDate;
        private String leaveDate;

        public class GuestVH extends RecyclerView.ViewHolder {
            @Bind(R.id.visitor_registration_guest_name)
            EditText TextViewName;
            @Bind(R.id.visitor_registration_guest_phone_num)
            EditText TextViewPhoneNum;
            @Bind(R.id.visitor_registration_guest_car_num)
            EditText TextViewCarNum;

            public GuestVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class TimeVH extends RecyclerView.ViewHolder {
            @Bind(R.id.visitor_registration_arrive_date_ll)
            LinearLayout mVisitorRegistrationArriveDateLl;
            @Bind(R.id.visitor_registration_arrive_date_tv)
            TextView mVisitorRegistrationArriveDateTV;
//            @Bind(R.id.visitor_registration_leave_date_btn)
//            Button mVisitorRegistrationLeaveDateBtn;
//            @Bind(R.id.visitor_registration_leave_date_tv)
//            TextView mVisitorRegistrationLeaveDateTv;

            public TimeVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class PostVH extends RecyclerView.ViewHolder {
            @Bind(R.id.visitor_registration_guest_add)
            Button mBtnAdd;
            @Bind(R.id.visitor_registration_btn)
            Button mButton;
            @Bind(R.id.visitor_registration_guest_post_ed)
            EditText mEditText;

            public PostVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class TitleVH extends RecyclerView.ViewHolder {
            @Bind(R.id.visitor_registration_title)
            Button mBtnTitle;

            public TitleVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 | position == 2) {
                return TYPE_TITLE;
            } else if (getItemCount() - position == 1) {
                return TYPE_POST;
            } else if (position == 1) {
                return TYPE_TIME;
            } else {
                return TYPE_GUEST;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View titleView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_regisration_title, parent, false);
            View guestView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_regisration_guest, parent, false);
            View timeView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_regisration_time, parent, false);
            View postView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_regisration_post, parent, false);

            switch (viewType) {
                case TYPE_TITLE:
                    return new TitleVH(titleView);
                case TYPE_GUEST:
                    return new GuestVH(guestView);
                case TYPE_TIME:
                    return new TimeVH(timeView);
                case TYPE_POST:
                    return new PostVH(postView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
            if (viewHolder instanceof TitleVH) {
                if (i == 0) {
                    ((TitleVH) viewHolder).mBtnTitle.setText("到访时间");
                } else {
                    ((TitleVH) viewHolder).mBtnTitle.setText("到访客人");
                }
            } else if (viewHolder instanceof GuestVH) {
                final VisitorInfo.VisitorsEntity visitorsEntity = new VisitorInfo.VisitorsEntity();
                mVisitorList.add(visitorsEntity);

                ((GuestVH) viewHolder).TextViewName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String visitorName = ((GuestVH) viewHolder).TextViewName.getText().toString();
                        visitorsEntity.setVisitorName(visitorName);
                    }
                });

                ((GuestVH) viewHolder).TextViewPhoneNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String phoneNum = ((GuestVH) viewHolder).TextViewPhoneNum.getText().toString();
                        if (GlobalUtils.isMobileNum(phoneNum)) {
                            visitorsEntity.setMobileNumber(phoneNum);
                        }
                    }
                });

                ((GuestVH) viewHolder).TextViewCarNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String carNum = ((GuestVH) viewHolder).TextViewCarNum.getText().toString();
                        if (GlobalUtils.isCarNum(carNum)) {
                            visitorsEntity.setPlateNumber(carNum);
                        }
                    }
                });

            } else if (viewHolder instanceof TimeVH) {
                final Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int hour = c.get(Calendar.HOUR);
                final int minute = c.get(Calendar.MINUTE);

                ((TimeVH) viewHolder).mVisitorRegistrationArriveDateLl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(VisitorRegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                                new TimePickerDialog(VisitorRegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        ((TimeVH) viewHolder).mVisitorRegistrationArriveDateTV
                                                .setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth + " " + hourOfDay + ":" + minute);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute, 00);

                                        arriveDate = simpleDateFormat.format(gregorianCalendar.getTime());
                                    }
                                }, hour, minute, true).show();
                            }
                        }, year, month, day).show();
                    }
                });
//                ((TimeVH) viewHolder).mVisitorRegistrationLeaveDateBtn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        new DatePickerDialog(VisitorRegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
//                                new TimePickerDialog(VisitorRegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                                    @Override
//                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                        ((TimeVH) viewHolder).mVisitorRegistrationLeaveDateTv
//                                                .setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth + " " + hourOfDay + ":" + minute);
//
//                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute, 00);
//
//                                        leaveDate = simpleDateFormat.format(gregorianCalendar.getTime());
//                                    }
//                                }, hour, minute, true).show();
//                            }
//                        }, year, month, day).show();
//                    }
//                });
            } else if (viewHolder instanceof PostVH) {

                ((PostVH) viewHolder).mBtnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addData(i);
                    }
                });


                ((PostVH) viewHolder).mButton.setOnClickListener(new View.OnClickListener() {
                    public long lastClick;

                    @Override
                    public void onClick(View v) {
                        //大于3秒才可通过
                        if (System.currentTimeMillis() - lastClick <= 3000) {
                            return;
                        }
                        lastClick = System.currentTimeMillis();

                        if (((PostVH) viewHolder).mEditText.getText().toString().isEmpty()) {
                            GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(), "请输入留言");
                            return;
                        }

                        for (VisitorInfo.VisitorsEntity visitorsEntity : mVisitorList) {
                            if (TextUtils.isEmpty(visitorsEntity.getVisitorName())
                                    && TextUtils.isEmpty(visitorsEntity.getPlateNumber())
                                    && TextUtils.isEmpty(visitorsEntity.getMobileNumber())) {
                                GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(), "访客信息有误,请填写正确");
                                return;
                            }
                        }

                        if (TextUtils.isEmpty(arriveDate)) {
                            GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(), "请输入日期");
                            return;
                        }

                        VisitorInfo visitorInfo = new VisitorInfo();
                        visitorInfo.setMyAddress("0");
                        visitorInfo.setRemark(((PostVH) viewHolder).mEditText.getText().toString());
                        visitorInfo.setVisitors(mVisitorList);

                        visitorInfo.setArriveDate(arriveDate);
                        visitorInfo.setLeaveDate(leaveDate);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        visitorInfo.setBookingDate(simpleDateFormat.format(new Date()));

                        final Register register = MyApplication.getInstance().getRegister();

                        if (register.getToken() == null) {
                            return;
                        }

                        Call<RequestMessage> call = Api.postVisitorInfo().onResult("Bearer " + register.getToken(), visitorInfo);

                        call.enqueue(new Callback<RequestMessage>() {
                            @Override
                            public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                                if (response.body() == null) {
                                    return;
                                }
                                GlobalUtils.showToastShort(VisitorRegistrationActivity.this, response.body().getMessage());

                                VisitorRegistrationActivity.this.setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                GlobalUtils.showToastShort(VisitorRegistrationActivity.this, "添加失败,请检查网络连接");
                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void addData(int position) {
            mDatas.add(position, "Insert One");
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
