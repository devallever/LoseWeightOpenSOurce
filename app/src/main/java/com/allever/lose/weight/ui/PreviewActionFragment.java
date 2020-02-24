package com.allever.lose.weight.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.ui.adapter.ActionQuickAdapter;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.bean.ActionItem;
import com.allever.lose.weight.ui.mvp.presenter.PreviewActionPresenter;
import com.allever.lose.weight.ui.mvp.view.IPreviewActionView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/2/27.
 */

public class PreviewActionFragment extends BaseFragment<IPreviewActionView, PreviewActionPresenter> implements IPreviewActionView {
    @BindView(R.id.id_preview_action_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_preview_action_btn_start)
    Button mBtnStart;
    @BindView(R.id.id_preview_action_btn_end)
    Button mBtnEnd;
    @BindView(R.id.id_preview_action_ll_rest_container)
    LinearLayout mLlRestContainer;
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;

    //第几天id
    private int mDayId = 1;
    //先从数据库中读取进度，进度100% 或 0 从当天第一个actionId开始读取
    private int mActionId;
    //标题
    private String mTitle = "";
    private Unbinder mButterKnifeBinder;

    public static PreviewActionFragment newInstance(int dayId, String title) {
        PreviewActionFragment fragment = new PreviewActionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_DAY_ID, dayId);
        bundle.putString(Constant.EXTRA_DAY_TEXT, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mDayId = args.getInt(Constant.EXTRA_DAY_ID);
            mTitle = args.getString(Constant.EXTRA_DAY_TEXT);
        }

        View view = inflater.inflate(R.layout.fragment_preview_action, container, false);
        mButterKnifeBinder = ButterKnife.bind(this, view);

        initView();

        mPresenter.getActionPreview(mDayId);

        return view;
    }

    @Override
    public void onDestroyView() {
        mButterKnifeBinder.unbind();
        super.onDestroyView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to action ready
                GlobalData.initTime();
                GlobalData.startTime = System.currentTimeMillis();
                startWithPop(ActionReadyFragment.newInstance(mDayId, mActionId));
            }
        });

        initToolbar(mToolbar, mTitle);
    }

    @Override
    public void setPreviewData(List<ActionItem> actionItemList) {
        if (actionItemList == null) {
            return;
        }
        if (actionItemList.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mBtnStart.setVisibility(View.INVISIBLE);

            mLlRestContainer.setVisibility(View.VISIBLE);
            mBtnEnd.setVisibility(View.VISIBLE);

            mBtnEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _mActivity.onBackPressed();
                }
            });

            //ScheduleRecord添加一条记录
            mPresenter.saveRestScheduleRecord(mDayId);
            EventBus.getDefault().post(Constant.EVENT_REFRESH_VIEW);
            return;
        }
        ActionQuickAdapter mAdapter = new ActionQuickAdapter(getActivity(), actionItemList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.action_footer, mRecyclerView, false));

        mActionId = actionItemList.get(0).getActionId();

    }

    @Override
    protected PreviewActionPresenter createPresenter() {
        return new PreviewActionPresenter();
    }
}
