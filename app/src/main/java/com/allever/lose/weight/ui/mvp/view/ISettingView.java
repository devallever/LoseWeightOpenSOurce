package com.allever.lose.weight.ui.mvp.view;

/**
 * Created by Mac on 18/3/22.
 */

public interface ISettingView {

    void setLanguage(int flag, String language);

    void showSyncDialog();

    void hideSyncDialog();

    void setSync(String account, String time);
}
