package com.allever.lose.weight.ui.dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mac on 2018/3/7.
 */

public class TTSFragment extends SupportFragment {

    @BindView(R.id.down_tts)
    TextView mDown;
    @BindView(R.id.set_tts)
    TextView mSetting;
    Unbinder unbinder;

    public static TTSFragment newInstance() {
        return new TTSFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tts, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.down_tts)
    public void onDownClicked() {
        Util.searchFromMarket(_mActivity, "text to speech");
    }

    @OnClick(R.id.set_tts)
    public void onSettingClicked() {
        startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
    }
}
