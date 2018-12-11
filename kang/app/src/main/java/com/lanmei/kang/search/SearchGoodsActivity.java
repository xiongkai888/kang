package com.lanmei.kang.search;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.MerchantTabGoodsBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SimpleTextWatcher;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.List;

import butterknife.InjectView;

/**
 * 搜索商品
 */
public class SearchGoodsActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    GoodsListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>> controller;
    private KangQiMeiApi api;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;
    private boolean isFirst = true;
    private List<MerchantTabGoodsBean> goodsBeanList;//第一次加载的数据

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_search_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mKeywordEditText.setFocusableInTouchMode(true);
        mKeywordEditText.setOnEditorActionListener(this);
        mKeywordEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(String.valueOf(s))){
                    api.add("goodsname", "");
                    mAdapter.setData(goodsBeanList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.search_goods);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        api =new KangQiMeiApi("app/good_list");

        mAdapter = new GoodsListAdapter(this);

        smartSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(this, 2));
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<MerchantTabGoodsBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(NoPageListBean<MerchantTabGoodsBean> response) {
                if (isFirst){
                    isFirst = false;
                    goodsBeanList = response.data;
                }
                return super.onSuccessResponse(response);
            }
        };
        controller.loadFirstPage();
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_keyword);
                return false;
            }
            loadSearchGoods(key);
            return true;
        }
        return false;
    }

    private void loadSearchGoods(String keyword) {
        api.add("goodsname", keyword);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.BOTH);
        controller.loadFirstPage();
    }

}
