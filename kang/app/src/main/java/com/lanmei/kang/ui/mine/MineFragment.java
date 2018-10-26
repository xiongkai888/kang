package com.lanmei.kang.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;

/**
 * Created by xkai on 2018/8/24.
 * 我的
 */

public class MineFragment extends BaseFragment {

    @InjectView(R.id.fl_content)
    FrameLayout fl_content;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUserType();
    }

    public void setUserType() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, CommonUtils.isUser(context) ? MineUserFragment.newInstance() : MineMerchantFragment.newInstance());
        transaction.commitAllowingStateLoss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setUserEvent(SetUserInfoEvent event) {
        setUserType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
