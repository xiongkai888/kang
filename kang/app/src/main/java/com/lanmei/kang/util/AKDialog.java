package com.lanmei.kang.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.view.ChangePhoneView;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

/**
 * Dialog工具类
 * Created by benio on 2015/10/11.
 */
public class AKDialog {

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg) {
        return getMessageDialog(context, null, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg) {
        return getMessageDialog(context, title, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg, DialogInterface.OnClickListener okListener) {
        return getMessageDialog(context, null, msg, okListener);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton("确定", okListener);
        return builder;
    }

    public static AlertDialog getChangePhoneDialog(Context context, String title, String phone, String type, final ChangePhoneListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ChangePhoneView view = (ChangePhoneView) View.inflate(context, R.layout.dialog_change_phone, null);
        view.setTitle(title);
        view.setPhone(phone);
        view.setType(type);
        view.setChangePhoneListener(new ChangePhoneView.ChangePhoneListener() {
            @Override
            public void succeed(String newPhone) {
                if (l != null) {
                    l.succeed(newPhone);
                }
            }

            @Override
            public void unBound() {
                if (l != null) {
                    l.unBound();
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    public interface ChangePhoneListener {
        void succeed(String newPhone);//更换手机号

        void unBound();//解绑银行卡
    }

    /**
     * 编辑分类
     * @param context
     * @param msg
     * @param confirmListener
     * @return
     */

    public static AlertDialog getCompileCategoryDialog(Context context, String msg, String name,final ConfirmListener confirmListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_compile_category, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        TextView mMessage = (TextView) view.findViewById(R.id.compile_tv);
        final EditText categoryEt = (EditText) view.findViewById(R.id.category_et);
        categoryEt.setText(name);//分类名
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        mMessage.setText(msg);//弹框内容
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    confirmListener.yes(CommonUtils.getStringByEditText(categoryEt));
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, null, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, title, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        return builder;
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, null, arrays, onClickListener);
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String title,
                                                      String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, null, arrays, selectIndex, onClickListener);
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // builder.setNegativeButton("取消", null);
        return builder;
    }

    public AKDialog() {
    }

    /**
     * 拍照、选择相册底部弹框提示
     *
     * @param context
     * @param activity
     * @param listener
     */
    public static void showBottomListDialog(Context context, Activity activity, final AlbumDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.album_dialog_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {//拍照
                    listener.photograph();
                }
                dialog.cancel();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相册
                if (listener != null) {
                    listener.photoAlbum();
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        lp.y = 20;
        lp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public interface AlbumDialogListener {
        void photograph();//拍照

        void photoAlbum();//相册
    }

    public static void getAlertDialog(Context context, String content, final AlertDialogListener l) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (l != null) {
                            l.yes();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).create();
        dialog.show();
    }

    public interface AlertDialogListener {
        void yes();
    }

    /**
     * 消费验证码
     *
     * @param context
     * @param yesStr
     * @param noStr
     * @param confirmListener
     * @return
     */
    public static AlertDialog getConsumeCodeDialog(final Context context, String yesStr, String noStr, final ConfirmListener confirmListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_consume_code, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        final EditText code = (EditText) view.findViewById(R.id.input_pwd_et);
        yes.setText(yesStr);
        no.setText(noStr);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeStr = CommonUtils.getStringByEditText(code);
                if (StringUtils.isEmpty(codeStr)) {
                    UIHelper.ToastMessage(context, R.string.input_consume_code);
                    return;
                }
                if (confirmListener != null) {
                    dialog.cancel();
                    confirmListener.yes(code.getText().toString().trim());
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        return dialog;
    }


    public interface ConfirmListener {
        void yes(String code);
    }

}
