package com.lanmei.kang.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lanmei.kang.updateversion.SDCardUtils;
import com.xson.common.utils.UIHelper;

import java.io.File;

/**
 * Created by xkai on 2017/9/13.
 */

public class ApkInstallBroadcast extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            deleteApk();
        }
    }

    private void deleteApk(){
        String apkUrl = SDCardUtils.getRootDirectory() + "/updateVersion";
        File file = new File(apkUrl);
        if (file.exists()){
            if (file.isDirectory()) { //该路径名表示的文件是否是一个目录（文件夹）
                File[] files = file.listFiles(); //列出当前文件夹下的所有文件
                for (File f : files) {
                    if (f.isFile()){
                        if (f.delete()){
                            UIHelper.ToastMessage(context,"已删除安装包");
                        }
                    }
                }
            }

        }
    }

}
