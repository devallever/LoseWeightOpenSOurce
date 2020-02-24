package com.allever.lose.weight.ui.mvp.view;

import android.graphics.Bitmap;

import com.allever.lose.weight.bean.ActionItem;

import java.util.List;

/**
 * Created by Mac on 18/3/1.
 */

public interface IActionView {
    void showExitDialog();

    void hideExitDialog();

    void showSoundDialog();

    void hideSoundDialog();

    void setData(ActionItem actionItem);

    void setCurrent(int currentLevel, int levelCount);

    void setActionImgList(List<String> urlList);

    void updateProgress(int progress);

    void updateActionImg(Bitmap bitmap);

    void startNextFragment();

    void startFinishFragment();

    void setCurrentActionId(int actionId);
}
