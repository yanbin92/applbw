package com.chinausky.lanbowan.model.conifg;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/10/28.
 */
public class RetrofitConfig {

    public static Retrofit build() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://lbw.usky.cn/")
                .build();

        return retrofit;
    }

}
