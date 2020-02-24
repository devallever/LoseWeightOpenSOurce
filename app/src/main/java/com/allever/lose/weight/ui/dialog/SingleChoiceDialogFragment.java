package com.allever.lose.weight.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;

import com.allever.lose.weight.R;
import com.allever.lose.weight.bean.TTSBean;

import java.util.List;

/**
 * Created by Mac on 2018/3/6.
 */

public class SingleChoiceDialogFragment extends DialogFragment {
    private String title;

    //    private String[] items;
    private List<TTSBean> mTtsBeanList;

    private DialogInterface.OnClickListener onClickListener;

    private DialogInterface.OnClickListener positiveCallback;

    public void show(String title, List<TTSBean> ttsBeanList, DialogInterface.OnClickListener onClickListener,
                     DialogInterface.OnClickListener positiveCallback, FragmentManager fragmentManager) {
        this.title = title;
        mTtsBeanList = ttsBeanList;
        this.onClickListener = onClickListener;
        this.positiveCallback = positiveCallback;
        show(fragmentManager, "SingleChoiceDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mTtsBeanList != null) {
            String[] labels;
            labels = new String[mTtsBeanList.size()];
            int selectPosition = 0;
            for (int i = 0; i < mTtsBeanList.size(); i++) {
                labels[i] = mTtsBeanList.get(i).getLabel();
                if (mTtsBeanList.get(i).isSelected()) selectPosition = i;
            }

            if (title != null && onClickListener != null && positiveCallback != null) {
                builder.setTitle(title)
                        .setSingleChoiceItems(labels, selectPosition, onClickListener)
                        .setPositiveButton(R.string.ok, positiveCallback);
            }
        }

        return builder.create();
    }

}
