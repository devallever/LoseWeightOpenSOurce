package com.allever.lose.weight.data;

import com.allever.lose.weight.bean.ActionData;
import com.allever.lose.weight.bean.ActionImage;
import com.allever.lose.weight.bean.TrainData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mac on 18/3/7.
 */

public class GlobalData {
    private GlobalData() {
    }

    public static Person person;
    public static Config config;
    public static List<Config.Reminder> reminderList = null;

    //30天训练列表
    public static List<TrainData> trainDataList = null;
    //日常训练列表
    public static List<TrainData> routinesDataList = null;
    //dayId
    public static Map<Integer, TrainData> trainDataMap = new HashMap<>();
    //动作对应的图片列表
    public static Map<Integer, ActionImage> actionImageMap = new HashMap<>();
    //动作对应的描述信息
    public static Map<Integer, ActionData> actionDataMap = new HashMap<>();

    public static String[] dayTitles;

    //开始时间
    public static long startTime;
    //结束时间
    public static long endTime;
    //暂停时间时长 mPauseDuration = mPauseDuration + (restartTime - pauseTime);
    public static long paustDuration;
    //暂停的当前时间
    public static long pauseTime;
    //回复锻炼的当前时间, 当restartTime重新被赋值的时候需要计算暂停时长
    public static long restartTime;

    /**
     * 某天训练开始时初始化
     */
    public static void initTime() {
        startTime = System.currentTimeMillis();
        pauseTime = startTime;
        restartTime = startTime;
        paustDuration = 0;
        endTime = startTime;
    }
}
