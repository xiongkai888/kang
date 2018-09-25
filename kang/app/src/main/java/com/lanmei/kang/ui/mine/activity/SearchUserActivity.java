package com.lanmei.kang.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.PhysiotherapyTabAdapter;
import com.lanmei.kang.adapter.SearchUserAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.api.SearchUserApi;
import com.lanmei.kang.bean.MerchantListBean;
import com.lanmei.kang.bean.SearchUserBean;
import com.lanmei.kang.util.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.EmptyRecyclerView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/***
 * 搜索用户（或搜索商家）
 */
public class SearchUserActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;
    @InjectView(R.id.empty_view)
    View mEmptyView;
    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;

    SearchUserAdapter mUserAdapter;
    PhysiotherapyTabAdapter adapter;


    String searchType;
    @Override
    public int getContentViewId() {
        return R.layout.activity_search_user;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mKeywordEditText.setOnEditorActionListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new SearchUserAdapter(this);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mUserAdapter);


        searchType = getIntent().getStringExtra("value");
        if (StringUtils.isSame("user",searchType)) {//用户搜索
            mUserAdapter = new SearchUserAdapter(this);
            mRecyclerView.setAdapter(mUserAdapter);
            mKeywordEditText.setHint(R.string.search_friends);
        } else {//商家搜索
            adapter = new PhysiotherapyTabAdapter(this);
            mRecyclerView.setAdapter(adapter);
            mKeywordEditText.setHint(R.string.search_merchant);
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = v.getText().toString().trim();
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(SearchUserActivity.this, R.string.input_keyword);
                return true;
            }
            if ("user".equals(searchType)) {//用户搜索
                ajaxSearchUser(key);
            } else {//商家搜索
                ajaxMerchant(key);
            }
            return false;
        }
        return false;
    }

    //搜索商家
    private void ajaxMerchant(String key) {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("place/Placelist");
        api.addParams("lat",SharedAccount.getInstance(this).getLat());
        api.addParams("lon",SharedAccount.getInstance(this).getLon());
        api.addParams("keyword",key);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantListBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantListBean> response) {
                List<MerchantListBean> list = response.getDataList();
                adapter.setData(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //搜索用户
    private void ajaxSearchUser(String key) {

        SearchUserApi api = new SearchUserApi();
        api.keyword = key;
        api.token = api.getToken(this);
        api.uid = api.getUserId(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<SearchUserBean>>() {
            @Override
            public void onResponse(NoPageListBean<SearchUserBean> response) {
                if (isFinishing()){
                    return;
                }
                List<SearchUserBean> list = response.getDataList();
                mUserAdapter.setData(list);
                mUserAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.back_iv)
    public void showBack(){//返回
        onBackPressed();
    }

}
