package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsSellBean;
import com.lanmei.kang.event.AddGoodsSellEvent;
import com.lanmei.kang.event.ScanSellGoodsEvent;
import com.lanmei.kang.event.ScanUserEvent;
import com.lanmei.kang.helper.AddGoodsSellHelper;
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
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加销售商品
 */
public class GoodsSellActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.ll_goods_sell)
    LinearLayout root;
    @InjectView(R.id.total_price_tv)
    FormatTextView totalPriceTv;
    @InjectView(R.id.number_et)
    EditText numberEt;//会员编号
    private AddGoodsSellHelper helper;

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_sell;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.goods_sell);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        helper = new AddGoodsSellHelper(this, root, totalPriceTv);
        numberEt.setOnEditorActionListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                helper.addItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //扫描成功获取商品编号的时候调用
    @Subscribe
    public void scanSellGoodsEvent(ScanSellGoodsEvent event) {
        helper.updateData(event.getResult());
    }

    //扫描用户条形码成功后调用
    @Subscribe
    public void scanUserEvent(ScanUserEvent event) {
        numberEt.setText(event.getResult());
        searchUsers(event.getResult());
    }

    UserBean userBean;

    //搜索会员
    private void searchUsers(String result) {
        KangQiMeiApi api = new KangQiMeiApi("app/member_list");
        api.add("menber_num", result).add("user_type", 0);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<UserBean>>() {
            @Override
            public void onResponse(NoPageListBean<UserBean> response) {
                if (isFinishing()) {
                    return;
                }
                List<UserBean> beanList = response.data;
                if (StringUtils.isEmpty(beanList)) {
                    userBean = null;
                    UIHelper.ToastMessage(getContext(), "不存在该会员");
                    return;
                }
                userBean = beanList.get(0);
                UIHelper.ToastMessage(getContext(), "存在该会员");
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_user_number);
                return false;
            }
            searchUsers(key);
            return true;
        }
        return false;
    }

    @OnClick({R.id.qr_code_iv, R.id.submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qr_code_iv:
                Bundle bundle = new Bundle();
                bundle.putInt("type", ScanActivity.USER_SCAN);//4扫描会员
                bundle.putBoolean("isQR", true);//条形码
                IntentUtil.startActivity(this, ScanActivity.class, bundle);
                break;
            case R.id.submit_tv:
                addSellGoods();
                break;
        }
    }

    private void addSellGoods() {
        String number = CommonUtils.getStringByEditText(numberEt);
        if (StringUtils.isEmpty(number)) {
            UIHelper.ToastMessage(this, getString(R.string.input_user_number));
            return;
        }
        if (StringUtils.isEmpty(userBean)) {
            searchUsers(number);
            return;
        }
        List<GoodsSellBean> list = helper.getList();
        int perfect = isPerfect(helper.getList());
        if (perfect == 1) {
            UIHelper.ToastMessage(this, "请先完善商品信息");
            return;
        } else if (perfect == 2) {
            return;
        }
        StringBuilder goodsid = new StringBuilder();
        StringBuilder price = new StringBuilder();
        StringBuilder num = new StringBuilder();
        StringBuilder danwei = new StringBuilder();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            String cornet = ((size - 1 != i) ? L.cornet : "");
            GoodsSellBean bean = list.get(i);
            goodsid.append(bean.getGid()).append(cornet);
            price.append(bean.getPrice()).append(cornet);
            num.append(bean.getNum()).append(cornet);
            danwei.append(bean.getUnit()).append(cornet);
        }

        KangQiMeiApi api = new KangQiMeiApi("app/goods_sale");
        api.add("uid", api.getUserId(this)).add("sellerid", userBean.getId())
                .add("goodsid", goodsid.toString()).add("price", price.toString()).add("num", num.toString()).add("danwei", danwei.toString());
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                EventBus.getDefault().post(new AddGoodsSellEvent());
                UIHelper.ToastMessage(getContext(), response.getInfo());
                finish();
            }
        });
    }

    private int isPerfect(List<GoodsSellBean> list) {
        for (GoodsSellBean bean : list) {
            if (StringUtils.isEmpty(bean.getNumber()) || bean.getNum() == 0 || bean.getPrice() == 0 || StringUtils.isEmpty(bean.getUnit())) {
                return 1;
            }
            if (StringUtils.isEmpty(bean.getGid())) {
                UIHelper.ToastMessage(this, "请先搜索编号为 " + bean.getNumber() + "的商品，确认是否存在！");
                return 2;
            }
        }
        return 3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
