package com.lanmei.kang.ui.merchant_tab.goods.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * 评价
 */
public class GoodsCommentFragment extends BaseFragment {

    GoodsDetailsBean bean;//商品信息bean
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
//    GoodsCommentAdapter mAdapter;
//    private SwipeRefreshController<NoPageListBean<GoodsCommentBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (!StringUtils.isEmpty(bundle)) {
            bean = (GoodsDetailsBean) bundle.getSerializable("bean");
        }
        if (bean == null){
            return;
        }
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));

//        GoodsCommentsApi api = new GoodsCommentsApi();
//        api.proid = bean.getId();
//        mAdapter = new GoodsCommentAdapter(context);
//        smartSwipeRefreshLayout.setAdapter(mAdapter);
//        controller = new SwipeRefreshController<NoPageListBean<GoodsCommentBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
//        };
//        controller.setIsFirstPageListener(new SwipeRefreshController.IsFirstPageListener() {
//            @Override
//            public void isFirst() {
//                if (mAdapter != null) {
//                    EventBus.getDefault().post(new OnlyCommentEvent(mAdapter.getData()));
//                }
//            }
//        });
//        controller.loadFirstPage();
    }

}
