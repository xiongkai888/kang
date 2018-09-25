package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/2/8.
 *
 */

public class AddCategoryApi extends ApiV2 {

    public String mid;//商家id
    public String name;//分类名称
    public String id;//分类id,修改时需要


    @Override
    protected String getPath() {
        return "place/addClassify";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
