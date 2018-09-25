package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/8/15.
 * 现在任务状态  查看是否签到、版本更新等
 */

public class LookTaskApi extends AbstractApi{

    public String token;

    @Override
    protected String getPath() {
        return "member/task";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
