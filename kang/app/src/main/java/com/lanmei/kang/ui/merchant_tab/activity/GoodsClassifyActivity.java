package com.lanmei.kang.ui.merchant_tab.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsClassifyAdapter;
import com.lanmei.kang.adapter.GoodsClassifyVerticalTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

public class GoodsClassifyActivity extends BaseActivity {

    @InjectView(R.id.tablayout)
    VerticalTabLayout tablayout;//垂直tabLayout
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    GoodsClassifyAdapter adapter;
    private List<MerchantTabClassifyBean> classifyList;//分类列表
    private KangQiMeiApi api;
    private HashMap<Integer,List<MerchantTabGoodsBean>>  hashMap;


    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_classify;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        hashMap = new HashMap<>();

        smartSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(this, 2));
        api = new KangQiMeiApi("app/good_list");
        adapter = new GoodsClassifyAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        loadGoodsClassify();
    }

    private void initTabLayout() {
        if (StringUtils.isEmpty(classifyList)) {
            return;
        }
        //垂直VerticalTab
        tablayout.setTabAdapter(new GoodsClassifyVerticalTabAdapter(this, classifyList));
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                refresh(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
        refresh(0);
    }

    private void refresh(final int position) {
        if (hashMap.containsKey(position)){
            adapter.setData(hashMap.get(position));
            adapter.notifyDataSetChanged();
            return;
        }
        api.addParams("classid", classifyList.get(position).getId());
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabGoodsBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabGoodsBean> response) {
                if (adapter == null){
                    return;
                }
                hashMap.put(position,response.data);
                adapter.setData(response.data);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //用户端-商家tab  产品分类
    private void loadGoodsClassify() {
        KangQiMeiApi api = new KangQiMeiApi("app/good_type");
        api.addParams("type",1);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                classifyList = response.data;
                initTabLayout();
            }
        });
    }

    @OnClick({R.id.back_iv, R.id.search_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.search_tv:
                CommonUtils.developing(this);
                break;
        }
    }
}
