package com.lanmei.kang.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.webviewpage.PhotoBrowserActivity;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 个人资料（别人的，不可编辑）
 */
public class PersonalDataNCActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.nick_tv)
    TextView mNickTv;//昵称
    @InjectView(R.id.qq_tv)
    TextView mQqTv;//qq
    @InjectView(R.id.mail_tv)
    TextView mMailTv;//邮箱
    @InjectView(R.id.phone_tv)
    TextView mPhoneTv;//电话
    @InjectView(R.id.address_tv)
    TextView mAddressTv;//地址
    @InjectView(R.id.signature_tv)
    TextView mSignatureTt;//个性签名
    @InjectView(R.id.personal_icons_iv)
    ImageView mPersonalIconsIV;//头像
    UserBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_data_nc;
    }

    public static void startActivityPersonalDataNC(Context context, UserBean bean) {
        Intent intent = new Intent(context, PersonalDataNCActivity.class);
        intent.putExtra("bean", bean);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.personal_data);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        bean = (UserBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            mNickTv.setText(bean.getNickname());
            mQqTv.setText(bean.getQq());
            mMailTv.setText(bean.getEmail());
            mPhoneTv.setText(bean.getPhone());
            mAddressTv.setText(bean.getAddress());
            mSignatureTt.setText(bean.getSignature());
            ImageHelper.load(this, bean.getPic(), mPersonalIconsIV, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        }
    }

    @OnClick(R.id.personal_icons_iv)
    public void showUserHead() {
        if (bean == null){
            return;
        }
        String pic = bean.getPic();
        if (StringUtils.isEmpty(pic)){
            return;
        }
        String[] arry = new String[1];
        arry[0] = pic;
        Intent intent = new Intent();
        intent.putExtra("imageUrls", arry);
        intent.putExtra("curImageUrl", pic);
        intent.putExtra("hide", true);
        intent.setClass(this, PhotoBrowserActivity.class);
        startActivity(intent);
    }

}
