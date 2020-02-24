package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.util.CalculationUtil;

/**
 * Created by Mac on 18/3/7.
 */

public class HeightWeightDialog extends BaseDialog {
    private static final String TAG = "HeightWeightDialog";
    private static final String DEFAULT_VALUE = "0";


    private Builder mBuilder;
    private RadioGroup mRgWeight;
    private RadioGroup mRgHeight;

    private RadioButton mRbKg;
    private RadioButton mRbLb;
    private RadioButton mRbCm;
    private RadioButton mRbIn;

    private EditText mEtWeight;
    private EditText mEtFt;
    private EditText mEtIn;
    private EditText mEtCm;
    private LinearLayout mLlEtHeightContainer;


    private static String mEtTextWeight = "0";
    private static String mEtTextCm = "0";
    private static String mEtTextIn = "0";
    private static String mEtTextFt = "0";

    private DataSource mDataSource = Repository.getInstance();


    private HeightWeightDialog(Activity activity) {
        super(activity);
    }

    private HeightWeightDialog(Builder builder) {
        super(builder.mActivity);
        mBuilder = builder;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_height_weight, null, false);

        mLlEtHeightContainer = (LinearLayout) view.findViewById(R.id.id_dialog_h_w_ll_et_in_container);

        mEtWeight = (EditText) view.findViewById(R.id.id_dialog_h_w_et_weight);
        mEtCm = (EditText) view.findViewById(R.id.id_dialog_h_w_et_height_cm);
        mEtFt = (EditText) view.findViewById(R.id.id_dialog_h_w_et_height_in_ft);
        mEtIn = (EditText) view.findViewById(R.id.id_dialog_h_w_et_height_in_in);

        mRbKg = (RadioButton) view.findViewById(R.id.id_dialog_h_w_rb_unit_kg);
        mRbLb = (RadioButton) view.findViewById(R.id.id_dialog_h_w_rb_unit_lb);
        mRbCm = (RadioButton) view.findViewById(R.id.id_dialog_h_w_rb_unit_cm);
        mRbIn = (RadioButton) view.findViewById(R.id.id_dialog_h_w_rb_unit_in);

        //数据库存储的体重值默认单位是kg， 也存储了体重单位，与体重值没有直接关系,
        mEtTextWeight = String.valueOf(GlobalData.person.getmCurWeight());
        if (GlobalData.person.getmWeightUnit().equalsIgnoreCase(Constant.UNIT_WEIGHT_KG)) {
            mRbKg.setChecked(true);
        } else {
            mRbLb.setChecked(true);
            mEtTextWeight = String.valueOf(CalculationUtil.kg2Lb(Float.valueOf(mEtTextWeight)));
        }
        mEtWeight.setText(mEtTextWeight);

        //cm值
        double heightValueCM = GlobalData.person.getmHeight();
        mEtTextCm = String.valueOf(heightValueCM);
        mEtCm.setText(mEtTextCm);
        //cm->ft.in
        float[] ftInValue = CalculationUtil.cm2ft_in(Float.valueOf(String.valueOf(heightValueCM)));
        mEtTextFt = String.valueOf(ftInValue[0]);
        mEtTextIn = String.valueOf(ftInValue[1]);
        mEtFt.setText(mEtTextFt);
        mEtIn.setText(mEtTextIn);
        if (GlobalData.person.getmHeightUnit().equalsIgnoreCase(Constant.UNIT_HEIGHT_CM)) {
            mRbCm.setChecked(true);
            mEtCm.setVisibility(View.VISIBLE);
            mLlEtHeightContainer.setVisibility(View.INVISIBLE);
            //CM
        } else {
            mRbIn.setChecked(true);
            mEtCm.setVisibility(View.INVISIBLE);
            mLlEtHeightContainer.setVisibility(View.VISIBLE);
        }


        TextView tvOk = (TextView) view.findViewById(R.id.id_dialog_h_w_tv_save);
        if (mBuilder.okText != null) {
            tvOk.setText(mBuilder.okText);
        }

