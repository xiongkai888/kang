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
import com.lanmei.kang.event.ScanUserEvent;
import com.lanmei.kang.helper.AddGoodsSellHelper;
import com.lanmei.kang.qrcode.ScanActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加销售商品
 */
public class GoodsSellActivity extends BaseActivity  implements TextView.OnEditorActionListener{

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


    //扫描用户条形码成功后调用
    @Subscribe
    public void scanUserEvent(ScanUserEvent event){
        numberEt.setText(event.getResult());
        searchUsers(event.getResult());
    }

    UserBean userBean;

    //搜索会员
    private void searchUsers(String result) {
        KangQiMeiApi api = new KangQiMeiApi("app/member_list");
        api.addParams("menber_num",result).addParams("user_type",0);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<UserBean>>() {
            @Override
            public void onResponse(NoPageListBean<UserBean> response) {
                if (isFinishing()){
                    return;
                }
                List<UserBean> beanList = response.data;
                if (StringUtils.isEmpty(beanList)){
                    userBean = null;
                    UIHelper.ToastMessage(getContext(),"不存在该用户");
                    return;
                }
                userBean = beanList.get(0);
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
                bundle.putInt("type",4);//4扫描会员
                bundle.putBoolean("isQR",true);//条形码
                IntentUtil.startActivity(this, ScanActivity.class,bundle);
                break;
            case R.id.submit_tv:
               CommonUtils.developing(this);
                break;
        }
    }
}
