package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.helper.AddGoodsSellHelper;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import butterknife.InjectView;

/**
 * 商品销售
 */
public class GoodsSellActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.ll_goods_sell)
    LinearLayout root;
    @InjectView(R.id.total_price_tv)
    FormatTextView totalPriceTv;
    private AddGoodsSellHelper helper;

    @Override
    public int getContentViewId() {
        return R.layout.activity_goods_sell;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.goods_sell);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        helper = new AddGoodsSellHelper(this,root,totalPriceTv);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                helper.addItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
