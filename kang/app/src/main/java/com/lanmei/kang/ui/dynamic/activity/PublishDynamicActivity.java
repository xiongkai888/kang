package com.lanmei.kang.ui.dynamic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.event.LocationChooseEvent;
import com.lanmei.kang.event.PublishDynamicEvent;
import com.lanmei.kang.helper.BGASortableNinePhotoHelper;
import com.lanmei.kang.ui.mine.activity.SearchPositionActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.CompressPhotoUtils;
import com.lanmei.kang.util.loc.LocationService;
import com.oss.ManageOssUpload;
import com.xson.common.api.AbstractApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.ProgressHUD;

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

    List<String> paths = new ArrayList<>(); // 本地需要上传图片的集合路径

    private static final int PERMISSION_LOCATION = 100;

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
    private ManageOssUpload manageOssUpload;//图片上传类

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_dynamic;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initPermission();
        /**初始化阿里云*/
        initProgressDialog();
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("发表动态");
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        initPhotoHelper();
        manageOssUpload = new ManageOssUpload(this);
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

    private LocationService locationService;

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
        // -----------location config ------------
        locationService = new LocationService(getApplicationContext());//放在SattingApp里面有问题
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            if (StringUtils.isEmpty(location.getCity())){
                initPermission();
                return;
            }
            mLocationTv.setText(location.getCity()+location.getDistrict()+location.getStreet()+location.getStreetNumber()+"号");
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    @Subscribe
    public void locationChooseEvent(LocationChooseEvent event) {
        mLocationTv.setText(event.getAddress());
    }

    /**
     * 图片上传进度框
     */
    //    private ProgressDialog mProgressDialog;
    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "", true, false, null);
        mProgressHUD.cancel();
    }


    List<String> successPath = new ArrayList<>();  // 存储上传阿里云成功后的上传路径


    private void ajaxHttp() {
        KangQiMeiApi api = new KangQiMeiApi("Posts/add");
        api.addParams("title",title);
        api.addParams("city",CommonUtils.getStringByTextView(mLocationTv));
        api.addParams("uid",api.getUserId(this));
        api.addParams("file",CommonUtils.getStringArr(successPath));
        api.setMethod(AbstractApi.Method.GET);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mProgressHUD.cancel();
                UIHelper.ToastMessage(PublishDynamicActivity.this, "发布成功");
                EventBus.getDefault().post(new PublishDynamicEvent());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(PublishDynamicActivity.this, error.getMessage().toString());
                mProgressHUD.cancel();
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
                mProgressHUD.show();
                paths = mPhotoHelper.getData();
                new CompressPhotoUtils().CompressPhoto(PublishDynamicActivity.this, paths, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                    @Override
                    public void success(List<String> list) {
                        paths = list;
                        ajaxPublish();
                    }
                },"4");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void ajaxPublish() {
        new UpdateImageViewTask().execute();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(PublishDynamicActivity.this, "上传图片失败：" + msg.obj);
//                    mPhotosSnpl.removeItem(msg.arg1);
                    break;
                case 2:
                    break;
            }
        }
    };


    public class UpdateImageViewTask extends AsyncTask<Void, Integer, Void> {

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //            Toast.makeText(PublishDynamicActivity.this, "开始执行", Toast.LENGTH_SHORT).show();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Void doInBackground(Void... params) {
            int size = paths.size();
            for (int i = 0; i < size; i++) {
                String picPath = paths.get(i);
                String urlPic = manageOssUpload.uploadFile_img(picPath,"4" );
                if (StringUtils.isEmpty(urlPic)) {
                    //写上传失败逻辑
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = i;
                    msg.obj = picPath;
                    mHandler.sendMessage(msg);
                } else {
                    successPath.add(urlPic);
                    L.d("CompressPhotoUtils","上传成功返回的图片地址:"+urlPic);
                }
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Void integer) {
            //            mProgressDialog.cancel();
            ajaxHttp();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
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
        if (locationService != null) {
            locationService.unregisterListener(mListener);
            locationService.stop();
            locationService = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
