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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.mvp.presenter.RoutinePresenter;
import com.allever.lose.weight.ui.mvp.view.IRoutineView;
import com.allever.lose.weight.bean.RoutineItem;
import com.allever.lose.weight.ui.adapter.RoutinesItemAdapter;
import com.allever.lose.weight.util.ScreenUtils;
import com.allever.lose.weight.ui.view.widget.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutinesFragment extends BaseFragment<IRoutineView, RoutinePresenter> implements IRoutineView {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Unbinder unbinder;

    private List<RoutineItem> mRoutineItemList = new ArrayList<>();

    private RoutinesItemAdapter trainItemAdapter;

    public static RoutinesFragment newInstance() {
        return new RoutinesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_routines, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        initView();
        mPresenter.getRoutineDataList();

        return view;
    }

    private void initView() {
        trainItemAdapter = new RoutinesItemAdapter(mRoutineItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(trainItemAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dp2px(10)));

        trainItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(_mActivity, ActionMainActivity.class);
                intent.putExtra(Constant.EXTRA_DAY_ID, mRoutineItemList.get(position).getId());
                intent.putExtra(Constant.EXTRA_DAY_TEXT, mRoutineItemList.get(position).getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected RoutinePresenter createPresenter() {
        return new RoutinePresenter();
    }

    @Override
    public void setRoutineList(List<RoutineItem> routineItemList) {
        mRoutineItemList.clear();
        for (RoutineItem routineItem : routineItemList) {
            mRoutineItemList.add(routineItem);
        }
        trainItemAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshView(String event) {
        if (Constant.EVENT_REFRESH_VIEW.equals(event)) {
            mPresenter.getRoutineDataList();
        }
    }
}
