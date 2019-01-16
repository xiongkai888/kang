package com.lanmei.kang.ui.merchant_tab.goods.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.GoodsCommentAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.GoodsCommentBean;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.lanmei.kang.event.OnlyCommentEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;


/**
 * 评价
 */
public class GoodsCommentFragment extends BaseFragment {

    GoodsDetailsBean bean;//商品信息bean
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<GoodsCommentBean>> controller;
    private boolean aBoolean;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
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
        KangQiMeiApi api = new KangQiMeiApi("app/comment");
//        api.add("userid",api.getUserId(context));
        api.add("goodsid",bean.getId());
        GoodsCommentAdapter adapter = new GoodsCommentAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<GoodsCommentBean>>(context, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(NoPageListBean<GoodsCommentBean> response) {
                if (!aBoolean){
                    aBoolean = !aBoolean;
                    EventBus.getDefault().post(new OnlyCommentEvent(response.data));
                }
                return super.onSuccessResponse(response);
            }
        };
//        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();

//        adapter.notifyDataSetChanged();
    }

}
