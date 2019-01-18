package com.lanmei.kang.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.BecomeDistributorBean;
import com.lanmei.kang.util.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.List;

import butterknife.InjectView;


/**
 * 成为经销商(或在线客服或者其他文章、关于我们)
 */

public class BecomeDistributorActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.webView_tv)
    WebView webView;


    @Override
    public int getContentViewId() {
        return R.layout.activity_become_distributor;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        String title = getIntent().getStringExtra("value");
        actionbar.setTitle(title);


        KangQiMeiApi api = new KangQiMeiApi("app/news");
        api.add("title",title);//文章标题
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<BecomeDistributorBean>>() {
            @Override
            public void onResponse(NoPageListBean<BecomeDistributorBean> response) {
                if (isFinishing()) {
                    return;
                }
                List<BecomeDistributorBean> list = response.data;
                if (!com.xson.common.utils.StringUtils.isEmpty(list)){
                    BecomeDistributorBean bean = list.get(0);
                    WebViewPhotoBrowserUtil.photoBrowser(getContext(), webView, bean.getContent());
                }
            }
        });
    }

}
