package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.BaseDialog;

/**
 * Created by Mac on 18/3/13.
 */

public class HeightUnitDialog extends BaseDialog {
    private IHeightUnitListener mListener;

    public HeightUnitDialog(Activity activity, IHeightUnitListener genderListener) {
        super(activity);
        mListener = genderListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_height_unit, null, false);

        RadioButton btnCm = (RadioButton) view.findViewById(R.id.id_dialog_height_unit_rb_cm);
        RadioButton btnIn = (RadioButton) view.findViewById(R.id.id_dialog_height_unit_rb_in);

        btnCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainHeightUnit(mActivity.getResources().getString(R.string.unit_cm));
                }
                hide();
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainHeightUnit(mActivity.getResources().getString(R.string.unit_in));
                }
                hide();
            }
        });
        return view;
    }

    @Override
    public void show(boolean cancelable) {
        super.show(cancelable);
    }

    public interface IHeightUnitListener {
        void onObtainHeightUnit(String heightUnit);
    }
}
