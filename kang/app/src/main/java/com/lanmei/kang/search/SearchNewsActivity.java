package com.lanmei.kang.search;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewsItemAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.event.NewsCommEvent;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

/**
 * 搜索资讯
 */
public class SearchNewsActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NewsItemAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NewsCategoryListBean>> controller;
    private KangQiMeiApi api;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_search_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mKeywordEditText.setFocusableInTouchMode(true);
        mKeywordEditText.setOnEditorActionListener(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.search_news);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        api =new KangQiMeiApi("post/index");

        mAdapter = new NewsItemAdapter(this);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCategoryListBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            loadSearchNews(key);
            return true;
        }
        return false;
    }

    private void loadSearchNews(String keyword) {
        api.add("keyword", keyword);
        controller.loadFirstPage();
    }

    //评论资讯详情时调用
    @Subscribe
    public void commEvent(NewsCommEvent event) {
        if (mAdapter != null) {
            List<NewsCategoryListBean> list = mAdapter.getData();
            if (StringUtils.isEmpty(list)) {
                return;
            }
            String id = event.getId();
            String commNum = event.getCommNum();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NewsCategoryListBean bean = list.get(i);
                if (StringUtils.isSame(id, bean.getId())) {
                    bean.setReviews(commNum);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
