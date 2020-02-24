package com.allever.lose.weight.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.mvp.presenter.HistoryPresenter;
import com.allever.lose.weight.ui.mvp.view.IHistoryView;
import com.allever.lose.weight.bean.ExerciseRecordItem;
import com.allever.lose.weight.ui.adapter.ExerciseRecordItemAdapter;
import com.allever.lose.weight.ui.view.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Mac on 2018/3/1.
 */

public class HistoryFragment extends BaseFragment<IHistoryView, HistoryPresenter> implements IHistoryView, CalendarView.OnDateSelectedListener {
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    private ExerciseRecordItemAdapter mAdapter;
    private List<ExerciseRecordItem> mExerciseRecordItemList = new ArrayList<>();

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();

        mPresenter.getRecordList();
        mPresenter.getCalendarData();

        return view;
    }

    private void initView() {
        initToolbar(mToolbar, R.string.history);

        mAdapter = new ExerciseRecordItemAdapter(mExerciseRecordItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL_LIST));

    }

    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        Log.i("HistoryFragment", "click:" + isClick);
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getYear() + "/" + calendar.getMonth());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected HistoryPresenter createPresenter() {
        return new HistoryPresenter();
    }

    @Override
    public void setRecordList(List<ExerciseRecordItem> exerciseRecordItemList) {
        mExerciseRecordItemList.clear();
        mExerciseRecordItemList.addAll(exerciseRecordItemList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCalendar(List<Person.ExerciseRecord> exerciseRecordList) {
        if (exerciseRecordList == null) {
            return;
        }
        mTextLunar.setText(R.string.today);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        List<Calendar> schemes = new ArrayList<>();

        for (Person.ExerciseRecord exerciseRecord : exerciseRecordList) {
            schemes.add(getSchemeCalendar(exerciseRecord.getYear(), exerciseRecord.getMonth(), exerciseRecord.getDay(), "100"));
        }
        mCalendarView.setSchemeDate(schemes);
        mCalendarView.setOnDateSelectedListener(this);
    }
}
