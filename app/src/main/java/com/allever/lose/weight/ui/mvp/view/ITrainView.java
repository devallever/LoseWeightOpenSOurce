package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.bean.DayInfoBean;

import java.util.List;

/**
 * Created by Mac on 18/3/16.
 */

public interface ITrainView {
    void setDayLeft(int leftDay, int progress, int total, int percent);

    void setTrainList(List<DayInfoBean> dayInfoBeanList);

    void refreshView();
}
