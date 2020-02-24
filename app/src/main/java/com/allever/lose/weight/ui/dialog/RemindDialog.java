package com.allever.lose.weight.ui.dialog;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.allever.lose.weight.R;
import com.allever.lose.weight.ui.adapter.ReminderAdapter;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.bean.ReminderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/2.
 */

public class RemindDialog extends BaseDialog {
    private RecyclerView mRv;
    private ReminderAdapter mAdapter;
    private List<ReminderItem> mReminderItemList;
    private Activity mContext;
    private TextView mTvFinish;
    private IRemindListener mDataListener;
    private int mPosition = -1;

    public RemindDialog(Activity activity, IRemindListener iRemindListener) {
        super(activity);
        mDataListener = iRemindListener;
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_finish_noti, null);
        initData();
        mRv = (RecyclerView) view.findViewById(R.id.id_dialog_finish_reminder_rv);
        mAdapter = new ReminderAdapter(mContext, mReminderItemList);
        mRv.setLayoutManager(new LinearLayoutManager(mContext));
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position;
                updateSelected(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mTvFinish = (TextView) view.findViewById(R.id.id_dialog_finish_reminder_tv_finish);
        mTvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataListener != null && mPosition != -1) {
                    mDataListener.onAddReminder(Integer.valueOf(mReminderItemList.get(mPosition).getTime().split(":")[0]));
                }
                hide();
            }
        });
        return view;
    }

    private void initData() {
        mReminderItemList = new ArrayList<>();
        for (int i = 6; i < 23; i++) {
            ReminderItem reminderItem = new ReminderItem();
            reminderItem.setTime(i + ": 00 ");
            reminderItem.setSelected(false);
            mReminderItemList.add(reminderItem);
        }
    }

    private void updateSelected(int position) {
        for (int i = 0; i < mReminderItemList.size(); i++) {
            ReminderItem reminderItem = mReminderItemList.get(i);
            if (i == position) {
                reminderItem.setSelected(true);
            } else {
                reminderItem.setSelected(false);
            }
            mReminderItemList.set(i, reminderItem);
        }
    }

    public interface IRemindListener {
        void onAddReminder(int hour);
    }
}
