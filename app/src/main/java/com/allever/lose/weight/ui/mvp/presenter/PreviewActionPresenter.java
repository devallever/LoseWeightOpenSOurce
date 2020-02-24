package com.allever.lose.weight.ui.mvp.presenter;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.view.IPreviewActionView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/1.
 */

public class PreviewActionPresenter extends BasePresenter<IPreviewActionView> {
    private DataSource mDataSource = Repository.getInstance();

    public void getActionPreview(int dayId) {
        //model.getPreviewActionData(dayId);

//        private int actionId;               //项目id
//        private String name;                //项目名称
//        private int time;                   //训练个数或秒数
//        private String timeText;                //持续时间或个数 x20 或20s
//        private List<String> pathList;      //动画图片列表->ActionPreview有效
//        private AnimationDrawable animationDrawable; //动画对象
//        private String videoUrl;            //
//        private List<String> descList;          //描述列表

        try {
            List<ActionItem> actionItemList = new ArrayList<>();
            int levelCount = mDataSource.getLevelCount(dayId);
            List<Integer> levelIdList = mDataSource.getActionIdList(dayId);
            List<String> levelNameList = mDataSource.getLevelNameList(dayId);
            List<Integer> levelTimeList = mDataSource.getLevelTimeList(dayId);
            for (int i = 0; i < levelCount; i++) {
                ActionItem actionItem = new ActionItem();
                actionItem.setActionId(levelIdList.get(i));
                actionItem.setName(levelNameList.get(i));
                actionItem.setTime(levelTimeList.get(i));
                actionItem.setTimeText("x" + levelTimeList.get(i));
                List<String> imgUrlList = mDataSource.getLevelImgUrlList(levelIdList.get(i));
                actionItem.setPathList(imgUrlList);
                AnimationDrawable animationDrawable = new AnimationDrawable();
                for (String url : imgUrlList) {
                    Drawable drawable = Drawable.createFromStream(MyApplication.getContext().getAssets().open(url), "");
                    animationDrawable.addFrame(drawable, 2000);
                }
                actionItem.setAnimationDrawable(animationDrawable);
                actionItemList.add(actionItem);
            }
            mViewRef.get().setPreviewData(actionItemList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * actionId 0: 休息默认的actionId
     */
    public void saveRestScheduleRecord(int dayId) {
        mDataSource.saveScheduleRecord(dayId, 0);
    }
}
