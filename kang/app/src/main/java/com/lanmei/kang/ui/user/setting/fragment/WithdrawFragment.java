package com.lanmei.kang.ui.user.setting.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.WithdrawApi;
import com.lanmei.kang.bean.WithdrawCardListBean;
import com.lanmei.kang.helper.ReceiverHelper;
import com.xson.common.helper.UserHelper;
import com.lanmei.kang.ui.user.setting.BoundKaActivity;
import com.lanmei.kang.ui.user.setting.ChooseKaActivity;
import com.lanmei.kang.ui.user.setting.ClubActivity;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.lanmei.kang.ui.user.setting.ChooseKaActivity.UNBOUND_SUCCEED;


/**
 * Created by Administrator on 2017/4/27.
 * 提现
 */

public class WithdrawFragment extends BaseFragment {

    public final static String CHOOSE_CARD = "CHOOSE_CARD";// 选择卡号广播

    @InjectView(R.id.card_tv)
    TextView mCardNameTv;
    @InjectView(R.id.balance_tv)
    TextView mBalanceTv;//余额
    ReceiverHelper mReceiverHelper;

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
        // 选择卡号广播
        mReceiverHelper =  new ReceiverHelper(context);
        mReceiverHelper.addAction(CHOOSE_CARD);
        mReceiverHelper.addAction(UNBOUND_SUCCEED);//解绑成功
        mReceiverHelper.registerReceiver();
        mReceiverHelper.setReceiveHelperListener(new ReceiverHelper.ReceiveHelperListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (CHOOSE_CARD.equals(action)) {
                    String cardName = intent.getStringExtra("cardName");
                    mCardNameTv.setText(cardName);
                }else if (UNBOUND_SUCCEED.equals(action)){
                    ajaxWithdraw();
                }
            }
        });

        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (bean != null) {
//            mBalanceTv.setText(bean.getMoney());
        }
        ajaxWithdraw();
    }

    private void ajaxWithdraw() {
        HttpClient httpClient = HttpClient.newInstance(context);
        WithdrawApi api = new WithdrawApi();
        api.token = UserHelper.getInstance(context).getToken();
        httpClient.request(api, new BeanRequest.SuccessListener<NoPageListBean<WithdrawCardListBean>>() {
            @Override
            public void onResponse(NoPageListBean<WithdrawCardListBean> response) {
                if (mCardNameTv == null){
                    return;
                }
                List<WithdrawCardListBean> list = response.data;
                if (list != null && list.size()>0){
                    WithdrawCardListBean bean = list.get(0);
                    if (bean != null){
                        mCardNameTv.setText(bean.getBanks_name());
                    }
                }else {
                    if (ClubActivity.no_bound_card){
                        alertDialog();
                        return;
                    }
                }
            }
        });

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
    public void showWithdrawKa(){//选择提现银行卡
        IntentUtil.startActivity(context, ChooseKaActivity.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReceiverHelper.onDestroy();
    }
}
