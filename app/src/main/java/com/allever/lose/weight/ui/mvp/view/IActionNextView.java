package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.bean.ActionItem;

/**
 * Created by Mac on 18/3/1.
 */

public interface IActionNextView {
    void setData(ActionItem actionItem);

    void setCurrent(int currentLevel, int levelCount);

    void setCurrentActionId(int actionId);
}
