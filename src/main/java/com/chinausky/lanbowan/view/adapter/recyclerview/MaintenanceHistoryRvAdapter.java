package com.chinausky.lanbowan.view.adapter.recyclerview;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.RepairInfo;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/8/28.
 */
public class MaintenanceHistoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TIME = 0;
    private static final int TYPE_INFO = 1;

    private onClickLister mOnClickLister;
    private List<RepairInfo> mRepairInfoList = new ArrayList<>();

    public interface onClickLister {
        void onClick(RepairInfo repairInfo);
    }

    public void setOnClickLister(onClickLister onClickLister) {
        mOnClickLister = onClickLister;
    }

    public List<RepairInfo> getRepairInfoList() {
        return mRepairInfoList;
    }

    public void setRepairInfoList(List<RepairInfo> repairInfoList) {
        mRepairInfoList.addAll(repairInfoList);
    }

    public void reFreshRepairInfoList(List<RepairInfo> repairInfoList) {
        mRepairInfoList = repairInfoList;
    }

    public class InfoVH extends RecyclerView.ViewHolder {

        @Bind(R.id.repair_history_info)
        LinearLayout mLinearLayout;

        @Bind(R.id.relativeUrl_img)
        SimpleDraweeView mRelativeUrlImg;
        @Bind(R.id.repairType_tv)
        TextView mRepirType;

        @Bind(R.id.repair_history_date)
        TextView mRepairHistoryDate;

        @Bind(R.id.applyDate_tv)
        TextView mApplyDate;

        @Bind(R.id.repairDate_tv)
        TextView mRepairDate;

        public InfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_INFO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View timeView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_visitor_history_time, parent, false);
        View infoView = GlobalUtils.convertViewFromXml(null, R.layout.recyclerview_maintenance_history_info, parent, false);

        return new InfoVH(infoView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof InfoVH) {
            if (mRepairInfoList.size() <= 0) {
                return;
            }

            final RepairInfo repairInfo = mRepairInfoList.get(i);

            if (repairInfo.getUrlPaths().size() <= 0) {
                return;
            }

            String imageViewUrl = repairInfo.getUrlPaths().get(0).getRelativeUrl();
            if (!TextUtils.equals(imageViewUrl, "lbw.usky.cn/Upload/Images/")) {
                ((InfoVH) viewHolder).mRelativeUrlImg.setImageURI(Uri.parse("http://" + repairInfo.getUrlPaths().get(0).getRelativeUrl()));
            }


            if (repairInfo.getRepairType() != null) {
                ((InfoVH) viewHolder).mRepirType.setText(repairInfo.getRepairType());
            }

            if (mOnClickLister != null) {
                ((InfoVH) viewHolder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickLister.onClick(repairInfo);
                    }
                });
            }

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");

            if (repairInfo.getApplyDate() != null) {
                try {
                    Date dt = sdf1.parse(repairInfo.getApplyDate());

                    ((InfoVH) viewHolder).mRepairHistoryDate.setText(sdf3.format(dt));
                    ((InfoVH) viewHolder).mApplyDate.setText(sdf4.format(dt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (repairInfo.getRepairDate() != null) {
                try {
                    Date dt = sdf1.parse(repairInfo.getRepairDate());

                    ((InfoVH) viewHolder).mRepairDate.setText(sdf4.format(dt));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        if (mRepairInfoList == null) {
            return 0;
        }
        return mRepairInfoList.size();
    }

    public interface DeleteRepairInfo {
        void onDelete();
    }
}
