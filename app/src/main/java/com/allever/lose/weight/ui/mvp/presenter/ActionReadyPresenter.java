package com.allever.lose.weight.ui.mvp.presenter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.view.IActionReadyView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mac on 18/2/28.
 */

public class ActionReadyPresenter extends BasePresenter<IActionReadyView> {
    private static final String TAG = "ActionReadyPresenter";
    private DataSource mDataSource = Repository.getInstance();

    public void getActionData(int dayId, int actionId) {
        try {
            ActionItem actionItem = new ActionItem();
            actionItem.setActionId(actionId);
            actionItem.setName(mDataSource.getActionName(actionId));
            actionItem.setTime(mDataSource.getLevelTime(dayId, actionId));
            actionItem.setTimeText("x" + mDataSource.getLevelTime(dayId, actionId));
            List<String> imgUrlList = mDataSource.getLevelImgUrlList(actionId);
            actionItem.setPathList(imgUrlList);
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (String url : imgUrlList) {
                Drawable drawable = Drawable.createFromStream(MyApplication.getContext().getAssets().open(url), "");
                animationDrawable.addFrame(drawable, 2000);
            }
            actionItem.setAnimationDrawable(animationDrawable);
            mViewRef.get().setData(actionItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void speak(String content, int model) {
        if (GlobalData.config.isTrainVoiceOption()) {
            MyApplication.speechInstant().speak(content, model, null);
        }
    }

    public void setPauseTime() {
        GlobalData.pauseTime = System.currentTimeMillis();
    }

    public void setEndTime() {
        GlobalData.endTime = System.currentTimeMillis();
        Log.d(TAG, "handleMessage: 该次训练暂停时长 = " + (GlobalData.paustDuration / 1000));
    }

    public void setReStartTime() {
        GlobalData.restartTime = System.currentTimeMillis();
    }

    public void setPauseDuration() {
        GlobalData.paustDuration = GlobalData.paustDuration + (GlobalData.restartTime - GlobalData.pauseTime);
    }

    public void saveExerciseRecord(int dayId) {
        mDataSource.saveExerciseRecord(dayId);
    }

    public void getCurrentActionId(int mDayId) {
        int actionId = mDataSource.getCurrentActionId(mDayId);
        mViewRef.get().setCurrentActionId(actionId);
    }
}
