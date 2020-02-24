package com.allever.lose.weight.base;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allever.lose.weight.R;
import com.allever.lose.weight.ui.mvp.presenter.BasePresenter;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mac on 18/3/1.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends SupportFragment {
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    protected void initToolbar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        initToolbar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar, int strId) {
        toolbar.setTitle(strId);
        initToolbar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }

    protected void showToast(String msg) {
        Toast.makeText(_mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int msg) {
        Toast.makeText(_mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract T createPresenter();
}
