package com.chinausky.lanbowan.view.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.VisitorInfo;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/8/27.
 */
public class VisitorHistoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_INFO = 1;

    private onClickLister mOnClickLister;
    public List<VisitorInfo> mVisitorInfoList = new ArrayList<>();

    public List<VisitorInfo> getVisitorInfoList() {
        return mVisitorInfoList;
    }

    public void setVisitorInfoList(List<VisitorInfo> visitorInfoList) {
        mVisitorInfoList.addAll(visitorInfoList);
    }

    public void reFreshVisitorInfoList(List<VisitorInfo> visitorInfoList) {
        mVisitorInfoList = visitorInfoList;
    }

    public interface onClickLister {
        void onClick(VisitorInfo visitorInfo);
    }

    public onClickLister getOnClickLister() {
        return mOnClickLister;
    }

    public void setOnClickLister(onClickLister onClickLister) {
        mOnClickLister = onClickLister;
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        public HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class InfoVH extends RecyclerView.ViewHolder {
        @Bind(R.id.visitor_history_data)
        TextView mVisitorHistoryData;
        @Bind(R.id.visitor_history_name)
        TextView mVisitorHistoryName;
        @Bind(R.id.visitor_history_arrive_time)
        TextView mVisitorHistoryArriveTime;
        @Bind(R.id.visitor_history_leave_time)
        TextView mVisitorHistoryLeaveTime;
        @Bind(R.id.visitor_history_info)
        LinearLayout mVisitorHistoryInfo;

        public InfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position % 2 == 0) {
//            return TYPE_HEADER;
//        } else {
//        }
        return TYPE_INFO;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View timeView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_history_time, parent, false);
        View infoView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_history_info, parent, false);

        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderVH(timeView);
            case TYPE_INFO:
                return new InfoVH(infoView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (mVisitorInfoList.size() <= 0) {
            return;
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");

        if (viewHolder instanceof HeaderVH) {

        } else if (viewHolder instanceof InfoVH) {
            if (mOnClickLister != null) {

                final VisitorInfo visitorInfo = mVisitorInfoList.get(i);
                String name = "";
                for (VisitorInfo.VisitorsEntity visitorsEntity : visitorInfo.getVisitors()) {
                    name = name + " " + visitorsEntity.getVisitorName();
                }

                String arriveDate = visitorInfo.getArriveDate();
                String leaveDate = visitorInfo.getLeaveDate();

                ((InfoVH) viewHolder).mVisitorHistoryName.setText(name);

                try {

                    if (!TextUtils.isEmpty(arriveDate)) {

                        Date arriveDt = sdf1.parse(arriveDate);


                        ((InfoVH) viewHolder).mVisitorHistoryData.setText(sdf2.format(arriveDt).toString());
                        ((InfoVH) viewHolder).mVisitorHistoryArriveTime.setText(sdf3.format(arriveDt).toString());
                    }

                    if (!TextUtils.isEmpty(leaveDate)) {

                        Date leaveDt = sdf1.parse(leaveDate);

                        ((InfoVH) viewHolder).mVisitorHistoryLeaveTime.setText(sdf3.format(leaveDt).toString());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((InfoVH) viewHolder).mVisitorHistoryInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickLister.onClick(visitorInfo);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mVisitorInfoList == null) {
            return 0;
        }
        return mVisitorInfoList.size();
    }


}