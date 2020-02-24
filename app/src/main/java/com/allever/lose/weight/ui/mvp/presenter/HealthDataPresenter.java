package com.allever.lose.weight.ui.mvp.presenter;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IHealthDataView;

/**
 * Created by Mac on 18/3/13.
 */

public class HealthDataPresenter extends BasePresenter<IHealthDataView> {
    private DataSource mDataSource = Repository.getInstance();

    public HealthDataPresenter() {
    }

    public void getGender() {
        int gender = mDataSource.getGender();
        if (gender == 0) {
            mViewRef.get().setGender(MyApplication.getContext().getResources().getString(R.string.male));
        } else {
            mViewRef.get().setGender(MyApplication.getContext().getResources().getString(R.string.female));
        }
    }

    public void getBirthday() {
        String birthday = mDataSource.getBirthday();
        mViewRef.get().setBirthday(birthday);
    }


    public void updateGender(int gender) {
        ;
        GlobalData.person.setmGender(gender);
        mDataSource.updatePersonInfo();
    }

    public void updateBirthday(int year, int month, int dayOfMonth) {
        GlobalData.person.setmYear(year);
        GlobalData.person.setmMonth(month);
        GlobalData.person.setmDay(dayOfMonth);
        mDataSource.updatePersonInfo();
    }
}
