package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2017/6/15.
 * 相册列表
 */

public class ItemsCompileBean implements Serializable{


    private String pic;
    private boolean isPicker;//是否来自相册
    private boolean isCover;//是不是封面

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }

    public boolean isPicker() {
        return isPicker;
    }

    public void setPicker(boolean picker) {
        isPicker = picker;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
