package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.InterestedAdapter;
import com.lanmei.kang.api.GoodFriendApi;
import com.lanmei.kang.bean.InterestedBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 可能感兴趣的好友
 */
public class InterestedFriendActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    InterestedAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_interested_friend;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.interested_people);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);
        mAdapter = new InterestedAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRefreshListListener(new InterestedAdapter.RefreshListListener() {
            @Override
            public void refresh() {
                initSwipeRefreshLayout();
            }
        });
        initSwipeRefreshLayout();
    }

    List<InterestedBean> mList = new ArrayList<>();

    private void initSwipeRefreshLayout() {
        HttpClient httpClient = HttpClient.newInstance(this);
        GoodFriendApi api = new GoodFriendApi();
        api.type = "2";//1、新朋友 2、感兴趣的
        api.uid = api.getUserId(this);
        api.token = api.getToken(this);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<InterestedBean>>() {
            @Override
            public void onResponse(NoPageListBean<InterestedBean> response) {
                if (isFinishing()){
                    return;
                }
                mList = response.getDataList();
                mAdapter.setData(mList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exchange:
                initSwipeRefreshLayout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
