package com.lanmei.kang.ui.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.kang.R;
import com.lanmei.kang.alipay.AlipayHelper;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.lanmei.kang.bean.OrderIDBean;
import com.lanmei.kang.bean.WeiXinBean;
import com.lanmei.kang.bean.ZhiFuBaoBean;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.helper.WXPayHelper;
import com.lanmei.kang.ui.mine.activity.MyItemsOrderActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.EditTextWatcher;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 预定信息
 */

public class ReserveInfoActivity extends BaseActivity {

    int type = 6;//支付类型  7：微信支付 1：支付宝 6：余额支付

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.order_num_et)
    EditText orderNumEt;//订购数量

    @InjectView(R.id.cb_zhifubao)
    CheckBox mZhifubaoCB;//支付宝
    @InjectView(R.id.cb_weixin)
    CheckBox mWeixinCB;//微信
    @InjectView(R.id.cb_balance)
    CheckBox mBalanceCB;//余额

    MerchantDetailsBean.GoodsBean bean;//商家的服务项目

    int orderNum = 1;//订购数量,默认1
    double price;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.items_tv)
    TextView itemsTv;
    @InjectView(R.id.reserve_date_tv)
    TextView reserveDateTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;


    @Override
    public int getContentViewId() {
        return R.layout.activity_reserve_info;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        bean = (MerchantDetailsBean.GoodsBean) bundle.getSerializable("bean");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.reserve_info);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);


        String priceStr = bean.getSell_price();
        if (!StringUtils.isEmpty(priceStr)) {
            price = Double.valueOf(priceStr);
        }
        nameTv.setText(bean.getPlace_name());
        itemsTv.setText(bean.getName());
        totalPriceTv.setText(price + "");
        reserveDateTv.setText(CommonUtils.getData());
        orderNumEt.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s + "")) {
                    orderNum = 0;
                } else {
                    orderNum = EditTextWatcher.StringToInt(s);
                }
            }
        });
    }


    //选择支付方式
    private void selectCheckBox(CheckBox cb1, CheckBox cb2, CheckBox cb3, int type) {
        this.type = type;
        cb1.setChecked(true);
        if (cb2.isChecked() || cb3.isChecked()) {
            cb2.setChecked(false);
            cb3.setChecked(false);
        }
    }

    public boolean isSelect() {
        return type == 6 || type == 7 || type == 1;
    }

    private void ajaxBalancePay() {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("reservation/save");
        api.add("uid",api.getUserId(this));
        api.add("mid",bean.getMid());
        api.add("items_id",bean.getId());
        api.add("guest",orderNum);
        long time = System.currentTimeMillis();
        api.add("stime",time);
        api.add("etime",time);
        api.add("pay_type",type);
        api.add("token",api.getToken(this));
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<OrderIDBean>>() {
            @Override
            public void onResponse(DataBean<OrderIDBean> response) {
                if (isFinishing()) {
                    return;
                }
                OrderIDBean bean = response.data;
                if (bean != null) {
                    loadPayMent(bean.getOrder_id() + "");
                }
            }
        });
    }

    private void loadPayMent(String order_id) {
        KangQiMeiApi api = new KangQiMeiApi("payment/pay");
        api.add("order_id",order_id);
        api.add("uid",api.getUserId(this));
        api.add("token",api.getToken(this));
        api.setMethod(AbstractApi.Method.GET);
        HttpClient httpClient = HttpClient.newInstance(this);
        if (type == 1) {//支付宝支付
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
                @Override
                public void onResponse(DataBean<String> response) {
                    if (isFinishing()) {
                        return;
                    }
                    String bean = response.data;
                    AlipayHelper alipayHelper = new AlipayHelper(ReserveInfoActivity.this);
                    alipayHelper.setPayParam(bean);
                    alipayHelper.payNow();
                }
            });
        } else if (type == 7) {//微信支付
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<WeiXinBean>>() {
                @Override
                public void onResponse(DataBean<WeiXinBean> response) {
                    if (isFinishing()) {
                        return;
                    }
                    WeiXinBean bean = response.data;
                    WXPayHelper payHelper = new WXPayHelper(ReserveInfoActivity.this);
                    payHelper.setPayParam(bean);
                    payHelper.orderNow();
                }
            });
        } else if (type == 6) {//余额
            httpClient.loadingRequest(api, new BeanRequest.SuccessListener<DataBean<ZhiFuBaoBean>>() {
                @Override
                public void onResponse(DataBean<ZhiFuBaoBean> response) {
                    if (isFinishing()) {
                        return;
                    }
                    IntentUtil.startActivity(ReserveInfoActivity.this, MyItemsOrderActivity.class);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    UIHelper.ToastMessage(ReserveInfoActivity.this, getString(R.string.balance_insufficient));
                }
            });
        }
    }


    @OnClick({R.id.order_subtract_iv, R.id.order_add_iv, R.id.ll_zhifubao_pay, R.id.ll_weixin_pay, R.id.ll_balance_pay,
            R.id.cb_zhifubao, R.id.cb_weixin, R.id.cb_balance, R.id.particulars_tv, R.id.bt_pay})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(this)){
            return;
        }
        switch (view.getId()) {
            case R.id.order_subtract_iv://订购减
                if (orderNum <= 1) {
                    return;
                }
                orderNum--;
                orderNumEt.setText(orderNum + "");
                totalPriceTv.setText(DoubleUtil.formatFloatNumber(orderNum * price));
                break;
            case R.id.order_add_iv://订购加
                if (orderNum == 9999) {
                    return;
                }
                orderNum++;
                orderNumEt.setText(orderNum + "");
                totalPriceTv.setText(DoubleUtil.formatFloatNumber(orderNum * price));
                break;

            case R.id.ll_weixin_pay:
            case R.id.cb_weixin://微信
                selectCheckBox(mWeixinCB, mZhifubaoCB, mBalanceCB, 7);
                break;
            case R.id.ll_zhifubao_pay:
            case R.id.cb_zhifubao://支付宝
                selectCheckBox(mZhifubaoCB, mWeixinCB, mBalanceCB, 1);
                break;
            case R.id.ll_balance_pay:
            case R.id.cb_balance://余额支付
                selectCheckBox(mBalanceCB, mWeixinCB, mZhifubaoCB, 6);
                break;
            case R.id.particulars_tv://明细
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.bt_pay://确认并支付
                if (isSelect()) {
                    ajaxBalancePay();
                } else {
                    UIHelper.ToastMessage(this, R.string.developing);
                }
                break;

        }
    }

    //支付成功调用
    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event) {
        IntentUtil.startActivity(ReserveInfoActivity.this, MyItemsOrderActivity.class);
        finish();
    }

}
