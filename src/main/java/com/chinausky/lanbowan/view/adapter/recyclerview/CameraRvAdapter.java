package com.chinausky.lanbowan.view.adapter.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.GetMyCamerasMessage;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/12/6.
 */
public class CameraRvAdapter extends RecyclerView.Adapter {
    private onClickListener mOnClickListener;
    private List<GetMyCamerasMessage> mCameras = new ArrayList<>();
    private long mLastClick;

    public List<GetMyCamerasMessage> getCameras() {
        return mCameras;
    }

    public void setCameras(List<GetMyCamerasMessage> cameras) {
        mCameras = cameras;
    }

    public interface onClickListener {
        void onClick(String ip, int port, String username, String password);
    }

    public onClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(onClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public class CameraVH extends RecyclerView.ViewHolder {
        @Bind(R.id.rv_camera_pic)
        SimpleDraweeView picIV;
        @Bind(R.id.rv_camera_name)
        TextView nameTV;

        public CameraVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());

        View InfoView = inflater.inflate(R.layout.item_camera, parent, false);

        return new CameraVH(InfoView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CameraVH) {
            if (mCameras.size() == 0) {
                return;
            }
            final GetMyCamerasMessage message = mCameras.get(position);
            ((CameraVH) holder).picIV.setImageURI(Uri.parse("http://" + message.getImageUrl()));
            ((CameraVH) holder).nameTV.setText(message.getCameraName());

            if (mOnClickListener != null) {
                ((CameraVH) holder).picIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip = message.getCloudServer();
                        int port = message.getCloudPort();
                        String username = message.getCloudAccount();
                        String password = message.getCloudPassword();

                        if (System.currentTimeMillis() - mLastClick <= 4000) {
                            GlobalUtils.showToastShort(MyApplication.getInstance().getApplicationContext(), "请不要重复点击");
                            return;
                        }
                        mOnClickListener.onClick(ip, port, username, password);

                        mLastClick = System.currentTimeMillis();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCameras.size();
    }

    public static class MyDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            int margin = GlobalUtils.dip2pix(parent.getContext(), 10f);
            if (position % 2 == 0) {
                outRect.set(0, margin, margin, 0);
            } else {
                outRect.set(0, margin, 0, 0);
            }
        }
    }
}
