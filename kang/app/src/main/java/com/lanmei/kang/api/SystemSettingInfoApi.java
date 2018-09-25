package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/18.
 * 系统基本设置信息
 */

public class SystemSettingInfoApi extends AbstractApi {

    public String token;

    @Override
    protected String getPath() {
        return "index/siteinfo";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
