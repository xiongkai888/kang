package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/8/31.
 * 商家相册更新
 */

public class MAUpdataApi extends ApiV2 {

    public String uid;
    public String token;
    public String pics;
    public String name;//场地名称
    public String stime;
    public String etime;
    public String place_address;//场地地址
    public String occupation_section;//商家设置的时间
    public String tel;
    public String place_introduction;//场地介绍
    public String work_money;//工作日用户 、教练、舵手的价格
    public String weekend_money;//周末用户 、教练、舵手的价格
    public String holidays_money;//节假日用户 、教练、舵手的价格
    public String holidays;//节假日 05-01,10-01

    @Override
    protected String getPath() {
        return "place/update";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
