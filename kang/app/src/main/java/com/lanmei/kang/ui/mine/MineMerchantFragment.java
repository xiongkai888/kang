package com.lanmei.kang.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.UserUpdateApi;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.xson.common.helper.UserHelper;
import com.lanmei.kang.ui.MainActivity;
import com.lanmei.kang.ui.merchant.ClientValuateActivity;
import com.lanmei.kang.ui.merchant.MerchantDataActivity;
import com.lanmei.kang.ui.merchant.MerchantOrderActivity;
import com.lanmei.kang.ui.merchant.activity.MerchantItemsActivity;
import com.lanmei.kang.ui.mine.activity.AlbumActivity;
import com.lanmei.kang.ui.mine.activity.MyOrderActivity;
import com.lanmei.kang.ui.mine.activity.PersonalDataActivity;
import com.lanmei.kang.ui.mine.activity.SettingActivity;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.oss.ManageOssUpload;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/25.
 * 我的（商家）
 */

public class MineMerchantFragment extends BaseFragment {

    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int CHOOSE_FROM_CAMERA = 2;
    private static final int RESULT_FROM_CROP = 3;

    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;

    Activity activity;

    private ManageOssUpload manageOssUpload;//图片上传类

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }


    public static MineMerchantFragment newInstance() {
        Bundle args = new Bundle();
        MineMerchantFragment fragment = new MineMerchantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_merchant;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        manageOssUpload = new ManageOssUpload(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUser(UserHelper.getInstance(context).getUserBean());//初始化用户信息
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetUserInfoEvent event) {
        setUser(UserHelper.getInstance(context).getUserBean());
    }

    private void setUser(UserBean userBean) {
        if (userBean == null) {
            userNameTv.setText("游客");
            headIv.setImageResource(R.mipmap.default_pic);
            return;
        }
        userNameTv.setText(userBean.getNickname());
        ImageHelper.load(context, userBean.getPic(), headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
    }

    private File tempImage;
    private File croppedImage;

    /**
     * 相机拍照
     */
    private void startActionCamera() {
        tempImage = ImageUtils.getTempFile(context, "head");
        if (tempImage == null) {
            UIHelper.ToastMessage(context, R.string.cannot_create_temp_file);
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
                image = ImageUtils.getImageFileFromPickerResult(context, data);
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
            UIHelper.ToastMessage(context, R.string.image_not_exists);
            return;
        }
        File imageFile = new File(image);
        if (!imageFile.exists()) {
            UIHelper.ToastMessage(context, R.string.image_not_exists);
            return;
        }
        croppedImage = ImageUtils.getTempFile(context, "head");
        if (croppedImage == null) {
            UIHelper.ToastMessage(context, R.string.cannot_create_temp_file);
            return;
        }
        Intent intent = ImageUtils.getImageCropIntent(getUriForFile(imageFile), Uri.fromFile(croppedImage));
        startActivityForResult(intent, RESULT_FROM_CROP);
    }


    @OnClick({R.id.head_iv, R.id.ll_m_data, R.id.ll_m_order, R.id.ll_m_album, R.id.ll_m_services, R.id.ll_m_evaluate, R.id.ll_m_setting, R.id.ll_m_center,R.id.ll_mime_order})
    public void onViewClicked(View view) {
        if (!CommonUtils.isLogin(context)){
            return;
        }
        switch (view.getId()) {
            case R.id.ll_m_data://商家资料
                IntentUtil.startActivity(context, MerchantDataActivity.class);
                break;
            case R.id.ll_m_order://订单
                IntentUtil.startActivity(context, MerchantOrderActivity.class);
                break;
            case R.id.ll_m_album://相册
                IntentUtil.startActivity(context, AlbumActivity.class);
//                AlbumOtherActivity.startActivityAlbumOther(context,"merchant_album");
                break;
            case R.id.ll_m_services://服务项目MerchantItemsActivity
                IntentUtil.startActivity(context, MerchantItemsActivity.class);
                break;
            case R.id.ll_m_evaluate://评论
                IntentUtil.startActivity(context, ClientValuateActivity.class);
                break;
            case R.id.ll_m_setting://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.ll_m_center://个人中心
                IntentUtil.startActivity(context, PersonalDataActivity.class);
                break;
            case R.id.ll_mime_order://我的订单
                IntentUtil.startActivity(context, MyOrderActivity.class);
                break;
            case R.id.head_iv://点击头像选择上传头像
                AKDialog.showBottomListDialog(context, activity, new AKDialog.AlbumDialogListener() {
                    @Override
                    public void photograph() {
                        isCamera = true;
                        applyWritePermission();
//                startActionCamera();
                    }

                    @Override
                    public void photoAlbum() {
                        isCamera = false;
                        applyWritePermission();
                    }
                });
                break;
        }
    }


    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.lanmei.kang.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
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
            String urlPic = manageOssUpload.uploadFile_img(params[0],"11");
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

    private void ajaxUploadingHead(final String headUrl) {
        HttpClient httpClient = HttpClient.newInstance(getContext());
        UserUpdateApi api = new UserUpdateApi();
        api.uid = api.getUserId(context);
        api.token = api.getToken(context);
        api.pic = headUrl;
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (userNameTv == null) {
                    return;
                }
                UserBean bean = CommonUtils.getUserBean(context);
                if (bean != null) {
                    bean.setPic(headUrl);
                    UserHelper.getInstance(context).saveBean(bean);
                    UIHelper.ToastMessage(getContext(), "上传头像成功");
                    ImageHelper.load(context, headUrl, headIv, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
                }
            }
        });
    }


    boolean isCamera = false;

    public void applyWritePermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (isCamera) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                if (isCamera) {
                    startActionCamera();
                } else {
                    startImagePick();
                }

            } else {
                requestPermissions(permissions, 1);
            }
        } else {
            if (isCamera) {
                startActionCamera();
            } else {
                startImagePick();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(context, "上传头像失败");
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
