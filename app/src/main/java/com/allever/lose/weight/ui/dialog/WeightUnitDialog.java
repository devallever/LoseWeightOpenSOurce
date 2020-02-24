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

public class WeightUnitDialog extends BaseDialog {
    private IWeightUnitListener mListener;

    public WeightUnitDialog(Activity activity, IWeightUnitListener genderListener) {
        super(activity);
        mListener = genderListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_weight_unit, null, false);

        RadioButton btnLb = (RadioButton) view.findViewById(R.id.id_dialog_weight_unit_rb_lb);
        RadioButton btnKg = (RadioButton) view.findViewById(R.id.id_dialog_weight_unit_rb_kg);

        btnLb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainWeightUnit(mActivity.getResources().getString(R.string.unit_lb));
                }
                hide();
            }
        });

        btnKg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainWeightUnit(mActivity.getResources().getString(R.string.unit_kg));
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

    public interface IWeightUnitListener {
        void onObtainWeightUnit(String weightUnit);
    }
}
