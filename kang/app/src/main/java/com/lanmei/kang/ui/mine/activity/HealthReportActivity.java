package com.lanmei.kang.ui.mine.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.lanmei.kang.KangApp;
import com.lanmei.kang.R;
import com.lanmei.kang.util.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.UserBean;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 健康报告
 */
public class HealthReportActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener ,OnPageErrorListener{

    @InjectView(R.id.pdfView)
    PDFView pdfView;
    private OkHttpClient okHttpClient;
    private String url = "";
    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_health_report;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
         toolbarNameTv.setText(R.string.health_report);

        okHttpClient = new OkHttpClient();
        CommonUtils.loadUserInfo(KangApp.applicationContext, new CommonUtils.UserInfoListener() {
            @Override
            public void userInfo(UserBean bean) {
                if (isFinishing()) {
                    return;
                }
                url = bean.getFiles_img();
                if (StringUtils.isEmpty(url)) {
                    return;
                }
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(SDPath, url.substring(url.lastIndexOf("/") + 1));
                if (file.exists()){
                    displayFromFile(file);
                    L.d("h_bl", "file.exists()");
                    return;
                }
                load();
            }

            @Override
            public void error(String error) {

            }
        });
    }


    @OnClick(R.id.back_iv)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    private void load() {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("h_bl", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, url.substring(url.lastIndexOf("/") + 1));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
//                        Log.d("h_bl", "progress=" + progress);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    L.d("h_bl", "文件下载成功");
                } catch (Exception e) {
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        menuTv.setText(String.format(getString(R.string.page_pdf),String.valueOf(page+1),String.valueOf(pageCount)));
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
//        Toast.makeText(this, "加载完成" + nbPages, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( MainActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==100){
                displayFromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), url.substring(url.lastIndexOf("/") + 1)));
            }
        }
    };

    private void displayFromFile(File file) {
        L.d("h_bl",file.getAbsolutePath());
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        L.d("h_bl", "Cannot load page :" + (page+1));
    }
}
