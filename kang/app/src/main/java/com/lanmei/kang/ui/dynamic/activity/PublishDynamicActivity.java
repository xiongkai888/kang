package com.lanmei.kang.ui.dynamic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.LocationChooseEvent;
import com.lanmei.kang.event.PublishDynamicEvent;
import com.lanmei.kang.helper.BGASortableNinePhotoHelper;
import com.lanmei.kang.ui.mine.activity.SearchPositionActivity;
import com.lanmei.kang.util.BaiduLocation;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.CompressPhotoUtils;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 发表动态
 */
public class PublishDynamicActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {

    private int PERMISSION_LOCATION = 100;

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.title_et)
    EditText mTitleEt;
    @InjectView(R.id.et_moment_add_content)
    EditText mContentEt;
    @InjectView(R.id.location_tv)
    TextView mLocationTv;
    @InjectView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;//拖拽排序九宫格控件
    BGASortableNinePhotoHelper mPhotoHelper;
    private CompressPhotoUtils compressPhotoUtils;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_dynamic;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initPermission();
        /**初始化阿里云*/
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("发表动态");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);

        initPhotoHelper();
        EventBus.getDefault().register(this);
    }

    private void initPhotoHelper() {
        mPhotosSnpl.setVisibility(View.VISIBLE);
        mPhotoHelper = new BGASortableNinePhotoHelper(this, mPhotosSnpl);
        // 设置拖拽排序控件的代理
        mPhotoHelper.setDelegate(this);
    }

    @OnClick(R.id.location_tv)
    public void showLocation() {
        IntentUtil.startActivity(this, SearchPositionActivity.class);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            } else {
                initBaiDu();
            }
        } else {
            initBaiDu();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION){
            initBaiDu();
        }
    }

    private void initBaiDu() {
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    mLocationTv.setText(location.getCity()+location.getDistrict()+location.getStreet()+location.getStreetNumber()+"号");
                    locationClient.stop();
                }
            }
        });
    }

    @Subscribe
    public void locationChooseEvent(LocationChooseEvent event) {
        mLocationTv.setText(event.getAddress());
    }


    private void ajaxHttp(List<String> successPath) {
        KangQiMeiApi api = new KangQiMeiApi("Posts/add");
        api.add("title",title);
        api.add("city",CommonUtils.getStringByTextView(mLocationTv));
        api.add("uid",api.getUserId(this));
        api.add("file",CommonUtils.toArray(successPath));
        api.setMethod(AbstractApi.Method.GET);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(PublishDynamicActivity.this, "发布成功");
                EventBus.getDefault().post(new PublishDynamicEvent());
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_dynamic, menu);
        return true;
    }

    String title;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_publish:
                //发布的时候
                title = mContentEt.getText().toString();
                if (StringUtils.isEmpty(title)) {
                    UIHelper.ToastMessage(this, R.string.qin);
                    return super.onOptionsItemSelected(item);
                }
                if (mPhotosSnpl.getItemCount() == 0) {
                    UIHelper.ToastMessage(this, "请选择要发布的图片");
                    return super.onOptionsItemSelected(item);
                }
                compressPhotoUtils = new CompressPhotoUtils(this);
                compressPhotoUtils.compressPhoto(CommonUtils.toArray(mPhotoHelper.getData()), new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                    @Override
                    public void success(List<String> list) {
                        if (isFinishing()) {
                            return;
                        }
                        ajaxHttp(list);
                    }
                }, "4");

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        mPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compressPhotoUtils != null) {
            compressPhotoUtils.cancelled();
        }
        EventBus.getDefault().unregister(this);
    }
}
