package com.allever.lose.weight.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.allever.lose.weight.MainActivity;
import com.allever.lose.weight.MyApplication;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.dialog.LanguageDialog;
import com.allever.lose.weight.ui.dialog.SyncGoogleFitDialog;
import com.allever.lose.weight.ui.mvp.presenter.SettingPresenter;
import com.allever.lose.weight.ui.mvp.view.ISettingView;
import com.allever.lose.weight.util.Util;
import com.allever.lose.weight.ui.dialog.SingleChoiceDialogFragment;
import com.allever.lose.weight.ui.dialog.SoundOptionsFragment;
import com.allever.lose.weight.ui.dialog.TTSFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Mac on 2018/3/1.
 */

public class SettingsFragment extends BaseFragment<ISettingView, SettingPresenter> implements ISettingView, LanguageDialog.ILanguageListener, SoundOptionsFragment.ISoundListener {

    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x01;
    private static final int RC_SIGN_IN = 0x02;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Unbinder unbinder;
    @BindView(R.id.test_voice)
    TextView testVoice;
    @BindView(R.id.setting)
    TextView mSetting;
    @BindView(R.id.select_engine)
    TextView selectEngine;
    @BindView(R.id.sound_options)
    TextView soundOptions;
    @BindView(R.id.id_fg_setting_tv_health_data)
    TextView mTvHealthData;
    @BindView(R.id.id_fg_setting_tv_unit_setting)
    TextView mTvUnitSetting;
    @BindView(R.id.id_fg_setting_tv_language_setting_setting)
    TextView mTvLanguageSetting;
    @BindView(R.id.id_fg_setting_tv_share)
    TextView mTvShare;
    @BindView(R.id.id_fg_setting_tv_delete_all_data)
    TextView mTvDeleteAllData;
    @BindView(R.id.id_fg_setting_tv_download_tts)
    TextView mTvDownloadTts;
    @BindView(R.id.id_fg_setting_tv_rate_us)
    TextView mTvRateUs;

    @BindView(R.id.id_sync_ll_sync_container)
    LinearLayout mLlSyncContainer;
    @BindView(R.id.id_sync_tv_account)
    TextView mTvAccount;
    @BindView(R.id.id_sync_tv_sync_time)
    TextView mTvSyncTime;
    @BindView(R.id.id_fg_setting_tv_reminder)
    TextView mTvReminder;
    @BindView(R.id.id_fg_setting_tv_feedback)
    TextView mTvFeedback;

    private TextToSpeech mSpeech;
    private int index;
    private List<TextToSpeech.EngineInfo> engineInfoList = new ArrayList<>();
    public static final String TAG = "SettingsFragment";

    private LanguageDialog mLanguageDialog;
    private SoundOptionsFragment mSoundDialog;
    private SyncGoogleFitDialog mSyncGoogleFitDialog;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        initToolbar(mToolbar, R.string.settings);
        mSpeech = MyApplication.speechInstant();
        initDialog();

        mPresenter.getLanguage(_mActivity);
        mPresenter.getSyncData();

