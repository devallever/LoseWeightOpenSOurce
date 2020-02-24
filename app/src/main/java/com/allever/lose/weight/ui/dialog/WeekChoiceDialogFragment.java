package com.allever.lose.weight.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;

import com.allever.lose.weight.R;
import com.allever.lose.weight.data.Config;

/**
 * Created by Mac on 2018/3/6.
 */

public class WeekChoiceDialogFragment extends DialogFragment {

    private String title;

    private boolean[] defaultSelectedStatus;

    private DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener;

    private DialogInterface.OnClickListener positiveCallback;

    public void show(Config.Reminder reminder, String title, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener,
                     DialogInterface.OnClickListener positiveCallback, FragmentManager fragmentManager) {
        this.title = title;
        this.onMultiChoiceClickListener = onMultiChoiceClickListener;
        this.positiveCallback = positiveCallback;
        if (reminder != null) {
            defaultSelectedStatus = new boolean[]{
                    reminder.isSunRepeat(),
                    reminder.isMonRepeat(),
                    reminder.isTueRepeat(),
                    reminder.isWebRepeat(),
                    reminder.isThurRepeat(),
                    reminder.isFriRepeat(),
                    reminder.isSatRepeat()
            };
        }

        show(fragmentManager, "WeekChoiceDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] items = getActivity().getResources().getStringArray(R.array.week_value);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMultiChoiceItems(items, defaultSelectedStatus, onMultiChoiceClickListener)
                .setPositiveButton(R.string.save, positiveCallback)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

}
