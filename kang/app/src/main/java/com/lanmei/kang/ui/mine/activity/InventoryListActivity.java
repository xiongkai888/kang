package com.lanmei.kang.ui.mine.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.InventoryFiltrateAdapter;
import com.lanmei.kang.adapter.InventoryListAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.InventoryListBean;
import com.lanmei.kang.bean.MerchantTabClassifyBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SimpleTextWatcher;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 库存
 */
public class InventoryListActivity extends BaseActivity implements TextView.OnEditorActionListener{

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText keywordEditText;
    @InjectView(R.id.by_tv)
    TextView byTv;
    @InjectView(R.id.by_rl)
    RelativeLayout byRl;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.ll_by)
    LinearLayout llBy;

    private SwipeRefreshController<NoPageListBean<InventoryListBean>> controller;
    private KangQiMeiApi api;
    private InventoryListAdapter adapter;
    private List<MerchantTabClassifyBean> merchantTabClassifyBeans;


    @Override
    public int getContentViewId() {
        return R.layout.activity_inventory_list;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        keywordEditText.setFocusableInTouchMode(true);
        keywordEditText.setOnEditorActionListener(this);
        keywordEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(String.valueOf(s))){
                    api.add("string", "");
                    controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
                }
            }
        });
        loadType();
        initSwipeRefreshLayout();
    }


    private void loadType(){
        KangQiMeiApi api = new KangQiMeiApi("app/good_type");
        api.add("type",1);//分类值(1总分类|2推荐分类)
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<MerchantTabClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<MerchantTabClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                merchantTabClassifyBeans = response.data;
                if (StringUtils.isEmpty(merchantTabClassifyBeans)){
                    merchantTabClassifyBeans = new ArrayList<>();
                }
                MerchantTabClassifyBean bean = new MerchantTabClassifyBean();
                bean.setClassname(getString(R.string.all));
                bean.setId("");
                bean.setSelect(true);
                merchantTabClassifyBeans.add(0,bean);
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
//            if (StringUtils.isEmpty(key)) {
//                UIHelper.ToastMessage(this, R.string.input_keyword);
//                return false;
//            }
            loadSearchGoods(key);
            return true;
        }
        return false;
    }

    private void loadSearchGoods(String keyword) {
        api.add("string", keyword);
//        api.add("order","");
//        api.add("type","");
//        byTv.setText(R.string.all);
//        CommonUtils.setCompoundDrawables(getContext(), byTv, R.mipmap.common_filter_arrow_down, 0, 2);
//        CommonUtils.setCompoundDrawables(getContext(), timeTv, R.mipmap.o_bothway, 0, 2);
        controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        api = new KangQiMeiApi("app/shoper_sale");
//        api.add("uid", "888");
        api.add("uid", api.getUserId(this));

        adapter = new InventoryListAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<InventoryListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
    }

    private PopupWindow window;

    private void popupWindow() {
        if (StringUtils.isEmpty(merchantTabClassifyBeans)){
            loadType();
            return;
        }
        CommonUtils.setCompoundDrawables(getContext(), byTv, R.mipmap.common_filter_arrow_up, R.color.color0066B3, 2);
        if (window != null) {
            window.showAsDropDown(llBy);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        InventoryFiltrateAdapter voterFiltrateAdapter = new InventoryFiltrateAdapter(this);
        voterFiltrateAdapter.setData(merchantTabClassifyBeans);
        view.setAdapter(voterFiltrateAdapter);
        voterFiltrateAdapter.setInventoryFiltrateListener(new InventoryFiltrateAdapter.InventoryFiltrateListener() {
            @Override
            public void onFiltrate(MerchantTabClassifyBean bean) {
                byTv.setText(bean.getClassname());
                window.dismiss();
                api.add("type",bean.getId());
                controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
            }
        });
//        int width = UIBaseUtils.dp2pxInt(this, 80);
        window = new PopupWindow(view, SysUtils.getScreenWidth(this), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
//        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
//        int xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(llBy);
//        L.d(L.TAG,"width:"+width+",paddingRight:"+paddingRight+",xoff:"+xoff);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setCompoundDrawables(getContext(), byTv, R.mipmap.common_filter_arrow_down, R.color.black, 2);
            }
        });
    }

    private boolean isUp;

    @OnClick({R.id.by_rl, R.id.time_rl,R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.by_rl:
                popupWindow();
                break;
            case R.id.back_iv:
                finish();
                break;
            case R.id.time_rl:
                CommonUtils.setCompoundDrawables(this, timeTv, isUp?R.mipmap.o_bothway_up:R.mipmap.o_bothway_down, 0, 2);
                api.add("order",isUp?"1":"2");
                controller.loadFirstPage(SmartSwipeRefreshLayout.Mode.BOTH);
                isUp = !isUp;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (window != null) {
            if (window.isShowing()) {
                window.dismiss();
            }
            window = null;
        }
    }

}
