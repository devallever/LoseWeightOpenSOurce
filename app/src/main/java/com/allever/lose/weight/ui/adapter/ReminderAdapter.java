package com.allever.lose.weight.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.ReminderItem;

import java.util.List;

/**
 * Created by Mac on 18/3/2.
 */

public class ReminderAdapter extends BaseQuickAdapter<ReminderItem, BaseViewHolder> {
    public ReminderAdapter(Context context, List<ReminderItem> reminderItemList) {
        super(R.layout.item_finish_reminder, reminderItemList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReminderItem item) {
        if (item == null) {
            return;
        }
        ImageView imageView = (ImageView) helper.getView(R.id.id_item_finish_reminder_iv_select);
        if (item.isSelected()) {
            imageView.setImageResource(R.drawable.rp_feel_level_checked);
        } else {
            imageView.setImageResource(R.drawable.rp_feel_level_unchecked);
        }
        helper.setText(R.id.id_item_finish_reminder_tv_time, item.getTime());
    }
}
