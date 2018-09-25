package com.lanmei.kang.util.version;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.xson.common.utils.StringUtils;


public class AutoUpdateUtil {

    /**
     * @param maxVersion 最高版本
     * @param context
     * @param updateurl  更新路径
     * @param content    内容提示
     * @throws NameNotFoundException
     */
    public synchronized static void update(String maxVersion,
                                           Context context, String updateurl, String content) throws NameNotFoundException {
        int versionCode = AppInfoUtil.getAppVersionCode(context);
        int maxVersionCode;
        if (!StringUtils.isEmpty(maxVersion)){
            maxVersionCode = Integer.parseInt(maxVersion);
        }
        Log.d("maxVersion","maxVersion = "+maxVersion+",getAppVersionCode = "+AppInfoUtil.getAppVersionCode(context));
        if (versionCode < 4) {// 是否必须更新
            if (!StringUtils.isEmpty(updateurl)) {// 如果不同版本，则更新
//                Intent intent = new Intent();
//                intent.putExtra("url", updateurl);
//                intent.setClass(context, UpdateService.class);
//                context.startService(intent);
				Dialog(context, updateurl, maxVersion+"版本更新", content);
            }
        } else {
//			Toast.makeText(context, "已经是最新版本，无需更新", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 弹窗
     */
    public static void Dialog(final Context context, final String url, String title, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pup_show_update_layout, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvContent = (TextView) view.findViewById(R.id.content);
        TextView tvSubmit = (TextView) view.findViewById(R.id.submit);
        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);

        tvTitle.setText(title);
        tvContent.setText("更新内容:\n" + content + "\n是否更新?");
        final Dialog mDialog = new Dialog(context, R.style.dialog);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("url", url);
                intent.setClass(context, UpdateService.class);
                context.startService(intent);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();

            }
        });

        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

}
