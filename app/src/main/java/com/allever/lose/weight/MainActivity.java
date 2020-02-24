package com.allever.lose.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.allever.lib.common.util.ToastUtils;
import com.allever.lib.recommend.RecommendDialog;
import com.allever.lib.recommend.RecommendDialogHelper;
import com.allever.lib.recommend.RecommendDialogListener;
import com.allever.lib.recommend.RecommendGlobal;
import com.allever.lose.weight.base.MyContextWrapper;
import com.allever.lose.weight.util.Util;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.allever.lose.weight.data.DataSource;
import com.allever.lose.weight.data.Repository;
import com.allever.lose.weight.util.Constant;
import com.allever.lose.weight.bean.MenuEvent;
import com.allever.lose.weight.ui.HistoryFragment;
import com.allever.lose.weight.ui.HomeFragment;
import com.allever.lose.weight.ui.ReminderFragment;
import com.allever.lose.weight.ui.SettingsFragment;
import com.allever.lose.weight.base.BaseMainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static kotlinx.coroutines.android.HandlerDispatcherKt.getMainHandler;

public class MainActivity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnFragmentOpenDrawerListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private DataSource mDataSource = Repository.getInstance();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());  //load root Fragment
        }
        setNavigationView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setNavigationView() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onOpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
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


    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);
        item.setCheckable(true);
        item.setChecked(true);
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = item.getItemId();
                final ISupportFragment topFragment = getTopFragment();
                switch (id) {
                    case R.id.plans:
                        startFragment(0);
                        break;
                    case R.id.report:
                        startFragment(3);
                        break;
                    case R.id.reminder:
                        ReminderFragment reminderFragment = findFragment(ReminderFragment.class);
                        if (reminderFragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(ReminderFragment.newInstance());
                                }
                            }, getFragmentAnimator().getPopExit());
                        } else {
                            popTo(ReminderFragment.class, false);
                        }
                        break;
                    case R.id.setting:
                        SettingsFragment settingsFragment = findFragment(SettingsFragment.class);
                        if (settingsFragment == null) {
                            popTo(HomeFragment.class, false, new Runnable() {
                                @Override
                                public void run() {
                                    start(SettingsFragment.newInstance());
                                }
                            }, getFragmentAnimator().getPopExit());
                        } else {
                            popTo(SettingsFragment.class, false);
                        }
                        break;
                    case R.id.reset_schedule:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setMessage(R.string.reset_schedule)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDataSource.deleteAllSchedule();
                                        EventBus.getDefault().post(Constant.EVENT_REFRESH_VIEW);
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        break;
                    default:
                        break;
                }
            }
        }, 300);
        return true;
    }

    private void startFragment(int pageIndex) {
        EventBus.getDefault().post(new MenuEvent(Constant.EVENT_MENU_START_HOME_PAGE, pageIndex));
        Log.d(TAG, "run: ");
        HomeFragment fragment = findFragment(HomeFragment.class);
        Bundle newBundle = new Bundle();
        newBundle.putInt(Constant.EXTRA_MAIN_PAGE_INDEX, pageIndex);
        fragment.putNewBundle(newBundle);
        start(fragment, SupportFragment.SINGLETASK);
    }

    @Override
    public void onBackPressedSupport() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            ISupportFragment topFragment = getTopFragment();

            // 主页的Fragment
            if (topFragment instanceof BaseMainFragment) {
                navigationView.setCheckedItem(R.id.plans);
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
//                finish();
                if (RecommendGlobal.INSTANCE.getRecommendData().isEmpty()) {
                    checkExit(null);
                } else {
                    showRecommendDialog();
                }
            }
        }
    }

    private void showRecommendDialog() {
        RecommendDialogHelper.INSTANCE.createRecommendDialog(this, new RecommendDialogListener() {
            @Override
            public void onMore(@Nullable Dialog dialog) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onReject(@Nullable Dialog dialog) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }

            @Override
            public void onBackPress(@Nullable Dialog dialog) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }
        });
    }

    private long firstPressedBackTime = 0L;

    protected void checkExit(Runnable runnable) {
        if (System.currentTimeMillis() - firstPressedBackTime < 2000) {
            finish();
        } else {
            ToastUtils.INSTANCE.show(getString(R.string.common_click_again_to_exit));
            firstPressedBackTime = System.currentTimeMillis();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionFinish(String event) {
        if (Constant.EVENT_START_HISTORY.equals(event)) {
            start(new HistoryFragment());
        }
    }

}
