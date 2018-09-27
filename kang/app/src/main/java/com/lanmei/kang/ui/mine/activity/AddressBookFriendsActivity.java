package com.lanmei.kang.ui.mine.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.Toast;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.AddressBookFriendsAdapter;
import com.lanmei.kang.bean.ContactsBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 通讯录好友
 */
public class AddressBookFriendsActivity extends BaseActivity {
    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID, Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;


    //    /**联系人名称**/
    //    private List<String> mContactsName = new ArrayList<String>();
    //
    //    /**联系人号码**/
    //    private List<String> mContactsNumber = new ArrayList<String>();
    //
    //    /**联系人头像**/
    //    private List<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

    private List<ContactsBean> mContactsList = new ArrayList<>();

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.listView)
    ListView mListView;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeyword;
    AddressBookFriendsAdapter mAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_address_book_friends;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /**得到手机通讯录联系人信息**/
        showContacts();
        //        /**得到手机SIM卡联系人人信息**/
        //        getSIMContacts();
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.address_book);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        mKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mAdapter.setData(mContactsList);
                    return;
                }
                List<ContactsBean> list = searchContentList(s.toString(), mContactsList);
                mAdapter.setData(list);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                }
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.mipmap.default_pic);
                }
                //                mContactsName.add(contactName);
                //                mContactsNumber.add(phoneNumber);
                //                mContactsPhonto.add(contactPhoto);
                ContactsBean bean = new ContactsBean();
                bean.setContactsName(contactName);
                bean.setContactsNumber(phoneNumber);
                bean.setContactsPhonto(contactPhoto);
                mContactsList.add(bean);
            }
            phoneCursor.close();
        }
    }


    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts() {
        ContentResolver resolver = getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                }
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //Sim卡中没有联系人头像
                //                mContactsName.add(contactName);
                //                mContactsNumber.add(phoneNumber);
            }
            phoneCursor.close();
        }
    }

    private List<ContactsBean> searchContentList(String key, List<ContactsBean> contactsList) {
        List<ContactsBean> list = null;
        if (contactsList != null && contactsList.size() > 0) {
            list = new ArrayList<>();
            int size = contactsList.size();
            for (int i = 0; i < size; i++) {
                ContactsBean bean = contactsList.get(i);
                if (bean != null && bean.getContactsName().contains(key)) {
                    list.add(bean);
                }
            }
        }
        return list;
    }

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getPhoneContacts();
            mAdapter = new AddressBookFriendsAdapter(this, mContactsList);
            mListView.setAdapter(mAdapter);
            mAdapter.setRecommendListener(new AddressBookFriendsAdapter.RecommendListener() {
                @Override
                public void recommend() {
                    UIHelper.ToastMessage(AddressBookFriendsActivity.this,R.string.developing);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
