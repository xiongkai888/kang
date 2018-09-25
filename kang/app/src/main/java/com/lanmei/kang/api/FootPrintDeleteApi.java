package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/2.
 * 删除足迹
 */

public class FootPrintDeleteApi extends AbstractApi {

    public String id;//列表中id
    public String token;//

    @Override
    protected String getPath() {
        return "footprint/del";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
