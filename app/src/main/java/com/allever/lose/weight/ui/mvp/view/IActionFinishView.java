package com.allever.lose.weight.ui.mvp.view;

/**
 * Created by Mac on 18/3/2.
 */

public interface IActionFinishView {
    void updataWeightEditText(float value);

    void setExerciseCount(int count);

    void setCalorie(int calorie);

    void setCurrentExerciseDuration(String duration);

    /**
     * @param weight 单位：kg
     * @param height 单位：m
     */
    void setbBmi(float weight, float height);

    void setDayFinish(int day);

    void setDayFinishTitle(String title);

    void setWeight(double weight);
}
