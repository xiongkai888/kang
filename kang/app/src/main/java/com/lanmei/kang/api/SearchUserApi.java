package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/7/21.
 * 搜索用户
 */

public class SearchUserApi  extends ApiV2 {

    public String keyword;//关键字
    public String token;
    public String uid;//用户id

    @Override
    protected String getPath() {
        return "member/search";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
