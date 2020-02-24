package com.allever.lose.weight.ui;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.mvp.presenter.ReminderPresenter;
import com.allever.lose.weight.ui.mvp.view.IReminderView;
import com.allever.lose.weight.ui.adapter.RemindRecAdapter;
import com.allever.lose.weight.util.ScreenUtils;
import com.allever.lose.weight.ui.view.widget.SpacesItemDecoration;
import com.allever.lose.weight.ui.dialog.TimePickerFragment;
import com.allever.lose.weight.ui.dialog.WeekChoiceDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Mac on 2018/3/1.
 */

public class ReminderFragment extends BaseFragment<IReminderView, ReminderPresenter> implements IReminderView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.add_reminder)
    FloatingActionButton addReminder;
    public static final String TAG = "ReminderFragment";
    private RemindRecAdapter mAdapter;

    private List<Config.Reminder> mReminderList;

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        unbinder = ButterKnife.bind(this, view);

        initToolbar(mToolbar, R.string.remind);

        initViewAndSetListner();

        return view;
    }

    private void initViewAndSetListner() {
        mReminderList = mPresenter.getReminderList();
        mAdapter = new RemindRecAdapter(mPresenter.getReminderList());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dp2px(10)));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemChildClick: reminderItem position = " + position);
                switch (view.getId()) {
                    case R.id.delete:
                        //删除提醒记录
                        mPresenter.deleteReminder(mReminderList.get(position));
                        break;
                    case R.id.reminder_switch:
                        Switch aSwitch = (Switch) view.findViewById(R.id.reminder_switch);
                        Config.Reminder reminder = mReminderList.get(position);
                        reminder.setRemindSwitch(aSwitch.isChecked());
                        if (aSwitch.isChecked()) {
                            mPresenter.setReminder(_mActivity, mReminderList.get(position));
                        } else {
                            mPresenter.cancelReminder(_mActivity, mReminderList.get(position));
                        }
                        //更新数据库
                        mPresenter.updateReminder(position, reminder);
                        break;
                    case R.id.id_item_remind_tv_repeat:
                        showWeek(mReminderList.get(position));
                        break;
                    default:
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected ReminderPresenter createPresenter() {
        return new ReminderPresenter();
    }

    @OnClick(R.id.add_reminder)
    public void onViewClicked() {
        final Config.Reminder reminder = new Config.Reminder();
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, "hour:" + hourOfDay + "min:" + minute);
                reminder.setHour(hourOfDay);
                reminder.setMinute(minute);
                reminder.setSecond(0);
                reminder.setRemindSwitch(true);
                reminder.setSunRepeat(true);
                reminder.setMonRepeat(true);
                reminder.setTueRepeat(true);
                reminder.setWebRepeat(true);
                reminder.setThurRepeat(true);
                reminder.setFriRepeat(true);
                reminder.setSatRepeat(true);

                showWeek(reminder);
            }
        });
    }

    public void showWeek(final Config.Reminder reminder) {
        if (reminder == null) {
            return;
        }
        String title = getString(R.string.repeat);
        WeekChoiceDialogFragment weekChoiceDialogFragment = new WeekChoiceDialogFragment();
        weekChoiceDialogFragment.show(reminder, title, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.d(TAG, "onClick: which = " + which);
                //0 -> 星期日
                //1 -> 星期一
                switch (which) {
                    case 0:
                        reminder.setSunRepeat(isChecked);
                        break;
                    case 1:
                        reminder.setMonRepeat(isChecked);
                        break;
                    case 2:
                        reminder.setTueRepeat(isChecked);
                        break;
                    case 3:
                        reminder.setWebRepeat(isChecked);
                        break;
                    case 4:
                        reminder.setThurRepeat(isChecked);
                        break;
                    case 5:
                        reminder.setFriRepeat(isChecked);
                        break;
                    case 6:
                        reminder.setSatRepeat(isChecked);
                        break;
                    default:
                        break;
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ok 添加(更新)一条提醒记录
                mPresenter.addReminder(reminder);
                mPresenter.setReminder(_mActivity, reminder);

            }
        }, getFragmentManager());
    }

    @Override
    public void updateRemindList(List<Config.Reminder> reminderList) {
        mReminderList = reminderList;
        mAdapter.notifyDataSetChanged();
    }

}
