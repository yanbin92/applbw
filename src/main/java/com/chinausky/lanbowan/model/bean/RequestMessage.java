package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/11/1.
 */
public class RequestMessage {

    /**
     * StatusCode : sample string 1
     * Message : sample string 2
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
