package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.ui.view.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mac on 18/3/7.
 */

public class YearSelectDialog extends BaseDialog {
    private static final String TAG = "YearSelectDialog";
    private View mView;
    private View.OnClickListener mPreListener;
    private IYearDataListener mYearListener;
    private int mGender = 0;
    private int mYear;

    public YearSelectDialog(Activity activity, View.OnClickListener preListener, IYearDataListener iYearDataListener) {
        super(activity);
        mPreListener = preListener;
        mYearListener = iYearDataListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_year_select, null);
        WheelView wheelView = (WheelView) view.findViewById(R.id.id_dialog_year_select_wheel_view_year_select);
        wheelView.setOffset(1);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                mYear = Integer.valueOf(item);
                Log.d(TAG, "onSelected: year = " + mYear);
            }
        });
        List<String> yearList = getYearData();
        wheelView.setItems(yearList);
        wheelView.setSeletion(yearList.size() - 1);
        mYear = Integer.valueOf(yearList.get(yearList.size() - 1));

        RadioButton rbMale = (RadioButton) view.findViewById(R.id.id_dialog_year_select_rb_male);
        rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = 0;
            }
        });
        RadioButton rbFemale = (RadioButton) view.findViewById(R.id.id_dialog_year_select_rb_female);
        rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = 1;
            }
        });

        if (GlobalData.person.getmGender() == 0) {
            rbMale.setChecked(true);
            rbFemale.setChecked(false);
        } else {
            rbMale.setChecked(false);
            rbFemale.setChecked(true);
        }


        TextView tvCancel = (TextView) view.findViewById(R.id.id_dialog_year_select_tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        TextView tvPre = (TextView) view.findViewById(R.id.id_dialog_year_select_tv_previous);
        if (mPreListener != null) {
            tvPre.setOnClickListener(mPreListener);
        } else {
            hide();
        }

        TextView tvOk = (TextView) view.findViewById(R.id.id_dialog_year_select_tv_save);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYearListener != null) {
                    mYearListener.onOptainYearData(mYear, mGender);
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

    private ArrayList<String> getYearData() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = year - 200; i <= year; i++) {
            list.add(i + "");
        }
        return list;
    }

    public interface IYearDataListener {
        void onOptainYearData(int year, int gender);
    }
}
