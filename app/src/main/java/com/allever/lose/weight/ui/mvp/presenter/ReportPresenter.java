package com.allever.lose.weight.ui.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.ui.mvp.view.IReportView;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.util.SyncGoogle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mac on 18/3/16.
 */

public class ReportPresenter extends BasePresenter<IReportView> {
    private static final String TAG = "ReportPresenter";
    private DataSource mDataSource;

    public ReportPresenter() {
        mDataSource = Repository.getInstance();
    }

    public void setWorkout() {
        int workout = mDataSource.getExerciseCount();
        mViewRef.get().setWorkout(workout);
    }

    public void getKcal() {
        int kcal = mDataSource.getAllKcal();
        mViewRef.get().setKcal(kcal);
    }

    public void getDuration() {
        String duration = mDataSource.getDurationStr();
        mViewRef.get().setDuration(duration);
    }

    public void getWeight() {
        double currentWeight = mDataSource.getCurrentWeight();
        mViewRef.get().setCurrentWeight(currentWeight, Constant.UNIT_WEIGHT_KG);

        double heavistWeight = mDataSource.getHeaviest();
        mViewRef.get().setHeaviestWeight(heavistWeight, Constant.UNIT_WEIGHT_KG);

        double lightestWeight = mDataSource.getLightest();
        mViewRef.get().setLightestWeight(lightestWeight, Constant.UNIT_WEIGHT_KG);
    }

    public void getHeight() {
        double height = mDataSource.getHeight();
        mViewRef.get().setHeight(height, Constant.UNIT_HEIGHT_CM);
    }

    public void updateWeightHeight(float weight, float height) {
        GlobalData.person.setmCurWeight(weight);
        GlobalData.person.setmHeight(height);
        mDataSource.updatePersonInfo();
    }

    public void getBMI() {
        float height = (float) mDataSource.getHeight() / 100f;
        float weight = (float) mDataSource.getWeight();
        int gender = mDataSource.getGender();
        mViewRef.get().setBMI(gender, weight, height);
    }

    public void getChartData() {
        List<Entry> values = new ArrayList<>();
        List<Person.WeightRecord> weightRecordList = mDataSource.getWeightRecordList();
        if (weightRecordList.size() > 0) {
            final Person.WeightRecord firstRecord = weightRecordList.get(0);
            final Date startDate = new Date(firstRecord.getYear(), firstRecord.getMonth() - 1, firstRecord.getDay());
            Date endDate = new Date();
            for (Person.WeightRecord weightRecord : weightRecordList) {
                endDate = new Date(weightRecord.getYear(), weightRecord.getMonth() - 1, weightRecord.getDay());
                int x = DateUtil.getIntervalDays(startDate, endDate);
                values.add(new Entry((float) x, (float) weightRecord.getWeight()));
            }
            mViewRef.get().setChartData(values, startDate, endDate);

        }

    }

    public void connectGoogleFit(Fragment fragment, int requestCode) {
        Log.d(TAG, "syncGoogleFit: isSync = " + GlobalData.config.isSync());
        if (!GlobalData.config.isSync()) {
            SyncGoogle.getIns().connectGoogleFit(fragment, requestCode);
        } else {
            mViewRef.get().showSyncDialog();
        }
    }

    public void getSyncData() {
        if (GlobalData.config.isSync()) {
            String account = GlobalData.config.getAccount();
            long syncTime = GlobalData.config.getSyncTime();
            mViewRef.get().setSync(account, DateUtil.formatTime(syncTime, DateUtil.FORMAT_MM_dd_HH_mm));
        } else {
            //设置默认值
            mViewRef.get().setSync(MyApplication.getContext().getResources().getString(R.string.keep_data_in_cloud),
                    MyApplication.getContext().getResources().getString(R.string.never_backed_up));
        }
    }

    public void saveSyncState(boolean sync) {
        GlobalData.config.setSync(sync);
        mDataSource.updateConfig();
    }

    public void saveSync(boolean sync, long time) {
        GlobalData.config.setSync(sync);
        GlobalData.config.setSyncTime(time);
        GlobalData.config.setAccount(GlobalData.config.getAccount());
        mDataSource.updateConfig();
    }

    public void syncWeight(float weight, int year, int month, int day) {
        if (GlobalData.config.isSync()) {
            SyncGoogle.getIns().syncWeight(weight, year, month, day, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: sync google fit success");
                        //refresh report
                        saveSync(true, System.currentTimeMillis());
                    } else {
                        Log.d(TAG, "onComplete: sync google fit fail");
                        Log.e(TAG, "onComplete: ", task.getException());
                    }
                }
            });
        }
    }

    public void loginGoogle(Fragment fragment, int requestCode) {
        SyncGoogle.getIns().loginGoogle(fragment, requestCode);
    }

    public void saveSyncAccount(String email) {
        GlobalData.config.setAccount(email);
        mDataSource.updateConfig();
    }

    public void saveWeightRecord(double weight, int year, int month, int day) {
        mDataSource.saveWeightRecord(weight, year, month, day);
    }
}
