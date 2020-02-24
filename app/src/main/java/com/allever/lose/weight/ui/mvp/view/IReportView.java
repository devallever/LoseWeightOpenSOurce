package com.allever.lose.weight.ui.mvp.view;

import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.List;

/**
 * Created by Mac on 18/3/16.
 */

public interface IReportView {
    void setWorkout(int workout);

    void setKcal(int kcal);

    void setDuration(String duration);

    void setCurrentWeight(double currentWeight, String unit);

    void setHeaviestWeight(double heaviestWeight, String unit);

    void setLightestWeight(double lightestWeight, String unit);

    void setBMI(int gender, float currentWeight, float height);

    void setHeight(double height, String unit);

    void refreshView();

    void setChartData(List<Entry> entryList, Date startDate, Date endDate);

    void showSyncDialog();

    void hideSyncDialog();

    void setSync(String account, String time);


}
