package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.bean.ActionItem;

/**
 * Created by Mac on 18/3/1.
 */

public interface IActionPauseView {
    void setData(ActionItem actionItem);

    void setLeftTime(String time);

    void setCurrent(int currentLevel, int levelCount);
}
