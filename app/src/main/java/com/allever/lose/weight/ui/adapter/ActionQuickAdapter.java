package com.allever.lose.weight.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.ActionItem;

import java.util.List;

/**
 * Created by Mac on 18/2/27.
 */

public class ActionQuickAdapter extends BaseQuickAdapter<ActionItem, BaseViewHolder> {
    private static final String TAG = "ActionQuickAdapter";
    private Context mContext;

    public ActionQuickAdapter(Context context, List<ActionItem> data) {
        super(R.layout.item_action, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ActionItem actionItem) {
        if (actionItem == null) {
            return;
        }
        holder.setText(R.id.id_item_action_tv_name, actionItem.getName());
        holder.setText(R.id.id_item_action_tv_count_time, actionItem.getTimeText());
        ImageView imageView = holder.getView(R.id.id_item_action_iv_guide);

        AnimationDrawable animationDrawable = actionItem.getAnimationDrawable();
        imageView.setBackgroundDrawable(actionItem.getAnimationDrawable());
        animationDrawable.setOneShot(false);
        animationDrawable.start();
    }
}
