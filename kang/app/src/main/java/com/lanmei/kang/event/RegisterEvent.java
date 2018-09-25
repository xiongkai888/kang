package com.lanmei.kang.event;

/**
 * Created by xkai on 2017/11/15.
 * 注册后转递用户名到登陆界面
 */

public class RegisterEvent {

    private String phone;

    public RegisterEvent(String phone){
            this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }
}
