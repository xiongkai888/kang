package com.lanmei.kang.ui.merchant_tab.goods.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.core.IPresenter;
import com.lanmei.kang.event.PaySucceedEvent;
import com.lanmei.kang.ui.MainActivity;
import com.lanmei.kang.ui.merchant_tab.activity.ConfirmOrderActivity;
import com.lanmei.kang.util.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.EmptyRecyclerView;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 购物车
 */
public class ShopCarActivity extends BaseActivity implements ShopCartContract.View, Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.empty_view)
    RelativeLayout mEmptyView;
    // 全选
    @InjectView(R.id.all_select_checkbox)
    CheckBox mAllSelectCheckbox;
    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;
    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.total_money_tv)
    FormatTextView mTotalMoneyTV;
    @InjectView(R.id.balance_bt)
    Button balanceBt;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;
    ShopCarAdapter adapter;
    Button goBt;//去逛逛
    ShopCartContract.Presenter mPresenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        toolbar.setTitle(R.string.shop);
        toolbar.setNavigationIcon(R.mipmap.back_g);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPresenter = new ShopCarPresenter(this, this);

        mEmptyView.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.empty_shop_car, mEmptyView, false);
        mEmptyView.addView(view);
        goBt = (Button)view.findViewById(R.id.go_bt);
        goBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showHome(ShopCarActivity.this);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShopCarAdapter(this, mPresenter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(adapter);

        mAllSelectCheckbox.setOnCheckedChangeListener(mAllSelectedChangeListener);

        mPresenter.start();
    }

    @Override
    public void setPresenter(IPresenter presenter) {

    }

    //支付成功事件
    @Subscribe
    public void paySucceedEvent(PaySucceedEvent event) {
        deleteGoods(delecteList);
    }

    @Override
    public void initShopCart(List<ShopCarBean> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_edit);

        //toolbar的menu点击事件的监听
        toolbar.setOnMenuItemClickListener(this);
        L.d("ShopCarActivity", "initShopCart");
        style = 1;
        balanceBt.setText(R.string.submit);
    }

    @Override
    public void showEmpty() {
        toolbar.getMenu().clear();
        llBottom.setVisibility(View.GONE);
        L.d("ShopCarActivity", "showEmpty");//
    }

    @Override
    public void refreshShopCart() {
        if (adapter != null) {
            L.d("ShopCarActivity", "refreshShopCart");//
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void summation(double sum, boolean selectedAll) {
        mTotalMoneyTV.setTextValue(String.format("%.1f", sum));

        mAllSelectCheckbox.setOnCheckedChangeListener(null);
        mAllSelectCheckbox.setChecked(selectedAll);
        mAllSelectCheckbox.setOnCheckedChangeListener(mAllSelectedChangeListener);

        L.d("ShopCarActivity", "summation");//
        refreshShopCart();
    }

    int style = 1;//1去付款，2删除

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_done);
            balanceBt.setText(R.string.delete);
            style = 2;
        } else if (id == R.id.action_done) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_edit);
            balanceBt.setText(R.string.submit);
            style = 1;
        }
        return true;
    }
    List<ShopCarBean> delecteList;//支付成功后要删除的数据
    @OnClick(R.id.balance_bt)
    public void onClick() {
        delecteList = mPresenter.getSeletctedCarList();
        if (style == 1) {//1去付款，2删除
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable)delecteList);
            IntentUtil.startActivity(getContext(), ConfirmOrderActivity.class, bundle);
        } else {
            if (StringUtils.isEmpty(delecteList)) {
                UIHelper.ToastMessage(this, "请选择要删除的商品");
                return;
            }
            AKDialog.getAlertDialog(this, "确认要删除选择的商品？", new AKDialog.AlertDialogListener() {
                @Override
                public void yes() {
                    deleteGoods(delecteList);
                }
            });
        }
    }

    private void deleteGoods(List<ShopCarBean> list) {
        mPresenter.deleteSelectShopCar(list);
        EventBus.getDefault().post(new ShowShopCountEvent());//商品详情显示购物车数量
    }

    private CompoundButton.OnCheckedChangeListener mAllSelectedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            mPresenter.selectAll(b);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
