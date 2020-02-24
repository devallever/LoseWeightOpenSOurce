package com.allever.lose.weight.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.LanguageItem;

import java.util.List;

/**
 * Created by Mac on 18/3/14.
 */

public class LanguageAdapter extends BaseQuickAdapter<LanguageItem, BaseViewHolder> {
    public LanguageAdapter(Context context, List<LanguageItem> languageItems) {
        super(R.layout.item_language, languageItems);

    }

    @Override
    protected void convert(BaseViewHolder holder, LanguageItem item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.id_item_language_tv_name, item.getName());
    }
}
