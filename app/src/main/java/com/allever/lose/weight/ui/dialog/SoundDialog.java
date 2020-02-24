package com.allever.lose.weight.ui.dialog;

import android.app.Activity;

import androidx.appcompat.widget.SwitchCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.base.BaseDialog;

/**
 * Created by Mac on 18/3/14.
 */

public class SoundDialog extends BaseDialog {
    private ISoundListener mListener;

    public SoundDialog(Activity activity, ISoundListener soundListener) {
        super(activity);
        mListener = soundListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sound, null, false);
        TextView tvOk = (TextView) view.findViewById(R.id.id_dialog_sound_tv_ok);

        final SwitchCompat swMute = (SwitchCompat) view.findViewById(R.id.id_dialog_sound_switch_mute);
        final SwitchCompat swVoice = (SwitchCompat) view.findViewById(R.id.id_dialog_sound_switch_voice);
        final SwitchCompat swTrainVoice = (SwitchCompat) view.findViewById(R.id.id_dialog_sound_switch_train_voice);

        swMute.setChecked(GlobalData.config.isMuteOption());
        swVoice.setChecked(GlobalData.config.isVoiceOption());
        swTrainVoice.setChecked(GlobalData.config.isTrainVoiceOption());

        swMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swMute.isChecked()) {
                    swVoice.setChecked(false);
                    swTrainVoice.setChecked(false);
                } else {
                    swVoice.setChecked(true);
                    swTrainVoice.setChecked(true);
                }
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainSound(swMute.isChecked(), swVoice.isChecked(), swTrainVoice.isChecked());
                }
                hide();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.id_dialog_sound_tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSoundDialogCancel();
                }
                hide();
            }
        });

        return view;
    }

    @Override
    public void show() {
        super.show(false);
    }

    public interface ISoundListener {
        void onObtainSound(boolean muteOption, boolean voiceOption, boolean trainVoiceOption);

        void onSoundDialogCancel();
    }
}
