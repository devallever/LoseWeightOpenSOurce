package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.BaseDialog;

/**
 * Created by allever on 18-3-7.
 */

public class ExitActionDialog extends BaseDialog {
    private ImageView mIvClose;
    private Button mBtnExit;
    private Button mBtnDelay;
    private Builder mBuilder;

    private ExitActionDialog(Activity activity) {
        super(activity);
    }

    private ExitActionDialog(Builder builder) {
        super(builder.mActivity);
        mBuilder = builder;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_action_exit, null);
        mIvClose = (ImageView) view.findViewById(R.id.id_dialog_action_exit_iv_close);
        mBtnExit = (Button) view.findViewById(R.id.id_dialog_action_exit_btn_exit);
        mBtnDelay = (Button) view.findViewById(R.id.id_dialog_action_exit_btn_delay);

        if (mBuilder == null) {
            return view;
        }

        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.mExitListener != null) {
                    mBuilder.mExitListener.onClick(ExitActionDialog.this);
                } else {
                    hide();
                }
            }
        });
        mBtnDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.mDelayListener != null) {
                    mBuilder.mDelayListener.onClick(ExitActionDialog.this);
                } else {
                    hide();
                }

            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.mCloseListener != null) {
                    mBuilder.mCloseListener.onClick(ExitActionDialog.this);
                } else {
                    hide();
                }

            }
        });
        return view;
    }


    public static class Builder {
        private Activity mActivity;
        private ClickListener mExitListener;
        private ClickListener mDelayListener;
        private ClickListener mCloseListener;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setExitListener(ClickListener exitListener) {
            mExitListener = exitListener;
            return this;
        }

        public Builder setDelayListener(ClickListener delayListener) {
            mDelayListener = delayListener;
            return this;
        }

        public Builder setCloseListener(ClickListener closeListener) {
            mCloseListener = closeListener;
            return this;
        }

        public ExitActionDialog build() {
            return new ExitActionDialog(Builder.this);
        }
    }

    public interface ClickListener {
        void onClick(BaseDialog dialog);
    }
}
