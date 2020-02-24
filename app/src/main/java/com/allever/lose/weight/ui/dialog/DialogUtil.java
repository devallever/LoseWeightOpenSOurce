package com.allever.lose.weight.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.allever.lose.weight.R;


/**
 * Created by Mac on 18/2/11.
 */

public class DialogUtil {

    public static AlertDialog createProgressAlertDialog(Activity activity, String msg, boolean canCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null, false);
        ((TextView) view.findViewById(R.id.id_dialog_tv_msg)).setText(msg);
        builder.setView(view);
        builder.setCancelable(canCancel);
        return builder.create();
    }

    public static AlertDialog createProgressAlertDialog(Activity activity, int msg, boolean canCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null, false);
        ((TextView) view.findViewById(R.id.id_dialog_tv_msg)).setText(msg);
        builder.setView(view);
        builder.setCancelable(canCancel);
        return builder.create();
    }

}
