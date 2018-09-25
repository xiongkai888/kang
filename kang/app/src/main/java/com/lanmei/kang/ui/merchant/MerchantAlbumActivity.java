package com.lanmei.kang.ui.merchant;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.MerchantAlbumAdapter;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.bean.AlbumBean;
import com.lanmei.kang.bean.MerchantInfoBean;
import com.lanmei.kang.event.MerchantUpdataAdEvent;
import com.lanmei.kang.ui.mine.activity.AlbumOtherActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.CompressPhotoUtils;
import com.oss.ManageOssUpload;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 更新商家轮播图
 */
public class MerchantAlbumActivity extends BaseActivity {

    List<String> paths = new ArrayList<>(); // 本地需要上传图片的集合路径(压缩后的)
    MerchantAlbumAdapter mAdapter;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.done_tv)
    TextView mDoneTv;//完成
    //    ReceiverHelper mReceiverHelper;
    int albumNum = 0;//网络的相片个数
    private ManageOssUpload manageOssUpload;//图片上传类
    MerchantInfoBean bean;

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_album;
    }

    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "", true, false, null);
        mProgressHUD.setCancelable(true);
        mProgressHUD.cancel();
    }

    String successPath = "";  // 存储上传阿里云成功后的上传路径

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            bean = (MerchantInfoBean)bundle.getSerializable("bean");
        }

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initProgressDialog();
        mDoneTv.setEnabled(false);//
        manageOssUpload = new ManageOssUpload(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        int padding = UIBaseUtils.dp2pxInt(this, 5);
        mRecyclerView.addItemDecoration(new AlbumOtherActivity.SpaceItemDecoration(padding));
        mAdapter = new MerchantAlbumAdapter(this);
        mAdapter.setAlbumListener(new MerchantAlbumAdapter.MerchantAlbumListener() {
            @Override
            public void deleteAlbums() {
                mAlbumBeanlist = mAdapter.getData();
                numAlbum = CommonUtils.getNativeAlbumsNum(mAlbumBeanlist);
                isChangeAlbum();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        setMerchantAlbum();
    }

    private void setMerchantAlbum() {
        if (bean == null || StringUtils.isEmpty(bean.getPics())) {
            return;
        }
        mAlbumBeanlist = CommonUtils.getAlbumList(bean.getPics());
        if (mAlbumBeanlist == null) {
            return;
        }
        albumNum = mAlbumBeanlist.size();
        mAdapter.setData(mAlbumBeanlist);
        setAlbum();
    }

    List<AlbumBean> mAlbumBeanlist;//


    //相册是否有改变
    public void isChangeAlbum() {
        if (mAlbumBeanlist != null) {
            int size = mAlbumBeanlist.size();
            if (size != albumNum) {
                mDoneTv.setEnabled(true);
                mDoneTv.setTextColor(getResources().getColor(R.color.white));
                return;
            } else {
                mDoneTv.setEnabled(false);
                mDoneTv.setTextColor(getResources().getColor(R.color.color999));
            }
            for (int i = 0; i < size; i++) {
                AlbumBean bean = mAlbumBeanlist.get(i);
                if (bean != null) {
                    if (bean.isPicker()) {
                        mDoneTv.setEnabled(true);
                        mDoneTv.setTextColor(getResources().getColor(R.color.white));
                        return;
                    }
                }
            }
        }
    }

    @OnClick(R.id.uploading_bt)
    public void showUploading() {
        if (numAlbum == 9) {
            UIHelper.ToastMessage(this, "一次最多只能上传九张");
            return;
        }
        choicePhotoWrapper();
    }

    @OnClick({R.id.back_iv, R.id.done_tv})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.done_tv:
                List<String> list = CommonUtils.getUploadingList(mAlbumBeanlist);
                if (list != null && list.size() > 0) {
                    mProgressHUD.show();
                    new CompressPhotoUtils().CompressPhoto(MerchantAlbumActivity.this, list, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                        @Override
                        public void success(List<String> list) {
                            paths = list;
                            new UpdateImageViewTask().execute();
                        }
                    },"5");
                } else {
                    successPath = "";
                    updateAblum();
                }
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//点击加号时调用
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "uploading");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 9 - numAlbum, null, false), REQUEST_CODE_CHOOSE_PHOTO);
        }
    }

    int numAlbum = 0;//已经选择的相片个数

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {//选择图片点击确定时
            ArrayList<String> list = BGAPhotoPickerActivity.getSelectedImages(data);
            List<AlbumBean> albumList = new ArrayList<>();
            if (list != null && list.size() > 0) {
                int size = list.size();
                numAlbum += size;
                for (int i = 0; i < size; i++) {
                    AlbumBean bean = new AlbumBean();
                    bean.setPic(list.get(i));
                    bean.setPicker(true);
                    albumList.add(bean);
                }
            }
            mAlbumBeanlist.addAll(albumList);
            isChangeAlbum();
            setAlbum();
        }
    }

    private void setAlbum() {
        if (mAlbumBeanlist != null) {
            String[] arry = CommonUtils.getStringArry(mAlbumBeanlist);
            mAdapter.setStringArry(arry);
            mAdapter.setData(mAlbumBeanlist);
            mAdapter.notifyDataSetChanged();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(MerchantAlbumActivity.this, "上传图片失败：" + msg.obj);
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
                String urlPic = manageOssUpload.uploadFile_img(picPath,"5");
                if (StringUtils.isEmpty(urlPic)) {
                    //写上传失败逻辑
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = i;
                    msg.obj = picPath;
                    mHandler.sendMessage(msg);
                } else {
                    successPath += urlPic + ",";
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
            updateAblum();//更新商家相册
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private void updateAblum() {
        if (this.isFinishing()) {
            return;
        }
        HttpClient httpClient = HttpClient.newInstance(this);
        KangQiMeiApi api = new KangQiMeiApi("place/update");
        api.addParams("token",api.getToken(this));
        api.addParams("uid",api.getUserId(this));
        String pics = CommonUtils.getSubString(CommonUtils.getAlbumsPics(mAlbumBeanlist) + successPath);
        api.addParams("pics",pics);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mProgressHUD.cancel();
                UIHelper.ToastMessage(MerchantAlbumActivity.this, response.getInfo().toString());
                //                successPath = "";
                //                mDoneTv.setEnabled(false);
                //                mDoneTv.setTextColor(getResources().getColor(R.color.color999));
//                DemoHelper.getInstance().UpdateUserInfo();
                EventBus.getDefault().post(new MerchantUpdataAdEvent());
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        mReceiverHelper.onDestroy();
    }
}
