package com.chinausky.lanbowan.view.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.GetMyCarInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/8/28.
 */
public class MyParkingRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NO_CAR = 0;
    private static final int TYPE_INFO = 1;
    private static final int TYPE_MORE = 2;

    private OnClickLister mOnClickLister;
    private OnDeleteClickLister mDeleteClickLister;
    private OnPayClickLister mPayClickLister;

    private List<Integer> mData = new ArrayList<>();
    private List<GetMyCarInfo> mMyCarInfoList = new ArrayList<>();

    public List<GetMyCarInfo> getMyCarInfoList() {
        return mMyCarInfoList;
    }

    public void setMyCarInfoList(List<GetMyCarInfo> myCarInfoList) {
        this.mMyCarInfoList = myCarInfoList;
    }

    public OnPayClickLister getPayClickLister() {
        return mPayClickLister;
    }

    public void setPayClickLister(OnPayClickLister payClickLister) {
        mPayClickLister = payClickLister;
    }

    public List getData() {
        return mData;
    }

    public void ChangeWithData() {
        mData.clear();
    }

    public void setData(int i) {
        GetMyCarInfo showMore = new GetMyCarInfo();
        showMore.setShowMore(true);

        if (mMyCarInfoList.size() > 0) {
            //save click position
            if (mData.size() > 0) {
                if (i == mData.get(0)) {
                    //click position equal to save position , so pop moreView
                    mData.clear();
                    mMyCarInfoList.remove(i + 1);
                    notifyDataSetChanged();
                } else if (i > mData.get(0)) {
                    //if click position greater than the save position , for mData is the location of the i-1
                    mMyCarInfoList.remove(mData.get(0) + 1);
                    mMyCarInfoList.add(i, showMore);
                    mData.clear();
                    mData.add(i - 1);
                    notifyDataSetChanged();
                } else if (i < mData.get(0)) {
                    //if click position less than the save position , no change
                    mMyCarInfoList.remove(mData.get(0) + 1);
                    mMyCarInfoList.add(i + 1, showMore);
                    mData.clear();
                    mData.add(i);
                    notifyDataSetChanged();
                }
            } else {
                mData.add(i);
                mMyCarInfoList.add(i + 1, showMore);
                notifyDataSetChanged();
            }
        }
    }

    public interface OnClickLister {
        void onClick(int i);
    }

    public void setOnClickLister(OnClickLister onClickLister) {
        mOnClickLister = onClickLister;
    }

    public interface OnDeleteClickLister {
        void onClick(HashMap ownerCarId, String plateNumber);
    }

    public interface OnPayClickLister {
        void onClick();
    }
    public void setOnDeleteClickLister(OnDeleteClickLister onDeleteClickLister) {
        mDeleteClickLister = onDeleteClickLister;
    }

    public class NoCarVH extends RecyclerView.ViewHolder {

        public NoCarVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class InfoVH extends RecyclerView.ViewHolder {
        @Bind(R.id.my_car_num_tv)
        TextView carNumTV;
        @Bind(R.id.my_car_tv)
        TextView carTV;
        @Bind(R.id.my_parking_car_layout)
        LinearLayout linearLayout;

        public InfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MoreVH extends RecyclerView.ViewHolder {
        @Bind(R.id.my_parking_delete)
        LinearLayout deleteLL;
        @Bind(R.id.my_parking_pay)
        LinearLayout payLL;

        public MoreVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMyCarInfoList.size() > 0) {

            GetMyCarInfo myCarInfo = mMyCarInfoList.get(position);

            if (myCarInfo.isShowMore()) {
                return TYPE_MORE;
            }

            return TYPE_INFO;
        }
        return TYPE_NO_CAR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View InfoView = inflater.inflate(R.layout.recyclerview_my_parking_info, parent, false);
        View moreView = inflater.inflate(R.layout.recyclerview_my_parking_more, parent, false);
        View noCarView = inflater.inflate(R.layout.recyclerview_my_parking_no_car, parent, false);

        if (viewType == TYPE_INFO) {
            return new InfoVH(InfoView);
        } else if (viewType == TYPE_MORE) {
            return new MoreVH(moreView);
        } else {
            return new NoCarVH(noCarView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof InfoVH) {
            if (mOnClickLister != null) {
                ((InfoVH) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickLister.onClick(i);
                    }
                });
            }

            GetMyCarInfo myCarInfo = null;
            int carNum;

            if (mMyCarInfoList.size() > 0) {
                myCarInfo = mMyCarInfoList.get(i);
                carNum = i + 1;
            }
            if (myCarInfo == null) {
                return;
            }

            ((InfoVH) viewHolder).carNumTV.setText(myCarInfo.getPlateName());
            ((InfoVH) viewHolder).carTV.setText(myCarInfo.getPlateNumber());
        } else if (viewHolder instanceof MoreVH) {
            ((MoreVH) viewHolder).deleteLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetMyCarInfo myCarInfo = null;

                    if (mMyCarInfoList.size() > 0) {
                        myCarInfo = mMyCarInfoList.get(i - 1);
                    }
                    if (myCarInfo == null) {
                        return;
                    }

                    HashMap ownerCarId = new HashMap();
                    ownerCarId.put("ownerCarId", myCarInfo.getOwnerCarId());

                    mDeleteClickLister.onClick(ownerCarId, myCarInfo.getPlateNumber());
                }
            });

            ((MoreVH) viewHolder).payLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPayClickLister.onClick();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mMyCarInfoList.size() == 0) {
            //tips for displaying no parking space
            return 1;
        }
        return mMyCarInfoList.size();
    }
}
