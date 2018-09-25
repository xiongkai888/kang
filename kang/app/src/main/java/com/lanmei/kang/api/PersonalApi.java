package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/29.
 * 请求我的个人资料
 */

public class PersonalApi extends ApiV2 {

    public String uid;// 用户id

    @Override
    protected String getPath() {
        return "member/member";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
