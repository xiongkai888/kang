package com.lanmei.kang.bean;

/**
 * Created by xkai on 2017/6/15.
 * 达人、封面、留言
 */

public class LeaveMessageBean {


    /**
     * id : 1
     * content : 都咯OK了
     * addtime : 0
     * nickname : 超级懒人
     * pic : null
     * atname : null
     * atpic : null
     */

    private String id;
    private String content;
    private String addtime;
    private String nickname;
    private String pic;
    private String atname;
    private String atpic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAtname() {
        return atname;
    }

    public void setAtname(String atname) {
        this.atname = atname;
    }

    public String getAtpic() {
        return atpic;
    }

    public void setAtpic(String atpic) {
        this.atpic = atpic;
    }
}
