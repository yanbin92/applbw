package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/11/1.
 */
public class OwnerInfo {

    /**
     * OwnerId : 5
     * MobileNumber : 15658503223
     * Email : 15658503223@qq.com
     * Password : E10ADC3949BA59ABBE56E057F20F883E
     * UserName : 15658503223
     * NickName : 15658503223
     * IdentityCard : null
     * CommunityAddress : 南泊湾A座303
     * CommonAddress : 南泊湾A座303
     * CurrentResidenceId : 2
     * LastLoginDate : null
     * LastLoginIp : null
     * AppId : null
     * IsAudited : true
     * CreateDate : null
     * CreateBy : null
     */

    private int OwnerId;
    private String MobileNumber;
    private String Email;
    private String Password;
    private String UserName;
    private String NickName;
    private Object IdentityCard;
    private String CommunityAddress;
    private String CommonAddress;
    private int CurrentResidenceId;
    private Object LastLoginDate;
    private Object LastLoginIp;
    private Object AppId;
    private boolean IsAudited;
    private Object CreateDate;
    private Object CreateBy;

    public void setOwnerId(int OwnerId) {
        this.OwnerId = OwnerId;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public void setIdentityCard(Object IdentityCard) {
        this.IdentityCard = IdentityCard;
    }

    public void setCommunityAddress(String CommunityAddress) {
        this.CommunityAddress = CommunityAddress;
    }

    public void setCommonAddress(String CommonAddress) {
        this.CommonAddress = CommonAddress;
    }

    public void setCurrentResidenceId(int CurrentResidenceId) {
        this.CurrentResidenceId = CurrentResidenceId;
    }

    public void setLastLoginDate(Object LastLoginDate) {
        this.LastLoginDate = LastLoginDate;
    }

    public void setLastLoginIp(Object LastLoginIp) {
        this.LastLoginIp = LastLoginIp;
    }

    public void setAppId(Object AppId) {
        this.AppId = AppId;
    }

    public void setIsAudited(boolean IsAudited) {
        this.IsAudited = IsAudited;
    }

    public void setCreateDate(Object CreateDate) {
        this.CreateDate = CreateDate;
    }

    public void setCreateBy(Object CreateBy) {
        this.CreateBy = CreateBy;
    }

    public int getOwnerId() {
        return OwnerId;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getUserName() {
        return UserName;
    }

    public String getNickName() {
        return NickName;
    }

    public Object getIdentityCard() {
        return IdentityCard;
    }

    public String getCommunityAddress() {
        return CommunityAddress;
    }

    public String getCommonAddress() {
        return CommonAddress;
    }

    public int getCurrentResidenceId() {
        return CurrentResidenceId;
    }

    public Object getLastLoginDate() {
        return LastLoginDate;
    }

    public Object getLastLoginIp() {
        return LastLoginIp;
    }

    public Object getAppId() {
        return AppId;
    }

    public boolean isIsAudited() {
        return IsAudited;
    }

    public Object getCreateDate() {
        return CreateDate;
    }

    public Object getCreateBy() {
        return CreateBy;
    }
}
