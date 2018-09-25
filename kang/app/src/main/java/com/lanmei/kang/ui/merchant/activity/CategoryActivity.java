package com.lanmei.kang.ui.merchant.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.CategoryAdapter;
import com.lanmei.kang.api.AddCategoryApi;
import com.lanmei.kang.api.ItemsCategoryApi;
import com.lanmei.kang.bean.CategoryBean;
import com.lanmei.kang.event.AddCategoryEvent;
import com.lanmei.kang.event.CompileProductEvent;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.EmptyRecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 分类
 */
public class CategoryActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.empty_view)
    View mEmptyView;
    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;

    CategoryAdapter mAdapter;

    String pid;

    @Override
    public int getContentViewId() {
        return R.layout.activity_category;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("分类");
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        pid = getIntent().getStringExtra("value");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CategoryAdapter(this);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setCompileCategoryListener(new CategoryAdapter.CompileCategoryListener() {
            @Override
            public void compileCategory(final CategoryBean bean) {
                dialog = AKDialog.getCompileCategoryDialog(CategoryActivity.this, getString(R.string.compile_category), bean.getName() ,new AKDialog.ConfirmListener() {
                    @Override
                    public void yes(String code) {
                        addCategory(code,bean.getId(),2);
                    }
                });
                dialog.show();
            }
        });

        loadClass();
    }

    boolean isFirst = true;

    private void loadClass() {
        ItemsCategoryApi api = new ItemsCategoryApi();
        api.mid = pid;
        api.token = api.getToken(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<CategoryBean>>() {
            @Override
            public void onResponse(NoPageListBean<CategoryBean> response) {
                mAdapter.setData(response.data);
                mAdapter.notifyDataSetChanged();
                if (isFirst){
                    isFirst = !isFirst;
                }else {
                    EventBus.getDefault().post(new AddCategoryEvent(response.data));
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    AlertDialog dialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                dialog = AKDialog.getCompileCategoryDialog(this, getString(R.string.add_category), "",new AKDialog.ConfirmListener() {
                    @Override
                    public void yes(String code) {
                        addCategory(code,"",1);
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCategory(String code,String id,final int type) {
        if (StringUtils.isEmpty(code)){
            UIHelper.ToastMessage(this,getString(R.string.input_category_name));
            return;
        }
        dialog.cancel();
        AddCategoryApi api = new AddCategoryApi();
        api.mid = pid;
        api.name = code;
        if (type != 1){//编辑分类
            api.id = id;
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                loadClass();
                if (type != 1){
                    UIHelper.ToastMessage(CategoryActivity.this,response.getInfo());
                }else {
                    UIHelper.ToastMessage(CategoryActivity.this,"添加成功");
                }
                EventBus.getDefault().post(new CompileProductEvent());//刷新服务项目列表
            }
        });
    }

}
