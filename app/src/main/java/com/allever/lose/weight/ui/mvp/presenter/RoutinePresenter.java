package com.allever.lose.weight.ui.mvp.presenter;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.IRoutineView;
import com.allever.lose.weight.bean.RoutineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/27.
 */

public class RoutinePresenter extends BasePresenter<IRoutineView> {
    private DataSource mDataSource = Repository.getInstance();


    public void getRoutineDataList() {
        List<RoutineItem> routineItemList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        mDataSource = Repository.getInstance();
        idList = mDataSource.getRoutineIdList();
        RoutineItem routineItem;
        for (Integer dayId : idList) {
            routineItem = new RoutineItem();
            switch (dayId) {
                case RoutineItem.ID_MORIING:
                    routineItem.setBgResId(R.drawable.cover_morning);
                    routineItem.setSmallResId(R.drawable.ic_morning);
                    routineItem.setTitle(MyApplication.getContext().getResources().getString(R.string.morning_stretch));
                    break;
                case RoutineItem.ID_SLEEP:
                    routineItem.setBgResId(R.drawable.cover_sleep);
                    routineItem.setSmallResId(R.drawable.ic_sleep);
                    routineItem.setTitle(MyApplication.getContext().getResources().getString(R.string.sleep_stretch));
                    break;
                default:
                    break;
            }
            routineItem.setCount("- " + mDataSource.getLevelCount(dayId) + " " + MyApplication.getContext().getResources().getString(R.string.routine_exercise));
            routineItem.setDuration("- " + mDataSource.getLevelDuration(dayId) + " " + MyApplication.getContext().getResources().getString(R.string.routine_duration));
            routineItem.setKcal("- " + mDataSource.getLevelKcal(dayId) + " " + MyApplication.getContext().getResources().getString(R.string.routine_kcal));
            routineItem.setId(dayId);
            routineItemList.add(routineItem);
        }

        mViewRef.get().setRoutineList(routineItemList);
    }
}
