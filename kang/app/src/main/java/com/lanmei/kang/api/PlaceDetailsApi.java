package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/22.
 */

public class PlaceDetailsApi extends AbstractApi {

    public String id;//列表id

    @Override
    protected String getPath() {
        return "place/details";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
