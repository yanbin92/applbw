package com.chinausky.lanbowan.controller.activity.maintenance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.PostPicRequestMessage;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RepairInfo;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.controller.activity.photo.ChoosePicActivity;
import com.chinausky.lanbowan.controller.activity.photo.PhotoActivity;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.utils.photo.Bimp;
import com.chinausky.lanbowan.model.utils.base.FileUtils;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/8/6.
 */
public class MaintenanceServiceActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_right_btn)
    ImageView mToolbarRightBtn;
    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;
    @Bind(R.id.img_phone)
    ImageView mImgPhone;
    @Bind(R.id.tv_week_monday_to_friday)
    TextView mTvWeekMondayToFriday;
    @Bind(R.id.tv_week_saturday_to_sunday)
    TextView mTvWeekSaturdayToSunday;


    @Bind(R.id.xiashuiguandao)
    CheckBox mXiashuiguandao;
    @Bind(R.id.matongshutong)
    CheckBox mMatongshutong;
    @Bind(R.id.tiaozha)
    CheckBox mTiaozha;
    @Bind(R.id.diannaoguzhang)
    CheckBox mDiannaoguzhang;
    @Bind(R.id.jiaju)
    CheckBox mJiaju;
    @Bind(R.id.qita)
    CheckBox mQita;

    private GridAdapter mAdapter;

    private List<String> list;

    public static void startActivity(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent(activity, MaintenanceServiceActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Bind(R.id.noScrollGridView)
    GridView mNoScrollGridView;

    @Bind(R.id.maintain_service_submit)
    Button mMaintainServiceSubmit;

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.repairMessage_edt)
    EditText mMessage;

    private List<CheckBox> checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_service);
        ButterKnife.bind(this);
        mTitleTv.setText("维修服务");


        checkBoxes = new ArrayList();
        checkBoxes.add(mXiashuiguandao);
        checkBoxes.add(mMatongshutong);
        checkBoxes.add(mTiaozha);
        checkBoxes.add(mDiannaoguzhang);
        checkBoxes.add(mJiaju);
        checkBoxes.add(mQita);

        for (final CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkBoxes.remove(checkBox);
                        for (CheckBox otherCB : checkBoxes) {
                            otherCB.setChecked(false);
                        }
                        checkBoxes.add(checkBox);
                    }
                }
            });
        }

        Init();
    }

    public void Init() {
        list = new ArrayList<>();
        mNoScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GridAdapter(this);
        mAdapter.update();
        mNoScrollGridView.setAdapter(mAdapter);
        mNoScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    PhotoPopWindows photoPopWindows = new PhotoPopWindows(MaintenanceServiceActivity.this, mNoScrollGridView);
                } else {
                    Intent intent = new Intent(MaintenanceServiceActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        mMaintainServiceSubmit.setOnClickListener(new View.OnClickListener() {

            public long lastClick;

            public void onClick(View v) {
                //大于3秒才可通过
                if (System.currentTimeMillis() - lastClick <= 3000) {
                    return;
                }
                lastClick = System.currentTimeMillis();

                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                    list.add(FileUtils.SDPATH + Str + ".JPEG");

                }

                CheckBox cb = null;

                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        cb = checkBox;
                    }
                }

                if (cb == null) {
                    GlobalUtils.showToastShort(MaintenanceServiceActivity.this, "请选择维修服务项目");
                    return;
                }

                if (mMessage.getText() == null) {
                    GlobalUtils.showToastShort(MaintenanceServiceActivity.this, "请输入留言");
                    return;
                }
                if (list.size() == 0) {
                    GlobalUtils.showToastShort(MaintenanceServiceActivity.this, "请上传照片");
                    return;
                }

                Register register = MyApplication.getInstance().getRegister();

                if (register.getToken() == null) {
                    return;
                }

                final String token = register.getToken();

                final RepairInfo repairInfo = new RepairInfo();
                repairInfo.setRepairMessage(mMessage.getText().toString());
                repairInfo.setRepairType(cb.getText().toString());
                repairInfo.setRepairId(0);
                repairInfo.setRepairRate("0");
                repairInfo.setRepairStatus("0");

                Map<String, RequestBody> map = new HashMap<>();

                for (String fileName : list) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileName));
                    map.put("file\"; filename=\"" + fileName, fileBody);
                }

                Call<PostPicRequestMessage> call = Api.postPic().onResult("Bearer " + token, map);
                call.enqueue(new Callback<PostPicRequestMessage>() {
                    @Override
                    public void onResponse(Response<PostPicRequestMessage> response, Retrofit retrofit) {
                        if (response.body() == null || response.body().getImageUrls().size() == 0) {
                            GlobalUtils.showToastShort(MaintenanceServiceActivity.this, "提交失败请重试");
                            return;
                        }

                        List<RepairInfo.UrlPathsEntity> urls = new ArrayList();

                        for (String url : response.body().getImageUrls()) {
                            RepairInfo.UrlPathsEntity urlPathsEntity = new RepairInfo.UrlPathsEntity();
                            urlPathsEntity.setRelativeUrl(url);
                            urls.add(urlPathsEntity);
                        }

                        repairInfo.setUrlPaths(urls);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        Date date = new Date();

                        repairInfo.setApplyDate(simpleDateFormat.format(date));

                        Call<RequestMessage> call = Api.postRepairInfo().onResult("Bearer " + token, repairInfo);
                        call.enqueue(new Callback<RequestMessage>() {
                            @Override
                            public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                                if (response.body() == null) {
                                    return;
                                }
                                GlobalUtils.showToastShort(MaintenanceServiceActivity.this, "维修申请已提交");
                                FileUtils.deleteDir();


                                MaintenanceServiceActivity.this.setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.toString();
                    }
                });

                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
//                FileUtils.deleteDir();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grid,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        mAdapter.update();
        super.onRestart();
    }

    public class PhotoPopWindows extends PopupWindow {

        public PhotoPopWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.7f;
            getWindow().setAttributes(params);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MaintenanceServiceActivity.this, ChoosePicActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        @Override
        public void dismiss() {
            super.dismiss();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        StringBuffer sDir = new StringBuffer();
        if (hasSDcard()) {
            sDir.append(Environment.getExternalStorageDirectory() + "/MyPicture/");
        } else {
            String dataPath = Environment.getRootDirectory().getPath();
            sDir.append(dataPath + "/MyPicture/");
        }

        File fileDir = new File(sDir.toString());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, String.valueOf(System.currentTimeMillis()) + ".jpg");

        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 3 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
        if (Bimp.bmp != null && Bimp.bmp.size() > 0) {
            Bimp.bmp.clear();
        }
        if (Bimp.drr != null && Bimp.drr.size() > 0) {
            Bimp.drr.clear();
        }
        if (Bimp.max != 0) {
            Bimp.max = 0;
        }
        super.onDestroy();
    }


    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}

