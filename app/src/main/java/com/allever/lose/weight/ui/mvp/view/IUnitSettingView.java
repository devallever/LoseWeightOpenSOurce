package com.allever.lose.weight.ui.mvp.view;

/**
 * Created by Mac on 18/3/14.
 */

public interface IUnitSettingView {
    void setWeightUnit(String weightUnit);

    void setHeightUnit(String heightUnit);

    void showWeightSelectDialog();

    void showHeightSelectDialog();

    void hideWeightSelectDialog();

    void hideHeightSelectDialog();
}
