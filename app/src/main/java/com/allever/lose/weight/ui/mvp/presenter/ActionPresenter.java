package com.allever.lose.weight.ui.mvp.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.view.IActionView;
import com.allever.lose.weight.util.DateUtil;

import java.io.IOException;
import java.util.List;


/**
 * Created by Mac on 18/2/28.
 */

public class ActionPresenter extends BasePresenter<IActionView> {
    private static final String TAG = "ActionPresenter";

    private static final int MSG_CHANGE_IMG = 0x04;
    private static final int MSG_DELAY_CHANGE = 2000;

    private DataSource mDataSource = Repository.getInstance();

    private List<String> mActionImgUrlList;
    private Handler mHandler;
    //该动作的训练次数
    private int mTime = 20;
    //给动作做到第几个
    private int mProgress = 1;
    //当前第几张图片
    private int mCurrentImgIndex = 1;

    private TextToSpeech mSpeech;

    //开始时间 该动作开始的时间-进入该页面后赋值
    private long mStartTime = System.currentTimeMillis();
    //结束时间 该动作结束的时间-结束该动作时赋值-调用nextFragment
    private long mEndTime = mStartTime;
    //暂停时间时长 mPauseDuration = mPauseDuration + (restartTime - pauseTime);
    private long mPauseDuration = 0;
    //暂停的当前时间 弹出对话框 或 点击展厅按钮 时赋值
    private long mPauseTime = mStartTime;
    //回复锻炼的当前时间, 当restartTime重新被赋值的时候需要计算暂停时长
    private long mReStartTime = mStartTime;


    private int currentAction;
    private int totalAction;

    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    //private Context mContext;

