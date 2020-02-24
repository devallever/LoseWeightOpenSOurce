package com.allever.lose.weight.ui;

import android.content.Context;
import android.os.Bundle;

import com.allever.lose.weight.R;
import com.allever.lose.weight.base.MyContextWrapper;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.util.Util;

import java.util.Locale;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public class ActionMainActivity extends SupportActivity {
    private static final String TAG = "ActionMainActivity";
    private static final String mDefaultTitle = "第一天";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        ButterKnife.bind(this);

        int dayId = getIntent().getIntExtra(Constant.EXTRA_DAY_ID, 1);
        String dayText = getIntent().getStringExtra(Constant.EXTRA_DAY_TEXT);
        if (dayText == null) {
            dayText = mDefaultTitle;
        }

        loadRootFragment(R.id.id_main_fg_container, PreviewActionFragment.newInstance(dayId, dayText));
        //loadRootFragment(R.id.id_main_fg_container, new ActionFinishFragment(dayId, 1));

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale;
        if (Util.isChinese()) {
            newLocale = Locale.CHINESE;
        } else {
            newLocale = Locale.ENGLISH;
        }

        super.attachBaseContext(MyContextWrapper.wrap(newBase, newLocale));
    }
}
