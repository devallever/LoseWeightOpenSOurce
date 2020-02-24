package com.allever.lose.weight.bean;

/**
 * Created by Mac on 18/3/13.
 */

public class RoutineItem {
    public static final int ID_MORIING = 31;
    public static final int ID_SLEEP = 32;

    //训练id
    int id;
    private String title;
    //动作个数
    private String count;
    private String duration;
    private String kcal;
    private int bgResId;
    private int smallResId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public int getBgResId() {
        return bgResId;
    }

    public void setBgResId(int bgResId) {
        this.bgResId = bgResId;
    }

    public int getSmallResId() {
        return smallResId;
    }

    public void setSmallResId(int smallResId) {
        this.smallResId = smallResId;
    }
}
