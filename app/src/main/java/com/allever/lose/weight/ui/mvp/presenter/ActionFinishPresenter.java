package com.allever.lose.weight.ui.mvp.presenter;


import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IActionFinishView;
import com.allever.lose.weight.util.CalculationUtil;

/**
 * Created by Mac on 18/3/2.
 */

public class ActionFinishPresenter extends BasePresenter<IActionFinishView> {
    private static final String TAG = "ActionFinishPresenter";
    private DataSource mDataSource = Repository.getInstance();


    public void calKg2Lb(float kgValue) {
        float lbValue = CalculationUtil.kg2Lb(kgValue);
        mViewRef.get().updataWeightEditText(lbValue);
    }

    public void calLb2Kg(float lbValue) {
        float kgValue = CalculationUtil.lb2Kg(lbValue);
        mViewRef.get().updataWeightEditText(kgValue);
    }

    public void getTrainCount() {
        int count = mDataSource.getExerciseCount();
        mViewRef.get().setExerciseCount(count);
    }

    public void getCalorie(int type) {
        int calorie = mDataSource.getCalories(type);
        //如果 calorie != -1
        mViewRef.get().setCalorie(calorie);
    }

    public void getBmi() {
        double weight = mDataSource.getWeight();
        double height = mDataSource.getHeight();
        //如果 weight height 都存在且！= 0
        mViewRef.get().setbBmi((float) weight, (float) (height));
    }

    public void getCurrentDay() {
        int dayId = mDataSource.getCurrentDay();
        String title;
        if (dayId - 1 >= 0 && dayId - 1 < GlobalData.dayTitles.length) {
            title = GlobalData.dayTitles[dayId - 1] + MyApplication.getContext().getResources().getString(R.string.finish);
        } else {
            title = dayId + MyApplication.getContext().getResources().getString(R.string.finish);
        }
        mViewRef.get().setDayFinishTitle(title);
    }

    public void addReminder(int hour) {
        Config.Reminder reminder = new Config.Reminder();
        reminder.setHour(hour);
        reminder.setMinute(0);
        reminder.setSecond(0);
        reminder.setRemindSwitch(true);
        reminder.setMonRepeat(true);
        reminder.setTueRepeat(true);
        reminder.setWebRepeat(true);
        reminder.setThurRepeat(true);
        reminder.setFriRepeat(true);
        reminder.setSatRepeat(true);
        reminder.setSunRepeat(true);
        mDataSource.addReminder(reminder);
        GlobalData.reminderList.add(reminder);
    }

    public void setWHData(float weight, float height) {
        GlobalData.person.setmCurWeight(weight);
        GlobalData.person.setmHeight(height);
        mDataSource.updatePersonInfo();
    }

    public void updateYear(int year) {
        GlobalData.person.setmYear(year);
        mDataSource.updatePersonInfo();
    }

    public void updateGender(int gender) {
        GlobalData.person.setmGender(gender);
        mDataSource.updatePersonInfo();
    }

    public void getDuration() {
        String duration = mDataSource.getCurrentExerciseDuration();
        mViewRef.get().setCurrentExerciseDuration(duration);
    }

    public void getWeight() {
        double weight = mDataSource.getCurrentWeight();
        mViewRef.get().setWeight(weight);
    }

    public void saveWeight(double weight) {
        GlobalData.person.setmCurWeight(weight);
        mDataSource.updatePersonInfo();
    }
}
