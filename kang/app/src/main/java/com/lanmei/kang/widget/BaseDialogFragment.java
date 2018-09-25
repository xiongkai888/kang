package com.lanmei.kang.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by xkai on 2017/5/12.
 * 对话框
 */

public class BaseDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    private DialogButtonClickListener mButtonClickListener;
    public static BaseDialogFragment newInstance(int messageStrId, int sureStrId, int cancelStrId) {
        Bundle args = new Bundle();
        args.putInt("messageStrId",messageStrId);
        args.putInt("sureStrId",sureStrId);
        args.putInt("cancelStrId",cancelStrId);
        BaseDialogFragment fragment = new BaseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setButtonClickListener(DialogButtonClickListener mButtonClickListener) {
        this.mButtonClickListener = mButtonClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle arguments = getArguments();
        builder
                .setMessage(arguments.getInt("messageStrId"))
                .setPositiveButton(arguments.getInt("sureStrId"), this)
                .setNegativeButton(arguments.getInt("cancelStrId"), this)
                .setCancelable(false);
        //.show(); // show cann't be use here

        return builder.create();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        dialogInterface.dismiss();
        if (mButtonClickListener == null) {
            return ;
        }

        if (which == DialogInterface.BUTTON_POSITIVE) {
            mButtonClickListener.onPositiveClick();
        } else  if (which == DialogInterface.BUTTON_NEGATIVE) {
            mButtonClickListener.onNegativeClick();
        }
    }

    // 单击事件
    public interface DialogButtonClickListener {
        // 确定
        void onPositiveClick();
        // 取消
        void onNegativeClick();
    }
}