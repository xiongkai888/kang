package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/8.
 */

public class KangQiMeiApi extends ApiV2 {

    private String path;

    public void setPath(String path) {
        this.path = path;
    }
    public KangQiMeiApi(){
    }

    public KangQiMeiApi(String path){
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
