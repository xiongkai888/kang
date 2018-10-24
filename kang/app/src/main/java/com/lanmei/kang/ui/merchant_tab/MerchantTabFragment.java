package com.lanmei.kang.ui.merchant_tab;

import android.os.Bundle;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AdBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.search.SearchGoodsActivity;
import com.lanmei.kang.ui.merchant_tab.activity.GoodsClassifyActivity;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/24.
 * 商家 - 的商家界面
 */

public class MerchantTabFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private MerchantTabAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_merchant_tab;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        smartSwipeRefreshLayout.initGridLinearLayout(2, 0);
        KangQiMeiApi api = new KangQiMeiApi("app/good_list");//热销产品
        api.add("hot",1);
        adapter = new MerchantTabAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>> controller = new SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
        adapter.notifyDataSetChanged();

        loadGoodsClassify();
        loadAd(1);
        loadAd(2);
    }


    //用户端-商家tab  轮播图
    private void loadAd(final int type) {
        KangQiMeiApi api = new KangQiMeiApi("app/index_img");
        api.add("type",type);//1是头部轮播，2是推荐图(即热门活动)
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<AdBean>>() {
            @Override
            public void onResponse(NoPageListBean<AdBean> response) {
                if (smartSwipeRefreshLayout == null) {
                    return;
                }
                if (StringUtils.isEmpty(response.data)) {
                    return;
                }
                if (type == 1){
                    adapter.setBannerParameter(response.data);
                }else {
                    adapter.setHotImgParameter(response.data);
                }
            }
        });
    }

    //用户端-商家tab  产品分类
    private void loadGoodsClassify() {
        KangQiMeiApi api = new KangQiMeiApi("app/good_type");
        api.add("type",2);//分类值(1总分类|2推荐分类)
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabClassifyBean> response) {
                if (adapter == null) {
                    return;
                }
                adapter.setClassifyParameter(response.data);
            }
        });
    }

    @OnClick({R.id.classify_iv, R.id.search_tv, R.id.message_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.classify_iv://分类
                IntentUtil.startActivity(context, GoodsClassifyActivity.class);
                break;
            case R.id.search_tv://搜索商品
                IntentUtil.startActivity(context, SearchGoodsActivity.class);
                break;
            case R.id.message_iv://消息
                CommonUtils.developing(context);
                break;
        }
    }
}
