package com.lanmei.kang.ui.merchant_tab;

import android.os.Bundle;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AdBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
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

import java.io.Serializable;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/24.
 * 商家 - 的商家界面
 */

public class MerchantTabFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    MerchantTabAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>> controller;
    private boolean isFirst = true;
    private List<MerchantTabClassifyBean> classifyList;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_merchant_tab;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        smartSwipeRefreshLayout.initGridLinearLayout(2, 0);
        KangQiMeiApi api = new KangQiMeiApi("app/good_list");//热销产品
        mAdapter = new MerchantTabAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(NoPageListBean<MerchantTabGoodsBean> response) {
                if (isFirst) {
                    loadGoodsClassify();
                    loadAd();
                    loadHotImg();
                    isFirst = !isFirst;
                }else {
                    loadAd();
                }
                return super.onSuccessResponse(response);
            }
        };
        controller.loadFirstPage();
    }

    //用户端-商家tab  轮播图
    private void loadAd() {
        KangQiMeiApi api = new KangQiMeiApi("app/index_img");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<AdBean>>() {
            @Override
            public void onResponse(NoPageListBean<AdBean> response) {
                if (mAdapter == null) {
                    return;
                }
                if (StringUtils.isEmpty(response.data)) {
                    return;
                }
                mAdapter.setBannerParameter(response.data);
            }
        });
    }

    //用户端-商家tab  产品分类
    private void loadGoodsClassify() {
        KangQiMeiApi api = new KangQiMeiApi("app/good_type");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabClassifyBean> response) {
                if (mAdapter == null) {
                    return;
                }
                classifyList = response.data;
                if (StringUtils.isEmpty(classifyList)) {
                    return;
                }
                mAdapter.setClassifyParameter(classifyList);
            }
        });
    }

    //用户端-商家tab  首页热门推荐图
    private void loadHotImg() {
        KangQiMeiApi api = new KangQiMeiApi("app/hot_img");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<AdBean>>() {
            @Override
            public void onResponse(NoPageListBean<AdBean> response) {
                if (mAdapter == null) {
                    return;
                }
                if (StringUtils.isEmpty(response.data)) {
                    return;
                }
                mAdapter.setHotImgParameter(response.data);
            }
        });
    }

    @OnClick({R.id.classify_iv, R.id.search_tv, R.id.message_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.classify_iv://分类
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) classifyList);
                IntentUtil.startActivity(context, GoodsClassifyActivity.class, bundle);
                break;
            case R.id.search_tv://搜索
                CommonUtils.developing(context);
                break;
            case R.id.message_iv://消息
                CommonUtils.developing(context);
                break;
        }
    }
}
