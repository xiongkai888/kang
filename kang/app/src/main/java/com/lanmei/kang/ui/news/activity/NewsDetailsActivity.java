package com.lanmei.kang.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewsDetailsCommentAdapter;
import com.lanmei.kang.api.NewsCommentApi;
import com.lanmei.kang.api.NewsCommentListApi;
import com.lanmei.kang.api.NewsDetailsApi;
import com.lanmei.kang.bean.NewsCommentBean;
import com.lanmei.kang.bean.NewsDetailsBean;
import com.lanmei.kang.event.NewsCommEvent;
import com.lanmei.kang.helper.ShareHelper;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 资讯详情
 */
public class NewsDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.compile_comment_et)
    EditText mCompileCommentEt;//写评论

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NewsDetailsCommentAdapter mAdapter;
    String id;//资讯id
    private ShareHelper mShareHelper;


    private SwipeRefreshController<NoPageListBean<NewsCommentBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_news_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        id = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.news_details);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));


        NewsCommentListApi api = new NewsCommentListApi();
        api.id = getIntent().getStringExtra("value");
        api.uid = api.getUserId(this);
        api.token = api.getToken(this);
        mAdapter = new NewsDetailsCommentAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCommentBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        loadBiDetails();//加载资讯详情
        //分享初始化
        mShareHelper = new ShareHelper(this);
    }


    private void loadBiDetails() {
        HttpClient httpClient = HttpClient.newInstance(this);
        NewsDetailsApi api = new NewsDetailsApi();
        api.id = id;
        api.uid = api.getUserId(this);
        api.token = api.getToken(this);
        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<NewsDetailsBean>>() {
            @Override
            public void onResponse(DataBean<NewsDetailsBean> response) {
                if (isFinishing()) {
                    return;
                }
                NewsDetailsBean bean = response.data;
                EventBus.getDefault().post(new NewsCommEvent(bean.getId(),bean.getReviews()));//设置资讯列表的评论数
                mAdapter.setNewsDetailsBean(bean);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                mShareHelper.share();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.send_info_tv})
    public void showInfo(View view) {//发送消息
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        switch (view.getId()) {
            case R.id.send_info_tv:
                ajaxSend(mCompileCommentEt.getText().toString());
                break;
        }

    }


    /**
     * @param content 评论内容
     */
    private void ajaxSend(String content) {
        if (StringUtils.isEmpty(content)){
            UIHelper.ToastMessage(this,getString(R.string.input_comment));
            return;
        }
        HttpClient httpClient = HttpClient.newInstance(this);
        NewsCommentApi api = new NewsCommentApi();
        api.content = content;
        api.id = id;
        api.uid = api.getUserId(this);
//        api.uid = api.getUrl();
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(NewsDetailsActivity.this, "评论成功");
                if (controller != null){
                    controller.loadFirstPage();
                    loadBiDetails();//加载资讯详情
                }
                mCompileCommentEt.setText("");
            }
        });
    }

    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareHelper.onDestroy();
    }
}
