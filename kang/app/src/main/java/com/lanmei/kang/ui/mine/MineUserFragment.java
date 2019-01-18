package com.lanmei.kang.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NumBean;
import com.lanmei.kang.bean.SiteinfoBean;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.ui.merchant.activity.MyTeamActivity;
import com.lanmei.kang.ui.mine.activity.CouponActivity;
import com.lanmei.kang.ui.mine.activity.GoodFriendsActivity;
import com.lanmei.kang.ui.mine.activity.GoodsCollectActivity;
import com.lanmei.kang.ui.mine.activity.HealthReportActivity;
import com.lanmei.kang.ui.mine.activity.MembershipCardActivity;
import com.lanmei.kang.ui.mine.activity.MyCollectActivity;
import com.lanmei.kang.ui.mine.activity.MyDynamicActivity;
import com.lanmei.kang.ui.mine.activity.MyGoodsOrderActivity;
import com.lanmei.kang.ui.mine.activity.PersonalDataActivity;
import com.lanmei.kang.ui.mine.activity.SettingActivity;
import com.lanmei.kang.ui.user.setting.ClubActivity;
import com.lanmei.kang.update.UpdateAppConfig;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
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
    @InjectView(R.id.rid_name_tv)
    TextView ridNameTv;

    @InjectView(R.id.m1_num_tv)
    TextView m1NumTv;
    @InjectView(R.id.m2_num_tv)
    TextView m2NumTv;
    @InjectView(R.id.m3_num_tv)
    TextView m3NumTv;
    @InjectView(R.id.m4_num_tv)
    TextView m4NumTv;
    @InjectView(R.id.yu_e)
    TextView yuE;
    @InjectView(R.id.you_hui_juan)
    TextView youHuiJuan;
    @InjectView(R.id.guan_zhu)
    TextView guanZhu;
    @InjectView(R.id.zixun_shou_cang)
    TextView zixunShouCang;
    @InjectView(R.id.wode_dongtai)
    TextView wodeDongtai;
    @InjectView(R.id.wode_haoyou)
    TextView wodeHaoyou;

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


    @OnClick({R.id.ll_data, R.id.m1, R.id.m2, R.id.m3, R.id.m4, R.id.yu_e, R.id.you_hui_juan, R.id.m5, R.id.m6, R.id.guan_zhu, R.id.zixun_shou_cang, R.id.wode_dongtai, R.id.wode_haoyou, R.id.m7, R.id.m8, R.id.m9, R.id.m10})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(context)) {
            return;
        }
        switch (view.getId()) {
            case R.id.m1:
                IntentUtil.startActivity(context, 1,MyGoodsOrderActivity.class);
                break;
            case R.id.m2:
                IntentUtil.startActivity(context, 2,MyGoodsOrderActivity.class);
                break;
            case R.id.m3:
                IntentUtil.startActivity(context, 3,MyGoodsOrderActivity.class);
                break;
            case R.id.m4:
                IntentUtil.startActivity(context, 0,MyGoodsOrderActivity.class);
                break;
            case R.id.yu_e:
                break;
            case R.id.you_hui_juan://优惠券
                IntentUtil.startActivity(context, CouponActivity.class);
                break;
            case R.id.m5://我的会员卡
                IntentUtil.startActivity(context, MembershipCardActivity.class);
                break;
            case R.id.m6://我的钱包
                IntentUtil.startActivity(context, ClubActivity.class);
                break;
            case R.id.guan_zhu:
                IntentUtil.startActivity(context, GoodsCollectActivity.class);
                break;
            case R.id.zixun_shou_cang://资讯收藏
                IntentUtil.startActivity(context, MyCollectActivity.class);
                break;
            case R.id.wode_dongtai://我的动态
                IntentUtil.startActivity(context, MyDynamicActivity.class);
                break;
            case R.id.wode_haoyou://我的好友
                IntentUtil.startActivity(context, GoodFriendsActivity.class);
                break;
            case R.id.m7://健康报告
                IntentUtil.startActivity(context, HealthReportActivity.class);
                break;
            case R.id.m8://我的团队
                IntentUtil.startActivity(context, MyTeamActivity.class);
                break;
            case R.id.m9://在线客服
                SiteinfoBean bean = UpdateAppConfig.bean;
                if (StringUtils.isEmpty(bean)){
                    return;
                }
                SiteinfoBean.BaseBean baseBean = bean.getBase();
                if (StringUtils.isEmpty(baseBean)){
                    return;
                }
                UIHelper.callPhone(context,baseBean.getPhone());
//                IntentUtil.startActivity(context, MainActivity.class);
                break;
            case R.id.m10://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.ll_data:
//                KangQiMeiApi api = new KangQiMeiApi("index/siteinfo");
//                HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<BaseBean>() {
//                    @Override
//                    public void onResponse(BaseBean response) {
//
//                    }
//                });
                IntentUtil.startActivity(context, PersonalDataActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetUserInfoEvent event) {
        setUser(event.getBean());
    }

    private void setUser(UserBean userBean) {
        if (userBean == null) {
            userNameTv.setText("游客");
            headIv.setImageResource(R.mipmap.default_pic);
            ridNameTv.setVisibility(View.GONE);
            return;
        }
        String rid = userBean.getRidname();
        if (StringUtils.isEmpty(rid)){
            ridNameTv.setVisibility(View.GONE);
        }else {
            ridNameTv.setVisibility(View.VISIBLE);
            ridNameTv.setText(rid);
        }
        userNameTv.setText(userBean.getNickname());
        ImageHelper.load(context, userBean.getPic(), headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!UserHelper.getInstance(context).hasLogin()) {
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi("app/allcount");
        api.add("uid",api.getUserId(context));
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<DataBean<NumBean>>() {
            @Override
            public void onResponse(DataBean<NumBean> response) {
                if (wodeHaoyou == null){
                    return;
                }
                NumBean bean = response.data;
                if (bean == null){
                    return;
                }
                setNum(m1NumTv,bean.getObligation());
                setNum(m2NumTv,bean.getReceiver());
                setNum(m3NumTv,bean.getAssess());

                yuE.setText(String.format(getString(R.string.mine_money),StringUtils.isEmpty(bean.getMoney())?"0":bean.getMoney()));
                youHuiJuan.setText(String.format(getString(R.string.mine_voucher),bean.getVoucher()+""));
                guanZhu.setText(String.format(getString(R.string.mine_goods_favour),bean.getGoods_favour()+""));

                zixunShouCang.setText(String.format(getString(R.string.mine_post_favour),bean.getPost_favour()+""));
                wodeDongtai.setText(String.format(getString(R.string.mine_posts_favour),bean.getPosts_favour()+""));
                wodeHaoyou.setText(String.format(getString(R.string.mine_friend),bean.getFriend()+""));
            }
        });
    }

    private void setNum(TextView textView,int num){
        if (num == 0){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(num+"");
        }
    }

}
