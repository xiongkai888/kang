package com.lanmei.kang.bean;

import android.graphics.Bitmap;

/**
 * Created by xkai on 2017/8/12.
 * 通讯录bean
 */

public class ContactsBean {

    private String contactsName;//名字
    private String contactsNumber;//号码
    private Bitmap contactsPhonto;//头像

    public String getContactsName() {
        return contactsName;
    }

    public String getContactsNumber() {
        return contactsNumber;
    }

    public Bitmap getContactsPhonto() {
        return contactsPhonto;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public void setContactsNumber(String contactsNumber) {
        this.contactsNumber = contactsNumber;
    }

    public void setContactsPhonto(Bitmap contactsPhonto) {
        this.contactsPhonto = contactsPhonto;
    }
}
