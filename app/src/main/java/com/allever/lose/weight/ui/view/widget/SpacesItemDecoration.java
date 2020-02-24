package com.allever.lose.weight.ui.view.widget;

import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

/**
 * @author hasee
 * @date 2017/8/29
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.top = space;
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
        } else {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = 0;
        }

    }
}
