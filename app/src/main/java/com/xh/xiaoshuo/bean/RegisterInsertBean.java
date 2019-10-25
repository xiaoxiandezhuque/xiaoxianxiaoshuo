package com.xh.xiaoshuo.bean;

public class RegisterInsertBean {

    private String phone;
    private String password;
    private long createTime;
    private long loginTime;

    public RegisterInsertBean(String phone, String password, long createTime, long loginTime) {
        this.phone = phone;
        this.password = password;
        this.createTime = createTime;
        this.loginTime = loginTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
