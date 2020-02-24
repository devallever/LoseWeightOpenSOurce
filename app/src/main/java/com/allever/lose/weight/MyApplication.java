package com.allever.lose.weight;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.allever.lib.common.app.App;
import com.allever.lib.recommend.RecommendGlobal;
import com.allever.lib.umeng.UMeng;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.bean.ActionData;
import com.allever.lose.weight.bean.ActionImage;
import com.allever.lose.weight.bean.TrainData;
import com.allever.lose.weight.util.Util;
import com.allever.lose.weight.util.ScreenUtils;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * Created by maozhi on 2018/2/27.
 */

public class MyApplication extends App {
    private static final String TAG = "MyApplication";
    private static MyApplication mContext;
    private static TextToSpeech mSpeech;

    private DataSource mDataSource;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        mContext = this;
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();
        ScreenUtils.init(this);

        com.android.absbase.App.setContext(this);

//        AdChainHelper.INSTANCE.init(this, AdConstants.INSTANCE.getAdData(), new AdFactory());

        //初始化LitePal
        LitePal.initialize(this);

        //初始化个人信息
        initDBData();

        //初始化语言
        Util.setLanguage(this);

        //获取训练数据
        initTrainData();

        //初始化语音
        speechInstant();


        //初始化友盟
        if (!BuildConfig.DEBUG) {
            UMeng.INSTANCE.init(this, null, null, true);
        }

        RecommendGlobal.INSTANCE.init(UMeng.INSTANCE.getChannel());

    }


    public static Context getContext() {
        return mContext;
    }

    public static TextToSpeech speechInstant() {
        if (mSpeech == null) {
            mSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int id = GlobalData.config.getLanguage();
                        switch (id) {
                            case Config.LANG_CHINESE:
                                mSpeech.setLanguage(Locale.CHINESE);
                                break;
                            case Config.LANG_ENGLISH:
                                mSpeech.setLanguage(Locale.ENGLISH);
                                break;
                            default:
                                mSpeech.setLanguage(Locale.CHINESE);
                                break;
                        }

                    }
                }
            });
        }
        return mSpeech;
    }


    private void initDBData() {
        Log.d(TAG, "initDBData: ");
        if (DataSupport.findAll(Person.class).size() == 0) {
            Person person = new Person();
            person.setmAge(0);
            person.setmCurWeight(50);
            person.setmHeight(160);
            person.setmWeightUnit(Constant.UNIT_WEIGHT_KG);
            person.setmHeightUnit(Constant.UNIT_HEIGHT_CM);
            person.setmGender(0);
            person.setmYear(2000);
            person.setmMonth(1);
            person.setmDay(1);
            person.save();
            GlobalData.person = person;
        } else {
            GlobalData.person = DataSupport.findAll(Person.class).get(0);
        }

        if (DataSupport.findAll(Config.class).size() == 0) {
            Config config = new Config();
            config.setLanguage(Config.LANG_CHINESE);
            config.setMuteOption(false);
            config.setVoiceOption(true);
            config.setTrainVoiceOption(true);
            config.setTts("");
            config.setSync(false);
            config.setAccount("");
            config.setSyncTime(System.currentTimeMillis());
            config.save();
            GlobalData.config = config;
        } else {
            GlobalData.config = DataSupport.findAll(Config.class).get(0);
        }

        List<Config.Reminder> reminderList = DataSupport.findAll(Config.Reminder.class);
        GlobalData.reminderList = reminderList;
    }

    public static void initTrainData() {
        Log.d(TAG, "initTrainData: ");
        //todo 耗时处理
        try {
            //30天训练列表
            GlobalData.trainDataList = Repository.getInstance().getDayActionList(MyApplication.getContext());
            for (TrainData trainData : GlobalData.trainDataList) {
                GlobalData.trainDataMap.put(Integer.valueOf(trainData.getName()), trainData);
            }
            //日常训练列表
            GlobalData.routinesDataList = Repository.getInstance().getRoutinesList(MyApplication.getContext());
            for (TrainData trainData : GlobalData.routinesDataList) {
                GlobalData.trainDataMap.put(Integer.valueOf(trainData.getName()), trainData);
            }
            //每个动作图片
            for (ActionImage actionImage : Repository.getInstance().getActionMap(MyApplication.getContext())) {
                GlobalData.actionImageMap.put(actionImage.getId(), actionImage);
            }
            //每个动作描述
            for (ActionData actionData : Repository.getInstance().getActionDesc(MyApplication.getContext())) {
                GlobalData.actionDataMap.put(actionData.getId(), actionData);
            }

            //
            GlobalData.dayTitles = getContext().getResources().getStringArray(R.array.day_title);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
