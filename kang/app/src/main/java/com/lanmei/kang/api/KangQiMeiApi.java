package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2018/1/8.
 */

public class KangQiMeiApi extends AbstractApi {

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
