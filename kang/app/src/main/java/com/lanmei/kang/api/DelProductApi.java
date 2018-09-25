package com.lanmei.kang.api;

import com.lanmei.kang.api.KangApi;

/**
 * Created by xkai on 2018/2/8.
 * 删除产品
 */

public class DelProductApi extends KangApi {

    public String goods_id;//商品id
    public String mid;//商家id

    @Override
    protected String getPath() {
        return "member/delProduct";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
