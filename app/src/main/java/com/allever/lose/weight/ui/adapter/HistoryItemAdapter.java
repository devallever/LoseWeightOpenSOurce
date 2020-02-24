package com.allever.lose.weight.ui.adapter;

import androidx.annotation.Nullable;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.DateUtil;

import java.util.Calendar;
import java.util.List;


public class HistoryItemAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    private static final String TAG = "HistoryItemAdapter";

    private String[] items = {"S", "M", "T", "W", "T", "F", "S"};
    private int mDay;
    private int mWeek;
    private int mMonth;
    private int mYear;

    private DataSource mDataSource = Repository.getInstance();

    public HistoryItemAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mYear = mCalendar.get(Calendar.YEAR);
        //从0开始
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        if (item == null) {
            return;
        }
        //1,2,3....
        int position = helper.getAdapterPosition();
        int displayDay;
        //这个月的天数
        int dayCount = DateUtil.getMonthDayCount(mYear, mMonth);
        //

        if (position == mWeek) {
            Log.d(TAG, "convert: setColor");
            displayDay = mDay;
            helper.setTextColor(R.id.text_week_date, mContext.getResources().getColor(R.color.green_16));
        } else {
            helper.setTextColor(R.id.text_week_date, mContext.getResources().getColor(R.color.black));
            //核心公式
            displayDay = mDay + position - mWeek;

            //下面是处理特殊情况
            //显示的天数比当前月的总天数大
            if (displayDay > dayCount) {
                displayDay = displayDay - dayCount;
            }

            //显示天数小于1
            if (displayDay < 1) {
                //上一个月的天数
                int lastMonthDayCount;
                //特殊情况这个月是1月份,上一个月就是上一年的12月
                if (mMonth == 1) {
                    lastMonthDayCount = DateUtil.getMonthDayCount(mYear - 1, 12);
                } else {
                    lastMonthDayCount = DateUtil.getMonthDayCount(mYear, mMonth - 1);
                }
                displayDay = displayDay + lastMonthDayCount;
            }
        }
        helper.setText(R.id.text_week_date, String.valueOf(displayDay));

        if (position > mWeek) {
            helper.setImageResource(R.id.image_week_day, R.drawable.ic_calendar_future);
        } else {
            if (mDataSource.getIsWork(mYear, mMonth, displayDay)) {
                helper.setImageResource(R.id.image_week_day, R.drawable.ic_calendar_completed);
            } else {
                helper.setImageResource(R.id.image_week_day, R.drawable.ic_calendar_not_completed);
            }
        }

        Log.d(TAG, "convert: position = " + position);
        helper.setText(R.id.text_week_abbr, items[position - 1]);

    }
}
