package com.chinausky.lanbowan.controller.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.aipuwaton.CameraActivity;
import com.chinausky.lanbowan.controller.activity.help.HelpActivity;
import com.chinausky.lanbowan.controller.activity.maintenance.MaintenanceHistoryActivity;
import com.chinausky.lanbowan.controller.activity.myparking.MyParkingActivity;
import com.chinausky.lanbowan.controller.activity.visitor.VisitorHistoryActivity;
import com.chinausky.lanbowan.controller.base.BaseFragment;
import com.chinausky.lanbowan.evideo.EvideoMainActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.GetPropertyAnnouncementMessage;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.view.custom.ClickFrameLayout;
import com.evideo.voip.EvideoVoipService;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fashi on 2015/7/10.
 */
public class HomeFragment extends BaseFragment {

    private View mView;

    @Bind(R.id.tv_property_announcement)
    TextView mTvPropertyAnnouncement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,mView);
        setViews();

        Register register = MyApplication.getInstance().getRegister();

        if (register.getToken() == null) {
            return null;
        }

        Call<GetPropertyAnnouncementMessage> call1 = Api.getPropertyAnnouncement().onResult("Bearer " + register.getToken());

        call1.enqueue(new Callback<GetPropertyAnnouncementMessage>() {
            @Override
            public void onResponse(Response<GetPropertyAnnouncementMessage> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }
                mTvPropertyAnnouncement.setText(response.body().getContext());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        return mView;
    }

    private void setViews() {
        ClickFrameLayout wodechepai = (ClickFrameLayout) mView.findViewById(R.id.wodechepai);
        ClickFrameLayout fangke = (ClickFrameLayout) mView.findViewById(R.id.fangke);
        ClickFrameLayout menjingka = (ClickFrameLayout) mView.findViewById(R.id.menjingka);
        ClickFrameLayout jiating = (ClickFrameLayout) mView.findViewById(R.id.jiating);
        ClickFrameLayout weixiufuwu = (ClickFrameLayout) mView.findViewById(R.id.weixiufuwu);
        ClickFrameLayout jiankong = (ClickFrameLayout) mView.findViewById(R.id.jiankong);
        ClickFrameLayout banghzuzhongxing = (ClickFrameLayout) mView.findViewById(R.id.banghzuzhongxing);
//
        wodechepai.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                MyParkingActivity.startActivity(getActivity());
            }
        });

        fangke.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                VisitorHistoryActivity.startActivity(getActivity());
            }
        });

        menjingka.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                new WaitVoipServiceTask().execute();
            }
        });

        jiating.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {

            }
        });

        weixiufuwu.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                MaintenanceHistoryActivity.startActivity(getActivity());
            }
        });

        jiankong.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });

        banghzuzhongxing.setClickListener(new ClickFrameLayout.ClickListener() {
            @Override
            public void onClick() {
                HelpActivity.startActivity(getActivity());
            }
        });

    }

    private class WaitVoipServiceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!EvideoVoipService.isReady()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
//                    throw new RuntimeException(
//                            "waiting thread sleep() has been interrupted");
                    GlobalUtils.showToastShort(getActivity(), "门禁卡初始化出错,请重试");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            EvideoMainActivity.startActivity(getActivity());
        }

    }
}
