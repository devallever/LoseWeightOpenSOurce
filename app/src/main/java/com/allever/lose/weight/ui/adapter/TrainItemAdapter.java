package com.allever.lose.weight.ui.adapter;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.DayInfoBean;

import java.util.List;


public class TrainItemAdapter extends BaseQuickAdapter<DayInfoBean, BaseViewHolder> {
    public TrainItemAdapter(@Nullable List<DayInfoBean> data) {
        super(R.layout.item_train, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DayInfoBean item) {
        if (item == null) {
            return;
        }
        CircleProgressBar circleProgressBar = helper.getView(R.id.id_item_train_progress_bar_train);
        CardView mCardView = helper.getView(R.id.card_view);
        helper.setText(R.id.tv_day, item.getTitle());
        if (item.isCurrentDay()) {
            mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.theme_color));
            helper.setTextColor(R.id.tv_day, Color.parseColor("#FFFFFF"));
            circleProgressBar.setProgressStartColor(mContext.getResources().getColor(R.color.white));
            circleProgressBar.setProgressEndColor(mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.id_item_train_tv_progress, mContext.getResources().getColor(R.color.white));
        } else {
            mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.tv_day, mContext.getResources().getColor(R.color.black));
            circleProgressBar.setProgressStartColor(mContext.getResources().getColor(R.color.green_200));
            circleProgressBar.setProgressEndColor(mContext.getResources().getColor(R.color.green_200));
            helper.setTextColor(R.id.id_item_train_tv_progress, mContext.getResources().getColor(R.color.green_200));
        }


        if (item.isFinish()) {
            //设置完成标志
            helper.setVisible(R.id.id_item_train_iv_finish, true);
            //其他隐藏
            helper.setVisible(R.id.id_item_train_rl_progress_bar_container, false);
            helper.setVisible(R.id.id_item_train_iv_rest, false);
        } else {
            helper.setVisible(R.id.id_item_train_iv_finish, false);
            if (item.getLevelCount() == 0) {
                //无动作 休息
                helper.setVisible(R.id.id_item_train_rl_progress_bar_container, false);
                helper.setVisible(R.id.id_item_train_iv_rest, true);
            } else {
                //有动作
                helper.setVisible(R.id.id_item_train_rl_progress_bar_container, true);
                helper.setVisible(R.id.id_item_train_iv_rest, false);

                int percent = Math.round(((item.getTrainedCount() / (float) item.getLevelCount()) * 100));
                helper.setText(R.id.id_item_train_tv_progress, percent + "%");
                circleProgressBar.setMax(item.getLevelCount());
                circleProgressBar.setProgress(item.getTrainedCount());
            }
        }
    }
}
