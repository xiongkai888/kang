package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2017/6/15.
 * 相册列表
 */

public class AlbumBean implements Serializable{


    /**
     * id : 4
     * uid : 5
     * pic : http://stdrimages.oss-cn-shenzhen.aliyuncs.com/5/149576411574.png
     */

    private String id;
    private String uid;
    private String pic;
    private boolean isPicker;//是否来自相册

    public boolean isPicker() {
        return isPicker;
    }

    public void setPicker(boolean picker) {
        isPicker = picker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
