package com.allever.lose.weight.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.allever.lose.weight.R;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.ui.dialog.HeightWeightDialog;
import com.allever.lose.weight.ui.dialog.SyncGoogleFitDialog;
import com.allever.lose.weight.ui.mvp.presenter.ReportPresenter;
import com.allever.lose.weight.ui.mvp.view.IReportView;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.util.DensityUtil;
import com.allever.lose.weight.ui.view.widget.BMIView;
import com.allever.lose.weight.ui.adapter.HistoryItemAdapter;
import com.allever.lose.weight.base.BaseMainFragment;
import com.allever.lose.weight.ui.dialog.WeightFragment;
import com.allever.lose.weight.util.ScreenUtils;
import com.allever.lose.weight.ui.view.widget.HorizontalDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends BaseMainFragment<IReportView, ReportPresenter> implements IReportView, HeightWeightDialog.IWHDataListener,
        WeightFragment.IWeightRecordListener {
    private static final String TAG = "ReportsFragment";

    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x01;
    private static final int RC_SIGN_IN = 0x02;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.add_weight)
    ImageView addWeight;
    @BindView(R.id.tv_edit_bmi)
    TextView mTvEditBmi;
    @BindView(R.id.tv_edit_height)
    TextView tvEditHeight;
    @BindView(R.id.tv_workout)
    TextView mTvWorkout;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_duration)
    TextView mTvDuration;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.heaviest)
    TextView mTvHeaviestWeight;
    @BindView(R.id.lightest)
    TextView mTvLightestWeight;
    @BindView(R.id.tv_current)
    TextView mTvCurrentWeight;
    @BindView(R.id.tv_current_height)
    TextView mTvCurrentHeight;
    @BindView(R.id.id_fg_report_bmi)
    BMIView mBmiView;
    @BindView(R.id.id_sync_ll_sync_container)
    LinearLayout mLlSyncContainer;
    @BindView(R.id.id_sync_tv_account)
    TextView mTvAccount;
    @BindView(R.id.id_sync_tv_sync_time)
    TextView mTvSyncTime;
    private HistoryItemAdapter mAdapter;

    private HeightWeightDialog mHeightWeightDialog;
    private SyncGoogleFitDialog mSyncGoogleFitDialog;

    public static Fragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        unbinder = ButterKnife.bind(this, view);

        //设置图表控件样式
        setChartStyle();

        initDialog();

        setRecyclerView();
        refreshView();

        return view;
    }

    private void initDialog() {
        mHeightWeightDialog = new HeightWeightDialog.Builder(_mActivity)
                .setOkBtn(getResources().getString(R.string.save), new BaseDialog.ClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog) {
                        //dialog.hide();
                    }
                })
                .setDataListener(ReportsFragment.this)
                .build();

    }

    private void setRecyclerView() {
        LayoutInflater layoutInflater = LayoutInflater.from(_mActivity);
        mAdapter = new HistoryItemAdapter(R.layout.item_weekly_calendar_item, getItemData());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter.addHeaderView(layoutInflater.inflate(R.layout.item_history_header, recyclerView, false));
        mAdapter.addFooterView(getFooter(layoutInflater));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new HorizontalDecoration(ScreenUtils.dp2px(10)));
    }

    @OnClick(R.id.add_weight)
    public void setWeight() {
        Log.i("ReportsFragment", "click");
        if (getParentFragment() instanceof HomeFragment) {
            ((HomeFragment) getParentFragment()).extraTransaction().startDontHideSelf(WeightFragment.newInstance(this));
        }
    }

    //数据无实际意义 只获取数量
    private List<Integer> getItemData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(i);
        }
        return list;
    }

    private View getFooter(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_history_footer, recyclerView, false);
        TextView records = view.findViewById(R.id.records);
        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof HomeFragment) {
                    ((HomeFragment) getParentFragment()).start(HistoryFragment.newInstance());
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter();
    }

    @OnClick({R.id.tv_edit_bmi, R.id.tv_edit_height})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_bmi:
            case R.id.tv_edit_height:
                mHeightWeightDialog.show(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void setWorkout(int workout) {
        mTvWorkout.setText(String.valueOf(workout));
    }

    @Override
    public void setKcal(int kcal) {
        mTvKcal.setText(String.valueOf(kcal));
    }

    @Override
    public void setDuration(String durationStr) {
        mTvDuration.setText(String.valueOf(durationStr));
    }

    @Override
    public void setCurrentWeight(double currentWeight, String unit) {
        mTvCurrentWeight.setText(currentWeight + unit);
    }

    @Override
    public void setHeaviestWeight(double heaviestWeight, String unit) {
        mTvHeaviestWeight.setText(heaviestWeight + unit);
    }

    @Override
    public void setLightestWeight(double lightestWeight, String unit) {
        mTvLightestWeight.setText(lightestWeight + unit);
    }

    @Override
    public void setBMI(int gender, float currentWeight, float height) {
        mBmiView.setGender(gender)
                .setWeight(currentWeight)
                .setHeight(height);
    }


    @Override
    public void setHeight(double height, String unit) {
        mTvCurrentHeight.setText(height + unit);
    }

    @Override
    public void onWHDataOptain(float weight, float height) {
        mPresenter.updateWeightHeight(weight, height);
        mHeightWeightDialog.hide();
        mPresenter.getBMI();
        mPresenter.getWeight();
        mPresenter.getHeight();
    }

    @Override
    public void refreshView() {
        mPresenter.setWorkout();
        mPresenter.getKcal();
        mPresenter.getDuration();
        mPresenter.getWeight();
        mPresenter.getHeight();
        mPresenter.getBMI();
        mPresenter.getChartData();
        mPresenter.getSyncData();
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshView(String event) {
        if (Constant.EVENT_REFRESH_VIEW.equals(event)) {
            refreshView();
        }
        if (Constant.EVENT_UPDATE_REPORT_SYNC.equalsIgnoreCase(event)) {
            mPresenter.getSyncData();
        }
    }


    private void setChartStyle() {
        // 显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        //显示y轴右边的值
        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置x轴数据偏移量
        xAxis.setYOffset(DensityUtil.dip2px(_mActivity, 3f));
        xAxis.setXOffset(DensityUtil.dip2px(_mActivity, 1f));

        YAxis yAxis = chart.getAxisLeft();
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 设置y轴数据偏移量
        yAxis.setXOffset(DensityUtil.dip2px(_mActivity, 3f));
        yAxis.setYOffset(DensityUtil.dip2px(_mActivity, -1f));

        chart.invalidate();
    }

    @Override
    public void onSaveClick(double weight, int year, int month, int day) {
        Log.d(TAG, "onSaveClick: ");
        //保存体重记录
        mPresenter.saveWeightRecord(weight, year, month, day);
        //刷新图表
        mPresenter.getChartData();
        mPresenter.getWeight();
        //sync google fit
        mPresenter.syncWeight((float) weight, year, month, day);
    }

    @Override
    public void setChartData(List<Entry> entryList, Date startDate, Date endDate) {
        if (entryList == null || startDate == null || endDate == null) {
            return;
        }
        final List<String> dayList = DateUtil.getIntevalDayStrList(startDate, endDate);
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dayList.get((int) value);
            }
        });

        if (entryList.size() == 0) {
            LineData data = new LineData();
            chart.setData(data);
            chart.invalidate();
            return;
        }
        LineDataSet lineDataSet;

        lineDataSet = new LineDataSet(entryList, getString(R.string.weight));
        // 设置曲线颜色
        lineDataSet.setColor(getResources().getColor(R.color.orange_500));
        // 设置平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        // 坐标点的小圆点
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleColor(getResources().getColor(R.color.orange_500));
        lineDataSet.setCircleSize(DensityUtil.dip2px(_mActivity, 1.2f));
        // 不显示坐标点的数据
        lineDataSet.setDrawValues(false);
        // 不显示定位线
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setLineWidth(DensityUtil.dip2px(_mActivity, 0.5f));

        LineData data = new LineData(lineDataSet);
        chart.setData(data);
        chart.invalidate();
    }

    @OnClick(R.id.id_sync_ll_sync_container)
    public void onSyncClicked() {
        //https://developers.google.com/identity/sign-in/android/
        //1.登录谷歌账号->
        //2.登录成功->授权google fit
        //3.允许->弹出同步框

//        if (Util.isApkAvailable(_mActivity, "com.google.android.gms")){
//            Log.d(TAG, "onSyncClicked: ener google");
//            mPresenter.loginGoogle(this,RC_SIGN_IN);
//        }else {
//            showToast("没安装谷歌服务");
//        }

        showToast(R.string.sync);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: REQUEST_OAUTH_REQUEST_CODE");
            //获取上一次的同步时间
            mPresenter.saveSyncState(true);
            //刷新界面
            EventBus.getDefault().post(Constant.EVENT_UPDATE_REPORT_SYNC);
            showSyncDialog();
        }

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            handleLoginGoogle(data);
        }
    }

    private void handleLoginGoogle(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            mPresenter.saveSyncAccount(account.getEmail());
            mPresenter.connectGoogleFit(ReportsFragment.this, REQUEST_OAUTH_REQUEST_CODE);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void showSyncDialog() {
        Log.d(TAG, "showSyncDialog: ");
        mSyncGoogleFitDialog = new SyncGoogleFitDialog(_mActivity);
        mSyncGoogleFitDialog.show(true);
    }

    @Override
    public void hideSyncDialog() {
        mSyncGoogleFitDialog.destroy();
    }

    @Override
    public void setSync(String account, String time) {
        mTvAccount.setText(account);
        mTvSyncTime.setText(getString(R.string.last_sync) + ": " + time);
        mTvSyncTime.setTextColor(getResources().getColor(R.color.green_16));
    }
}
