package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/26.
 * 删除课程
 */

public class DelCourseApi extends AbstractApi {

    public String id;//资讯id
    public String token;

    @Override
    protected String getPath() {
        return "course_apply/del";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
