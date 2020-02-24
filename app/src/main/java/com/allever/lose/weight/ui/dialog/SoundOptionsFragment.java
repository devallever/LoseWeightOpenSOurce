package com.allever.lose.weight.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.GlobalData;

/**
 * Created by Mac on 2018/3/6.
 */

public class SoundOptionsFragment extends DialogFragment {
    private static final String TAG = "SoundOptionsFragment";
    private Activity mActivity;
    private ISoundListener mListener;
    private View.OnClickListener mCancelListener;

    public SoundOptionsFragment() {
    }

    @SuppressLint("ValidFragment")
    public SoundOptionsFragment(Activity activity, ISoundListener soundListener) {
        mActivity = activity;
        mListener = soundListener;
    }

    @SuppressLint("ValidFragment")
    public SoundOptionsFragment(Activity activity, ISoundListener soundListener, View.OnClickListener cancelListener) {
        mActivity = activity;
        mListener = soundListener;
        mCancelListener = cancelListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sound_options);
        builder.setCancelable(false);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sound_options, null);

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

        TextView tvOk = (TextView) view.findViewById(R.id.id_dialog_sound_tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onObtainSoundOption(swMute.isChecked(), swVoice.isChecked(), swTrainVoice.isChecked());
                }
                dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.id_dialog_sound_tv_cancel);
        if (mCancelListener != null) {
            tvCancel.setOnClickListener(mCancelListener);
        } else {
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        builder.setView(view);
        return builder.create();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "SoundOptionsFragment");
    }

    public interface ISoundListener {
        void onObtainSoundOption(boolean isMute, boolean voice, boolean trainVoice);
    }

}
