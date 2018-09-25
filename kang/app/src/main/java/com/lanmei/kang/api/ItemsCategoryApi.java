package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/18.
 * 我的产品分类
 */

public class ItemsCategoryApi extends ApiV2 {

    public String mid;//商家id
    public String token;//

    @Override
    protected String getPath() {
        return "member/category";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
