package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/20.
 * 点击收藏(商家)
 */

public class CollectMerchantApi extends ApiV2 {

    public String id;//资讯id
    public String uid;//用户id
    public String del;//1 为取消收藏  不传默认收藏

    @Override
    protected String getPath() {
        return "place/do_favour";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
