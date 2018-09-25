package com.lanmei.kang.ui.news.fragment;

import android.os.Bundle;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.NewsItemAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.event.NewsCommEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/25.
 * 资讯列表
 */

public class NewsListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NewsItemAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NewsCategoryListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


//    NewsCategoryTabBean bean;

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
//        Bundle bundle = getArguments();
//        bean = (NewsCategoryTabBean) bundle.getSerializable("bean");
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));

        Bundle bundle = getArguments();
        KangQiMeiApi api = new KangQiMeiApi("post/index");
        api.addParams("cid",bundle.getString("cid"));
        mAdapter = new NewsItemAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCategoryListBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }



    //评论资讯详情时调用
    @Subscribe
    public void commEvent(NewsCommEvent event) {
        if (mAdapter != null) {
            List<NewsCategoryListBean> list = mAdapter.getData();
            if (StringUtils.isEmpty(list)) {
                return;
            }
            String id = event.getId();
            String commNum = event.getCommNum();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NewsCategoryListBean bean = list.get(i);
                if (StringUtils.isSame(id, bean.getId())) {
                    bean.setReviews(commNum);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
