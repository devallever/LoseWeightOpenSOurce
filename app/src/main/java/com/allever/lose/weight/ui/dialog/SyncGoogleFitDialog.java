package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.GlobalData;
import com.allever.lose.weight.data.Person;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.util.DateUtil;
import com.allever.lose.weight.util.SyncGoogle;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Mac on 18/3/26.
 */

public class SyncGoogleFitDialog extends BaseDialog {
    private static final int MSG_SYNC_ING = 0x01;

    private int mTimeCount = 10;
    private static final String TAG = "SyncGoogleFitDialog";
    private Button mBtnSync;

    private DataSource mDataSource = Repository.getInstance();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SYNC_ING:
                    if (mTimeCount > 0) {
                        mTimeCount--;
                        mHandler.sendEmptyMessageDelayed(MSG_SYNC_ING, 1000);
                        mBtnSync.setText(mTimeCount + "");
                    } else {
                        mBtnSync.setBackgroundColor(mActivity.getResources().getColor(R.color.green_16));
                        mBtnSync.setClickable(true);
                        mBtnSync.setText(mActivity.getResources().getString(R.string.sync));
                        mTimeCount = 10;
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public SyncGoogleFitDialog(Activity activity) {
        super(activity);
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sync_google_git, null, false);

        TextView tvAccount = (TextView) view.findViewById(R.id.id_dialog_sync_tv_account);
        final TextView tvSyncTime = (TextView) view.findViewById(R.id.id_dialog_sync_tv_last_sync_time);

        ImageView ivClose = (ImageView) view.findViewById(R.id.id_dialog_sync_iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy();
            }
        });
        TextView tvExit = (TextView) view.findViewById(R.id.id_dialog_sync_tv_exit);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fitness.getConfigClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity)).disableFit();
                GlobalData.config.setSync(false);
                mDataSource.updateConfig();
                Toast.makeText(mActivity, R.string.already_exit_google_fit, Toast.LENGTH_SHORT).show();

                //刷新report
                EventBus.getDefault().post(Constant.EVENT_UPDATE_REPORT_SYNC);
                destroy();
            }
        });

        final List<Person.WeightRecord> weightRecordList = mDataSource.getWeightRecordList();
        mBtnSync = (Button) view.findViewById(R.id.id_dialog_sync_btn_sync);


        mBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < weightRecordList.size(); i++) {
                    final int index = i;
                    Person.WeightRecord record = weightRecordList.get(i);
                    float weight = (float) record.getWeight();
                    int year = record.getYear();
                    int month = record.getMonth();
                    int day = record.getDay();
                    //
                    SyncGoogle.getIns().syncWeight(weight, year, month, day, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Data insert was successful!");
                                if (index == weightRecordList.size() - 1) {
                                    Toast.makeText(mActivity, R.string.sync_complete, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e(TAG, "There was a problem inserting the dataset.", task.getException());
                            }
                        }
                    });
                }

                //循环结束
                GlobalData.config.setSync(true);
                GlobalData.config.setSyncTime(System.currentTimeMillis());
                GlobalData.config.setAccount(GlobalData.config.getAccount());
                mDataSource.updateConfig();
                //
                tvSyncTime.setText(DateUtil.formatSyncTime(GlobalData.config.getSyncTime()));
                EventBus.getDefault().post(Constant.EVENT_UPDATE_REPORT_SYNC);
                mHandler.sendEmptyMessageDelayed(MSG_SYNC_ING, 1000);
                mBtnSync.setBackgroundColor(mActivity.getResources().getColor(R.color.gray));
                mBtnSync.setClickable(false);

            }
        });


        if (GlobalData.config.isSync()) {
            String account = GlobalData.config.getAccount();
            long syncTime = GlobalData.config.getSyncTime();
            tvAccount.setText(account);
            tvSyncTime.setText(DateUtil.formatSyncTime(syncTime));
        }
        return view;
    }

    @Override
    public void show(boolean cancelable) {
        super.show(cancelable);
    }


}
