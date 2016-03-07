package com.chinausky.lanbowan.model.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by succlz123 on 15/9/17.
 */
@DatabaseTable(tableName = "user_info")
public class Register {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String token;

    @DatabaseField
    private String MobileNumber;

    @DatabaseField
    private String Email;

    @DatabaseField
    private String VerificationCode;

    @DatabaseField
    private String UserName;

    @DatabaseField
    private String CommunityAddress;

    @DatabaseField
    private boolean IsOwner;

    @DatabaseField
    private String Password;

    @DatabaseField
    private String ConfirmPasswrod;

    @DatabaseField
    private String Sex;

    @DatabaseField
    private String MessagValidateId;

    public String getMessagValidateId() {
        return MessagValidateId;
    }

    public void setMessagValidateId(String messagValidateId) {
        MessagValidateId = messagValidateId;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    /**
     * StatusCode : Error
     * Message : 两次输入密码必须一致
     */

    private String StatusCode;
    private String Message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCommunityAddress() {
        return CommunityAddress;
    }

    public void setCommunityAddress(String communityAddress) {
        CommunityAddress = communityAddress;
    }

    public boolean isOwner() {
        return IsOwner;
    }

    public void setIsOwner(boolean isOwner) {
        IsOwner = isOwner;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPasswrod() {
        return ConfirmPasswrod;
    }

    public void setConfirmPasswrod(String confirmPasswrod) {
        ConfirmPasswrod = confirmPasswrod;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public String getMessage() {
        return Message;
    }
}
