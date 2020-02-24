package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.bean.ActionItem;

/**
 * Created by Mac on 18/3/1.
 */

public interface IActionReadyView {
    void setData(ActionItem actionItem);

    void updateLeftTimeText(String time);

    void setCurrentActionId(int currentActionId);
}
