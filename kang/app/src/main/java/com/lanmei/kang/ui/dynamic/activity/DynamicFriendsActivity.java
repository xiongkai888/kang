package com.lanmei.kang.ui.dynamic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicFriendsAdapter;
import com.lanmei.kang.bean.DynamicBean;
import com.lanmei.kang.bean.UserInfoBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.CircleImageView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 动态列表的头像进入
 */
public class DynamicFriendsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    DynamicFriendsAdapter mAdapter;
    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.grade_tv)
    TextView gradeTv;
    @InjectView(R.id.signature_tv)
    TextView signatureTv;
    @InjectView(R.id.follow_num_tv)
    TextView followNumTv;
    @InjectView(R.id.fans_num_tv)
    TextView fansNumTv;
    @InjectView(R.id.friend_num_tv)
    TextView friendNumTv;
    String uid;
    DynamicBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_dynamic_friends;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)){
            bean = (DynamicBean)bundle.getSerializable("bean");
            uid = bean.getUid();
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

//        if (StringUtils.isEmpty(uid)) {
//            return;
//        }
//        PersonalApi api = new PersonalApi();
//        api.uid = uid;
//        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
//            @Override
//            public void onResponse(BaseBean response) {
//
//            }
//        });
        DemoHelper.getInstance().getUserBean(KangApp.HX_USER_Head + uid, false, new DemoHelper.UserInfoListener() {
            @Override
            public void succeed(UserInfoBean bean) {
                if (isFinishing()) {
                    return;
                }
                setUserInfo(bean);
            }
        });
        setData();
    }

    private void setUserInfo(UserInfoBean bean) {
        signatureTv.setText(bean.getSignature());
    }

    private void setData() {
        if (StringUtils.isEmpty(bean)){
            return;
        }
        ImageHelper.load(this,bean.getPic(),headIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        nameTv.setText(bean.getNickname());
//        gradeTv.setText(bean.getLevname());

//        followNumTv.setText(bean.getFollowed());
//        fansNumTv.setText(bean.getFans());
//        friendNumTv.setText(bean.getFriend());

        mAdapter = new DynamicFriendsAdapter(getSupportFragmentManager(),uid);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    @OnClick({R.id.ll_attention, R.id.ll_fans, R.id.ll_good_friends})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_attention:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
            case R.id.ll_fans:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
            case R.id.ll_good_friends:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
        }
    }
}
