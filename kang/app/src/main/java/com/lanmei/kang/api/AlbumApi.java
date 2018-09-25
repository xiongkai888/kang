package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/25.
 * 相册
 */

public class AlbumApi extends ApiV2 {

    public String act;//当值为 1 时表示添加，-1时表示删除，默认为列表
    public String token;
    public String pic;//图片路径，多图用 | 隔开
    public String id;//删除时用到，多个用 , 隔开
    public String uid;//查看指定人的相册列表

    @Override
    protected String getPath() {
        return "talent/album";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
