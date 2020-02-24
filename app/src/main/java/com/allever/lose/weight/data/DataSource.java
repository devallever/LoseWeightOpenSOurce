package com.allever.lose.weight.data;

import java.util.List;

/**
 * Created by Mac on 2018/3/7.
 */

public interface DataSource {
    List<Integer> getRoutineIdList();

    /**
     * @return gender 0:男  1:女
     */
    int getGender();

    /**
     * @return birthday 2000-01-01
     */
    String getBirthday();


    /**
     * 保存运动记录
     *
     * @param dayId
     */
    void saveExerciseRecord(int dayId);

    /**
     * 保存运动进度记录
     *
     * @param dayId
     * @param actionId
     */
    void saveScheduleRecord(int dayId, int actionId);

    void updatePersonInfo();

    void updateWeightRecord(int year, int month, int day, double weight);

    /**
     * 获取30天训练所有动作总数
     */
    int getTrainLevelTotalCount();

    /**
     * 获取30天以完成的动作数量
     */
    int getTotalTrainedCount();

    /**
     * 获取30天剩余课程数
     *
     * @return
     */
    int getLeftDay();


    /**
     * 获取(30)训练id列表
     */
    List<Integer> getDayIdList();

    /**
     * 获取当前课程
     *
     * @return dayId==type
     */
    int getCurrentDay();

    /**
     * 获取当天已经完成的动作数
     **/
    int getLevelTrainedCount(int type);

    void updateConfig();

    void addReminder(Config.Reminder reminder);

    void deleteReminder(Config.Reminder reminder);

    List<Person.ExerciseRecord> getAllExerciseRecord();

    void deleteAllSchedule();

    void deleteAllData();

    boolean isFinishTrain(int dayId);

    void saveWeightRecord(double wight, int year, int month, int day);

    List<Person.WeightRecord> getWeightRecordList();

    double getHistoryWeight(int year, int month, int day);

    /***
     * 获取课程的消耗卡路里
     * kcal = 体重 * 小时 * 强度系数
     * @param dayId
     * @return 消耗
     */
    int getLevelKcal(int dayId);

    double getHeight();

    double getWeight();


    /**
     * 获取当次训练持续时间
     */
    String getCurrentExerciseDuration();

    int getCalories(int dayId);

    /**
     * @param duration 已经完成的秒数
     */
    String getLeftTime(int dayId, int actionId, int duration);

    /**
     * 获取训练次数
     */
    int getExerciseCount();

    /**
     * 获取指定天数训练到的动作id
     */
    int getCurrentActionId(int dayId);

    /**
     * 获取第几个动作 如果有3条记录，当前为第四个动作
     */
    int getActionIndex(int dayId);

    /**
     * 获取动作描述内容
     */
    List<String> getLevelDescList(int actionId);

    /**
     * 获取动作持续的个数或秒数
     */
    int getLevelTime(int dayId, int actionId);

    /**
     * 获取动作名称
     */
    String getActionName(int actionId);

    /**
     * 获取动作动画图片列表
     */
    List<String> getLevelImgUrlList(int actionId);

    /**
     * 获取当天训练动作持续的个数或秒数
     */
    List<Integer> getLevelTimeList(int dayId);

    /**
     * 获取当天所有训练动作的名称
     */
    List<String> getLevelNameList(int dayId);

    /**
     * 获取当天所有训练动作的actionId
     */
    List<Integer> getActionIdList(int dayId);

    int getAllKcal();

    String getDurationStr();

    long getDurationSecond();

    String getLevelDuration(int dayId);

    boolean getIsWork(int year, int month, int day);

    double getHeaviest();

    double getLightest();

    double getCurrentWeight();

    String getWeightUnit();

    String getHeightUnit();

    /**
     * 获取当天训练动作个数
     */
    int getLevelCount(int dayId);
}
