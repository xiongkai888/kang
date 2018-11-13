package com.lanmei.kang.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.ui.merchant.activity.MyTeamActivity;
import com.lanmei.kang.ui.mine.activity.CouponActivity;
import com.lanmei.kang.ui.mine.activity.GoodFriendsActivity;
import com.lanmei.kang.ui.mine.activity.HealthReportActivity;
import com.lanmei.kang.ui.mine.activity.MembershipCardActivity;
import com.lanmei.kang.ui.mine.activity.MyCollectActivity;
import com.lanmei.kang.ui.mine.activity.MyDynamicActivity;
import com.lanmei.kang.ui.mine.activity.MyGoodsOrderActivity;
import com.lanmei.kang.ui.mine.activity.MyOrderActivity;
import com.lanmei.kang.ui.mine.activity.PersonalDataActivity;
import com.lanmei.kang.ui.mine.activity.SettingActivity;
import com.lanmei.kang.ui.shake.ShakeActivity;
import com.lanmei.kang.ui.user.setting.ClubActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/25.
 * 我的（用户）
 */

public class MineUserFragment extends BaseFragment {

    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;

    public static MineUserFragment newInstance() {
        Bundle args = new Bundle();
        MineUserFragment fragment = new MineUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_user;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUser(UserHelper.getInstance(context).getUserBean());//初始化用户信息
    }

    @OnClick({R.id.ll_health_report,R.id.ll_team,R.id.ll_goods_order,R.id.ll_membership_card,R.id.head_iv, R.id.ll_my_order, R.id.ll_my_personal, R.id.ll_my_coupon, R.id.ll_my_collect,R.id.ll_my_account,
            R.id.ll_my_friend, R.id.ll_my_dynamic, R.id.ll_online, R.id.ll_setting, R.id.ll_vibrator})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(context)) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_team://我的团队
                IntentUtil.startActivity(context, MyTeamActivity.class);
                break;
            case R.id.ll_membership_card://我的会员卡
                IntentUtil.startActivity(context, MembershipCardActivity.class);
                break;
            case R.id.ll_my_order://我的项目订单
                IntentUtil.startActivity(context, MyOrderActivity.class);
                break;
            case R.id.ll_goods_order://我的商品订单
                IntentUtil.startActivity(context, MyGoodsOrderActivity.class);
                break;
            case R.id.ll_my_personal://我的资料
            case R.id.head_iv://头像
                IntentUtil.startActivity(context, PersonalDataActivity.class);
                break;
            case R.id.ll_my_coupon://我的优惠卷
                IntentUtil.startActivity(context, CouponActivity.class);
                break;
            case R.id.ll_my_collect://我的收藏
                IntentUtil.startActivity(context, MyCollectActivity.class);
                break;
            case R.id.ll_my_friend://我的好友
                IntentUtil.startActivity(context, GoodFriendsActivity.class);
                break;
            case R.id.ll_my_account://我的账号
                IntentUtil.startActivity(context, ClubActivity.class);
                break;
            case R.id.ll_my_dynamic://我的动态
                IntentUtil.startActivity(context, MyDynamicActivity.class);
                break;
            case R.id.ll_online://在线咨询
                IntentUtil.startActivity(context, com.hyphenate.chatuidemo.ui.MainActivity.class);
                break;
            case R.id.ll_setting://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.ll_vibrator://摇一摇测试
                IntentUtil.startActivity(context, ShakeActivity.class);
                break;
            case R.id.ll_health_report://健康报告
                IntentUtil.startActivity(context, HealthReportActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetUserInfoEvent event) {
        setUser(UserHelper.getInstance(context).getUserBean());
    }

    private void setUser(UserBean userBean) {
        if (userBean == null) {
            userNameTv.setText("游客");
            headIv.setImageResource(R.mipmap.default_pic);
            return;
        }
        userNameTv.setText(userBean.getNickname());
        ImageHelper.load(context, userBean.getPic(), headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
