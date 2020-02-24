package com.allever.lose.weight.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.BaseFragment;
import com.allever.lose.weight.ui.dialog.HeightUnitDialog;
import com.allever.lose.weight.ui.dialog.WeightUnitDialog;
import com.allever.lose.weight.ui.mvp.presenter.UnitSettingPresenter;
import com.allever.lose.weight.ui.mvp.view.IUnitSettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mac on 18/3/14.
 */

public class UnitSettingFragment extends BaseFragment<IUnitSettingView, UnitSettingPresenter> implements IUnitSettingView,
        WeightUnitDialog.IWeightUnitListener,
        HeightUnitDialog.IHeightUnitListener {

    private static final String TAG = "UnitSettingFragment";

    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_fg_unit_setting_tv_weight_unit)
    TextView mTvWeightUnit;
    @BindView(R.id.id_fg_unit_setting_ll_weight_container)
    LinearLayout mLlWeightContainer;
    @BindView(R.id.id_fg_unit_setting_tv_height_unit)
    TextView mTvHeightUnit;
    @BindView(R.id.id_fg_unit_setting_ll_height_container)
    LinearLayout mLlHeightContainer;

    private Unbinder mUnbinder;
    private WeightUnitDialog mWeightUnitDialog;
    private HeightUnitDialog mHeightUnitDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.fragment_unit_setting, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.getHeightUnit();
        mPresenter.getWeightUnit();
        initToolbar(mToolbar, R.string.unit_setting);
        initDialog();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void initDialog() {
        mWeightUnitDialog = new WeightUnitDialog(_mActivity, this);
        mHeightUnitDialog = new HeightUnitDialog(_mActivity, this);
    }

    @Override
    public void setWeightUnit(String weightUnit) {
        mTvWeightUnit.setText(weightUnit);
    }

    @Override
    public void setHeightUnit(String heightUnit) {
        mTvHeightUnit.setText(heightUnit);
    }

    @Override
    public void showWeightSelectDialog() {
        mWeightUnitDialog.show(true);
    }

    @Override
    public void showHeightSelectDialog() {
        mHeightUnitDialog.show(true);
    }

    @Override
    public void hideWeightSelectDialog() {
        mWeightUnitDialog.hide();
    }

    @Override
    public void hideHeightSelectDialog() {
        mHeightUnitDialog.hide();
    }

    @Override
    protected UnitSettingPresenter createPresenter() {
        return new UnitSettingPresenter();
    }

    @OnClick({R.id.id_fg_unit_setting_ll_weight_container, R.id.id_fg_unit_setting_ll_height_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_fg_unit_setting_ll_weight_container:
                mWeightUnitDialog.show(true);
                break;
            case R.id.id_fg_unit_setting_ll_height_container:
                mHeightUnitDialog.show(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onObtainWeightUnit(String weightUnit) {
        setWeightUnit(weightUnit);
        //更新数据库
        mPresenter.updateWeightUnit(weightUnit);
    }

    @Override
    public void onObtainHeightUnit(String heightUnit) {
        setHeightUnit(heightUnit);
        //更新数据库
        mPresenter.updateHeightUnit(heightUnit);
    }
}
