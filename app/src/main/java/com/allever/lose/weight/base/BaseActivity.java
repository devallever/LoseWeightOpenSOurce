package com.allever.lose.weight.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allever.lose.weight.ui.mvp.presenter.BasePresenter;
import com.allever.lose.weight.util.Util;

import java.util.Locale;

/**
 * Created by allever on 18-2-28.
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        mPresenter.attachView((V) this); //view 与 Presenter 关联
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
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

    protected abstract T createPresenter();
}
