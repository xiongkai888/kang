package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/26.
 * 课程报名
 */

public class CourseApplyApi extends AbstractApi {


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
        return "course_apply/add";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
