package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/12/11.
 */
public class MessageReturn {

    /**
     * StatusCode : Error
     * Message : 该手机号已被注册
     */

    private String StatusCode;
    private String Message;

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
