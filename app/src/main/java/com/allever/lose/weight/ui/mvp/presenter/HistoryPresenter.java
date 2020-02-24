package com.allever.lose.weight.ui.mvp.presenter;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IHistoryView;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.bean.ExerciseRecordItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/21.
 */

public class HistoryPresenter extends BasePresenter<IHistoryView> {
    private DataSource mDataSource = Repository.getInstance();

    public void getRecordList() {
        List<ExerciseRecordItem> exerciseRecordItemList = new ArrayList<>();
        List<Person.ExerciseRecord> exerciseRecordList = mDataSource.getAllExerciseRecord();
        for (Person.ExerciseRecord exerciseRecord : exerciseRecordList) {
            ExerciseRecordItem exerciseRecordItem = new ExerciseRecordItem();

            exerciseRecordItem.setDayId(exerciseRecord.getType());
            int dayId = exerciseRecord.getType();
            //30天情况
            if (dayId - 1 >= 0 && dayId - 1 < GlobalData.dayTitles.length) {
                exerciseRecordItem.setName(GlobalData.dayTitles[dayId - 1]);
            } else {
                exerciseRecordItem.setName(String.valueOf(exerciseRecordItem.getDayId()));
            }
            //日常训练
            switch (dayId) {
                case 31:
                    exerciseRecordItem.setName(MyApplication.getContext().getResources().getString(R.string.morning_stretch));
                    break;
                case 32:
                    exerciseRecordItem.setName(MyApplication.getContext().getResources().getString(R.string.sleep_stretch));
                    break;
                default:
                    break;
            }

            exerciseRecordItem.setDate(DateUtil.formatTime(exerciseRecord.getStartTime(), DateUtil.FORMAT_MM_dd));
            long duration = exerciseRecord.getEndTime() - exerciseRecord.getStartTime() - exerciseRecord.getPauseDuration();
            exerciseRecordItem.setDuration(DateUtil.second2FormatMinute((int) duration / 1000));
            exerciseRecordItem.setStartTime(DateUtil.formatTime(exerciseRecord.getStartTime(), DateUtil.FORMAT_HH_mm));

            exerciseRecordItemList.add(exerciseRecordItem);
        }

        mViewRef.get().setRecordList(exerciseRecordItemList);
    }

    public void getCalendarData() {
        mViewRef.get().setCalendar(mDataSource.getAllExerciseRecord());
    }
}
