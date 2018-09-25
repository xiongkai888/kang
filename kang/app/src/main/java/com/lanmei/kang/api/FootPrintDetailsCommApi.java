package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/25.
 *
 * 足迹详情 评论列表评论
 */

public class FootPrintDetailsCommApi extends AbstractApi {

    public String footprint_id;//查看指定的评论 列表
    public String token;
    public String uid;
    public String content;//

    @Override
    protected String getPath() {
        return "FootprintReviews/add";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
