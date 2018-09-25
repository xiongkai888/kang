package com.lanmei.kang.webviewpage;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MJavascriptInterface {
    private Context context;
    private String [] imageUrls;

    public MJavascriptInterface(Context context,String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", imageUrls);
        intent.putExtra("curImageUrl", img);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
//        for (int i = 0; i < imageUrls.length; i++) {
//            Log.e("imageSrcList"+i,"图片地址："+imageUrls[i].toString()+",img = "+img);
//        }
    }
}
