package com.lanmei.kang.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewsTabAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewsTabBean;
import com.lanmei.kang.search.SearchNewsActivity;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/25.
 * 资讯
 */

public class NewsFragment extends BaseFragment {

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        KangQiMeiApi api = new KangQiMeiApi("post/category");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<NewsTabBean>>() {
            @Override
            public void onResponse(NoPageListBean<NewsTabBean> response) {
                if (mTabLayout == null){
                    return;
                }
                initTabLayout(response.data);
            }
        });
    }

    private void initTabLayout(List<NewsTabBean> list){
        if (StringUtils.isEmpty(list)){
            return;
        }
        NewsTabAdapter tabAdapter = new NewsTabAdapter(getChildFragmentManager());
        tabAdapter.setList(list);
        mViewPager.setAdapter(tabAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @OnClick(R.id.ll_search)
    public void onViewClicked() {//搜索
        IntentUtil.startActivity(context, SearchNewsActivity.class);
    }
}
