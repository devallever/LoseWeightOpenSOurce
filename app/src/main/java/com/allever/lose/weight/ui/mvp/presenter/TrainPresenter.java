package com.allever.lose.weight.ui.mvp.presenter;

import android.util.Log;

import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.ui.mvp.view.ITrainView;
import com.allever.lose.weight.bean.DayInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/16.
 */

public class TrainPresenter extends BasePresenter<ITrainView> {
    private static final String TAG = "TrainPresenter";
    private DataSource mDataSource;

    public TrainPresenter() {
        mDataSource = Repository.getInstance();
    }

    private String[] mDayTitles;

    public void getLeftDay() {
        int total = mDataSource.getTrainLevelTotalCount();
        int trainedCount = mDataSource.getTotalTrainedCount();
        int leftDay = mDataSource.getLeftDay();
        int percent = Math.round(((trainedCount / (float) total) * 100));
        mViewRef.get().setDayLeft(leftDay, trainedCount, total, percent);
    }

    public void getDayList() {
        List<DayInfoBean> dayInfoBeanList = new ArrayList<>();
        List<Integer> dayIdList = mDataSource.getDayIdList();
        int lastTrainDayId = mDataSource.getCurrentDay();
        DayInfoBean dayInfoBean;
        mDayTitles = GlobalData.dayTitles;
        for (int dayId : dayIdList) {
            dayInfoBean = new DayInfoBean();
            dayInfoBean.setType(dayId);
            dayInfoBean.setCurrentDay(lastTrainDayId == dayId);
            dayInfoBean.setLevelCount(mDataSource.getLevelCount(dayId));
            dayInfoBean.setTrainedCount(mDataSource.getLevelTrainedCount(dayId));

            if (dayId - 1 >= 0 && dayId - 1 < mDayTitles.length) {
                dayInfoBean.setTitle(mDayTitles[dayId - 1]);
            } else {
                dayInfoBean.setTitle(String.valueOf(dayId));
            }

            Log.d(TAG, "getDayList: day :" + dayId + " " + mDataSource.isFinishTrain(dayId));
            dayInfoBean.setFinish(mDataSource.isFinishTrain(dayId));

            dayInfoBeanList.add(dayInfoBean);
        }
        mViewRef.get().setTrainList(dayInfoBeanList);
    }
}
