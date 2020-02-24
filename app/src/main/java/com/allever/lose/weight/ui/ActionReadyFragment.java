package com.allever.lose.weight.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.presenter.ActionReadyPresenter;
import com.allever.lose.weight.ui.mvp.view.IActionReadyView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.util.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/2/28.
 */

public class ActionReadyFragment extends BaseFragment<IActionReadyView, ActionReadyPresenter> implements IActionReadyView {
    private static final String TAG = "ActionReadyFragment";

    @BindView(R.id.id_fg_action_ready_iv_guide)
    ImageView mIvGuide;
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_fg_action_ready_progress_bar)
    CircleProgressBar mCircleProgressBar;
    @BindView(R.id.id_fg_action_ready_tv_time)
    TextView mTvTime;
    @BindView(R.id.id_fg_action_ready_btn_start_pause)
    FloatingActionButton mBtnStartPause;
    @BindView(R.id.id_fg_action_ready_tv_skip)
    TextView mTvSkip;
    @BindView(R.id.id_fg_action_ready_tv_desc)
    TextView mTvDesc;

    private AnimationDrawable mAnimationDrawable;
    private ValueAnimator mValueAnimator;

    //倒计时的时长
    private int mTime = 10;
    private int mCurrentProgress = 0;

    //该天某项目id：应该从数据库中获取，先默认为1
    private int mActionId = 1;
    //第几天id
    private int mDayId = 1;

    private ActionItem mActionItem;
    private Unbinder mButterKnifeBinder;

    public static ActionReadyFragment newInstance(int dayId, int actionId) {
        ActionReadyFragment fragment = new ActionReadyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_DAY_ID, dayId);
        bundle.putInt(Constant.EXTRA_ACTION_ID, actionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        mButterKnifeBinder.unbind();
        mValueAnimator.cancel();
        mAnimationDrawable.stop();
        super.onDestroyView();
    }

    @Override
    protected ActionReadyPresenter createPresenter() {
        return new ActionReadyPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_action_ready, container, false);
        mButterKnifeBinder = ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            mDayId = args.getInt(Constant.EXTRA_DAY_ID);
            mActionId = args.getInt(Constant.EXTRA_ACTION_ID);
        }

        initView();
        initToolbar(mToolbar, R.string.ready);

        mPresenter.getCurrentActionId(mDayId);
        mPresenter.getActionData(mDayId, mActionId);
        mPresenter.speak(getString(R.string.ready_start), TextToSpeech.QUEUE_ADD);
        mPresenter.speak(mActionItem.getName(), TextToSpeech.QUEUE_ADD);
        mPresenter.speak(String.valueOf(mActionItem.getTime()), TextToSpeech.QUEUE_ADD);

        startTimeCounter();

        return view;
    }

    private void initView() {
        mCircleProgressBar.setMax(mTime * 1000);

        mBtnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mValueAnimator.isRunning()) {
                    mValueAnimator.cancel();
                    mAnimationDrawable.stop();
                    mBtnStartPause.setImageResource(R.drawable.exo_controls_play);
                    //暂停运动计时
                    mPresenter.setPauseTime();
                } else {
                    startTimeCounter();
                    mAnimationDrawable.start();
                    mBtnStartPause.setImageResource(R.drawable.exo_controls_pause);
                    //恢复运动计时
                    mPresenter.setReStartTime();
                    mPresenter.setPauseDuration();
                }
            }
        });

        mTvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValueAnimator.cancel();
                mAnimationDrawable.stop();
                startWithPop(ActionFragment.newInstance(mDayId, mActionId));
            }
        });

    }

    private void startTimeCounter() {
        mValueAnimator = ValueAnimator.ofInt(mCurrentProgress, mTime * 1000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mTime * 1000 - mCurrentProgress);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentProgress = (Integer) animation.getAnimatedValue();
                int time = (mTime - mCurrentProgress / 1000);
                mTvTime.setText(time + "\"");
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
                    mPresenter.speak(getString(R.string.start), TextToSpeech.QUEUE_FLUSH);
                    startWithPop(ActionFragment.newInstance(mDayId, mActionId));
                }
            }
        });
        mValueAnimator.start();
    }

    @Override
    public void setData(ActionItem actionItem) {
        if (actionItem == null) {
            return;
        }
        mActionItem = actionItem;
        mAnimationDrawable = actionItem.getAnimationDrawable();
        mIvGuide.setImageDrawable(mAnimationDrawable);
        mAnimationDrawable.setOneShot(false);
        mAnimationDrawable.start();

        mTvDesc.setText(actionItem.getName() + actionItem.getTimeText());
    }

    @Override
    public void setCurrentActionId(int currentActionId) {
        mActionId = currentActionId;
    }

    @Override
    public void updateLeftTimeText(String time) {
        mTvTime.setText(time);
    }

    @Override
    public boolean onBackPressedSupport() {
        //结束运动
        mPresenter.setEndTime();
        //保存记录
        mPresenter.saveExerciseRecord(mDayId);
        EventBus.getDefault().post(Constant.EVENT_REFRESH_VIEW);
        return super.onBackPressedSupport();
    }
}
