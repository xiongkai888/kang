package com.lanmei.kang.ui.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ItemsImgAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantDetailsBean;
import com.lanmei.kang.event.CollectItemsEvent;
import com.lanmei.kang.ui.merchant.ReserveInfoActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.EditTextWatcher;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商家项目详情
 */
public class MerchantItemsDetailsActivity extends BaseActivity {

    @InjectView(R.id.banner)
    ConvenientBanner banner;
    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.price_tv)
    TextView priceTv;
    @InjectView(R.id.collect_iv)
    ImageView collectIv;
    @InjectView(R.id.content_tv)
    TextView contentTv;//项目介绍
    @InjectView(R.id.order_num_et)
    EditText orderNumEt;//订购数量
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    MerchantDetailsBean.GoodsBean bean;//商家的服务项目

    int orderNum = 1;//订购数量,默认1
    String phone;


    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_items_details;
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        bean = (MerchantDetailsBean.GoodsBean) bundle.getSerializable("bean");
        phone = bundle.getString("phone");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(bean.getPlace_name());
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);
        //广告轮播
        CommonUtils.setBanner(banner, bean.getFile(),true);
        contentTv.setText("\u3000\u3000" + bean.getContent());
        nameTv.setText(bean.getName());
        priceTv.setText(String.format(getString(R.string.price), bean.getSell_price()));
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
        ItemsImgAdapter adapter = new ItemsImgAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setData(bean.getPic());
        recyclerView.setAdapter(adapter);
        collectIv.setImageResource(StringUtils.isSame(bean.getGood_favoured(), CommonUtils.isOne) ? R.mipmap.icon_collect_on : R.mipmap.icon_collect_off);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_message:
//                if (!CommonUtils.isLogin(this)) {
//                    break;
//                }
//                UIHelper.ToastMessage(this, R.string.developing);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @OnClick({R.id.collect_iv, R.id.phone_iv, R.id.order_subtract_iv, R.id.order_add_iv, R.id.ll_service, R.id.ll_order_now})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(this)){
            return;
        }
        switch (view.getId()) {
            case R.id.collect_iv://收藏
                collectItem();
                break;
            case R.id.order_subtract_iv://订购减
                if (orderNum <= 1) {
                    return;
                }
                orderNum--;
                orderNumEt.setText(orderNum + "");
                break;
            case R.id.order_add_iv://订购加
                if (orderNum == 9999) {
                    return;
                }
                orderNum++;
                orderNumEt.setText(orderNum + "");
                break;
            case R.id.ll_service://客服咨询(联系电话)
                if (StringUtils.isEmpty(phone)) {
                    UIHelper.ToastMessage(this, "暂无联系电话");
                    return;
                }
                UIHelper.callPhone(this, phone);
                break;
            case R.id.ll_order_now://立即购买(预定消息)
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                bundle.putString("name", bean.getPlace_name());
                IntentUtil.startActivity(this, ReserveInfoActivity.class, bundle);
                break;
        }
    }

    //收藏项目（服务）
    private void collectItem() {
        KangQiMeiApi api = new KangQiMeiApi("member/do_favour");
        api.addParams("id",bean.getId());
        api.addParams("uid",api.getUserId(this));
        if (StringUtils.isSame(bean.getGood_favoured(), CommonUtils.isOne)) {
            api.addParams("del",bean.getGood_favoured());
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (StringUtils.isSame(bean.getGood_favoured(), CommonUtils.isOne)) {
                    bean.setGood_favoured(CommonUtils.isZero);
                } else {
                    bean.setGood_favoured(CommonUtils.isOne);
                }
                collectIv.setImageResource(StringUtils.isSame(bean.getGood_favoured(), CommonUtils.isOne) ? R.mipmap.icon_collect_on : R.mipmap.icon_collect_off);
                EventBus.getDefault().post(new CollectItemsEvent(bean.getId(),bean.getGood_favoured()));//收藏商品
                UIHelper.ToastMessage(MerchantItemsDetailsActivity.this, response.getInfo());
            }
        });
    }
}
