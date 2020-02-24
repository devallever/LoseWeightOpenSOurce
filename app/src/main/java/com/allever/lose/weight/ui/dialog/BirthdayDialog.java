package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.ui.view.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mac on 18/3/13.
 */

public class BirthdayDialog extends BaseDialog {
    private static final String TAG = "BirthdayDialog";

    private WheelView mWVYear;
    private WheelView mWVMonth;
    private WheelView mWVDay;
    private int mYear;
    private int mMonth;
    private int mDay;

    private IBirthdayListener mListener;

    public BirthdayDialog(Activity activity, IBirthdayListener birthdayListener) {
        super(activity);
        mListener = birthdayListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_birthday, null, false);

        mWVYear = (WheelView) view.findViewById(R.id.id_dialog_birthday_wv_year);
        mWVMonth = (WheelView) view.findViewById(R.id.id_dialog_birthday_wv_month);
        mWVDay = (WheelView) view.findViewById(R.id.id_dialog_birthday_wv_day);

        initYear();
        initMonth();
        initDay();

        TextView tvOk = (TextView) view.findViewById(R.id.id_dialog_birthday_tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainBirthday(mYear, mMonth, mDay);
                }
                hide();
            }
        });
        TextView tvCancel = (TextView) view.findViewById(R.id.id_dialog_birthday_tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        return view;
    }

    private void initDay() {
        mWVDay.setOffset(1);
        mWVDay.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mDay = selectedIndex;
                Log.d(TAG, "onSelected: day = " + mDay);
            }
        });
        mWVDay.setItems(getDay());
        mWVDay.setSeletion(0);
        mDay = 1;
    }

    private void initMonth() {
        mWVMonth.setOffset(1);
        mWVMonth.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mMonth = selectedIndex;
                Log.d(TAG, "onSelected: month = " + mMonth);
                //mWVDay.setItems(getDay());
            }
        });
        mWVMonth.setItems(getMonthData());

        mWVMonth.setSeletion(0);
        mMonth = 1;
    }

    private void initYear() {
        mWVYear.setOffset(1);
        mWVYear.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mYear = Integer.valueOf(item);
                Log.d(TAG, "onSelected: year = " + mYear);
            }
        });
        List<String> yearList = getYearData();
        mWVYear.setItems(yearList);
        mWVYear.setSeletion(yearList.size() - 1);
        mYear = Integer.valueOf(yearList.get(yearList.size() - 1));
    }

    private List<String> getYearData() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = year - 200; i <= year; i++) {
            list.add(i + "");
        }
        return list;
    }

    private List<String> getMonthData() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    private List<String> getDay() {
        int dayCount = 30;
        switch (mMonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayCount = 30;
                break;
            case 2:
                if (DateUtil.isLeapYear(mYear)) {
                    dayCount = 29;
                } else {
                    dayCount = 28;
                }
                break;
            default:
                break;

        }
        Log.d(TAG, "getDay: dayCount =  " + dayCount);
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= dayCount; i++) {
            list.add(i + "");
        }
        return list;
    }


    public interface IBirthdayListener {
        void onObtainBirthday(int year, int month, int day);
    }

}
