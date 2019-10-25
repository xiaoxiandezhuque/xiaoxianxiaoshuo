package com.xh.xiaoshuo.bean;

public class ForgetPasswordReqBean {

    private String code;
    private String password;
    private String phone;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ForgetPasswordReqBean( String phone,String code, String password) {
        this.code = code;
        this.password = password;
        this.phone = phone;
    }
}
