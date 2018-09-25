package com.lanmei.kang.bean;

import java.util.List;

/**
 * Created by xkai on 2017/5/26.
 * 我的课程(或活动)
 */

public class MyCourseBean  {


    /**
     * id : 11
     * uid : 6
     * post_id : 105
     * name : 熊猫
     * sex : 1
     * icon_phone : 15914369252
     * item : 芭蕾
     * people : 55
     * descr : 我喜欢街舞
     * status : 1
     * addtime : 1496381638
     * title : 我是标题
     * file : []
     * label : 阿大使馆io
     * content : 内容内容
     * post_addtime : 1496214315
     * favoured : 0
     */

    private String id;
    private String uid;
    private String post_id;
    private String name;
    private String sex;
    private String phone;
    private String item;
    private String people;
    private String descr;
    private String status;
    private String addtime;
    private String title;
    private String label;
    private String content;
    private String post_addtime;
    private String favoured;
    private List<String> file;

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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_addtime() {
        return post_addtime;
    }

    public void setPost_addtime(String post_addtime) {
        this.post_addtime = post_addtime;
    }

    public String getFavoured() {
        return favoured;
    }

    public void setFavoured(String favoured) {
        this.favoured = favoured;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }
}
