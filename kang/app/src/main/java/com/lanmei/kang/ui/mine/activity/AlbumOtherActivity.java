package com.lanmei.kang.ui.mine.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.AlbumOtherAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AlbumBean;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.List;

import butterknife.InjectView;

/**
 * 好友相册（或商家相册）
 */
public class AlbumOtherActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    AlbumOtherAdapter mAdapter;

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_album_other;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("商家相册");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        int padding = UIBaseUtils.dp2pxInt(this, 5);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(padding));
        mAdapter = new AlbumOtherAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        String uid = getIntent().getStringExtra("value");
//        if ("merchant_album".equals(uid)){//商家的相册
//            UserBean bean = UserHelper.getInstance(this).getUserBean();
//            if (bean == null || StringUtils.isEmpty(bean.getPics())){
//                return;
//            }
//            List<AlbumBean> list = CommonUtils.getAlbumList(bean.getPics());
//            setAlbum(list);
//        }else {//好友的相册
            initSwipeRefreshLayout(uid);
//        }
    }

    private void setAlbum(List<AlbumBean> list) {  if (list != null){
        String[] arry = CommonUtils.getStringArry(list);
        mAdapter.setStringArry(arry);
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    }


    private void initSwipeRefreshLayout(String uid) {
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("talent/album");
        api.addParams("uid",api.getUserId(this));
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<AlbumBean>>() {
            @Override
            public void onResponse(NoPageListBean<AlbumBean> response) {
                if (AlbumOtherActivity.this.isFinishing()){
                    return;
                }
                List<AlbumBean> list = response.getDataList();
                setAlbum(list);
            }
        });
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.top = space/2;
                outRect.bottom = space/2;
                outRect.left = space;
        }
    }

}
