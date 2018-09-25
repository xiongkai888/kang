package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/27.
 * 课程：团体课程、私人课程、在线课程
 */

public class CourseApi extends AbstractApi {

    public String token;//
    public String status;//1、已报名 2、已参加 ?? 哪有已报名和已参加？？？

    @Override
    protected String getPath() {
        return "customize_apply/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
