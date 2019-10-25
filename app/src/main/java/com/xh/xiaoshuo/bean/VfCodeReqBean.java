package com.xh.xiaoshuo.bean;

public class VfCodeReqBean {
    private String phone;
    private int type;  // 1注册，2修改密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VfCodeReqBean(String phone, int type) {
        this.phone = phone;
        this.type = type;
    }

    public VfCodeReqBean(String phone) {
        this.phone = phone;
    }
}