        return view;
    }

    private void initDialog() {
        mLanguageDialog = new LanguageDialog(_mActivity, this);
        mSoundDialog = new SoundOptionsFragment(_mActivity, this);
    }

    @OnClick(R.id.test_voice)
    public void setTestVoice() {
        mSpeech.speak(getString(R.string.tts_tset), TextToSpeech.QUEUE_FLUSH, null);
        showTTSTestDialog();
    }

    @OnClick(R.id.setting)
    public void setmSetting() {
        startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
    }

    private void showTTSTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
        builder.setMessage(R.string.voice_dialog_title);
        builder.setPositiveButton(R.string.voice_dialog_can, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.voice_dialog_cant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                extraTransaction().startDontHideSelf(TTSFragment.newInstance());
            }
        });
        builder.show();
    }

    public void showSingleChoiceDialogFragment() {
        engineInfoList = mSpeech.getEngines();
        SingleChoiceDialogFragment singleChoiceDialogFragment = new SingleChoiceDialogFragment();
        singleChoiceDialogFragment.show("请选择TTS语音", mPresenter.getTTSEngins(mSpeech), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextToSpeech.EngineInfo engineInfo = engineInfoList.get(index);
                Log.i(TAG, engineInfo.name);
                mSpeech.setEngineByPackageName(engineInfo.name);
                //保存引擎配置
                mPresenter.saveTTSConfig(engineInfo.name);
                dialog.dismiss();
            }
        }, getFragmentManager());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSyncView(String event) {
        if (Constant.EVENT_UPDATE_REPORT_SYNC.equalsIgnoreCase(event)) {
            mPresenter.getSyncData();
        }
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @OnClick(R.id.sound_options)
    public void onSoundOptionsClicked() {
        mSoundDialog.show(getFragmentManager());
    }

    @OnClick(R.id.id_fg_setting_tv_health_data)
    public void onHealthDataClick() {
        start(new HealthDataFragment());
    }

    @OnClick(R.id.id_fg_setting_tv_unit_setting)
    public void onOnUnitSettingClicked() {
        start(new UnitSettingFragment());
    }

    @OnClick(R.id.id_fg_setting_tv_language_setting_setting)
    public void onLanguageSettingClicked() {
        mLanguageDialog.show(true);
    }

    @Override
    public void onObtainLanguage(int id, String language) {
        Log.d(TAG, "onObtainLanguage: id = " + id);
        Log.d(TAG, "onObtainLanguage: language = " + language);
        //更新数据
        mTvLanguageSetting.setText(getString(R.string.language_select) + " - " + language);
        mPresenter.saveLanguage(id);

        //更新语言
        Util.setLanguage(_mActivity);
        Util.restartApp(_mActivity, MainActivity.class);
    }

    @OnClick(R.id.id_fg_setting_tv_share)
    public void onShareClicked() {
        Intent shareIntent = Util.getShareIntent(_mActivity);
        startActivity(shareIntent);
    }

    @Override
    public void onObtainSoundOption(boolean isMute, boolean voice, boolean trainVoice) {
        //更新数据库
        mPresenter.saveVoiceOption(isMute, voice, trainVoice);
    }

    @Override
    public void setLanguage(int flag, String language) {
        mTvLanguageSetting.setText(getString(R.string.language_select) + " - " + language);
    }

    @OnClick(R.id.id_fg_setting_tv_delete_all_data)
    public void onDeleteAllDataClick() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(_mActivity)
                .setMessage(R.string.delete_all_data)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteAllData();
                        Util.restartApp(_mActivity, MainActivity.class);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.id_fg_setting_tv_download_tts)
    public void onDownlodTTSClicked() {
        Util.searchFromMarket(_mActivity, "text to speech");
    }

    @OnClick(R.id.id_fg_setting_tv_rate_us)
    public void onRateUsClicked() {
        Util.openAppInPlay(_mActivity, _mActivity.getPackageName());
    }

    @OnClick(R.id.select_engine)
    public void onSelectEngine() {
        showSingleChoiceDialogFragment();
    }


    @OnClick(R.id.id_sync_ll_sync_container)
    public void onSyncClicked() {
        //https://developers.google.com/identity/sign-in/android/
        //1.登录谷歌账号->
        //2.登录成功->授权google fit
        //3.允许->弹出同步框

//        if (Util.isApkAvailable(_mActivity, "com.google.android.gms")){
//            mPresenter.loginGoogle(this,RC_SIGN_IN);
//        }else {
//            showToast("没安装谷歌服务");
//        }

        showToast(R.string.sync);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: REQUEST_OAUTH_REQUEST_CODE");
            //获取上一次的同步时间
            mPresenter.saveSyncState(true);
            EventBus.getDefault().post(Constant.EVENT_UPDATE_REPORT_SYNC);
            showSyncDialog();
        }

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            handleLoginGoogle(data);
        }
    }

    private void handleLoginGoogle(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            mPresenter.saveSyncAccount(account.getEmail());
            mPresenter.connectGoogleFit(SettingsFragment.this, REQUEST_OAUTH_REQUEST_CODE);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void showSyncDialog() {
        Log.d(TAG, "showSyncDialog: ");
        mSyncGoogleFitDialog = new SyncGoogleFitDialog(_mActivity);
        mSyncGoogleFitDialog.show(true);
    }

    @Override
    public void hideSyncDialog() {
        mSyncGoogleFitDialog.destroy();
    }

    @Override
    public void setSync(String account, String time) {
        mTvAccount.setText(account);
        mTvSyncTime.setText(getString(R.string.last_sync) + ": " + time);
        mTvSyncTime.setTextColor(getResources().getColor(R.color.green_16));
    }

    @OnClick(R.id.id_fg_setting_tv_reminder)
    public void onRemindClicked() {
        start(new ReminderFragment());
    }

    @OnClick(R.id.id_fg_setting_tv_feedback)
    public void onFeedBackClicked() {
        mPresenter.sendFeedBack(_mActivity);
    }
}
