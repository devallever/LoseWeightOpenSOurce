package com.allever.lose.weight.ui.mvp.presenter;

import android.content.Context;

import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IReminderView;
import com.allever.lose.weight.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/20.
 */

public class ReminderPresenter extends BasePresenter<IReminderView> {
    private DataSource mDataSource = Repository.getInstance();

    public ReminderPresenter() {
    }


    public void addReminder(Config.Reminder reminder) {
        if (reminder == null) {
            return;
        }
        mDataSource.addReminder(reminder);
        if (!GlobalData.reminderList.contains(reminder)) {
            GlobalData.reminderList.add(reminder);
        }
        mViewRef.get().updateRemindList(GlobalData.reminderList);
    }

    public void updateReminder(int position, Config.Reminder reminder) {
        GlobalData.reminderList.set(position, reminder);
        mDataSource.addReminder(reminder);
        mViewRef.get().updateRemindList(GlobalData.reminderList);
    }

    public void deleteReminder(Config.Reminder reminder) {
        mDataSource.deleteReminder(reminder);
        GlobalData.reminderList.remove(reminder);
        mViewRef.get().updateRemindList(GlobalData.reminderList);
    }

    public void setReminder(Context context, Config.Reminder reminder) {
        List<Integer> remindDayOfWeekList = getRemindDayOfWeekList(reminder);
        for (int day : remindDayOfWeekList) {
            int code = Integer.parseInt(String.valueOf(day) + String.valueOf(reminder.getHour()) + String.valueOf(reminder.getMinute()));
            TimeUtils.startRemind(context, reminder.getHour(), reminder.getMinute(), day, code);
        }
    }

    public void cancelReminder(Context context, Config.Reminder reminder) {
        List<Integer> remindDayOfWeekList = getRemindDayOfWeekList(reminder);
        for (int day : remindDayOfWeekList) {
            int requestCode = Integer.parseInt(String.valueOf(day) + String.valueOf(reminder.getHour()) + String.valueOf(reminder.getMinute()));
            TimeUtils.stopRemind(context, requestCode);
        }
    }

    private List<Integer> getRemindDayOfWeekList(Config.Reminder reminder) {
        List<Integer> remindDayOfWeekList = new ArrayList<>();
        if (reminder.isSunRepeat()) remindDayOfWeekList.add(7);
        if (reminder.isMonRepeat()) remindDayOfWeekList.add(1);
        if (reminder.isTueRepeat()) remindDayOfWeekList.add(2);
        if (reminder.isWebRepeat()) remindDayOfWeekList.add(3);
        if (reminder.isThurRepeat()) remindDayOfWeekList.add(4);
        if (reminder.isFriRepeat()) remindDayOfWeekList.add(5);
        if (reminder.isSatRepeat()) remindDayOfWeekList.add(6);
        return remindDayOfWeekList;
    }

    public List<Config.Reminder> getReminderList() {
        //mViewRef.get().setReminderList(GlobalData.reminderList);
        return GlobalData.reminderList;
    }
}
