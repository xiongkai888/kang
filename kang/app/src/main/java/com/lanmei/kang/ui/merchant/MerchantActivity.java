package com.lanmei.kang.ui.merchant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.KangQiMeiApi;
import com.lanmei.kang.ui.BaseHxActivity;
import com.lanmei.kang.ui.login.LoginActivity;
import com.lanmei.kang.ui.merchant.activity.MerchantItemsActivity;
import com.lanmei.kang.ui.mine.activity.PersonalDataActivity;
import com.lanmei.kang.ui.mine.activity.SettingActivity;
import com.lanmei.kang.util.AKDialog;
import com.oss.ManageOssUpload;
import com.xson.common.api.AbstractApi;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.UserHelper;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商家
 */
public class MerchantActivity extends BaseHxActivity {

    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int CHOOSE_FROM_CAMERA = 2;
    private static final int RESULT_FROM_CROP = 3;

//    @InjectView(R.id.toolbar)
//    CenterTitleToolbar mToolbar;
    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;

    private ManageOssUpload manageOssUpload;//图片上传类

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        manageOssUpload = new ManageOssUpload(this);
//        setSupportActionBar(mToolbar);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayShowTitleEnabled(true);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setTitle("商家");
//        actionbar.setHomeAsUpIndicator(R.mipmap.back);

    }

    String pics = "";


    @Override
    public void login() {
        IntentUtil.startActivity(this, LoginActivity.class);
    }

    @Override
    public void updateUnreadMessageCount(int msgCount) {//接收到信息、从我的信息退出后才调用
//        CommonUtils.setMsgCount(mMsgCountTv, msgCount);
    }

    @Override
    public void updateTotalUnreadCount(int totalCount) {//从我的信息退出后才调用
        //        Log.d("mianac","totalCount = "+totalCount);
    }

    @OnClick(R.id.head_iv)
    public void showIconModeDialog() {//点击头像选择上传头像

        AKDialog.showBottomListDialog(this, this, new AKDialog.AlbumDialogListener() {
            @Override
            public void photograph() {
                startActionCamera();
            }

            @Override
            public void photoAlbum() {
                startImagePick();
            }
        });
    }


    private File tempImage;
    private File croppedImage;

    /**
     * 相机拍照
     */
    private void startActionCamera() {
        tempImage = ImageUtils.getTempFile(this, "head");
        if (tempImage == null) {
            UIHelper.ToastMessage(this, R.string.cannot_create_temp_file);
            return;
        }
        Intent intent = ImageUtils.getImageCaptureIntent(Uri.fromFile(tempImage));
        startActivityForResult(intent, CHOOSE_FROM_CAMERA);
    }

    private void startImagePick() {
        Intent intent = ImageUtils.getImagePickerIntent();
        startActivityForResult(intent, CHOOSE_FROM_GALLAY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        String image;
        switch (requestCode) {
            case CHOOSE_FROM_GALLAY:
                image = ImageUtils.getImageFileFromPickerResult(this, data);
                startActionCrop(image);
                break;
            case CHOOSE_FROM_CAMERA:
                //注意小米拍照后data 为null
                image = tempImage.getPath();
                startActionCrop(image);
                break;
            case RESULT_FROM_CROP:
                uploadNewPhoto();// 上传新照片
                break;
            default:
                //                refresh();
                break;
        }
    }

    String heaPathStr;//选择头像剪切后的路径

    private void uploadNewPhoto() {
        if (croppedImage != null) {
            heaPathStr = croppedImage.getAbsolutePath();
            new UpdateHeadViewTask().execute(heaPathStr);
        }
    }

    private void startActionCrop(String image) {
        if (TextUtils.isEmpty(image)) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        File imageFile = new File(image);
        if (!imageFile.exists()) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        croppedImage = ImageUtils.getTempFile(this, "head");
        if (croppedImage == null) {
            UIHelper.ToastMessage(this, R.string.cannot_create_temp_file);
            return;
        }
//        Intent intent = ImageUtils.getImageCropIntent(imageFile, croppedImage);
//        startActivityForResult(intent, RESULT_FROM_CROP);
    }


    @OnClick({R.id.ll_m_data, R.id.ll_m_order, R.id.ll_m_album, R.id.ll_m_services, R.id.ll_m_evaluate, R.id.ll_m_setting, R.id.ll_m_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_m_data://商家资料
                IntentUtil.startActivity(this,MerchantDataActivity.class);
                break;
            case R.id.ll_m_order://订单
                IntentUtil.startActivity(this,MerchantOrderActivity.class);
                break;
            case R.id.ll_m_album://相册
                UIHelper.ToastMessage(this,R.string.developing);
                break;
            case R.id.ll_m_services://服务项目MerchantItemsActivity
                IntentUtil.startActivity(this,MerchantItemsActivity.class);
                break;
            case R.id.ll_m_evaluate://评论
                IntentUtil.startActivity(this,ClientValuateActivity.class);
                break;
            case R.id.ll_m_setting://设置
                IntentUtil.startActivity(this,SettingActivity.class);
                break;
            case R.id.ll_m_center://个人中心
                IntentUtil.startActivity(this, PersonalDataActivity.class);
                break;
        }
    }


    public class UpdateHeadViewTask extends AsyncTask<String, Integer, String> {

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
        protected String doInBackground(String... params) {
            String urlPic = manageOssUpload.uploadFile_img(params[0],"10");
            if (StringUtils.isEmpty(urlPic)) {
                //写上传失败逻辑
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
            return urlPic;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(String headUrl) {
            if (!StringUtils.isEmpty(headUrl)) {
                ajaxUploadingHead(headUrl);
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private void ajaxUploadingHead(String headUrl) {
        KangQiMeiApi api = new KangQiMeiApi("member/update");
        api.addParams("token",api.getToken(this));
        api.addParams("pic",headUrl);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), "上传头像成功");
                ajaxUserInfo();//请求更新后的用户信息
            }
        });
    }


    private void ajaxUserInfo() {
        HttpClient httpClient = HttpClient.newInstance(getContext());
        KangQiMeiApi api = new KangQiMeiApi("member/member");
        api.addParams("token",api.getToken(this));
        api.setMethod(AbstractApi.Method.GET);
        httpClient.request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                if (MerchantActivity.this.isFinishing()) {
                    return;
                }
                UserBean bean = response.data;
                if (bean != null) {
                    UserHelper.getInstance(getContext()).saveBean(bean);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(MerchantActivity.this, "上传头像失败");
                    break;
            }
        }
    };

}
