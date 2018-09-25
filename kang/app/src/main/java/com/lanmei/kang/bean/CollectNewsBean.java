package com.lanmei.kang.bean;

import java.util.List;

/**
 * Created by xkai on 2018/2/2.
 * 收藏 资讯列表
 */

public class CollectNewsBean {

    /**
     * id : 86
     * title : NEWS｜清华经管赛艇友谊赛开桨 北大清华校友分获冠军
     * file : ["http://stdrimages.img-cn-shenzhen.aliyuncs.com/170525/5926cf22e16d8.jpg","http://stdrimages.img-cn-shenzhen.aliyuncs.com/170713/5967679a99a35.jpg","http://stdrimages.img-cn-shenzhen.aliyuncs.com/170713/5967679f5d1c5.jpg"]
     * addtime : 1499777617
     */

    private String id;
    private String title;
    private String addtime;
    private List<String> file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }
}
