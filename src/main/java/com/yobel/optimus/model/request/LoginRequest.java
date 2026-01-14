package com.yobel.optimus.model.request;

public class LoginRequest {
    private String username;
    private String password;
    private String session;

    public LoginRequest(String username, String password, String session) {
        this.username = username;
        this.password = password;
        this.session = session;
    }

}