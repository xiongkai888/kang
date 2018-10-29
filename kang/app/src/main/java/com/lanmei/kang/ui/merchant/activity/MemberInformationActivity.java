package com.lanmei.kang.ui.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MemberInformationAdapter;
import com.lanmei.kang.bean.TeamBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;

/**
 * 会员信息
 */
public class MemberInformationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pic_iv)
    CircleImageView picIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private TeamBean bean;


    @Override
    public int getContentViewId() {
        return R.layout.activity_member_information;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (TeamBean) bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("会员信息");
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_w);

        if (bean == null){
            return;
        }
        ImageHelper.load(this,bean.getPic(),picIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        nameTv.setText(bean.getNickname());

        MemberInformationAdapter adapter = new MemberInformationAdapter(this,bean);
        adapter.setData(getInformation());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private List<String> getInformation(){
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "加入时间："
                , "推广人数："
                , "联系电话："
                , "联系地址："
        );
        return list;
    }

}
