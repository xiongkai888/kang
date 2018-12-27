package com.lanmei.kang.ui.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 注册协议
 */
public class ProtocolActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_tv)
    TextView contentTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_protocol;
    }



    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.register_protocol);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        KangQiMeiApi api = new KangQiMeiApi("app/grvp");
        api.add("type",1);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()){
                    return;
                }
                contentTv.setText(response.data);
            }
        });
    }

}
