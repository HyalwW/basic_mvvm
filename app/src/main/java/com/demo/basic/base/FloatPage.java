package com.demo.basic.base;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public abstract class FloatPage<DB extends ViewDataBinding> implements LifecycleOwner {
    protected DB rootView;
    protected Context context;
    private LifecycleRegistry lifecycleRegistry;

    public FloatPage() {
        initLifeCycle();
    }

    public void hide() {
        if (rootView.getRoot().getVisibility() == View.VISIBLE) {
            rootView.getRoot().setVisibility(View.INVISIBLE);
            onVisibleChanged(false);
        }
    }

    public void show() {
        if (rootView.getRoot().getVisibility() != View.VISIBLE) {
            rootView.getRoot().setVisibility(View.VISIBLE);
            onVisibleChanged(true);
        }
    }

    View init(LayoutInflater inflater, ViewGroup parent) {
        rootView = DataBindingUtil.bind(onCreate(inflater, parent));
        return rootView.getRoot();
    }

    public View getRoot() {
        return rootView.getRoot();
    }

    private void initLifeCycle() {
        lifecycleRegistry = new LifecycleRegistry(this);
        if (Build.VERSION.SDK_INT >= 19) {
            lifecycleRegistry.addObserver((LifecycleEventObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_STOP) {
                    rootView.getRoot().cancelPendingInputEvents();
                }
            });
        }
    }

    void performLifecycleEvent(Lifecycle.Event event) {
        lifecycleRegistry.handleLifecycleEvent(event);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    protected abstract View onCreate(LayoutInflater inflater, ViewGroup parent);

    protected abstract void onAddToWindow();

    protected abstract void onVisibleChanged(boolean visible);

    protected abstract void onRemoveFromWindow();

    protected abstract void onDestroy();
}
