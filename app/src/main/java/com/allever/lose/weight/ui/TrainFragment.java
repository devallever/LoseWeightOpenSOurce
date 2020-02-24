package com.allever.lose.weight.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.ui.mvp.presenter.TrainPresenter;
import com.allever.lose.weight.ui.mvp.view.ITrainView;
import com.allever.lose.weight.bean.DayInfoBean;
import com.allever.lose.weight.ui.adapter.TrainItemAdapter;
import com.allever.lose.weight.base.BaseMainFragment;
import com.allever.lose.weight.util.ScreenUtils;
import com.allever.lose.weight.ui.view.widget.SpacesItemDecoration;
import com.github.mikephil.charting.formatter.IFillFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainFragment extends BaseMainFragment<ITrainView, TrainPresenter> implements ITrainView {
    @BindView(R.id.image_workout)
    ImageView image;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_day_left)
    TextView mTvTimeLeft;
    @BindView(R.id.tv_progress)
    TextView finishPercent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.bannerContainer)
    ViewGroup bannerContainer;
    Unbinder unbinder;
    private List<DayInfoBean> mDayInfoBeanList = new ArrayList<>();
    private TrainItemAdapter mAdapter;

    public static TrainFragment newInstance() {
        return new TrainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        refreshView();

        return view;
    }

    private void initView() {
        mAdapter = new TrainItemAdapter(mDayInfoBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dp2px(10)));

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(_mActivity, ActionMainActivity.class);
                intent.putExtra(Constant.EXTRA_DAY_ID, mDayInfoBeanList.get(position).getType());
                intent.putExtra(Constant.EXTRA_DAY_TEXT, mDayInfoBeanList.get(position).getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected TrainPresenter createPresenter() {
        return new TrainPresenter();
    }

    @Override
    public void setDayLeft(int leftDay, int progress, int total, int percent) {
        mTvTimeLeft.setText(leftDay + getResources().getString(R.string.day_left));
        mProgressBar.setMax(total);
        mProgressBar.setProgress(progress);
        finishPercent.setText(percent + "%");
    }

    @Override
    public void setTrainList(List<DayInfoBean> dayInfoBeanList) {
        mDayInfoBeanList.clear();
        for (DayInfoBean dayInfoBean : dayInfoBeanList) {
            mDayInfoBeanList.add(dayInfoBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshView() {
        mPresenter.getLeftDay();
        mPresenter.getDayList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshView(String event) {
        if (Constant.EVENT_REFRESH_VIEW.equals(event)) {
            refreshView();
        }
    }
}
