package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/18.
 * 资讯分类列表
 */

public class NewsCategoryListApi extends AbstractApi {

    public String cid;//分类id

    @Override
    protected String getPath() {
        return "post/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
