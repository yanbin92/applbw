package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/10/29.
 */
public class SignInInfo {


    /**
     * access_token : eaOLSWGZKRS3skWgSmJk2v1oZF_Ca9E9RxOxqn5yA6hvfTF5vJSh3X9XTkJ8YyZbfHwHoMBjUg55cblOZ_o6qqljgtVbT2Z-kPUYnqgcqLrnbHdc2NTpCXfkTmu8ZSTLoIWdr8N3MZLmcfCHHMH6Z4qpo-Xv04ELlCq07e_uWR2V2L_ZGHXviJQ0opdFw8JfPW5YOPlPg341sCePoUdPhZgeuEtkX3xUkIIc019ioGk
     * token_type : bearer
     * expires_in : 15551999
     * refresh_token : 3bd141c8b3ab438da64f37677da8d153
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
