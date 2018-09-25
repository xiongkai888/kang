package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/26.
 * 活动报名
 */

public class ActivityApplyApi extends AbstractApi {


    public String token;//
    public String name;//姓名
    public int sex;//性别
    public String phone;//电话号码
    public String item;//报名项目
    public String people;//预约人数
    public String descr;//备注
    public String post_id;//资讯id


    @Override
    protected String getPath() {
        return "activity_apply/add";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
