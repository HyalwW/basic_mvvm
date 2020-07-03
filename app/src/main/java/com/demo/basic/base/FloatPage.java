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
public abstract class FloatPage<M extends BaseFloatModel, DB extends ViewDataBinding> implements LifecycleOwner {
    protected DB rootView;
    protected M model;
    protected Context context;
    private LifecycleRegistry lifecycleRegistry;

    public FloatPage() {
        initLifeCycle();
    }

    public void hide() {
        if (getRoot().getVisibility() == View.VISIBLE) {
            getRoot().setVisibility(View.INVISIBLE);
            onVisibleChanged(false);
        }
    }

    public void show() {
        if (getRoot().getVisibility() != View.VISIBLE) {
            getRoot().setVisibility(View.VISIBLE);
            onVisibleChanged(true);
        }
    }

    View init(Context context, LayoutInflater inflater, ViewGroup parent) {
        this.context = context;
        rootView = DataBindingUtil.bind(onCreate(inflater, parent));
        model = initModel();
        model.create(context);
        rootView.setVariable(modelId(), model);
        return getRoot();
    }

    public View getRoot() {
        return rootView.getRoot();
    }

    private void initLifeCycle() {
        lifecycleRegistry = new LifecycleRegistry(this);
        if (Build.VERSION.SDK_INT >= 19) {
            lifecycleRegistry.addObserver((LifecycleEventObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_STOP) {
                    getRoot().cancelPendingInputEvents();
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

    void destroy() {
        model.destroy();
        onDestroy();
    }

    protected abstract View onCreate(LayoutInflater inflater, ViewGroup parent);

    protected abstract M initModel();

    protected abstract int modelId();

    protected abstract void onAddToWindow();

    protected abstract void onVisibleChanged(boolean visible);

    protected abstract void onRemoveFromWindow();

    protected abstract void onDestroy();
}
