package com.lanmei.kang.bean;

/**
 * @author xkai 投票筛选
 */
public class InventoryFiltrateBean {

    private boolean isSelect;
    private String id = "";
    private String name;

    public boolean isSelect() {
        return isSelect;
    }

    public String getName() {
        return name;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}