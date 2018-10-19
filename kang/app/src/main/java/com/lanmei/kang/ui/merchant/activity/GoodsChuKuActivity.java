package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.event.ChuKuEvent;
import com.lanmei.kang.event.ChuKuGoodsInfoEvent;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 出库(入库)
 */
public class GoodsChuKuActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.merchant_name_tv)
    TextView merchantNameTv;//选择商家
    @InjectView(R.id.number_et)
    EditText numberEt;//商品编号
    @InjectView(R.id.num_tv)
    EditText numTv;//数量
    @InjectView(R.id.price_et)
    EditText priceEt;//单价
    @InjectView(R.id.unit_et)
    EditText unitEt;//单位
    @InjectView(R.id.ll_merchant)
    View llMerchant;
    @InjectView(R.id.ll_price)
    View llPrice;
    private boolean isChuKu;
    private OptionPicker picker;
    private List<UserBean> list;//商家列表
    private String pid;//商家id
    private MerchantTabGoodsBean merchantTabGoodsBean;//根据商品编号搜索出来的商品

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_chu_ku;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        isChuKu = StringUtils.isSame(getIntent().getStringExtra("value"), CommonUtils.isZero);
        numberEt.setOnEditorActionListener(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(isChuKu ? R.string.chu_ku : R.string.ru_ku);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        if (!isChuKu) {//入库
            llMerchant.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
        } else {
            loadMerchantList();
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_serial_number);
                return false;
            }
            searchGoods(key);
            return true;
        }
        return false;
    }

    private void loadMerchantList() {
        KangQiMeiApi api = new KangQiMeiApi("app/member_list");
        api.addParams("user_type", 1);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<UserBean>>() {
            @Override
            public void onResponse(NoPageListBean<UserBean> response) {
                if (isFinishing()) {
                    return;
                }
                list = response.data;
                if (StringUtils.isEmpty(list)) {
                    UIHelper.ToastMessage(getContext(),getString(R.string.no_merchant_list_data));
                    return;
                }
                initPicker(getListAtString(list));
            }
        });
    }

    //出库入库商品扫描成功时调用
    @Subscribe
    public void chuKuGoodsInfoEvent(ChuKuGoodsInfoEvent event) {
        L.d(L.TAG, event.getBarcode());
        String barcode = event.getBarcode();
        searchGoods(barcode);
        numberEt.setText(barcode);

    }

    private void searchGoods(String barcode) {
        KangQiMeiApi api = new KangQiMeiApi("app/good_list");
        api.addParams("barcode", barcode);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabGoodsBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabGoodsBean> response) {
                if (isFinishing()) {
                    return;
                }
                List<MerchantTabGoodsBean> beanList = response.data;
                if (StringUtils.isEmpty(beanList)) {
                    merchantTabGoodsBean = null;
                    priceEt.setText("");
                    unitEt.setText("");
                    UIHelper.ToastMessage(getContext(), "不存在该商品");
                    return;
                }
                merchantTabGoodsBean = beanList.get(0);
                numTv.setText(merchantTabGoodsBean.getInventory());
                priceEt.setText(merchantTabGoodsBean.getPrice());
            }
        });
    }

    private List<String> getListAtString(List<UserBean> list) {
        List<String> stringList = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringList.add(list.get(i).getNickname());
        }
        return stringList;
    }


    private void initPicker(List<String> items) {
        picker = new OptionPicker(this, items);
        picker.setOffset(3);
        picker.setSelectedIndex(1);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                merchantNameTv.setText(item);
                pid = list.get(index).getId();
            }
        });
    }

    @OnClick({R.id.ll_merchant, R.id.qr_code_iv, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_merchant://选择商家
                if (StringUtils.isEmpty(list)) {
                    loadMerchantList();
                    return;
                }
                if (picker != null) {
                    picker.show();
                }
                break;
            case R.id.qr_code_iv://条形码扫描
                Bundle bundle = new Bundle();
                bundle.putInt("type", ScanActivity.CHUKU_RUKU_SCAN);//出库入库
                bundle.putBoolean("isQR", true);//条形码
                IntentUtil.startActivity(this, ScanActivity.class, bundle);
                break;
            case R.id.submit_bt://提交
                loadGoods();
                break;
        }
    }

    private void loadGoods() {
        if (isChuKu) {
            if (StringUtils.isEmpty(pid)) {
                UIHelper.ToastMessage(this, getString(R.string.choose_merchant));
                return;
            }
        }
        String goodsid = CommonUtils.getStringByEditText(numberEt);
        if (StringUtils.isEmpty(goodsid)) {
            UIHelper.ToastMessage(this, getString(R.string.input_serial_number));
            return;
        }
        String number = CommonUtils.getStringByEditText(numTv);
        if (StringUtils.isEmpty(number)) {
            UIHelper.ToastMessage(this, getString(R.string.input_number));
            return;
        }
        String price = CommonUtils.getStringByEditText(priceEt);
        if (isChuKu) {
            if (StringUtils.isEmpty(price)) {
                UIHelper.ToastMessage(this, getString(R.string.input_price));
                return;
            }
        }
        String danwei = CommonUtils.getStringByEditText(unitEt);
        if (StringUtils.isEmpty(danwei)) {
            UIHelper.ToastMessage(this, getString(R.string.input_unit));
            return;
        }
        KangQiMeiApi api = new KangQiMeiApi(isChuKu ? "app/good_cstoreroom" : "app/good_rstoreroom");
        api.addParams("goodsid", goodsid).addParams("number", number).addParams("danwei", danwei);
        if (isChuKu) {
            api.addParams("pid", pid).addParams("price", price);
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getInfo());
                EventBus.getDefault().post(new ChuKuEvent());
                finish();
            }
        });
    }
}
