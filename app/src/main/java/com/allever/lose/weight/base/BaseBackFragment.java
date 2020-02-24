package com.allever.lose.weight.base;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.allever.lose.weight.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mac on 2018/3/7.
 */
public class BaseBackFragment extends SupportFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }
}
