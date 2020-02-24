package com.allever.lose.weight.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.ReminderBean;

import java.util.List;


public class ReminderItemAdapter extends BaseQuickAdapter<ReminderBean, BaseViewHolder> {
    private String[] items = {"Sun", "Mon", "Tue", "Wen", "Tus", "Fri", "Sta"};

    public ReminderItemAdapter(int layoutResId, @Nullable List<ReminderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReminderBean item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_time, item.getHour() + ":" + item.getMinute());
        List<ReminderBean.Info> infoList = item.getInfo();
        StringBuilder week = new StringBuilder();
        for (ReminderBean.Info info : infoList) {
            week.append(" ").append(items[info.getDay()]);
        }
        helper.setText(R.id.tv_weekly, week);

        helper.addOnClickListener(R.id.reminder_switch);
        helper.addOnClickListener(R.id.delete);
    }
}
