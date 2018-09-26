package com.lanmei.kang.ui.user.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.RechargeAdapter;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.RechargeSubBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.WXPayHelper;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.WrapHeightGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.gridView)
    WrapHeightGridView mGridView;//充值金额列表
    RechargeAdapter mAdapter;
    @InjectView(R.id.ll_input_money)
    LinearLayout mLLInputMoney;//输入金额布局

    @InjectView(R.id.cb_zhifubao)
    CheckBox mZhifubaoCB;//支付宝
    @InjectView(R.id.cb_weixin)
    CheckBox mWeixinCB;//微信
    @InjectView(R.id.input_money_et)
    EditText mInputMoneyEt;//输入其他金额
    List<RechargeSubBean> list;

    private boolean isOtherMoney = false;//是不是其他金额
    private String rechargeMoney;//充值的金额

    @Override
    public int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("充值");
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        mAdapter = new RechargeAdapter(this);
        list = getList();
        mAdapter.setData(list);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {
                    isOtherMoney = true;
                    rechargeMoney = "";
                    mLLInputMoney.setVisibility(View.VISIBLE);
                } else {
                    isOtherMoney = false;
                    rechargeMoney = list.get(position).getMoneyInt()+"";
                    mLLInputMoney.setVisibility(View.GONE);
                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    RechargeSubBean bean = list.get(i);
                    bean.setSelected(false);
                }
                list.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                L.d("BeanRequest",""+position);
            }
        });

        EventBus.getDefault().register(this);
    }


    public List<RechargeSubBean> getList() {
        List<RechargeSubBean> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            RechargeSubBean bean = new RechargeSubBean();
            if (i != 6){
                int money = (i+1)*500;
                bean.setMoney(money+"元");
                bean.setMoneyInt(money);
            }else {
                bean.setMoney("其他金额");
            }
            list.add(bean);
        }
        return list;
    }

    @OnClick(R.id.affirm_bt)
    public void showAffirmRecharge() {//确认并充值

        if (isOtherMoney) {
            rechargeMoney = mInputMoneyEt.getText().toString().trim();
            if (StringUtils.isEmpty(rechargeMoney)) {
                UIHelper.ToastMessage(this, R.string.input_recharge_money);
                return;
            }
        } else {
            if (StringUtils.isEmpty(rechargeMoney)) {
                UIHelper.ToastMessage(this, R.string.choose_recharge_money);
                return;
            }
        }
        ajaxPay();//充值
    }

    private void ajaxPay() {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("member/recharge");
        api.addParams("token",api.getToken(this));
        api.addParams("money",rechargeMoney);
        api.addParams("pay_type",type);
        if (type == 1) {//支付宝充值
//            UIHelper.ToastMessage(this,R.string.developing);
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
                @Override
                public void onResponse(DataBean<String> response) {
                    if (isFinishing()) {
                        return;
                    }
                    String bean = response.data;
                    AlipayHelper alipayHelper = new AlipayHelper(RechargeActivity.this);
                    alipayHelper.setPayParam(bean);
                    alipayHelper.payNow();
                }
            });
        } else if (type == 7) {//微信充值
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<WeiXinBean>>() {
                @Override
                public void onResponse(DataBean<WeiXinBean> response) {
                    if (RechargeActivity.this.isFinishing()) {
                        return;
                    }
                    WeiXinBean bean = response.data;
                    WXPayHelper payHelper = new WXPayHelper(RechargeActivity.this);
                    payHelper.setPayParam(bean);
                    payHelper.orderNow();
                }
            });
        }

    }


    @OnClick({R.id.ll_zhifubao_pay, R.id.ll_weixin_pay,
            R.id.cb_zhifubao, R.id.cb_weixin})
    public void selectPayMethod(View view) {//选择充值方式
        switch (view.getId()) {

            case R.id.ll_weixin_pay:
            case R.id.cb_weixin://微信
                selectCheckBox(mWeixinCB, mZhifubaoCB, 7);
                break;
            case R.id.ll_zhifubao_pay:
            case R.id.cb_zhifubao://支付宝
                selectCheckBox(mZhifubaoCB, mWeixinCB, 1);
                break;
        }

    }


    int type = 1;// 1:支付宝充值   7：微信充值

    //选择支付方式
    private void selectCheckBox(CheckBox cb1, CheckBox cb2, int type) {
        this.type = type;
        cb1.setChecked(true);
        if (cb2.isChecked()) {
            cb2.setChecked(false);
        }
    }


    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
