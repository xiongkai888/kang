package com.lanmei.kang.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.ActivityResultEvent;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.lanmei.kang.helper.CameraHelper;
import com.lanmei.kang.ui.merchant.ClientValuateActivity;
import com.lanmei.kang.ui.merchant.MerchantDataActivity;
import com.lanmei.kang.ui.merchant.MerchantOrderActivity;
import com.lanmei.kang.ui.merchant.activity.ChuKuListActivity;
import com.lanmei.kang.ui.merchant.activity.GoodsSellListActivity;
import com.lanmei.kang.ui.merchant.activity.MerchantItemsActivity;
import com.lanmei.kang.ui.merchant.activity.MyTeamActivity;
import com.lanmei.kang.ui.mine.activity.AlbumActivity;
import com.lanmei.kang.ui.mine.activity.MyGoodsOrderActivity;
import com.lanmei.kang.ui.mine.activity.MyOrderActivity;
import com.lanmei.kang.ui.mine.activity.PersonalDataActivity;
import com.lanmei.kang.ui.mine.activity.SettingActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
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
 * 我的（商家）
 */

public class MineMerchantFragment extends BaseFragment {

    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;

    private CameraHelper cameraHelper;

    public static MineMerchantFragment newInstance() {
        Bundle args = new Bundle();
        MineMerchantFragment fragment = new MineMerchantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_merchant;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        cameraHelper = new CameraHelper(context);
        cameraHelper.setHeadUrlListener(new CameraHelper.HeadUrlListener() {
            @Override
            public void getUrl(String url) {
                if (userNameTv == null) {
                    return;
                }
                ajaxUploadingHead(url);
            }
        });

        setUser(UserHelper.getInstance(context).getUserBean());//初始化用户信息
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

    //在我的上家界面修改头像时
    @Subscribe
    public void ActivityResultEvent(ActivityResultEvent event) {
        switch (event.getType()) {
            case CameraHelper.CHOOSE_FROM_GALLAY:
                cameraHelper.startActionCrop(event.getData());
                L.d(L.TAG,"MineMerchantFragment.ActivityResultEvent 0");
                break;
            case CameraHelper.CHOOSE_FROM_CAMERA:
                //注意小米拍照后data 为null
                cameraHelper.startActionCrop(cameraHelper.getTempImage().getPath());
                L.d(L.TAG,"MineMerchantFragment.ActivityResultEvent 1");
                break;
            case CameraHelper.RESULT_FROM_CROP:
                cameraHelper.uploadNewPhoto(headIv);//
                cameraHelper.execute();
                L.d(L.TAG,"MineMerchantFragment.ActivityResultEvent 2");
                break;
        }
    }

    @OnClick({R.id.ll_team,R.id.ll_goods_order,R.id.ll_m_sell, R.id.ll_m_chuku, R.id.ll_m_ruku, R.id.head_iv, R.id.ll_m_data, R.id.ll_m_order, R.id.ll_m_album, R.id.ll_m_services, R.id.ll_m_evaluate, R.id.ll_m_setting, R.id.ll_m_center, R.id.ll_mime_order})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(context)) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_team://我的团队
                IntentUtil.startActivity(context, MyTeamActivity.class);
                break;
            case R.id.ll_m_sell://商品销售
                IntentUtil.startActivity(context, GoodsSellListActivity.class);
                break;
            case R.id.ll_m_chuku://出库
                IntentUtil.startActivity(context, ChuKuListActivity.class, CommonUtils.isZero);
                break;
            case R.id.ll_m_ruku://入库
                IntentUtil.startActivity(context, ChuKuListActivity.class, CommonUtils.isOne);
                break;
            case R.id.ll_m_data://商家资料
                IntentUtil.startActivity(context, MerchantDataActivity.class);
                break;
            case R.id.ll_m_order://订单
                IntentUtil.startActivity(context, MerchantOrderActivity.class);
                break;
            case R.id.ll_goods_order://我的商品订单
                IntentUtil.startActivity(context, MyGoodsOrderActivity.class);
                break;
            case R.id.ll_m_album://相册
                IntentUtil.startActivity(context, AlbumActivity.class);
                break;
            case R.id.ll_m_services://服务项目MerchantItemsActivity
                IntentUtil.startActivity(context, MerchantItemsActivity.class);
                break;
            case R.id.ll_m_evaluate://评论
                IntentUtil.startActivity(context, ClientValuateActivity.class);
                break;
            case R.id.ll_m_setting://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.ll_m_center://个人中心
                IntentUtil.startActivity(context, PersonalDataActivity.class);
                break;
            case R.id.ll_mime_order://我的订单
                IntentUtil.startActivity(context, MyOrderActivity.class);
                break;
            case R.id.head_iv://点击头像选择上传头像
                cameraHelper.showDialog();
                break;
        }
    }

    private void ajaxUploadingHead(final String headUrl) {
        if (StringUtils.isEmpty(headUrl)) {
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi("member/update");
        api.add("token", api.getToken(context));
        api.add("uid", api.getUserId(context));
        api.add("pic", headUrl);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (userNameTv == null) {
                    return;
                }
                CommonUtils.loadUserInfo(KangApp.applicationContext, null);
                UIHelper.ToastMessage(getContext(), response.getInfo());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
