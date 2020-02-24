package com.allever.lose.weight.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.allever.lose.weight.bean.ActionData;
import com.allever.lose.weight.bean.ActionImage;
import com.allever.lose.weight.bean.ActionInfo;
import com.allever.lose.weight.bean.Exercise;
import com.allever.lose.weight.bean.TrainData;
import com.allever.lose.weight.util.DateUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by Mac on 2018/3/7.
 */

public class Repository implements DataSource {
    private static final String TAG = "Repository";

    private static Repository INSTANCE = null;

    public static Repository getInstance() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public int getCurrentDay() {
        Person.ExerciseRecord exerciseRecord = DataSupport.findLast(Person.ExerciseRecord.class);
        if (exerciseRecord == null) {
            return 1;
        } else {
            return exerciseRecord.getType();
        }
    }

    /**
     * 获取30天剩余课程数
     *
     * @return
     */
    @Override
    public int getLeftDay() {
        if (GlobalData.trainDataList == null) {
            return 30;
        }
        int finishDay = 0;
        for (TrainData trainData : GlobalData.trainDataList) {
            int id = Integer.valueOf(trainData.getName());
            List<Person.ScheduleRecord> list = DataSupport.where("type = ?", String.valueOf(id)).find(Person.ScheduleRecord.class);
            if (trainData.getExercise().size() == 0) {
                if (list.size() == 1) {
                    finishDay += 1;
                }
            } else {
                if (list.size() == trainData.getExercise().size()) {
                    finishDay += 1;
                }
            }
        }
        return GlobalData.trainDataList.size() - finishDay;
    }

    @Override
    public List<Integer> getDayIdList() {
        List<Integer> list = new ArrayList<>();
        if (GlobalData.trainDataList == null) {
            return list;
        }
        List<TrainData> trainDataList = GlobalData.trainDataList;
        for (TrainData trainData : trainDataList) {
            list.add(Integer.valueOf(trainData.getName()));
        }
        return list;
    }

    /**
     * 获取总卡路里数
     * kcal = 体重 * 小时 * 强度系数(平均值)
     *
     * @return
     */
    @Override
    public int getAllKcal() {
        //单位ms
        long duration = getDurationSecond();

        float factorTotal = 0;
        for (Map.Entry entry : GlobalData.actionDataMap.entrySet()) {
            ActionData actionData = (ActionData) entry.getValue();
            factorTotal += actionData.getFactor();
        }
        float valFactor = factorTotal / GlobalData.actionDataMap.size();

        int kcal = (int) (GlobalData.person.getmCurWeight() * ((duration / 1000f) / 3600f) * valFactor);
        return kcal;
    }


    /**
     * 获取总运动时长
     *
     * @return 3:10
     */
    @Override
    public String getDurationStr() {
        long duration = getDurationSecond();
        Log.d(TAG, "getDuration: duration = " + duration / 1000);
        return DateUtil.second2FormatMinute((int) duration / 1000);
    }

    /**
     * 求所有记录持续时间
     *
     * @return 毫秒
     */
    @Override
    public long getDurationSecond() {
        List<Person.ExerciseRecord> list = DataSupport.findAll(Person.ExerciseRecord.class);
        long duration = 0;
        for (Person.ExerciseRecord exerciseRecord : list) {
            long end = exerciseRecord.getEndTime();
            long start = exerciseRecord.getStartTime();
            long pauseDuration = exerciseRecord.getPauseDuration();
            Log.d(TAG, "getDuration: end = " + end);
            Log.d(TAG, "getDuration: start = " + start);
            Log.d(TAG, "getDuration: pause = " + pauseDuration);
            duration = duration + (exerciseRecord.getEndTime() - exerciseRecord.getStartTime() - exerciseRecord.getPauseDuration());
            Log.d(TAG, "getDuration: duration = " + duration);
        }
        return duration;
    }

    /***
     * 获取课程的总时长
     * @param dayId
     * @return 格式化的时间 8:30
     */
    @Override
    public String getLevelDuration(int dayId) {
        TrainData trainData = GlobalData.trainDataMap.get(dayId);
        int totalCount = 0;
        for (Exercise exercise : trainData.getExercise()) {
            totalCount += exercise.getTime();
        }
        int totalSecond = totalCount * 4;
        return DateUtil.second2FormatMinute(totalSecond);
    }


