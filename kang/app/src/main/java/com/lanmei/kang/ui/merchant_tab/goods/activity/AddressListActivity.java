package com.lanmei.kang.ui.merchant_tab.goods.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.AddressListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AddressListBean;
import com.lanmei.kang.event.AddAddressEvent;
import com.lanmei.kang.event.AlterAddressEvent;
import com.lanmei.kang.event.ChooseAddressEvent;
import com.lanmei.kang.util.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;


/**
 * 地址列表（选择收货地址）
 */
public class AddressListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    AddressListAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("选择收货地址");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        EventBus.getDefault().register(this);

        loadAddressList();
        smartSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this));
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        mAdapter = new AddressListAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setChooseAddressListener(new AddressListAdapter.ChooseAddressListener() {
            @Override
            public void choose(AddressListBean bean) {
                EventBus.getDefault().post(new ChooseAddressEvent(bean));
                finish();
            }

            @Override
            public void setDefault(String id, int position) {
                setAddressDefault(id, position);
            }

            @Override
            public void delete(final String id,final int position) {
                AKDialog.getAlertDialog(getContext(), "确定要删除该地址？", new AKDialog.AlertDialogListener() {
                    @Override
                    public void yes() {
                        KangQiMeiApi api = new KangQiMeiApi("");
                        api.addParams("userid",api.getUserId(getContext()));
                        api.addParams("operation",3);
                        api.addParams("id",id);
                        HttpClient.newInstance(getContext()).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                            @Override
                            public void onResponse(BaseBean response) {
                                mAdapter.getData().remove(position);
                                mAdapter.notifyItemRemoved(position);
                                mAdapter.notifyDataSetChanged();
                                EventBus.getDefault().post(new AlterAddressEvent());//
                            }
                        });
                    }
                });
            }
        });
    }

    //设为默认
    private void setAddressDefault(String id, final int position) {
        KangQiMeiApi api = new KangQiMeiApi("");
        api.addParams("userid",api.getUserId(this));
        api.addParams("operation",2);
        api.addParams("id",id);
        api.addParams("mDefault",1);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                List<AddressListBean> list = mAdapter.getData();
                int size = list.size();
                for (int i=0;i<size;i++){
                    list.get(i).setDefaultX("0");
                }
                list.get(position).setDefaultX("1");
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadAddressList() {
        KangQiMeiApi api = new KangQiMeiApi("");
        api.addParams("userid",api.getUserId(this));
        api.addParams("operation",4);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<AddressListBean>>() {
            @Override
            public void onResponse(NoPageListBean<AddressListBean> response) {
                List<AddressListBean> list = response.data;
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //添加地址的时候
    @Subscribe
    public void addAddressEvent(AddAddressEvent event) {
        loadAddressList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add://添加
                IntentUtil.startActivity(this, AddAddressActivity.class);
                break;
            //            case R.id.action_confirm://确定
            //                UIHelper.ToastMessage(this,R.string.developing);
            //                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