        TextView tvCancel = (TextView) view.findViewById(R.id.id_dialog_h_w_tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        mRgWeight = (RadioGroup) view.findViewById(R.id.id_dialog_h_w_rg_weight_unit_container);
        mRgWeight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mEtTextWeight = mEtWeight.getText().toString();
                if (mEtTextWeight.isEmpty()) {
                    mEtTextWeight = "0";
                }
                switch (checkedId) {
                    case R.id.id_dialog_h_w_rb_unit_kg:
                        GlobalData.person.setmWeightUnit(Constant.UNIT_WEIGHT_KG);
                        //数值转换->更新显示
                        mEtTextWeight = String.valueOf(CalculationUtil.lb2Kg(Float.valueOf(mEtTextWeight)));
                        mEtWeight.setText(mEtTextWeight);
                        break;
                    case R.id.id_dialog_h_w_rb_unit_lb:
                        GlobalData.person.setmWeightUnit(Constant.UNIT_WEIGHT_LB);
                        //数值转换->更新显示
                        mEtTextWeight = String.valueOf(CalculationUtil.kg2Lb(Float.valueOf(mEtTextWeight)));
                        mEtWeight.setText(mEtTextWeight);
                        break;
                    default:
                        break;
                }
                mDataSource.updatePersonInfo();
            }
        });

        mRgHeight = (RadioGroup) view.findViewById(R.id.id_dialog_h_w_rg_height_unit_container);
        mRgHeight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_dialog_h_w_rb_unit_cm:
                        GlobalData.person.setmHeightUnit(Constant.UNIT_HEIGHT_CM);
                        mLlEtHeightContainer.setVisibility(View.INVISIBLE);
                        mEtCm.setVisibility(View.VISIBLE);

                        mEtTextFt = mEtFt.getText().toString();
                        mEtTextIn = mEtIn.getText().toString();
                        if (mEtTextFt.isEmpty()) mEtTextFt = DEFAULT_VALUE;
                        if (mEtTextIn.isEmpty()) mEtTextIn = DEFAULT_VALUE;
                        float cm = CalculationUtil.ft_in2cm(Float.valueOf(mEtTextFt), Float.valueOf(mEtTextIn));
                        mEtCm.setText(String.valueOf(String.valueOf(cm)));

                        break;
                    case R.id.id_dialog_h_w_rb_unit_in:
                        GlobalData.person.setmHeightUnit(Constant.UNIT_HEIGHT_IN);
                        mLlEtHeightContainer.setVisibility(View.VISIBLE);
                        mEtCm.setVisibility(View.INVISIBLE);

                        mEtTextCm = mEtCm.getText().toString();
                        if (mEtTextCm.isEmpty()) mEtTextCm = DEFAULT_VALUE;
                        float[] value = CalculationUtil.cm2ft_in(Float.valueOf(mEtTextCm));
                        mEtFt.setText(String.valueOf(value[0]));
                        mEtIn.setText(String.valueOf(value[1]));
                        break;
                    default:
                        break;
                }
                mDataSource.updatePersonInfo();
            }
        });


        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.whdataListener != null) {
                    mEtTextWeight = mEtWeight.getText().toString();
                    if (mEtTextWeight.isEmpty()) {
                        mEtTextWeight = DEFAULT_VALUE;
                    }
                    Log.d(TAG, "onClick: weight unit  = " + GlobalData.person.getmWeightUnit());
                    if (GlobalData.person.getmWeightUnit().equalsIgnoreCase(Constant.UNIT_WEIGHT_LB)) {
                        mEtTextWeight = String.valueOf(CalculationUtil.lb2Kg(Float.valueOf(mEtWeight.getText().toString())));
                    } else {

                    }

                    mEtTextCm = mEtCm.getText().toString();
                    if (mEtTextCm.isEmpty()) {
                        //mEtTextCm = DEFAULT_VALUE;
                        mEtTextFt = mEtFt.getText().toString();
                        mEtTextIn = mEtIn.getText().toString();
                        if (mEtTextFt.isEmpty() && mEtTextIn.isEmpty()) {
                            mEtTextCm = DEFAULT_VALUE;
                        } else {
                            if (mEtTextFt.isEmpty()) mEtTextFt = DEFAULT_VALUE;
                            if (mEtTextIn.isEmpty()) mEtTextIn = DEFAULT_VALUE;
                            mEtTextCm = String.valueOf(CalculationUtil.ft_in2cm(Float.valueOf(mEtTextFt), Float.valueOf(mEtTextIn)));
                        }
                    }


                    if (mBuilder.okListener != null) {
                        float weight = Float.valueOf(mEtTextWeight);
                        float height = Float.valueOf(mEtTextCm);
                        if (weight < 0) {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.weight_not_allow), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (height < 0) {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.height_not_allow), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mBuilder.whdataListener.onWHDataOptain(weight, height);
                        mBuilder.okListener.onClick(HeightWeightDialog.this);
                    } else {
                        hide();
                    }
                }
            }
        });


        return view;
    }


    @Override
    public void show(boolean cancelable) {
        super.show(cancelable);
    }

    public static class Builder {
        private Activity mActivity;
        private String okText;
        private ClickListener okListener;
        private IWHDataListener whdataListener;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setOkBtn(String name, ClickListener okClick) {
            okText = name;
            okListener = okClick;
            return this;
        }

        public Builder setDataListener(IWHDataListener iDataListner) {
            whdataListener = iDataListner;
            return this;
        }

        public HeightWeightDialog build() {
            return new HeightWeightDialog(this);
        }

    }

    public interface IWHDataListener {
        void onWHDataOptain(float weight, float height);
    }

}
