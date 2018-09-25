package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/24.
 * 动态列表
 */

public class DynamicListApi extends KangApi {


    public String  id;//用户id
    public String uid;//查看指定人的贴,比如自己的
    public String type;//1、精选列表2、朋友圈 3、最新  最新的动态即不传uid和type

    @Override
    protected String getPath() {
        return "posts/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
