package com.allever.lose.weight.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mac on 2018/3/7.
 */

public class WeightFragment extends SupportFragment implements CalendarView.OnDateSelectedListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    Unbinder unbinder;
    @BindView(R.id.current_date)
    TextView currentDate;
    @BindView(R.id.weight)
    LinearLayout weight;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.edit_weight)
    EditText editWeight;
    private int fetureColor;
    private DataSource mDataSource;
    private int mDay;
    private int mMonth;
    private int mYear;
    private static IWeightRecordListener mListener;

    public static WeightFragment newInstance(IWeightRecordListener weightRecordListener) {
        Bundle args = new Bundle();
        WeightFragment fragment = new WeightFragment();
        fragment.setArguments(args);
        mListener = weightRecordListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        unbinder = ButterKnife.bind(this, view);
        fetureColor = Color.parseColor("#d0d0d0");
        mDataSource = Repository.getInstance();
        setDate();
        return view;
    }

    private void setDate() {
        currentDate.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mCalendarView.setOnDateSelectedListener(this);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        if (calendar.getDay() > mCalendarView.getCurDay()) {
            mCalendarView.setTextColor(fetureColor, fetureColor, fetureColor, fetureColor, fetureColor);
        }
        editWeight.setText(String.valueOf(mDataSource.getHistoryWeight(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay())));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        //TODO 格式化日期
        currentDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mDay = calendar.getDay();
        mMonth = calendar.getMonth();
        mYear = calendar.getYear();
        editWeight.setText(String.valueOf(mDataSource.getHistoryWeight(mYear, mMonth, mDay)));
    }


    @OnClick({R.id.cancel, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                pop();
                break;
            case R.id.save:
                if (mListener != null) {
                    String valueStr = editWeight.getText().toString();
                    if (valueStr.isEmpty()) valueStr = "0";
                    double weight = Double.valueOf(valueStr);
                    if (weight < 0) {
                        Toast.makeText(_mActivity, _mActivity.getResources().getString(R.string.weight_not_allow), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mListener.onSaveClick(weight, mYear, mMonth, mDay);
                    pop();
                }

                break;
            default:
                break;
        }
    }

    public interface IWeightRecordListener {
        void onSaveClick(double weight, int year, int month, int day);
    }
}
