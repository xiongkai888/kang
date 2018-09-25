package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/12.
 * 发布足迹
 */

public class PublishFootprintApi extends AbstractApi {

    public String token;
    public String place_id;//场地id
    public String item_id;//项目id
    public String distance;//距离
//    public String ranking;//名次
    public String play_time;//划船时间戳
    public String duration;//时长
    public String content;//发布的内容
    public String pic;//上传的图片

    @Override
    protected String getPath() {
        return "footprint/save";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
