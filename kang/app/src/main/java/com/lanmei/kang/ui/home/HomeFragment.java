package com.lanmei.kang.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.HomeAdapter;
import com.lanmei.kang.adapter.HomeCategoryAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.HomeBean;
import com.lanmei.kang.event.LocationEvent;
import com.lanmei.kang.ui.home.activity.RecomMerchantListActivity;
import com.lanmei.kang.ui.mine.activity.SearchUserActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.HomeListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/24.
 * 场地
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    @InjectView(R.id.city_tv)
    TextView mCityTv;//定位城市
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;//首页分类列表
    @InjectView(R.id.recyclerView1)
    RecyclerView recyclerView1;//商家列表
    @InjectView(R.id.banner)
    ConvenientBanner banner;//轮播图
    HomeAdapter mAdapter;
    private boolean isFirst = true;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initListView();
        mCityTv.setText(SharedAccount.getInstance(context).getCity());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        loadHome();
    }
    KangQiMeiApi api;

    private void initListView() {
        api = new KangQiMeiApi("index/home");
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color3DCE45,R.color.color00D3C4);
        mAdapter = new HomeAdapter(context);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView1.setNestedScrollingEnabled(false);
        //添加Android自带的分割线
        recyclerView1.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView1.setAdapter(mAdapter);
    }



    @OnClick({R.id.ll_search, R.id.message_iv,R.id.ll_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search://搜索
                IntentUtil.startActivity(context, SearchUserActivity.class);
                break;
            case R.id.message_iv://客服
                if (!CommonUtils.isLogin(context)){
                    return;
                }
                IntentUtil.startActivity(context, com.hyphenate.chatuidemo.ui.MainActivity.class);
                break;
            case R.id.ll_more://更多
                IntentUtil.startActivity(context, RecomMerchantListActivity.class);
                break;
        }
    }

    /**
     * MainActivity 定位成功调用
     * @param event
     */
    @Subscribe
    public void locationEvent(LocationEvent event){
        mCityTv.setText(event.getCity());
        api.addParams("lon",event.getLongitude());
        api.addParams("lat",event.getLatitude());
        loadHome();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        loadHome();
    }


    private void loadHome() {
        api.addParams("limit", 6);
        api.addParams("lon",  SharedAccount.getInstance(context).getLon());
        api.addParams("lat", SharedAccount.getInstance(context).getLat());
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<HomeListBean<HomeBean>>() {
            @Override
            public void onResponse(HomeListBean<HomeBean> response) {
                if (mCityTv == null) {
                    return;
                }
                setHome(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    UIHelper.ToastMessage(context, error.getMessage());
                }

            }
        });
    }

    private void setHome(HomeListBean<HomeBean> response) {
        if (StringUtils.isEmpty(response)){
            return;
        }
        mAdapter.setData(response.getDataList());
        mAdapter.notifyDataSetChanged();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (!StringUtils.isEmpty(response.getCategoryList())) {//首页分类列表
            HomeCategoryAdapter adAdapter = new HomeCategoryAdapter(context);
            adAdapter.setData(response.getCategoryList());
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerView.setAdapter(adAdapter);
        }

        if (!isFirst){
            return;
        }
        if (!StringUtils.isEmpty(response.getBannerList())) {
            List<HomeListBean.BannerBean> bannerList = response.getBannerList();
            List<String> list = new ArrayList<>();
            for (HomeListBean.BannerBean bean : bannerList) {
                list.add(bean.getPic());
            }
            CommonUtils.setBanner(banner, list, isFirst);//防止不断刷新，banner越滑越快
            if (isFirst) {
                isFirst = !isFirst;
            }
        }

    }

}
