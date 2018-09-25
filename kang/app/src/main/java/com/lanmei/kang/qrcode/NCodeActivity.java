package com.lanmei.kang.qrcode;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.lanmei.kang.R;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.L;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.ybao.zxing.CreateDCode;

import butterknife.InjectView;

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
        actionbar.setHomeAsUpIndicator(R.mipmap.back);

        String des3Code = getIntent().getStringExtra("value");
        try {
            Des des = new Des();
            String code = des.encrypt(des3Code);
            L.d("des3Code","原始密钥："+des3Code);
            L.d("des3Code","加密后："+code);
            L.d("des3Code","解密后："+des.decode(code));
            imageView.setImageBitmap(CreateDCode.CreateQRCode(code, 1000));
        } catch (Exception e) {
            UIHelper.ToastMessage(this,"生成二维码失败");
            e.printStackTrace();
            L.d("des3Code","生成二维码失败："+e.getMessage());
            finish();
        }

    }
}
