package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/12/4.
 */
public class GetMyCamerasMessage {

    /**
     * CameraId : 5
     * CameraName : 道路1
     * GroupName : 花园
     * DeviceId : 3301061000008
     * Channel : 0
     * CloudServer : 114.215.102.51
     * CloudPort : 9000
     * CloudAccount : 1
     * CloudPassword : 1
     * ImageUrl : lbw.usky.cn/Upload/Camera/Daolu_01.png
     * IsOnline : false
     */

    private int CameraId;
    private String CameraName;
    private String GroupName;
    private String DeviceId;
    private String Channel;
    private String CloudServer;
    private int CloudPort;
    private String CloudAccount;
    private String CloudPassword;
    private String ImageUrl;
    private boolean IsOnline;

    public void setCameraId(int CameraId) {
        this.CameraId = CameraId;
    }

    public void setCameraName(String CameraName) {
        this.CameraName = CameraName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }

    public void setCloudServer(String CloudServer) {
        this.CloudServer = CloudServer;
    }

    public void setCloudPort(int CloudPort) {
        this.CloudPort = CloudPort;
    }

    public void setCloudAccount(String CloudAccount) {
        this.CloudAccount = CloudAccount;
    }

    public void setCloudPassword(String CloudPassword) {
        this.CloudPassword = CloudPassword;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public void setIsOnline(boolean IsOnline) {
        this.IsOnline = IsOnline;
    }

    public int getCameraId() {
        return CameraId;
    }

    public String getCameraName() {
        return CameraName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getChannel() {
        return Channel;
    }

    public String getCloudServer() {
        return CloudServer;
    }

    public int getCloudPort() {
        return CloudPort;
    }

    public String getCloudAccount() {
        return CloudAccount;
    }

    public String getCloudPassword() {
        return CloudPassword;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public boolean isIsOnline() {
        return IsOnline;
    }
}
