package com.lanmei.kang.updateversion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.util.CommonUtils;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author wenjie 版本更新的工具类
 */
public class UpdateVersionUtil {

    /**
     * 弹出新版本提示
     *
     * @param context 上下文
     */
    public static void showDialog(final Context context,String description) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        final File file = new File(SDCardUtils.getRootDirectory() + "/updateVersion/" + CommonUtils.getVersionCode() + "kang.apk");
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);

        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        tvUpdateTile.setText(description);

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_update_id_ok) {
                    //新版本已经下载
                    if (file.exists() && file.getName().contains(SDCardUtils.getRootDirectory() + "/updateVersion/"+ CommonUtils.getVersionCode() + "kang.apk")) {
                        Intent intent = ApkUtils.getInstallIntent(file);
                        context.startActivity(intent);
                    } else {
                        //没有下载，则开启服务下载新版本
                        Intent intent = new Intent(context, UpdateVersionService.class);
                        intent.putExtra("downloadUrl", CommonUtils.getUpdateAppUrl());
                        context.startService(intent);
                    }
                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 收起通知栏
     *
     * @param context
     */
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService(Context.STORAGE_SERVICE);
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
