package com.allever.lose.weight.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.dialog.ExitActionDialog;
import com.allever.lose.weight.ui.mvp.presenter.ActionNextPresenter;
import com.allever.lose.weight.ui.mvp.view.IActionNextView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/3/1.
 */

public class ActionNextFragment extends BaseFragment<IActionNextView, ActionNextPresenter> implements IActionNextView {
    private static final int DIALOG_EXIT = 0x01;
    private static final int DIALOG_DELAY = 0x02;
    private static final int DIALOG_CLOSE = 0x03;

    @BindView(R.id.id_fg_action_next_iv_guide)
    ImageView mIvGuide;
    @BindView(R.id.id_fg_action_next_tv_time)
    TextView mTvTime;
    @BindView(R.id.id_fg_action_next_tv_progress)
    TextView mTvCurrentLevel;
    @BindView(R.id.id_fg_action_next_tv_name)
    TextView mTvName;
    @BindView(R.id.id_fg_action_next_progress_bar)
    CircleProgressBar mCircleProgressBar;
    @BindView(R.id.id_fg_action_next_h_progress)
    ProgressBar mHProgressBar;
    @BindView(R.id.id_fg_action_next_tv_skip)
    TextView mTvSkip;

    private Unbinder mButterKnifeBinder;
    private AnimationDrawable mAnimationDrawable;
    private ValueAnimator mValueAnimator;

    private int mTime = 30;
    private int mCurrentProgress = 0;

    private int mDayId;
    private int mActionId;

    private ExitActionDialog mExitDialog;
    private int mDialogOption = DIALOG_CLOSE;

    public static ActionNextFragment newInstance(int dayId, int actionId) {
        ActionNextFragment fragment = new ActionNextFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_DAY_ID, dayId);
        bundle.putInt(Constant.EXTRA_ACTION_ID, actionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_action_next, container, false);
        mButterKnifeBinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDayId = bundle.getInt(Constant.EXTRA_DAY_ID);
            mActionId = bundle.getInt(Constant.EXTRA_ACTION_ID);
        }

        mPresenter.getCurrentActionId(mDayId);
        mPresenter.getActionData(mDayId, mActionId);
        mPresenter.getCurrentLevel(mDayId, mActionId);

        initView();

        startTimeCounter();

        return view;
    }

    private void initView() {
        mCircleProgressBar.setMax(mTime * 1000);

        mExitDialog = new ExitActionDialog.Builder(_mActivity)
                .setExitListener(new ExitActionDialog.ClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog) {
                        mDialogOption = DIALOG_EXIT;
                        hideExitDialog();
                        _mActivity.onBackPressed();
                        mPresenter.saveExerciseRecord(mDayId);
                        EventBus.getDefault().post(Constant.EVENT_REFRESH_VIEW);
                    }
                })
                .setCloseListener(new ExitActionDialog.ClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog) {
                        mDialogOption = DIALOG_CLOSE;
                        hideExitDialog();
                        startTimeCounter();
                        mAnimationDrawable.start();
                    }
                })
                .setDelayListener(new ExitActionDialog.ClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog) {
                        mDialogOption = DIALOG_DELAY;
                        hideExitDialog();
                        _mActivity.onBackPressed();
                        mPresenter.saveExerciseRecord(mDayId);
                        EventBus.getDefault().post(Constant.EVENT_REFRESH_VIEW);
                    }
                }).build();
    }

    @Override
    public void onDestroyView() {
        mButterKnifeBinder.unbind();
        mValueAnimator.cancel();
        mAnimationDrawable.stop();
        super.onDestroyView();
    }

    @Override
    public void setData(ActionItem actionItem) {
        if (actionItem == null) {
            return;
        }
        mAnimationDrawable = actionItem.getAnimationDrawable();
        mIvGuide.setImageDrawable(mAnimationDrawable);
        mAnimationDrawable.setOneShot(false);
        mAnimationDrawable.start();

        mTvName.setText(actionItem.getName());

        mPresenter.speak(getString(R.string.next), TextToSpeech.QUEUE_ADD);
        mPresenter.speak(actionItem.getName(), TextToSpeech.QUEUE_ADD);
    }

    @Override
    public void setCurrent(int currentLevel, int levelCount) {
        mTvCurrentLevel.setText(currentLevel + "/" + levelCount);
        mHProgressBar.setMax(levelCount);
        mHProgressBar.setProgress(currentLevel);
    }

    @Override
    protected ActionNextPresenter createPresenter() {
        return new ActionNextPresenter();
    }

    private void startTimeCounter() {
        mValueAnimator = ValueAnimator.ofInt(mCurrentProgress, mTime * 1000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mTime * 1000 - mCurrentProgress);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentProgress = (Integer) animation.getAnimatedValue();
                mTvTime.setText((mTime - mCurrentProgress / 1000) + "\"");
                mCircleProgressBar.setProgress(mCurrentProgress);
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationDrawable.stop();
                //start actionFragment
                mValueAnimator.cancel();
                if (mCurrentProgress == mTime * 1000) {
                    //release
                    startWithPop(ActionFragment.newInstance(mDayId, mActionId));
                }
            }
        });
        mValueAnimator.start();
    }

    public void showExitDialog() {
        mExitDialog.show();
    }

    public void hideExitDialog() {
        mExitDialog.hide();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mDialogOption == DIALOG_CLOSE) {
            showExitDialog();
            mValueAnimator.cancel();
            mAnimationDrawable.stop();
            return true;
        }
        return super.onBackPressedSupport();
    }

    @OnClick(R.id.id_fg_action_next_tv_skip)
    public void onClickSkip() {
        mValueAnimator.cancel();
        mAnimationDrawable.stop();
        startWithPop(ActionFragment.newInstance(mDayId, mActionId));
    }

    @Override
    public void setCurrentActionId(int actionId) {
        mActionId = actionId;
    }

}
