package com.chinausky.lanbowan.controller.activity.photo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.utils.photo.ImageItem;
import com.chinausky.lanbowan.view.adapter.other.ImageGridAdapter;
import com.chinausky.lanbowan.model.utils.photo.AlbumHelper;
import com.chinausky.lanbowan.model.utils.photo.Bimp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageGridActivity extends Activity {

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    public static final String EXTRA_IMAGE_LIST = "imagelist";

    // ArrayList<Entity> dataList;
    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    Button bt;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择3张图片", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_grid);
        ButterKnife.bind(this);

        mTitleTv.setText("相册");

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initView();
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); ) {
                    list.add(it.next());
                }

//                if (Bimp.act_bool) {
//                    Intent intent = new Intent(ImageGridActivity.this, MaintenanceServiceActivity.class);
//                    startActivity(intent);
//                    Bimp.act_bool = false;
//                }
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < 3) {
                        Bimp.drr.add(list.get(i));
                    }
                }
                finish();
            }

        });
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                bt.setText("完成" + "(" + count + ")");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }

                adapter.notifyDataSetChanged();
            }

        });

    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
