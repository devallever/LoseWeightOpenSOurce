package com.allever.lose.weight.ui.mvp.view;

/**
 * Created by Mac on 18/3/13.
 */

public interface IHealthDataView {
    void setGender(String gender);

    void setBirthday(String birthday);

    void showGenderDialog();

    void showBirthdayDialog();

    void hideGenderDialog();

    void hideBirthdayDialog();

}
