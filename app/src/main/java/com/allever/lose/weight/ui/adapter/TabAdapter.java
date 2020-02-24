package com.allever.lose.weight.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.allever.lose.weight.ui.ReportsFragment;
import com.allever.lose.weight.ui.RoutinesFragment;
import com.allever.lose.weight.ui.TrainFragment;

/**
 * Created by hasee on 2017/8/14.
 */

public class TabAdapter extends FragmentPagerAdapter {
    String[] mTitles;

    public TabAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return TrainFragment.newInstance();
        } else if (position == 1) {
            return RoutinesFragment.newInstance();
        } else {
            return ReportsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
