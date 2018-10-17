package com.lanmei.kang.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lanmei.kang.R;
import com.lanmei.kang.event.ChuKuGoodsInfoEvent;
import com.lanmei.kang.event.ScanEvent;
import com.lanmei.kang.event.ScanSucceedEvent;
import com.lanmei.kang.event.ScanUserEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

import butterknife.InjectView;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 扫一扫
 * http://blog.csdn.net/qq_25943493/article/details/52065096
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final int CHOOSE_FROM_GALLAY = 1;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.zxingview)
    ZXingView mQRCodeView;

    private boolean isQR;//true为条形码扫描 false为二维码扫描
    private int type;//1添加出库入库时扫描的商品、2消费、3商家订单、4扫描会员

    @Override
    public int getContentViewId() {
        return R.layout.activity_scan;
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            type = bundle.getInt("type");
            isQR = bundle.getBoolean("isQR");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mQRCodeView.setDelegate(this);

        if (isQR){
            mQRCodeView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
            mQRCodeView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
        }else {
            mQRCodeView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
            mQRCodeView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
        }

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.qr_code);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_image1);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        switch (type){
            case 1://
                EventBus.getDefault().post(new ChuKuGoodsInfoEvent(result));//ChuKuGoodsInfoEvent
                break;
            case 2://
                EventBus.getDefault().post(new ScanEvent(result));
                break;
            case 3://
                EventBus.getDefault().post(new ScanSucceedEvent());
                break;
            case 4://扫描会员
                EventBus.getDefault().post(new ScanUserEvent(result));
                break;
        }
        vibrate();
        onBackPressed();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, R.string.open_camera_error, Toast.LENGTH_LONG).show();
    }

    //.扫码成功时候添加震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mQRCodeView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mQRCodeView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }


    // 扫描本地图片
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        int[] pixels = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
        scanBitmap.getPixels(pixels, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("xs", e.getMessage(), e);

        }
        return null;
    }


    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("xs", formart);
            } else {
                formart = str;
                Log.i("xs", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CHOOSE_FROM_GALLAY:
                final String image = ImageUtils.getImageFileFromPickerResult(this, data);
                Log.i("xs", "image=" + image);


                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Result result = scanningImage(image);
                        // String result = decode(photo_path);
                        if (result == null) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_LONG)
                                    .show();
                            Looper.loop();
                        } else {
                            Log.i("xs", result.toString());
                            // Log.i("123result", result.getText());
                            // 数据返回
                            String recode = recode(result.toString());
                            Log.i("xs", "url=" + recode);
//                            WebViewActivity.startActivity(ScanActivity.this, "扫描商品", "http://sc.jb51.net/uploads/allimg/131203/8-1312032134360-L.jpg", recode);
                        }
                    }
                }).start();

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
