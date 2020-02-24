package com.allever.lose.weight.bean;

import android.graphics.drawable.AnimationDrawable;

import java.util.List;

/**
 * Created by Mac on 18/2/27.
 */

public class ActionItem {
    private int actionId;               //项目id
    private String name;                //项目名称
    private int time;                   //训练个数或秒数
    private String timeText;                //持续时间或个数 x20 或20s
    private List<String> pathList;      //动画图片列表->ActionPreview有效
    private AnimationDrawable animationDrawable; //动画对象
    private String videoUrl;            //
    private List<String> descList;          //描述列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public AnimationDrawable getAnimationDrawable() {
        return animationDrawable;
    }

    public void setAnimationDrawable(AnimationDrawable animationDrawable) {
        this.animationDrawable = animationDrawable;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<String> getDescList() {
        return descList;
    }

    public void setDescList(List<String> descList) {
        this.descList = descList;
    }
}
