package com.allever.lose.weight.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.dialog.GenderDialog;
import com.allever.lose.weight.ui.mvp.presenter.HealthDataPresenter;
import com.allever.lose.weight.ui.mvp.view.IHealthDataView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/3/13.
 */

public class HealthDataFragment extends BaseFragment<IHealthDataView, HealthDataPresenter> implements IHealthDataView,
        GenderDialog.IGenderListener,
        DatePickerDialog.OnDateSetListener {
    private static final String TAG = "HealthDataFragment";

    @BindView(R.id.id_fg_health_data_tv_gender)
    TextView mTvGender;
    @BindView(R.id.id_fg_health_data_ll_gender_container)
    LinearLayout mLlGenderContainer;
    @BindView(R.id.id_fg_health_data_tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.id_fg_health_data_ll_birthday_container)
    LinearLayout mLlBirthdayContainer;
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;


    private Unbinder mButterKnifeBinder;
    private GenderDialog mGenderDialog;
    private DatePickerDialog mBirthdayDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.fragment_health_data, container, false);
        mButterKnifeBinder = ButterKnife.bind(this, view);
        initToolbar(mToolbar, R.string.health_data);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        initDialog();

        mPresenter.getGender();
        mPresenter.getBirthday();
        return view;
    }

    private void initDialog() {
        mGenderDialog = new GenderDialog(_mActivity, this);
        mBirthdayDialog = new DatePickerDialog(_mActivity, this, mYear, mMonth, mDay);

    }

    @Override
    public void onDestroyView() {
        mButterKnifeBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void setGender(String gender) {
        mTvGender.setText(gender);
    }

    @Override
    public void setBirthday(String birthday) {
        mTvBirthday.setText(birthday);
    }

    @Override
    public void showGenderDialog() {
        mGenderDialog.show(true);
    }

    @Override
    public void showBirthdayDialog() {
        mBirthdayDialog.show();
    }

    @Override
    public void hideGenderDialog() {
        mGenderDialog.hide();
    }

    @Override
    public void hideBirthdayDialog() {
        mBirthdayDialog.hide();
    }

    @Override
    protected HealthDataPresenter createPresenter() {
        return new HealthDataPresenter();
    }

    @OnClick({R.id.id_fg_health_data_ll_gender_container, R.id.id_fg_health_data_ll_birthday_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_fg_health_data_ll_gender_container:
                showGenderDialog();
                break;
            case R.id.id_fg_health_data_ll_birthday_container:
                showBirthdayDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onObtainGender(int gender, String text) {
        Log.d(TAG, "onObtainGender: gender = " + gender);
        //更新数据库
        mPresenter.updateGender(gender);
        mTvGender.setText(text);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        //回调月份从0开始
        mMonth = month + 1;
        mDay = dayOfMonth;
        Log.d(TAG, "onDateSet: year = " + year);
        Log.d(TAG, "onDateSet: month = " + mMonth);
        Log.d(TAG, "onDateSet: day = " + dayOfMonth);
        //更新数据库
        mTvBirthday.setText(year + "-" + mMonth + "-" + dayOfMonth);
        mPresenter.updateBirthday(year, mMonth, dayOfMonth);
    }
}
