package com.lanmei.kang.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class NCodeActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.img_code)
    ImageView imageView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_code;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("消费二维码");
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        String des3Code = getIntent().getStringExtra("value");
        final String code = Des.encrypt(des3Code);
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(code, BGAQRCodeUtil.dp2px(NCodeActivity.this, 250), Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    UIHelper.ToastMessage(NCodeActivity.this, "生成二维码失败");
                    finish();
                }
            }
        }.execute();

    }
}
