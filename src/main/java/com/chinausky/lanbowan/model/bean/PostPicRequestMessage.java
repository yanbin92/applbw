package com.chinausky.lanbowan.model.bean;

import java.util.List;

/**
 * Created by succlz123 on 15/11/6.
 */
public class PostPicRequestMessage {

    /**
     * StatusCode : Ok
     * ImageUrls : ["20151106_22305710-30bd-4a77-b897-ceeb184c67b7.jpg"]
     * Message : 上传成功
     */

    private String StatusCode;
    private String Message;
    private List<String> ImageUrls;

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setImageUrls(List<String> ImageUrls) {
        this.ImageUrls = ImageUrls;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public String getMessage() {
        return Message;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }
}
