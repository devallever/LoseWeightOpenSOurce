package com.allever.lose.weight.ui.dialog;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;
import com.allever.lose.weight.ui.adapter.LanguageAdapter;
import com.allever.lose.weight.base.BaseDialog;
import com.allever.lose.weight.bean.LanguageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 18/3/14.
 */

public class LanguageDialog extends BaseDialog {
    private RecyclerView mRecyclerView;
    private LanguageAdapter mAdapter;
    private List<LanguageItem> mLanguageItems;
    private ILanguageListener mListener;

    public LanguageDialog(Activity activity, ILanguageListener languageListener) {
        super(activity);
        mListener = languageListener;
        init();
    }

    private void init() {
        mLanguageItems = new ArrayList<>();
        LanguageItem languageItemChinese = new LanguageItem();
        languageItemChinese.setId(Config.LANG_CHINESE);
        languageItemChinese.setName(mActivity.getResources().getString(R.string.chinese));
        languageItemChinese.setSelect(true);

        LanguageItem languageItemEnglish = new LanguageItem();
        languageItemEnglish.setId(Config.LANG_ENGLISH);
        languageItemEnglish.setName(mActivity.getResources().getString(R.string.english));
        languageItemEnglish.setSelect(false);

        mLanguageItems.add(languageItemChinese);
        mLanguageItems.add(languageItemEnglish);
    }

    @Override
    public View getDialogView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_language_select, null, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_dialog_language_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new LanguageAdapter(mActivity, mLanguageItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mListener != null) {
                    mListener.onObtainLanguage(mLanguageItems.get(position).getId(), mLanguageItems.get(position).getName());
                }
                hide();
            }
        });
        return view;
    }

    private void updateSelected(int position) {
        for (int i = 0; i < mLanguageItems.size(); i++) {
            LanguageItem reminderItem = mLanguageItems.get(i);
            if (i == position) {
                reminderItem.setSelect(true);
            } else {
                reminderItem.setSelect(false);
            }
            mLanguageItems.set(i, reminderItem);
        }
    }

    public interface ILanguageListener {
        void onObtainLanguage(int id, String name);
    }

}
