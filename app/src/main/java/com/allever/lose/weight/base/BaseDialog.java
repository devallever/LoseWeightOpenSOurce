package com.allever.lose.weight.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;

import android.view.View;


/**
 * @author allever
 */

public abstract class BaseDialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private static final int MSG_DIALOG_SHOW = 0x60;
    private static final int MSG_DIALOG_HIDE = 0x61;
    private AlertDialog mDialog;
    protected Activity mActivity;
    protected Handler mHandler;
    protected DialogInterface.OnShowListener mOnShowListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;

    public BaseDialog(Activity activity) {
        this.mActivity = activity;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_DIALOG_SHOW:
                        boolean cancelable = msg.arg1 == 1 ? true : false;
                        showDialog(cancelable);
                        break;

                    case MSG_DIALOG_HIDE:
                        hideDialog();
                        break;
                }
            }
        };
    }

    public abstract View getDialogView();

    public void show() {
        show(false);
    }

    public void show(boolean cancelable) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_DIALOG_SHOW;
        msg.arg1 = cancelable ? 1 : 0;
        mHandler.sendMessage(msg);
    }

    public void hide() {
        mHandler.sendEmptyMessageDelayed(MSG_DIALOG_HIDE, 50);
    }

    private void showDialog(boolean cancelable) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(mActivity).create();

            View v = getDialogView();
            if (v != null) {
                mDialog.setView(v);
            }
            mDialog.setCancelable(cancelable);
            mDialog.setOnShowListener(this);
            mDialog.setOnDismissListener(this);
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
            mHandler.removeMessages(MSG_DIALOG_HIDE);
        }
    }

    private void hideDialog() {
        mHandler.removeMessages(MSG_DIALOG_HIDE);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        try {
            mHandler.removeMessages(MSG_DIALOG_SHOW);
            mHandler.removeMessages(MSG_DIALOG_HIDE);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialogInterface);
        }
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        if (mOnShowListener != null) {
            mOnShowListener.onShow(dialogInterface);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public interface ClickListener {
        void onClick(BaseDialog dialog);
    }
}
