package com.allever.lose.weight.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.RoutineItem;

import java.util.List;


public class RoutinesItemAdapter extends BaseQuickAdapter<RoutineItem, BaseViewHolder> {

    public RoutinesItemAdapter(@Nullable List<RoutineItem> data) {
        super(R.layout.fragment_routines_item, data);
    }

    public RoutinesItemAdapter(int layoutResId, @Nullable List<RoutineItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, RoutineItem routineItem) {
        if (routineItem == null) {
            return;
        }
        holder.setText(R.id.id_item_routine_tv_title, routineItem.getTitle());
        holder.setText(R.id.id_item_routine_tv_exercise, routineItem.getCount());
        holder.setText(R.id.id_item_routine_tv_duration, routineItem.getDuration());
        holder.setText(R.id.id_item_routine_tv_kcal, routineItem.getKcal());
        holder.setImageResource(R.id.id_item_routine_iv_type_small, routineItem.getSmallResId());
        holder.setImageResource(R.id.id_item_routine_iv_type_bg, routineItem.getBgResId());
    }
}
