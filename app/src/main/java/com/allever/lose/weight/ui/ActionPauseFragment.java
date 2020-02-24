package com.allever.lose.weight.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.presenter.ActionPausePresenter;
import com.allever.lose.weight.ui.mvp.view.IActionPauseView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/3/1.
 */

public class ActionPauseFragment extends BaseFragment<IActionPauseView, ActionPausePresenter> implements IActionPauseView {
    private static final String TAG = "ActionPauseFragment";

    @BindView(R.id.id_fg_action_pause_iv_guide)
    ImageView mIvGuide;
    @BindView(R.id.id_fg_action_pause_iv_close)
    ImageView mIvClose;
    @BindView(R.id.id_fg_action_pause_tv_video)
    TextView mTvVideo;
    @BindView(R.id.id_fg_action_pause_tv_name)
    TextView mTvName;
    @BindView(R.id.id_fg_action_pause_tv_action_desc)
    TextView mTvDesc;
    @BindView(R.id.id_action_pause_tv_left_time)
    TextView mTvLeftTime;
    @BindView(R.id.id_fg_action_pause_progress_bar)
    ProgressBar mProgressBar;


    private Unbinder mButterKnifeBinder;
    private AnimationDrawable mAnimationDrawable;

    private int mDayId = 1;
    private int mActionId = 1;
    private int mDuration = 0;

    public static ActionPauseFragment newInstance(int dayId, int actionId, int duration) {
        ActionPauseFragment fragment = new ActionPauseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_DAY_ID, dayId);
        bundle.putInt(Constant.EXTRA_ACTION_ID, actionId);
        bundle.putInt(Constant.EXTRA_DURATION, duration);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_action_pause, container, false);
        mButterKnifeBinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mActionId = bundle.getInt(Constant.EXTRA_ACTION_ID);
            mDayId = bundle.getInt(Constant.EXTRA_DAY_ID);
            mDuration = bundle.getInt(Constant.EXTRA_DURATION);
        }

        initView();
        mPresenter.getActionData(mDayId, mActionId);
        mPresenter.getLeftTime(mDayId, mActionId, mDuration);
        mPresenter.getCurrentLevel(mDayId, mActionId);
        return view;
    }

    @Override
    public void onDestroyView() {
        mButterKnifeBinder.unbind();
        super.onDestroyView();
    }

    private void initView() {
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void setData(final ActionItem actionItem) {
        if (actionItem == null) {
            return;
        }
        mAnimationDrawable = actionItem.getAnimationDrawable();
        mIvGuide.setImageDrawable(mAnimationDrawable);
        mAnimationDrawable.setOneShot(false);
        mAnimationDrawable.start();

        mTvName.setText(actionItem.getName());
        StringBuilder stringBuilder = new StringBuilder();
        for (String desc : actionItem.getDescList()) {
            stringBuilder.append("\n" + desc + "\n");
        }
        mTvDesc.setText(stringBuilder.toString());

//        mTvVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showToast(actionItem.getVideoUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionItem.getVideoUrl()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setPackage("com.google.android.youtube");
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public void setLeftTime(String time) {
        mTvLeftTime.setText(time);
    }

    @Override
    public void setCurrent(int currentLevel, int levelCount) {
        mProgressBar.setMax(levelCount);
        mProgressBar.setProgress(currentLevel);
    }

    @Override
    protected ActionPausePresenter createPresenter() {
        return new ActionPausePresenter();
    }

    @Override
    public boolean onBackPressedSupport() {
        Log.d(TAG, "onBackPressedSupport: ");
        EventBus.getDefault().post(Constant.EVENT_ON_RESTART_ACTION);
        return super.onBackPressedSupport();
    }
}
