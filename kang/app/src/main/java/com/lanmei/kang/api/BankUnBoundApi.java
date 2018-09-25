package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/8/23.
 * 解绑银行卡
 */

public class BankUnBoundApi extends AbstractApi {

    public String id;
    public String token;

    @Override
    protected String getPath() {
        return "banks/del";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
