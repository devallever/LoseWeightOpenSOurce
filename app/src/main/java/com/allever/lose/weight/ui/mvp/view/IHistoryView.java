package com.allever.lose.weight.ui.mvp.view;

import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.bean.ExerciseRecordItem;

import java.util.List;

/**
 * Created by Mac on 18/3/21.
 */

public interface IHistoryView {
    void setRecordList(List<ExerciseRecordItem> exerciseRecordItemList);

    void setCalendar(List<Person.ExerciseRecord> exerciseRecordList);
}
