package com.allever.lose.weight.ui.mvp.presenter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.view.IActionNextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mac on 18/3/1.
 */

public class ActionNextPresenter extends BasePresenter<IActionNextView> {
    private DataSource mDataSource = Repository.getInstance();

    public void getActionData(int dayId, int actionId) {
        try {
            ActionItem actionItem = new ActionItem();
            actionItem.setName(mDataSource.getActionName(actionId));
            List<String> imgUrlList = mDataSource.getLevelImgUrlList(actionId);
            actionItem.setPathList(imgUrlList);
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (String url : imgUrlList) {
                Drawable drawable = Drawable.createFromStream(MyApplication.getContext().getAssets().open(url), "");
                animationDrawable.addFrame(drawable, 2000);
            }
            actionItem.setAnimationDrawable(animationDrawable);
            List<String> descList = mDataSource.getLevelDescList(actionId);
            actionItem.setDescList(descList);
            mViewRef.get().setData(actionItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentLevel(int dayId, int actionId) {
        int currentLevel = mDataSource.getActionIndex(dayId);
        int levelCount = mDataSource.getLevelCount(dayId);
        mViewRef.get().setCurrent(currentLevel, levelCount);
    }

    public void speak(String content, int model) {
        if (!GlobalData.config.isMuteOption() && GlobalData.config.isTrainVoiceOption()) {
            MyApplication.speechInstant().speak(content, model, null);
        }
    }

    public void saveExerciseRecord(int dayId) {
        mDataSource.saveExerciseRecord(dayId);
    }

    public void getCurrentActionId(int mDayId) {
        int actionId = mDataSource.getCurrentActionId(mDayId);
        mViewRef.get().setCurrentActionId(actionId);
    }
}
