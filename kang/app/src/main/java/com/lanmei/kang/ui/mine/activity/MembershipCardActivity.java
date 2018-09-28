package com.lanmei.kang.ui.mine.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.CircleImageView;

import butterknife.InjectView;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;


/**
 * 会员卡号
 */
public class MembershipCardActivity extends BaseActivity{


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pic_iv)
    CircleImageView pic_iv;
    @InjectView(R.id.iv_barcode_with_content)
    ImageView mBarcodeWithContentIv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_membership_card;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.merbership_card_num);
        actionbar.setHomeAsUpIndicator(R.mipmap.back_g);
        ImageHelper.load(this, CommonUtils.getUserBean(this).getPic(),pic_iv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
        createBarcodeWidthContent();
    }


    private void createBarcodeWidthContent() {
        new AsyncTask<Void, Void, Bitmap>() {//可能存在内存泄漏
            @Override
            protected Bitmap doInBackground(Void... params) {
                int width = BGAQRCodeUtil.dp2px(MembershipCardActivity.this, 250);
                int height = BGAQRCodeUtil.dp2px(MembershipCardActivity.this, 70);
                int textSize = BGAQRCodeUtil.sp2px(MembershipCardActivity.this, 18);
                return QRCodeEncoder.syncEncodeBarcode("8888 8888 888", width, height, textSize);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null && mBarcodeWithContentIv != null) {
                    mBarcodeWithContentIv.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }

}
