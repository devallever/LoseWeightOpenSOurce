package com.allever.lose.weight.ui.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import androidx.fragment.app.Fragment;

import android.util.Log;

import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.ui.mvp.view.ISettingView;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.util.SyncGoogle;
import com.allever.lose.weight.bean.TTSBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/22.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {
    private static final String TAG = "SettingPresenter";
    private DataSource mDataSource = Repository.getInstance();

    public void getLanguage(Context context) {
        if (context == null) {
            return;
        }
        int id = GlobalData.config.getLanguage();
        String language = "";
        switch (id) {
            case Config.LANG_CHINESE:
                language = context.getResources().getString(R.string.chinese);
                break;
            case Config.LANG_ENGLISH:
                language = context.getResources().getString(R.string.english);
                break;
            default:
                break;
        }
        mViewRef.get().setLanguage(id, language);
    }

    public void saveLanguage(int id) {
        Log.d(TAG, "saveLanguage: id = " + id);
        GlobalData.config.setLanguage(id);
        mDataSource.updateConfig();
    }

    public void saveTTSConfig(String name) {
        GlobalData.config.setTts(name);
        mDataSource.updateConfig();
    }


    public void connectGoogleFit(Fragment fragment, int requestCode) {
        Log.d(TAG, "syncGoogleFit: isSync = " + GlobalData.config.isSync());
        if (!GlobalData.config.isSync()) {
            SyncGoogle.getIns().connectGoogleFit(fragment, requestCode);
        } else {
            mViewRef.get().showSyncDialog();
        }
    }

    public void getSyncData() {
        if (GlobalData.config.isSync()) {
            String account = GlobalData.config.getAccount();
            long syncTime = GlobalData.config.getSyncTime();
            mViewRef.get().setSync(account, DateUtil.formatSyncTime(syncTime));
        } else {
            //设置默认值
            mViewRef.get().setSync(MyApplication.getContext().getResources().getString(R.string.keep_data_in_cloud),
                    MyApplication.getContext().getResources().getString(R.string.never_backed_up));
        }
    }

    public void saveSyncState(boolean sync) {
        GlobalData.config.setSync(sync);
        mDataSource.updateConfig();
    }

    public void loginGoogle(Fragment fragment, int requestCode) {
        SyncGoogle.getIns().loginGoogle(fragment, requestCode);
    }

    public void saveSyncAccount(String email) {
        GlobalData.config.setAccount(email);
        mDataSource.updateConfig();
    }

    public void sendFeedBack(Activity activity) {
        if (activity == null) {
            return;
        }
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
        Uri uri = Uri.parse("mailto:" + Constant.FEEDBACK_TO);
        String[] email = {Constant.FEEDBACK_TO};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.feed_back_subject));
        intent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.feed_back_content));
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.select_mail_app)));
    }

    public void saveVoiceOption(boolean isMute, boolean voice, boolean trainVoice) {
        GlobalData.config.setMuteOption(isMute);
        GlobalData.config.setVoiceOption(voice);
        GlobalData.config.setTrainVoiceOption(trainVoice);
        Repository.getInstance().updateConfig();
    }

    public List<TTSBean> getTTSEngins(TextToSpeech speech) {
        List<TTSBean> ttsBeanList = new ArrayList<>();
        if (speech == null) {
            return ttsBeanList;
        }
        List<TextToSpeech.EngineInfo> engineInfoList = speech.getEngines();
        TTSBean ttsBean;
        for (TextToSpeech.EngineInfo engineInfo : engineInfoList) {
            ttsBean = new TTSBean();
            ttsBean.setName(engineInfo.name);
            ttsBean.setLabel(engineInfo.label);
            if (engineInfo.name.equalsIgnoreCase(GlobalData.config.getTts())) {
                ttsBean.setSelected(true);
            } else {
                ttsBean.setSelected(false);
            }
            ttsBeanList.add(ttsBean);
        }
        return ttsBeanList;
    }

    public void deleteAllData() {
        mDataSource.deleteAllData();
    }
}
