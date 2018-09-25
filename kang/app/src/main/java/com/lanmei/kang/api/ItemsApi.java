package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/19.
 * 获取项目如：单人艇、双人艇等
 */

public class ItemsApi extends AbstractApi {

    @Override
    protected String getPath() {
        return "place/items";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