    public ActionPresenter(final Context context) {
        //mContext = context;
        mSpeech = MyApplication.speechInstant();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_CHANGE_IMG:
                        try {
                            if (mProgress <= mTime) {
                                if (mActionImgUrlList.size() > 1) {
                                    //继续更新图片
                                    int isOrigin = (mCurrentImgIndex % mActionImgUrlList.size()) % 2; //0: 原始位 1：动作位
                                    if (isOrigin == 1) {
                                        //播放语音
                                        if (!mSpeech.isSpeaking()) {
                                            if (!GlobalData.config.isMuteOption() && GlobalData.config.isTrainVoiceOption()) {
                                                mSpeech.speak(String.valueOf(mProgress), TextToSpeech.QUEUE_FLUSH, null);
                                            } else if (!GlobalData.config.isMuteOption() && GlobalData.config.isVoiceOption()) {
                                                Log.d(TAG, "handleMessage: playcountdown");
                                                playCountDown(context);
                                            }
                                        }
                                        mViewRef.get().updateProgress(mProgress);
                                        mProgress++;
                                    }
                                    mViewRef.get().updateActionImg(BitmapFactory.decodeStream(MyApplication.getContext().getAssets().open(mActionImgUrlList.get(mCurrentImgIndex % mActionImgUrlList.size()))));
                                    mCurrentImgIndex++;
                                    mHandler.sendEmptyMessageDelayed(MSG_CHANGE_IMG, MSG_DELAY_CHANGE);
                                } else {
                                    //只有一张图片的情况
                                    if (!mSpeech.isSpeaking()) {
                                        if (!GlobalData.config.isMuteOption() && GlobalData.config.isTrainVoiceOption()) {
                                            mSpeech.speak(String.valueOf(mProgress), TextToSpeech.QUEUE_FLUSH, null);
                                        } else if (!GlobalData.config.isMuteOption() && GlobalData.config.isVoiceOption()) {
                                            playCountDown(context);
                                        }
                                    }
                                    mViewRef.get().updateActionImg(BitmapFactory.decodeStream(MyApplication.getContext().getAssets().open(mActionImgUrlList.get(0))));
                                    mViewRef.get().updateProgress(mProgress);
                                    mProgress++;
                                    mHandler.sendEmptyMessageDelayed(MSG_CHANGE_IMG, MSG_DELAY_CHANGE);
                                }

                            } else {
                                mViewRef.get().updateActionImg(BitmapFactory.decodeStream(MyApplication.getContext().getAssets().open(mActionImgUrlList.get(0))));
                                //结束`更新图片 跳转 finish 或 next
                                setEndTime();

                                Log.d(TAG, "handleMessage: currentActionIndex = " + currentAction);
                                Log.d(TAG, "handleMessage: totalActionCount =" + totalAction);
                                if (currentAction < totalAction) {
                                    mViewRef.get().startNextFragment();
                                } else {
                                    mViewRef.get().startFinishFragment();
                                }

                                Log.d(TAG, "handleMessage: 该动作开始时间 = " + DateUtil.formatTime(mStartTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
                                Log.d(TAG, "handleMessage: 该动作结束时间 = " + DateUtil.formatTime(mStartTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
                                Log.d(TAG, "handleMessage: 该动作暂停时长 = " + (mPauseDuration / 1000));

                                Log.d(TAG, "handleMessage: 该次训练开始时间 = " + DateUtil.formatTime(GlobalData.startTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
                                Log.d(TAG, "handleMessage: 该次训练结束时间 = " + DateUtil.formatTime(GlobalData.endTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
                                Log.d(TAG, "handleMessage: 该次训练暂停时长 = " + (GlobalData.paustDuration / 1000));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        mSoundPool = new SoundPool(4, 3, 0);
        mAudioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
    }

    public void getActionData(int dayId, int actionId) {
        try {
            ActionItem actionItem = new ActionItem();
            actionItem.setActionId(actionId);
            actionItem.setName(mDataSource.getActionName(actionId));
            actionItem.setTime(mDataSource.getLevelTime(dayId, actionId));
            actionItem.setTimeText("x" + mDataSource.getLevelTime(dayId, actionId));
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
            mTime = actionItem.getTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentLevel(int dayId, int actionId) {
        currentAction = mDataSource.getActionIndex(dayId);
        totalAction = mDataSource.getLevelCount(dayId);
        mViewRef.get().setCurrent(currentAction, totalAction);
    }

    public void getActionImgUrlList(int type, int mActionId) {
        List<String> list = mDataSource.getLevelImgUrlList(mActionId);
        mViewRef.get().setActionImgList(list);
        mActionImgUrlList = list;
        try {
            mViewRef.get().updateActionImg(BitmapFactory.decodeStream(MyApplication.getContext().getAssets().open(mActionImgUrlList.get(0))));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void setStartTime() {
        mStartTime = System.currentTimeMillis();
    }

    private void sendRemoveMessage() {
        mHandler.removeMessages(MSG_CHANGE_IMG);
    }

    public void sendStartMessage() {
        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_IMG, MSG_DELAY_CHANGE);
    }

    private void setPauseTime() {
        mPauseTime = System.currentTimeMillis();
        GlobalData.pauseTime = mPauseTime;
    }

    private void setEndTime() {
        mEndTime = System.currentTimeMillis();
        GlobalData.endTime = mEndTime;

        Log.d(TAG, "handleMessage: 该动作开始时间 = " + DateUtil.formatTime(mStartTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        Log.d(TAG, "handleMessage: 该动作结束时间 = " + DateUtil.formatTime(mEndTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        Log.d(TAG, "handleMessage: 该动作暂停时长 = " + (mPauseDuration / 1000));

        Log.d(TAG, "handleMessage: 该次训练开始时间 = " + DateUtil.formatTime(GlobalData.startTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        Log.d(TAG, "handleMessage: 该次训练结束时间 = " + DateUtil.formatTime(GlobalData.endTime, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        Log.d(TAG, "handleMessage: 该次训练暂停时长 = " + (GlobalData.paustDuration / 1000));
    }

    public void setReStartTime() {
        mReStartTime = System.currentTimeMillis();
        GlobalData.restartTime = mReStartTime;
    }

    public void setPauseDuration() {
        mPauseDuration = mPauseDuration + (mReStartTime - mPauseTime);
        GlobalData.paustDuration = GlobalData.paustDuration + (GlobalData.restartTime - GlobalData.pauseTime);
    }

    public void speak(String content, int model) {
        mSpeech.speak(content, model, null);
    }

    public void restartAction(int dayId, int actionId) {
        getActionImgUrlList(dayId, actionId);
        setReStartTime();
        setPauseDuration();
    }

    public void pauseAction() {
        sendRemoveMessage();
        setPauseTime();
    }

    public void stopAction() {
        setEndTime();
    }

    public void saveActionScheduleRecord(int dayId, int actionId) {
        Log.d(TAG, "saveActionScheduleRecord: ");
        mDataSource.saveScheduleRecord(dayId, actionId);
    }

    public void saveExerciseRecord(int dayId) {
        Log.d(TAG, "saveExerciseRecord: ");
        mDataSource.saveExerciseRecord(dayId);
    }

    public void updateSoundOption(boolean muteOption, boolean voiceOption, boolean trainVoiceOption) {
        GlobalData.config.setMuteOption(muteOption);
        GlobalData.config.setVoiceOption(voiceOption);
        GlobalData.config.setTrainVoiceOption(trainVoiceOption);
        mDataSource.updateConfig();
    }

    private void playCountDown(Context context) {
        final int id = mSoundPool.load(context, R.raw.td_countdown, 1);
        final int j = this.mAudioManager.getStreamVolume(3);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(id, j, j, 1, 0, 1.0F);
            }
        });
    }

    public void getCurrentActionId(int mDayId) {
        int actionId = mDataSource.getCurrentActionId(mDayId);
        mViewRef.get().setCurrentActionId(actionId);
    }

    public void pauseTTS() {
        mSpeech.stop();
    }

    public int getCurrentDurationTime() {
        int duration = (int) (mPauseTime - mStartTime - (mPauseDuration + (mReStartTime - mPauseTime))) / 1000;
        return (int) duration;
    }
}
