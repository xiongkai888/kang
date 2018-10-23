package com.lanmei.kang.util;

import com.xson.common.api.AbstractApi;

public class Constant {

    public static final int quantity = 2;//列表item数

    // 微信
    public static final String WEIXIN_APP_ID = "wxf8b150eeb8daca98";
    public static final String WEIXIN_APP_SECRET = "91b81003b8ca818be5816951aa1db46e";
    public static final String testBucket = "stdrimages";  // 这个就是你申请的bucket名称

    //支付宝回调
    public static final String ALIPAY_NOTIFY_URL = AbstractApi.API_URL + "payment/callback/_id/1";

}
