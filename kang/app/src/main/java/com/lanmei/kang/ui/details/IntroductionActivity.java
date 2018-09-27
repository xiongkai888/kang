package com.lanmei.kang.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 场地介绍、费用介绍
 */
public class IntroductionActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.introduction_tv)
    TextView mIntroductionTv;//场地介绍或费用介绍

    @Override
    public int getContentViewId() {
        return R.layout.activity_introduction;
    }

    public static void startActivityIntroduction(Context context,String titleStr,String content){
        Intent intent = new Intent(context,IntroductionActivity.class);
        intent.putExtra("title",titleStr);
        intent.putExtra("content",content);
        context.startActivity(intent);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        actionbar.setTitle(title);
        String content = intent.getStringExtra("content");
        mIntroductionTv.setText(content);
    }

}
