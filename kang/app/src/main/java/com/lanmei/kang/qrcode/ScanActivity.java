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
import com.lanmei.kang.event.ScanEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.L;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

import butterknife.InjectView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 扫一扫
 * http://blog.csdn.net/qq_25943493/article/details/52065096
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = "scan";
    private static final int CHOOSE_FROM_GALLAY = 1;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.zxingview)
    ZXingView mQRCodeView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mQRCodeView.startSpot();
        mQRCodeView.setResultHandler(this);//这个参数是一个Delegate接口，已经在本类实现了，也可以在这里进行实现

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.qr_code);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_image1);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        EventBus.getDefault().post(new ScanEvent(result));
        vibrate();
        onBackPressed();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        L.d(TAG, "onScanQRCodeOpenCameraError");
        Toast.makeText(this, R.string.open_camera_error, Toast.LENGTH_LONG).show();
    }

    //.扫码成功时候添加震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.stopCamera();
        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_publish_dynamic, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_publish:
////                Toast.makeText(this, "相册", Toast.LENGTH_LONG).show();
//                Intent intent = ImageUtils.getImagePickerIntent();
//                startActivityForResult(intent, CHOOSE_FROM_GALLAY);
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


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
