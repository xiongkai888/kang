package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/24.
 * 发布动态
 */

public class PublishDynamicApi extends ApiV2 {

    public String uid;
    public String title;//标题
    public String city;//地址
//    public String content;//动态内容
    public String[] file;//图片

    @Override
    protected String getPath() {
        return "Posts/add";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
