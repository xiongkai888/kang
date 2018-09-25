package com.lanmei.kang.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.event.LoginQuitEvent;
import com.lanmei.kang.helper.UserHelper;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/25.
 * 我的
 */

public class MineFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.fl_content)
    FrameLayout fl_content;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mToolbar.setTitle(R.string.mine);
        setUserType();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void setUserType() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (UserHelper.getInstance(context).hasLogin()) {
            if (StringUtils.isSame(CommonUtils.getUserBean(context).getUser_type(), CommonUtils.isZero)) {
                transaction.replace(R.id.fl_content, MineUserFragment.newInstance());
            } else {
                transaction.replace(R.id.fl_content, MineMerchantFragment.newInstance());
            }
        } else {
            transaction.replace(R.id.fl_content, MineUserFragment.newInstance());
        }
        transaction.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOrQuit(LoginQuitEvent event) {
        setUserType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
