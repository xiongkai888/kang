package com.lanmei.kang.ui.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.api.UserUpdateApi;
import com.lanmei.kang.event.SetUserInfoEvent;
import com.xson.common.helper.UserHelper;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.oss.ManageOssUpload;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.helper.SimpleTextWatcher;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 个人资料
 */
public class PersonalDataActivity extends BaseActivity {

    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int CHOOSE_FROM_CAMERA = 2;
    private static final int RESULT_FROM_CROP = 3;

    @InjectView(R.id.personal_icons_iv)
    ImageView mPersonalIconsIV;//头像
    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.ll_personal_icons)
    LinearLayout mllHeadOnClick;//点击上传头像
    @InjectView(R.id.nick_tv)
    TextView mNickTv;//昵称
    @InjectView(R.id.qq_tv)
    TextView mQqTv;//qq
    @InjectView(R.id.mail_tv)
    TextView mMailTv;//邮箱
    @InjectView(R.id.phone_tv)
    TextView mPhoneTv;//电话
    @InjectView(R.id.address_tv)
    TextView mAddressTv;//地址
    @InjectView(R.id.signature_et)
    EditText mSignatureEt;//个性签名
    @InjectView(R.id.save_bt)
    Button mSaveButton;//保存


    private ManageOssUpload manageOssUpload;//图片上传类


    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_data;
    }

    UserBean bean;
    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "", true, false, null);
        mProgressHUD.cancel();
    }

    String name;//姓名
    String qq;//qq
    String email;//邮箱
    String phone;//手机号码
    String address;//地址
    String signature;//个性签名
    String pic;//头像地址

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.personal_data);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);
        initProgressDialog();

        bean = UserHelper.getInstance(this).getUserBean();
        if (bean != null) {
            name = bean.getNickname();
            qq = bean.getQq();
            email = bean.getEmail();
            phone = bean.getPhone();
            address = bean.getAddress();
            signature = bean.getSignature();
            headPathStr = pic = bean.getPic();

            mNickTv.setText(name);
            mQqTv.setText(qq);
            mMailTv.setText(email);
            mPhoneTv.setText(phone);
            mAddressTv.setText(address);
            mSignatureEt.setText(signature);
            ImageHelper.load(this, pic, mPersonalIconsIV, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
        }
        manageOssUpload = new ManageOssUpload(this);
        mSaveButton.setEnabled(false);
        mSaveButton.setBackgroundResource(R.drawable.button_corners_4_999);
        mSignatureEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataIsChange();
            }
        });
    }


    //资料是否有改动过
    private void dataIsChange() {
        String cNickTv = mNickTv.getText().toString().trim();
        String cPhone = mPhoneTv.getText().toString().trim();
        String cQianming = mSignatureEt.getText().toString().trim();
        String cEmail = mMailTv.getText().toString().trim();
        String cQq = mQqTv.getText().toString().trim();
        String cAddress = mAddressTv.getText().toString().trim();
        if (!StringUtils.isSame(cNickTv,name) || !StringUtils.isSame(cQianming,signature) || !StringUtils.isSame(cEmail,email)
                || !StringUtils.isSame(cAddress,address)
                || !StringUtils.isSame(cQq,qq)
                || !StringUtils.isSame(cPhone,phone)
                || !StringUtils.isSame(headPathStr,pic)) {
            mSaveButton.setEnabled(true);
            mSaveButton.setBackgroundResource(R.drawable.button_corners_4_radius);
        } else {
            mSaveButton.setEnabled(false);
            mSaveButton.setBackgroundResource(R.drawable.button_corners_4_999);
        }
    }

    private void ajaxSaveDate() {
        if (StringUtils.isSame(headPathStr,pic)) {
            loadUpDate();
        }else {
            mProgressHUD.show();
            new UpdateHeadViewTask().execute(headPathStr);
        }
    }

    private void loadUpDate() {
        final String name = CommonUtils.getStringByTextView(mNickTv);
        final String qq = CommonUtils.getStringByTextView(mQqTv);//
        final String email =  CommonUtils.getStringByTextView(mMailTv);
        final String phone = CommonUtils.getStringByTextView(mPhoneTv);//
        final String address = CommonUtils.getStringByTextView(mAddressTv);//
        final String signature = CommonUtils.getStringByEditText(mSignatureEt);//

        HttpClient httpClient = HttpClient.newInstance(this);
        UserUpdateApi api = new UserUpdateApi();
        api.uid = api.getUserId(this);
        if (StringUtils.isEmpty(mHeadUrl)){
            api.pic = pic;
        }else {
            api.pic = mHeadUrl;
        }
        api.nickname = name;
        api.qq = qq;
        api.email = email;
        api.phone = phone;
        api.address = address;
        api.signature = signature;
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(PersonalDataActivity.this, "修改资料成功");
                if (!StringUtils.isEmpty(bean)){
                    bean.setNickname(name);
                    bean.setQq(qq);
                    bean.setEmail(email);
                    bean.setPhone(phone);
                    bean.setAddress(address);
                    bean.setSignature(signature);
                    if (!StringUtils.isEmpty(mHeadUrl)){
                        bean.setPic(mHeadUrl);
                    }
                    UserHelper.getInstance(PersonalDataActivity.this).saveBean(bean);
                    EventBus.getDefault().post(new SetUserInfoEvent());
                }
                finish();
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
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
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

    @OnClick(R.id.ll_personal_icons)
    public void showIconModeDialog() {
        AKDialog.showBottomListDialog(this, this, new AKDialog.AlbumDialogListener() {
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
//                startImagePick();
            }
        });
    }

    @OnClick({R.id.ll_nick, R.id.ll_QQ, R.id.ll_mail, R.id.ll_phone, R.id.ll_address, R.id.save_bt})
    public void showOnClick(View view) {
        switch (view.getId()) {//1:我的昵称  2：我的qq  3:我的邮箱  4：我的电话  5：我的联系地址
            case R.id.ll_nick://昵称
                startActivityPersonal(101, CommonUtils.getStringByTextView(mNickTv));
                break;
            case R.id.ll_QQ://qq
                startActivityPersonal(102, CommonUtils.getStringByTextView(mQqTv));
                break;
            case R.id.ll_mail://邮箱
                startActivityPersonal(103, CommonUtils.getStringByTextView(mMailTv));
                break;
            case R.id.ll_phone://电话
                startActivityPersonal(104, CommonUtils.getStringByTextView(mPhoneTv));
                break;
            case R.id.ll_address://地址
                startActivityPersonal(105, CommonUtils.getStringByTextView(mAddressTv));
                break;
            case R.id.save_bt://保存
                ajaxSaveDate();
                break;
        }
    }

    private void startActivityPersonal(int type,String value) {
        IntentUtil.startActivityForResult(this,PersonalCompileActivity.class,value,type);
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
        Intent intent = ImageUtils.getImageCaptureIntent(getUriForFile(tempImage));
        startActivityForResult(intent, CHOOSE_FROM_CAMERA);
    }

    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.lanmei.kang.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
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
        String compile = "";
        if (data != null) {
            compile = data.getStringExtra("compile");
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
                dataIsChange();
                break;
            case 101:
                mNickTv.setText(compile);
                dataIsChange();
                break;
            case 102:
                mQqTv.setText(compile);
                dataIsChange();
                break;
            case 103:
                mMailTv.setText(compile);
                dataIsChange();
                break;
            case 104:
                mPhoneTv.setText(compile);
                dataIsChange();
                break;
            case 105:
                mAddressTv.setText(compile);
                dataIsChange();
                break;
            default:
                break;
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
        Intent intent = ImageUtils.getImageCropIntent(getUriForFile(imageFile), Uri.fromFile(croppedImage));
        startActivityForResult(intent, RESULT_FROM_CROP);
    }

    String headPathStr;//选择头像剪切后的路径()

    private void uploadNewPhoto() {
        if (!StringUtils.isEmpty(croppedImage) && croppedImage.exists()) {
            try {
                ImageUtils.compressImage(croppedImage.getPath(), 300, 300, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                L.e(e);
            }
        }
        if (croppedImage != null) {
            headPathStr = croppedImage.getAbsolutePath();
//            pic = heaPathStr;
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(headPathStr, mPersonalIconsIV.getWidth(), mPersonalIconsIV.getHeight());
            mPersonalIconsIV.setImageBitmap(bitmap);
        }
    }

    String mHeadUrl;//上传阿里云返回的头像地址

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
            String urlPic = manageOssUpload.uploadFile_img(params[0],"12");
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
            mHeadUrl = headUrl;
            mProgressHUD.cancel();
            if (!StringUtils.isEmpty(mHeadUrl)){
                loadUpDate();
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(PersonalDataActivity.this, "上传头像失败");
                    break;
            }
        }
    };
}
