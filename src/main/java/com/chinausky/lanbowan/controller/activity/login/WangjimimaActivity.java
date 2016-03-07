package com.chinausky.lanbowan.controller.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.MessageReturn;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.model.bean.SendMessageBean;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.controller.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by succlz123 on 15/12/11.
 */
public class WangjimimaActivity extends BaseActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, WangjimimaActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_left_btn)
    ImageView toolbarLeftBtn;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar_right_btn)
    ImageView toolbarRightBtn;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.huoquyanzhengma)
    Button huoquyanzhengma;
    @Bind(R.id.yanzhengma)
    EditText yanzhengma;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @Bind(R.id.bt_submit)
    Button btSubmit;

    private String mMobileNumber;
    private String mVerificationCode;
    private String mPassword;
    private String mConfirmPasswrod;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangjimima);
        ButterKnife.bind(this);
        toolbarTitle.setText("忘记密码");

    }


    @OnClick(R.id.bt_submit)
    void onClickSubmit() {
        mMobileNumber = etPhone.getText().toString();

        if (!TextUtils.isEmpty(mMobileNumber) && !isPhoneValid(mMobileNumber)) {
            etPhone.setError(getString(R.string.error_invalid_phone));
            return;
        }

        mPassword = etPassword.getText().toString();
        if (TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword)) {
            etPassword.setError(getString(R.string.error_password_required));
            return;
        }

        mConfirmPasswrod = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(mConfirmPasswrod)) {
            etConfirmPassword.setError(getString(R.string.error_confirm_password_required));
            return;
        }
        if (!mPassword.equals(mConfirmPasswrod)) {
            etConfirmPassword.setError(getString(R.string.error_different_password));
        }

        mVerificationCode = yanzhengma.getText().toString();
        if (TextUtils.isEmpty(mVerificationCode)) {
            yanzhengma.setError("请输入验证码");
            return;
        }

        final Register register = new Register();

        register.setMobileNumber(mMobileNumber);
        register.setVerificationCode(mVerificationCode);
        register.setMessagValidateId(message);
        register.setPassword(mPassword);
        register.setConfirmPasswrod(mConfirmPasswrod);

        Call<RequestMessage> call = Api.postForgetPWD().onResult(register);
        call.enqueue(new Callback<RequestMessage>() {
            @Override
            public void onResponse(Response<RequestMessage> response, Retrofit retrofit) {
                if (response.body() != null) {
                    if (response.body().getStatusCode().equals("Error")) {
                        GlobalUtils.showToastShort(WangjimimaActivity.this, response.body().getMessage());
                    } else {
                        GlobalUtils.showToastShort(WangjimimaActivity.this, response.body().getMessage());
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        finish();
        onBackPressed();
    }

    @OnClick(R.id.huoquyanzhengma)
    void onHuoquyanzhengma() {
        mMobileNumber = etPhone.getText().toString();

        if (!TextUtils.isEmpty(mMobileNumber) && !isPhoneValid(mMobileNumber)) {
            etPhone.setError(getString(R.string.error_invalid_phone));
            return;
        }

        SendMessageBean sendMessageBean = new SendMessageBean();
        sendMessageBean.setMobileNumber(mMobileNumber);

        Call<MessageReturn> messageReturnCall = Api.forGetPWD().onResult(sendMessageBean);

        messageReturnCall.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Response<MessageReturn> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getStatusCode().equals("Error")) {
                    GlobalUtils.showToastLong(WangjimimaActivity.this, "验证码已经发送，或者本日验证码超限,无法继续使用!");
                }
                message = response.body().getMessage();
            }

            @Override
            public void onFailure(Throwable t) {
                GlobalUtils.showToastShort(WangjimimaActivity.this, "网络连接故障");
            }
        });
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private boolean isNameValid(String name) {
        return name.length() < 6;
    }

}
