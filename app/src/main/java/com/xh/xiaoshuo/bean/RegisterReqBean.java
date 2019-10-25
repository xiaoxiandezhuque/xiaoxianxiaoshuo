package com.xh.xiaoshuo.bean;



public class RegisterReqBean {

    private String phone;
    private String code;
    private String password;
    private String invitedCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getInvitedCode() {
        return invitedCode;
    }

    public void setInvitedCode(String invitedCode) {
        this.invitedCode = invitedCode;
    }

    public RegisterReqBean(String phone, String code, String password, String invitedCode) {
        this.phone = phone;
        this.code = code;
        this.password = password;
        this.invitedCode = invitedCode;
    }

    public RegisterReqBean(String phone, String code, String password) {
        this.phone = phone;
        this.code = code;
        this.password = password;
    }

    public RegisterReqBean() {
    }
}
