package com.allever.lose.weight.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

/**
 * Created by Mac on 2018/3/6.
 */

public class TimePickerFragment extends DialogFragment {
    private String time = "";

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //新建日历类用于获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
    }

    public void show(FragmentManager fragmentManager, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
        show(fragmentManager, "TimePickerFragment");
    }

    public void setTime(String date) {
        time += date;
    }
}
