package com.allever.lose.weight.bean;

/**
 * Created by Mac on 2018/3/8.
 */

public class DayInfoBean {
    //第几天对应的id
    private int type;
    //已经训练过的动作数
    private int trainedCount;
    //当天动作总数
    private int levelCount;
    //    //时间
//    private long time;
    //最新完成的动作所属第几天
    private boolean currentDay;
    private String title;
    private boolean finish;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTrainedCount() {
        return trainedCount;
    }

    public void setTrainedCount(int trainedCount) {
        this.trainedCount = trainedCount;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public boolean isCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        this.currentDay = currentDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
