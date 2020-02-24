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

public class GenderDialog extends BaseDialog {
    private IGenderListener mListener;

    public GenderDialog(Activity activity, IGenderListener genderListener) {
        super(activity);
        mListener = genderListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_gender, null, false);

        RadioButton btnMale = (RadioButton) view.findViewById(R.id.id_dialog_gender_rb_male);
        RadioButton btnFemale = (RadioButton) view.findViewById(R.id.id_dialog_gender_rb_female);
        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainGender(0, mActivity.getResources().getString(R.string.female));
                }
                hide();
            }
        });

        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainGender(1, mActivity.getResources().getString(R.string.female));
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

    public interface IGenderListener {
        void onObtainGender(int gender, String text);
    }
}
