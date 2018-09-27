package com.lanmei.kang.ui.dynamic.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.DynamicDetailsCommAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.DynamicBean;
import com.lanmei.kang.bean.DynamicCommBean;
import com.lanmei.kang.event.DynamicLikedEvent;
import com.lanmei.kang.event.PublishDynamicEvent;
import com.lanmei.kang.helper.ShareHelper;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.view.DetailsMoreView;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 动态  动态详情
 */
public class DynamicDetailsActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.compile_comment_et)
    EditText mCompileCommentEt;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    DynamicDetailsCommAdapter mAdapter;
    SwipeRefreshController<NoPageListBean<DynamicCommBean>> controller;
    String uid;//用户id
    String id;//动态（帖子）id
    DynamicBean mbean;
    boolean isSelf;//是不是自己的动态
    private ShareHelper mShareHelper;
    int who;

    @Override
    public int getContentViewId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("动态详情");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        //分享初始化
        mShareHelper = new ShareHelper(this);
        initSwipeRefreshLayout();
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        mbean = (DynamicBean) bundle.getSerializable("bean");
        who = bundle.getInt("who");
        if (mbean != null) {
            uid = mbean.getUid();
            id = mbean.getId();
            isSelf = StringUtils.isSame(uid, CommonUtils.getUserId(this));
        }
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));


        KangQiMeiApi api = new KangQiMeiApi("PostsReviews/index");
        api.addParams("posts_id",id);
        api.addParams("uid",api.getUserId(this));
        api.setMethod(AbstractApi.Method.GET);
        mAdapter = new DynamicDetailsCommAdapter(this, mbean, isSelf);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<DynamicCommBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        mAdapter.setShare(mShareHelper);
        mAdapter.setType(who);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (!CommonUtils.isLogin(this)) {
                    break;
                }
                if (isSelf) {
                    popupWindow();
                } else {
                    mShareHelper.share();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void popupWindow() {
        DetailsMoreView view = (DetailsMoreView) View.inflate(this, R.layout.view_details_more, null);
        int width = UIBaseUtils.dp2pxInt(this, 80);
        final PopupWindow window = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
        int xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(mToolbar, xoff, 2);
        view.setDetailsMoreListener(new DetailsMoreView.DetailsMoreListener() {
            @Override
            public void delete() {
                window.dismiss();
                dialogDelete();
            }

            @Override
            public void shareMore() {
                window.dismiss();
                mShareHelper.share();
            }
        });
    }


    //删除
    private void dialogDelete() {
        AKDialog.getAlertDialog(this, "确定要删除该动态？", new AKDialog.AlertDialogListener() {
            @Override
            public void yes() {
                loadDeleteActivity();
            }
        });
    }


    private void loadDeleteActivity() {//删除动态
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("posts/del");
        api.addParams("id",id);
        api.addParams("uid",api.getUserId(this));
        api.addParams("token",api.getToken(this));

        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(DynamicDetailsActivity.this, response.getInfo());
                EventBus.getDefault().post(new PublishDynamicEvent());
                finish();
            }
        });
    }


    @OnClick(R.id.send_info_tv)
    public void sendInfo() {//发送评论
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        String content = mCompileCommentEt.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            UIHelper.ToastMessage(this, getString(R.string.input_comment));
            return;
        }
        ajaxSend(content);
    }

    private void ajaxSend(String content) {
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("PostsReviews/add");
        api.addParams("content",content);
        api.addParams("uid",api.getUserId(this));
        api.addParams("posts_id",id);
        api.setMethod(AbstractApi.Method.GET);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(DynamicDetailsActivity.this, "评论成功");
                mbean.setReviews((Integer.valueOf(mbean.getReviews()) + 1) + "");
                EventBus.getDefault().post(new DynamicLikedEvent(mbean.getId(), mbean.getLike(), mbean.getLiked(), mbean.getReviews()));
                if (controller != null) {
                    controller.loadFirstPage();
                }
                mCompileCommentEt.setText("");
            }
        });
    }

    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareHelper.onDestroy();
    }
}
