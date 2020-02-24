package com.allever.lose.weight.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.ExerciseRecordItem;

import java.util.List;

/**
 * Created by Mac on 18/3/21.
 */

public class ExerciseRecordItemAdapter extends BaseQuickAdapter<ExerciseRecordItem, BaseViewHolder> {
    public ExerciseRecordItemAdapter(List<ExerciseRecordItem> exerciseRecordItemList) {
        super(R.layout.fragment_history_item, exerciseRecordItemList);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExerciseRecordItem item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_day, item.getName());
        holder.setText(R.id.id_item_exercise_record_tv_duration, item.getDuration());
        holder.setText(R.id.id_item_exercise_record_tv_date, item.getDate());
        holder.setText(R.id.id_item_exercise_record_tv_start_time, item.getStartTime());
    }
}
