package com.allever.lose.weight.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.allever.lib.common.util.ActivityCollector;
import com.allever.lib.recommend.RecommendActivity;
import com.allever.lib.umeng.UMeng;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.bean.MenuEvent;
import com.allever.lose.weight.ui.mvp.presenter.HomePresenter;
import com.allever.lose.weight.ui.mvp.view.IHomeView;
import com.allever.lose.weight.ui.adapter.TabAdapter;
import com.allever.lose.weight.base.BaseMainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mac on 2018/3/1.
 */

public class HomeFragment extends BaseMainFragment<IHomeView, HomePresenter> implements IHomeView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;

    private int mPageIndex;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//加上这句话，menu才会显示出来
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        EventBus.getDefault().register(this);

        if (savedInstanceState != null) {
            mPageIndex = savedInstanceState.getInt(Constant.EXTRA_MAIN_PAGE_INDEX, 0);
        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.app_bar_title);
        initToolbarNav(toolbar, true);


        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            appCompatActivity.setSupportActionBar(toolbar);
        }

        setTabLayout();

        return view;
    }

    private void setTabLayout() {
        tabLayout.setSelectedTabIndicatorColor(0xFFFFFFFF);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_30days));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_tourines));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_reports));
        TabAdapter mAdapter = new TabAdapter(getChildFragmentManager(), getString(R.string.tab_30days), getString(R.string.tab_tourines), getString(R.string.tab_reports));
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(mPageIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartPage(MenuEvent event) {
        if (Constant.EVENT_MENU_START_HOME_PAGE.equals(event.event)) {
            viewpager.setCurrentItem(event.pageIndex);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.recommend) {
            Activity activity = getActivity();
            if (activity != null) {
                RecommendActivity.Companion.start(activity, UMeng.INSTANCE.getChannel());
            }

        }
        if (id == R.id.homeAsUp) {
            mOpenDraweListener.onOpenDrawer();
        }
        return super.onOptionsItemSelected(item);
    }
}
