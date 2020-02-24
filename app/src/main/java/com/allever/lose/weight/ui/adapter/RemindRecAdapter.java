package com.allever.lose.weight.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.util.DateUtil;

import java.util.List;

/**
 * Created by Mac on 18/3/20.
 */

public class RemindRecAdapter extends BaseQuickAdapter<Config.Reminder, BaseViewHolder> {
    public RemindRecAdapter(List<Config.Reminder> reminderList) {
        super(R.layout.fragment_reminder_item, reminderList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Config.Reminder item) {
        if (item == null) {
            return;
        }
        String remindTime = DateUtil.formatHourMinute(item.getHour(), item.getMinute());
        holder.setText(R.id.tv_time, remindTime);
        holder.setChecked(R.id.reminder_switch, item.isRemindSwitch());
        StringBuilder repeatStr = new StringBuilder();
        if (item.isMonRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.monday) + " ");
        }
        if (item.isTueRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.tuesday) + " ");
        }
        if (item.isWebRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.wednesday) + " ");
        }
        if (item.isThurRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.thursday) + " ");
        }
        if (item.isFriRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.friday) + " ");
        }
        if (item.isSatRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.saturday) + " ");
        }
        if (item.isSunRepeat()) {
            repeatStr.append(mContext.getResources().getString(R.string.sunday) + " ");
        }
        holder.setText(R.id.tv_weekly, repeatStr.toString());
        holder.addOnClickListener(R.id.reminder_switch);
        holder.addOnClickListener(R.id.delete);
        holder.addOnClickListener(R.id.id_item_remind_tv_repeat);

    }
}
