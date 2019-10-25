package com.xh.xiaoshuo.bean;


import com.blankj.utilcode.util.StringUtils;

public class UserReqBean {

    private String phone;
    private String password;

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

    public UserReqBean(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public UserReqBean() {

    }

    public boolean check() {
        if (!StringUtils.isTrimEmpty(phone) &&
                !StringUtils.isTrimEmpty(password) &&
                phone.length() == 11 &&
                password.length() >= 6 &&
                password.length() <= 20) {
            return true;
        }
        return false;
    }
}
