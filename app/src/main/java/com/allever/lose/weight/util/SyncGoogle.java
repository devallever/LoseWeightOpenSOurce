package com.allever.lose.weight.util;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.HistoryClient;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.allever.lose.weight.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mac on 18/3/27.
 */

public class SyncGoogle {
    private static ExecutorService mSingleThreadExecutor;
    private static HistoryClient mHistoryClient;

    private SyncGoogle() {
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        mHistoryClient = Fitness.getHistoryClient(MyApplication.getContext(), GoogleSignIn.getLastSignedInAccount(MyApplication.getContext()));
    }

    private static class Holder {
        public static final SyncGoogle INSTANCE = new SyncGoogle();
    }

    public static SyncGoogle getIns() {
        return Holder.INSTANCE;
    }

    public void syncWeight(final float weight, final int year, final int month, final int day, @NonNull final OnCompleteListener onCompleteListener) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mHistoryClient.insertData(Util.createWeightDataSet(weight, year, month, day))
                        .addOnCompleteListener(onCompleteListener);
            }
        });
    }

    public void loginGoogle(final Fragment fragment, final int requestCode) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                GoogleSignInClient googleSignInClient;
                //登录google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(fragment.getContext(), gso);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                fragment.startActivityForResult(signInIntent, requestCode);
            }
        });

    }

    public void loginGoogle(final Activity activity, final int requestCode) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                GoogleSignInClient googleSignInClient;
                //登录google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(activity, gso);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                activity.startActivityForResult(signInIntent, requestCode);
            }
        });

    }

    public void connectGoogleFit(final Fragment fragment, final int requestCode) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FitnessOptions fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
                        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
                        .build();
                GoogleSignIn.requestPermissions(
                        fragment,
                        requestCode,
                        GoogleSignIn.getLastSignedInAccount(fragment.getContext()),
                        fitnessOptions);
            }
        });
    }

    public void connectGoogleFit(final Activity activity, final int requestCode) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FitnessOptions fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
                        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
                        .build();
                GoogleSignIn.requestPermissions(
                        activity,
                        requestCode,
                        GoogleSignIn.getLastSignedInAccount(activity),
                        fitnessOptions);
            }
        });
    }
}
