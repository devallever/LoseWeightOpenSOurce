package com.allever.lose.weight.data;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mac on 2018/3/7.
 */

public class Person extends DataSupport {
    private int id;
    //用户的年龄
    private int mAge = 18;

    //出生年月日
    private int mYear = 2000;
    private int mMonth = 1;
    private int mDay = 1;
    //性别 0：男  1：女
    private int mGender = 0;
    //用户的当前体重
    private double mCurWeight = 65.0;
    //用户的身高
    private double mHeight = 170.0;
    //体重单位
    private String mWeightUnit = "kg";
    //身高单位
    private String mHeightUnit = "cm";
    //体重记录列表
    private List<WeightRecord> mWeightList = new ArrayList<WeightRecord>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public double getmCurWeight() {
        return mCurWeight;
    }

    public void setmCurWeight(double mCurWeight) {
        this.mCurWeight = mCurWeight;
    }

    public double getmHeight() {
        return mHeight;
    }

    public void setmHeight(double mHeight) {
        this.mHeight = mHeight;
    }

    public String getmWeightUnit() {
        return mWeightUnit;
    }

    public void setmWeightUnit(String mWeightUnit) {
        this.mWeightUnit = mWeightUnit;
    }

    public String getmHeightUnit() {
        return mHeightUnit;
    }

    public void setmHeightUnit(String mHeightUnit) {
        this.mHeightUnit = mHeightUnit;
    }

    public List<WeightRecord> getmWeightList() {
        return mWeightList;
    }

    public void setmWeightList(List<WeightRecord> mWeightList) {
        this.mWeightList = mWeightList;
    }

    public List<ExerciseRecord> getmExerciseList() {
        return mExerciseList;
    }

    public void setmExerciseList(List<ExerciseRecord> mExerciseList) {
        this.mExerciseList = mExerciseList;
    }

    public List<ScheduleRecord> getScheduleRecordList() {
        return scheduleRecordList;
    }

    public void setScheduleRecordList(List<ScheduleRecord> scheduleRecordList) {
        this.scheduleRecordList = scheduleRecordList;
    }


    /**
     * 添加体重记录
     *
     * @param weight 体重
     * @param year   年
     * @param month  月
     * @param day    日
     */
    public void addWeightRecord(double weight, int year, int month, int day) {

        WeightRecord weightRecord = new WeightRecord(weight, year, month, day);

        //添加体重记录
        if (mWeightList != null) {
            if (mWeightList.indexOf(weightRecord) != -1) {
                mWeightList.add(weightRecord);
            }
        }

        weightRecord.saveOrUpdate("year = ? and month = ? and day = ?", String.valueOf(year), String.valueOf(month), String.valueOf(day));
    }

    /**
     * 排序体重记录
     */
    public void sortWeightList() {

    }


    /**
     * 训练记录列表
     */
    private List<ExerciseRecord> mExerciseList = new ArrayList<ExerciseRecord>();

    /**
     * 开始训练记录
     *
     * @param type      训练类型
     * @param startTime 开始时间
     */
    public void startExerciseRecord(int type, long startTime) {
    }

    /**
     * 结束训练记录
     *
     * @param type    训练类型
     * @param endTime 结束时间
     */
    public void endExerciseRecord(int type, long endTime) {

    }

    /**
     * 体重记录
     */
    public static class WeightRecord extends DataSupport {

        private int id;

        private double weight = 0.0;

        private int year = 0;

        private int month = 0;

        private int day = 0;

        public WeightRecord(double weight, int year, int month, int day) {
            this.weight = weight;
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WeightRecord that = (WeightRecord) o;

            if (Double.compare(that.weight, weight) != 0) return false;
            if (year != that.year) return false;
            if (month != that.month) return false;
            return day == that.day;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    /**
     * 运动记录
     */
    public static class ExerciseRecord extends DataSupport {
        private int id;

        //训练的类别，比如第一天，第五天或睡前拉伸
        private int type = 0;

        //训练开始时间
        private long startTime = 0;

        //暂停持续时间
        private long pauseDuration = 0;

        //训练停止时间
        private long endTime = 0;

        //训练日期
        private int year = 0;
        private int month = 0;
        private int day = 0;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getPauseDuration() {
            return pauseDuration;
        }

        public void setPauseDuration(long pauseDuration) {
            this.pauseDuration = pauseDuration;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }


    private List<ScheduleRecord> scheduleRecordList = new LinkedList<>();

    public static class ScheduleRecord extends DataSupport {
        private int id;
        //动作id
        private int actionId;
        //训练id、第几天对应id
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }


}
