package com.demo.basic.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Lifecycle;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/14
 * Description: blablabla
 */
interface IPageLifecycle {
    View onCreateView(LayoutInflater inflater, ViewGroup parent);

    void onCreate();

    void onStart();

    void onResume();

    void onVisibleChanged(boolean visible);

    void onPause();

    void onStop();

    void onDestroy();

    void performLifecycleEvent(Lifecycle.Event event);
}
