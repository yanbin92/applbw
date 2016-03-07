package com.chinausky.lanbowan.controller.activity.photo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chinausky.lanbowan.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/11/10.
 */
public class HeeActivity extends AppCompatActivity {
    @Bind(R.id.photo_hee)
    SimpleDraweeView mSimpleDraweeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hee);
        ButterKnife.bind(this);

        String url = getIntent().getExtras().getString("url");


        if (url!=null){
            mSimpleDraweeView.setImageURI(Uri.parse(url));
        }
    }
}