    /**
     * 指定日期是否运动
     *
     * @param
     * @return
     */
    @Override
    public boolean getIsWork(int year, int month, int day) {
        List<Person.ExerciseRecord> exerciseRecords = DataSupport.where("year = ? and month = ? and day = ?",
                String.valueOf(year),
                String.valueOf(month),
                String.valueOf(day))
                .find(Person.ExerciseRecord.class);

        List<Person.ExerciseRecord> all = DataSupport.findAll(Person.ExerciseRecord.class);
        if (exerciseRecords.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double getHeaviest() {
        //对数据库排序查找
        List<Person.WeightRecord> weightRecordList = DataSupport.order("weight desc").find(Person.WeightRecord.class);
        if (weightRecordList.size() > 0) {
            return weightRecordList.get(0).getWeight();
        } else {
            return 0;
        }
    }

    @Override
    public double getLightest() {
        List<Person.WeightRecord> weightRecordList = DataSupport.order("weight asc").find(Person.WeightRecord.class);
        if (weightRecordList.size() > 0) {
            return weightRecordList.get(0).getWeight();
        } else {
            return 0;
        }
    }


    /**
     * 获取当前体重
     *
     * @param
     * @return
     */
    @Override
    public double getCurrentWeight() {
        return GlobalData.person.getmCurWeight();
    }

    /**
     * 获取体重单位
     *
     * @return
     */
    @Override
    public String getWeightUnit() {
        return GlobalData.person.getmWeightUnit();
    }

    /**
     * 获取身高单位
     *
     * @return
     */
    @Override
    public String getHeightUnit() {
        return GlobalData.person.getmHeightUnit();
    }

    /**
     * 获取当天训练动作个数
     */
    @Override
    public int getLevelCount(int dayId) {
        return GlobalData.trainDataMap.get(dayId).getExercise().size();
    }

    /**
     * 获取当天所有训练动作的actionId
     */
    @Override
    public List<Integer> getActionIdList(int dayId) {
        List<Integer> list = new ArrayList<>();
        List<Exercise> exerciseList = GlobalData.trainDataMap.get(dayId).getExercise();
        for (Exercise exercise : exerciseList) {
            list.add(exercise.getActionId());
        }
        return list;
    }

    /**
     * 获取当天所有训练动作的名称
     */
    @Override
    public List<String> getLevelNameList(int dayId) {
        List<String> list = new ArrayList<>();
        List<Exercise> exerciseList = GlobalData.trainDataMap.get(dayId).getExercise();
        for (Exercise exercise : exerciseList) {
            list.add(GlobalData.actionDataMap.get(exercise.getActionId()).getName());
        }
        return list;
    }

    /**
     * 获取当天所有训练动作的个数
     */
    @Override
    public List<Integer> getLevelTimeList(int dayId) {
        List<Integer> list = new ArrayList<>();
        List<Exercise> exerciseList = GlobalData.trainDataMap.get(dayId).getExercise();
        for (Exercise exercise : exerciseList) {
            list.add(exercise.getTime());
        }
        return list;
    }

    @Override
    public List<String> getLevelImgUrlList(int actionId) {
        List<String> imgList = new ArrayList<>();
        List<ActionInfo> actionInfos = GlobalData.actionImageMap.get(actionId).getUris();
        for (ActionInfo actionInfo : actionInfos) {
            imgList.add(actionInfo.getUri());
        }
        return imgList;
    }

    @Override
    public String getActionName(int actionId) {
        return GlobalData.actionDataMap.get(actionId).getName();
    }

    /**
     * 获取动作个数或秒数
     */
    @Override
    public int getLevelTime(int dayId, int actionId) {
        TrainData trainData = GlobalData.trainDataMap.get(dayId);
        List<Exercise> exerciseList = trainData.getExercise();
        for (Exercise exercise : exerciseList) {
            if (exercise.getActionId() == actionId) {
                return exercise.getTime();
                //return 1;
            }
        }
        return 20;
    }

    @Override
    public List<String> getLevelDescList(int actionId) {
        return GlobalData.actionDataMap.get(actionId).getDesc();
    }

    /**
     * 获取第几个动作 如果有3条记录，当前为第四个动作
     */
    @Override
    public int getActionIndex(int dayId) {
        List<Person.ScheduleRecord> scheduleRecordList = DataSupport.where("type = ?", String.valueOf(dayId)).find(Person.ScheduleRecord.class);
        int currentAction = scheduleRecordList.size() + 1;
        if (currentAction - 1 == getLevelCount(dayId)) {
            //已经完成全部
            currentAction = 1;
            //清空进度记录
            for (Person.ScheduleRecord scheduleRecord : scheduleRecordList) {
                scheduleRecord.delete();
            }
        }
        return currentAction;
    }

    /**
     * 判断是否完成当天训练
     */
    @Override
    public boolean isFinishTrain(int dayId) {
        List<Person.ScheduleRecord> scheduleRecordList = DataSupport.where("type = ?", String.valueOf(dayId)).find(Person.ScheduleRecord.class);
        if (scheduleRecordList.size() == getLevelCount(dayId) && scheduleRecordList.size() != 0) {
            //已经完成全部
            return true;
        } else {
            //当天没有训练的情况,如果已经完成，数据表仅有一条记录
            //Log.d(TAG, "isFinishTrain: scheduleRecordList.size() = " + scheduleRecordList.size());
            if (scheduleRecordList.size() == 1 && getLevelCount(dayId) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCurrentActionId(int dayId) {
        int exerciseIndex = getActionIndex(dayId) - 1;
        TrainData trainData = GlobalData.trainDataMap.get(dayId);
        return trainData.getExercise().get(exerciseIndex).getActionId();
    }

    /**
     * 获取当天剩余动作的剩余时间
     */
    @Override
    public String getLeftTime(int dayId, int actionId, int duration) {
//        TrainData trainData = GlobalData.trainDataMap.get(dayId);
//        List<Exercise> exerciseList = trainData.getExercise();
//        int index = 0;
//        for (int i=0; i<exerciseList.size(); i++){
//            if (actionId == exerciseList.get(i).getActionId()){
//                index = i;
//                break;
//            }
//        }
//        int totalSecond = 0;
//        for (int i = index; i < exerciseList.size(); i++){
//            if (exerciseList.get(i).getActionId() == 3){
//                totalSecond += exerciseList.get(i).getTime();
//            }else {
//                totalSecond += exerciseList.get(i).getTime() * 4;
//            }
//
//        }
//        int leftSecond = totalSecond - duration;
//        int min = leftSecond / 60;
//        int second = leftSecond % 60;
//        return min + ":" + second;
        return "";
    }


    /**
     * 获取训练次数
     */
    @Override
    public int getExerciseCount() {
        return DataSupport.findAll(Person.ExerciseRecord.class).size();
    }

    /**
     * 获取运动完成后消耗的卡路里
     * 体重 * 小时 * 系数
     */
    @Override
    public int getCalories(int dayId) {
        //读取最新一条记录
        Person.ExerciseRecord exerciseRecord = DataSupport.findLast(Person.ExerciseRecord.class);
        long duration = exerciseRecord.getEndTime() - exerciseRecord.getStartTime() - exerciseRecord.getPauseDuration();
        TrainData trainData = GlobalData.trainDataMap.get(dayId);
        float factorTotal = 0;
        for (Exercise exercise : trainData.getExercise()) {
            factorTotal += GlobalData.actionDataMap.get(exercise.getActionId()).getFactor();
        }
        float valFactor = factorTotal / trainData.getExercise().size();

        int kcal = (int) (GlobalData.person.getmCurWeight() * ((duration / 1000f) / 3600f) * valFactor);
        return kcal;
    }

    /**
     * 获取当次训练持续时间
     */
    @Override
    public String getCurrentExerciseDuration() {
        long duration = GlobalData.endTime - GlobalData.startTime - GlobalData.paustDuration;
        return DateUtil.second2FormatMinute((int) duration / 1000);
    }

    @Override
    public double getWeight() {
        return GlobalData.person.getmCurWeight();
    }

    @Override
    public double getHeight() {
        return GlobalData.person.getmHeight();
    }

    /***
     * 获取课程的消耗卡路里
     * kcal = 体重 * 小时 * 强度系数
     * @param dayId
     * @return 消耗
     */
    @Override
    public int getLevelKcal(int dayId) {
        TrainData trainData = GlobalData.trainDataMap.get(dayId);
        double totalCount = 0;
        for (Exercise exercise : trainData.getExercise()) {
            totalCount += exercise.getTime();
        }
        double totalSecond = totalCount * 6;
        double kcal = GlobalData.person.getmCurWeight() * (totalSecond / 3600f) * 1.5;
        return (int) kcal;
    }


    @Override
    public List<Integer> getRoutineIdList() {
        List<Integer> list = new ArrayList<>();
        if (GlobalData.routinesDataList == null) {
            return list;
        }
        for (TrainData trainData : GlobalData.routinesDataList) {
            list.add(Integer.valueOf(trainData.getName()));
        }
        return list;
    }

    @Override
    public int getGender() {
        return GlobalData.person.getmGender();
    }


    @Override
    public String getBirthday() {
        return GlobalData.person.getmYear() + "-" + GlobalData.person.getmMonth() + "-" + GlobalData.person.getmDay();
    }

    /**
     * 添加运动记录
     *
     * @param dayId
     */
    @Override
    public void saveExerciseRecord(int dayId) {
        Log.d(TAG, "saveExerciseRecord: ");
        Person.ExerciseRecord exerciseRecord = new Person.ExerciseRecord();
        exerciseRecord.setType(dayId);
        exerciseRecord.setEndTime(GlobalData.endTime);
        exerciseRecord.setStartTime(GlobalData.startTime);
        exerciseRecord.setPauseDuration(GlobalData.paustDuration);
        exerciseRecord.setYear(DateUtil.getYear(System.currentTimeMillis()));
        exerciseRecord.setMonth(DateUtil.getMonth(System.currentTimeMillis()));
        exerciseRecord.setDay(DateUtil.getDay(System.currentTimeMillis()));
        exerciseRecord.save();
    }

    @Override
    public void updatePersonInfo() {
        Person person = GlobalData.person;
        person.saveOrUpdate("id = ?", String.valueOf(person.getId()));
//        person.update(person.getId());
    }


    /**
     * 添加或更新体重记录
     */
    @Override
    public void updateWeightRecord(int year, int month, int day, double weight) {
        Person.WeightRecord weightRecord = new Person.WeightRecord(weight, year, month, day);
        weightRecord.saveOrUpdate(
                "year = ? and month = ? and day = ?",
                String.valueOf(year),
                String.valueOf(month),
                String.valueOf(day));
    }


    /**
     * 获取动作总数
     */
    @Override
    public int getTrainLevelTotalCount() {
        if (GlobalData.trainDataList == null) {
            return 0;
        }
        int total = 0;
        for (TrainData trainData : GlobalData.trainDataList) {
            total += trainData.getExercise().size();
        }
        return total;
    }

    /**
     * 获取训练过的动作总数
     */
    @Override
    public int getTotalTrainedCount() {
        return DataSupport.findAll(Person.ScheduleRecord.class).size();
    }

    /**
     * 获取当天训练过的动作数
     */
    @Override
    public int getLevelTrainedCount(int dayId) {
        return DataSupport.where("type = ?", String.valueOf(dayId)).find(Person.ScheduleRecord.class).size();
    }

    /**
     * 保存运动进度记录
     *
     * @param dayId
     * @param actionId
     */
    @Override
    public void saveScheduleRecord(int dayId, int actionId) {
        //日常训练不算入训练进度
        if (dayId > 30) {
            return;
        }
        Person.ScheduleRecord scheduleRecord = new Person.ScheduleRecord();
        scheduleRecord.setType(dayId);
        scheduleRecord.setActionId(actionId);
        scheduleRecord.saveOrUpdate("type = ? and actionId = ?",
                String.valueOf(dayId),
                String.valueOf(actionId));
    }

    @Override
    public void updateConfig() {
        Log.d(TAG, "updateConfig: ");
        Config config = GlobalData.config;
        config.saveOrUpdate("id = ?", String.valueOf(config.getId()));
    }

    /**
     * 添加/更新一条提醒记录
     *
     * @param reminder
     */
    @Override
    public void addReminder(Config.Reminder reminder) {
        if (reminder == null) {
            return;
        }
        reminder.saveOrUpdate("hour = ? and minute = ?", String.valueOf(reminder.getHour()), String.valueOf(reminder.getMinute()));
    }

    /**
     * 删除提醒
     */
    @Override
    public void deleteReminder(Config.Reminder reminder) {
        if (reminder == null) {
            return;
        }
        reminder.delete();
    }

    /**
     * 获取所有训练记录
     */
    @Override
    public List<Person.ExerciseRecord> getAllExerciseRecord() {
        return DataSupport.order("id desc").find(Person.ExerciseRecord.class);
    }

    /**
     * 删除训练进度
     */
    @Override
    public void deleteAllSchedule() {
        DataSupport.deleteAll(Person.ScheduleRecord.class);
    }

    @Override
    public void deleteAllData() {
        DataSupport.deleteAll(Person.ScheduleRecord.class);
        DataSupport.deleteAll(Config.class);
        DataSupport.deleteAll(Person.ExerciseRecord.class);
        DataSupport.deleteAll(Person.class);
        DataSupport.deleteAll(Person.WeightRecord.class);
        DataSupport.deleteAll(Config.Reminder.class);
    }

    @Override
    public void saveWeightRecord(double wight, int year, int month, int day) {
        Person.WeightRecord weightRecord = new Person.WeightRecord(wight, year, month, day);
        weightRecord.saveOrUpdate("year = ? and month = ? and day = ?", String.valueOf(year), String.valueOf(month), String.valueOf(day));
    }

    @Override
    public List<Person.WeightRecord> getWeightRecordList() {
        List<Person.WeightRecord> list = DataSupport.order("year asc, month asc, day asc").find(Person.WeightRecord.class);
        for (Person.WeightRecord weightRecord : list) {
            Log.d(TAG, "getWeightRecordList: " + weightRecord.getYear() + "-" + weightRecord.getMonth() + "-" + weightRecord.getDay() + ": " + weightRecord.getWeight() + " kg");
        }
        return list;
    }

    @Override
    public double getHistoryWeight(int year, int month, int day) {
        List<Person.WeightRecord> list = DataSupport.where("year = ? and month = ? and day = ?",
                String.valueOf(year),
                String.valueOf(month),
                String.valueOf(day)
        ).find(Person.WeightRecord.class);
        if (list.size() > 0) {
            return list.get(0).getWeight();
        }
        return 0;
    }

    public List<ActionImage> getActionMap(Context context) throws IOException {
        String json = readFileFromAssets(context, "uri_map.json");
        List<ActionImage> list = jsonToBeanList(json, ActionImage.class);
        return list;
    }

    public List<TrainData> getDayActionList(Context context) throws IOException {
        String json = readFileFromAssets(context, "td_body/beginner1.json");
        List<TrainData> trainData = jsonToBeanList(json, TrainData.class);
        return trainData;
    }

    public List<TrainData> getRoutinesList(Context context) throws IOException {
        String json = readFileFromAssets(context, "td_body/routines_list.json");
        List<TrainData> trainData = jsonToBeanList(json, TrainData.class);
        return trainData;
    }

    public String readFileFromAssets(Context context, String fileName) throws IOException {
        if (null == context || null == fileName) return null;
        AssetManager am = context.getAssets();
        InputStream input = am.open(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        output.close();
        input.close();
        return output.toString();
    }

    public <T> List<T> jsonToBeanList(String json, Class<T> t) {
        List<T> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        for (JsonElement element : jsonarray) {
            list.add(gson.fromJson(element, t));
        }
        return list;
    }

    public List<ActionData> getActionDesc(Context context) throws IOException {
        int id = GlobalData.config.getLanguage();
        String json = "";
        switch (id) {
            case Config.LANG_CHINESE:
                json = readFileFromAssets(context, "action_data_zh.json");
                break;
            case Config.LANG_ENGLISH:
                json = readFileFromAssets(context, "action_data_en.json");
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        List<ActionData> actionDataList = jsonToBeanList(json, ActionData.class);
        return actionDataList;
    }
}
