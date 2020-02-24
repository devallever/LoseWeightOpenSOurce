package com.allever.lose.weight.ui.mvp.presenter;

import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IUnitSettingView;

/**
 * Created by Mac on 18/3/14.
 */

public class UnitSettingPresenter extends BasePresenter<IUnitSettingView> {
    private DataSource mDataSrouce = Repository.getInstance();

    public void getWeightUnit() {
        String weightUnit = mDataSrouce.getWeightUnit();
        mViewRef.get().setWeightUnit(weightUnit);
    }

    public void getHeightUnit() {
        String heightUnit = mDataSrouce.getHeightUnit();
        mViewRef.get().setHeightUnit(heightUnit);
    }

    public void updateWeightUnit(String weightUnit) {
        GlobalData.person.setmWeightUnit(weightUnit);
        mDataSrouce.updatePersonInfo();
    }

    public void updateHeightUnit(String heightUnit) {
        GlobalData.person.setmHeightUnit(heightUnit);
        mDataSrouce.updatePersonInfo();
    }
}
