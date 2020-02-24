package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.data.Config;

import java.util.List;

/**
 * Created by Mac on 18/3/20.
 */

public interface IReminderView {
    void updateRemindList(List<Config.Reminder> reminderList);
}
