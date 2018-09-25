package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/6/15.
 * 达人  封面  留言  发布留言
 */

public class LeavePublishApi extends AbstractApi {


    public String token;
    public String content;
    public String mid;//达人的mid

    @Override
    protected String getPath() {
        return "TalentReviews/add";
    }


    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
