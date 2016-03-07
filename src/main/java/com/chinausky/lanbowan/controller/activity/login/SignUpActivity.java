package com.chinausky.lanbowan.controller.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.api.Api;
import com.chinausky.lanbowan.model.bean.MessageReturn;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.SendMessageBean;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.controller.base.BaseActivity;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpActivity extends BaseActivity {

    @Bind(R.id.toolbar_left_btn)
    ImageView toolbarLeftBtn;
    @Bind(R.id.toolbar_right_btn)
    ImageView toolbarRightBtn;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.huoquyanzhengma)
    Button huoquyanzhengma;
    @Bind(R.id.yanzhengma)
    EditText yanzhengma;
    @Bind(R.id.youxiang)
    EditText youxiangET;
    @Bind(R.id.dizhi)
    EditText dizhiET;
    @Bind(R.id.bt_submit)
    Button btSubmit;

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private boolean isNameValid(String name) {
        return name.length() < 6;
    }


    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.et_phone)
    EditText mEtPhone;

    @Bind(R.id.et_name)
    EditText mEtName;

    @Bind(R.id.et_password)
    EditText mEtPassword;

    @Bind(R.id.et_confirm_password)
    EditText mEtConfirmPassword;

    @Bind(R.id.rb_male)
    RadioButton mRbMale;

    @Bind(R.id.rb_female)
    RadioButton mRbFemale;

    @Bind(R.id.rb_not_owner)
    RadioButton mRbNotOwner;

    @Bind(R.id.rb_owner)
    RadioButton mRbOwner;

    private String mMobileNumber;
    private String mEmail;
    private String mVerificationCode;
    private String mUserName;
    private String mCommunityAddress;
    private boolean mIsOwner = false;
    private String mPassword;
    private String mConfirmPasswrod;
    private boolean mIsMan = true;
    private long mLastClick;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mTitleTv.setText("注册");


    }

    @OnClick(R.id.bt_submit)
    void onClick() {
        mMobileNumber = mEtPhone.getText().toString();

        if (!TextUtils.isEmpty(mMobileNumber) && !isPhoneValid(mMobileNumber)) {
            mEtPhone.setError(getString(R.string.error_invalid_phone));
            return;
        }

        mUserName = mEtName.getText().toString();

        if (TextUtils.isEmpty(mUserName)) {
            mEtName.setError(getString(R.string.error_name_required));
            return;
        }

        if (!isNameValid(mUserName)) {
            mEtName.setError("姓名过长");
        }

        if (mRbOwner.isChecked()) {
            mIsOwner = true;
        }

        if (mRbFemale.isChecked()) {
            mIsMan = false;
        }

        mPassword = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword)) {
            mEtPassword.setError(getString(R.string.error_password_required));
            return;
        }

        mConfirmPasswrod = mEtConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(mConfirmPasswrod)) {
            mEtConfirmPassword.setError(getString(R.string.error_confirm_password_required));
            return;
        }
        if (!mPassword.equals(mConfirmPasswrod)) {
            mEtConfirmPassword.setError(getString(R.string.error_different_password));
        }

        mVerificationCode = yanzhengma.getText().toString();
        if (TextUtils.isEmpty(mVerificationCode)) {
            yanzhengma.setError("请输入验证码");
            return;
        }

        mEmail = youxiangET.getText().toString();
        if (TextUtils.isEmpty(mEmail)) {
            youxiangET.setError("请输入邮箱");
            return;
        }

        String mailCheck = "^\\\\w+([-+.]\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*$";
        Pattern regex = Pattern.compile(mailCheck);

        if (regex.matcher(mEmail).matches()) {
            youxiangET.setError("请输入邮箱或者邮箱格式有误");
            return;
        }

        mCommunityAddress = dizhiET.getText().toString();
        if (TextUtils.isEmpty(mCommunityAddress)) {
            dizhiET.setError("请输入地址");
            return;
        }

        final Register register = new Register();

        register.setMobileNumber(mMobileNumber);
        register.setEmail(mEmail);
        register.setVerificationCode(mVerificationCode);
        register.setUserName(mUserName);
        register.setCommunityAddress(mCommunityAddress);
        register.setIsOwner(mIsOwner);
        register.setMessagValidateId(message);
        if (mIsMan) {
            register.setSex("男");
        } else {
            register.setSex("女");
        }
        register.setPassword(mPassword);
        register.setConfirmPasswrod(mConfirmPasswrod);


        if (System.currentTimeMillis() - mLastClick <= 2000) {
            return;
        }
        mLastClick = System.currentTimeMillis();

        Call<Register> call = Api.register().onResult(register);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Response<Register> response, Retrofit retrofit) {
                if (response.body() != null) {
                    if (response.body().getStatusCode().equals("Error")) {
                        GlobalUtils.showToastShort(SignUpActivity.this, response.body().getMessage());
                    } else {
                        GlobalUtils.showToastShort(SignUpActivity.this, response.body().getMessage());

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
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
        mMobileNumber = mEtPhone.getText().toString();

        if (!TextUtils.isEmpty(mMobileNumber) && !isPhoneValid(mMobileNumber)) {
            mEtPhone.setError(getString(R.string.error_invalid_phone));
            return;
        }

        SendMessageBean sendMessageBean = new SendMessageBean();
        sendMessageBean.setMobileNumber(mMobileNumber);

        Call<MessageReturn> messageReturnCall = Api.sendMessage().onResult(sendMessageBean);

        messageReturnCall.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Response<MessageReturn> response, Retrofit retrofit) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getStatusCode().equals("Error")) {
                    mEtPhone.setError(response.body().getMessage());
//                    GlobalUtils.showToastShort(SignUpActivity.this, response.body().getMessage());
                } else {
                    message = response.body().getMessage();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GlobalUtils.showToastShort(SignUpActivity.this, "网络连接故障");
            }
        });
    }
}
