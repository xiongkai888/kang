package com.lanmei.kang.ui.user.setting.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.WithdrawCardListBean;
import com.lanmei.kang.event.CardEvent;
import com.lanmei.kang.ui.user.setting.BoundKaActivity;
import com.lanmei.kang.ui.user.setting.ChooseKaActivity;
import com.lanmei.kang.ui.user.setting.ClubActivity;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/27.
 * 提现
 */

public class WithdrawFragment extends BaseFragment {

    @InjectView(R.id.card_tv)
    TextView mCardNameTv;
    @InjectView(R.id.balance_tv)
    TextView mBalanceTv;//余额

    @Override
    public int getContentViewId() {
        return R.layout.fragment_withdraw;
    }

    public static WithdrawFragment newInstance() {
        Bundle args = new Bundle();
        WithdrawFragment fragment = new WithdrawFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (bean != null) {
            mBalanceTv.setText(bean.getMoney());
        }
        ajaxWithdraw();
    }

    private void ajaxWithdraw() {
        HttpClient httpClient = HttpClient.newInstance(context);
        KangQiMeiApi api = new KangQiMeiApi("member/bank_card");
        api.add("token", api.getToken(context));
        api.setMethod(AbstractApi.Method.GET);
        httpClient.request(api, new BeanRequest.SuccessListener<NoPageListBean<WithdrawCardListBean>>() {
            @Override
            public void onResponse(NoPageListBean<WithdrawCardListBean> response) {
                if (mCardNameTv == null) {
                    return;
                }
                List<WithdrawCardListBean> list = response.data;
                if (list != null && list.size() > 0) {
                    WithdrawCardListBean bean = list.get(0);
                    if (bean != null) {
                        mCardNameTv.setText(bean.getBanks_name());
                    }
                } else {
                    if (ClubActivity.no_bound_card) {
                        alertDialog();
                    }
                }
            }
        });
    }

    @Subscribe
    public void cardEvent(CardEvent event){
        switch (event.getType()){
            case 1:
                mCardNameTv.setText(event.getName());
                break;
            case 2:
                ajaxWithdraw();
                break;
        }
    }

    private void alertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(R.string.no_bound_card)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ClubActivity.no_bound_card = false;
                        IntentUtil.startActivity(context, BoundKaActivity.class);
                    }
                })
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ClubActivity.no_bound_card = false;
                    }
                })
                .setCancelable(false).create();
        dialog.show();
    }

    @OnClick(R.id.ll_choose_ka)
    public void showWithdrawKa() {//选择提现银行卡
        IntentUtil.startActivity(context, ChooseKaActivity.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
