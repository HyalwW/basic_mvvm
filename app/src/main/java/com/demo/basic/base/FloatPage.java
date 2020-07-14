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
public abstract class FloatPage<M extends BaseFloatModel, DB extends ViewDataBinding> implements LifecycleOwner, IPageLifecycle {
    protected DB rootView;
    protected M model;
    protected Context context;
    private LifecycleRegistry lifecycleRegistry;

    boolean isHidden;

    public FloatPage() {
        initLifecycle();
    }

    public void hide() {
        if (getRoot().getVisibility() == View.VISIBLE) {
            getRoot().setVisibility(View.INVISIBLE);
            isHidden = true;
            onVisibleChanged(false);
        }
    }

    public void show() {
        if (getRoot().getVisibility() != View.VISIBLE) {
            getRoot().setVisibility(View.VISIBLE);
            isHidden = false;
            onVisibleChanged(true);
        }
    }

    View init(Context context, LayoutInflater inflater, ViewGroup parent) {
        this.context = context;
        rootView = DataBindingUtil.bind(onCreateView(inflater, parent));
        model = initModel();
        if (model != null) {
            model.create(context);
            rootView.setVariable(modelId(), model);
        }
        return getRoot();
    }

    public View getRoot() {
        return rootView.getRoot();
    }

    private void initLifecycle() {
        lifecycleRegistry = new LifecycleRegistry(this);
        if (Build.VERSION.SDK_INT >= 19) {
            lifecycleRegistry.addObserver((LifecycleEventObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_STOP) {
                    getRoot().cancelPendingInputEvents();
                }
            });
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onVisibleChanged(boolean visible) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void performLifecycleEvent(Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                doCreate();
                break;
            case ON_START:
                doStart();
                break;
            case ON_RESUME:
                doResume();
                break;
            case ON_PAUSE:
                doPause();
                break;
            case ON_STOP:
                doStop();
                break;
            case ON_DESTROY:
                doDestroy();
                break;
        }
        lifecycleRegistry.handleLifecycleEvent(event);
    }

    private void doCreate() {
        onCreate();
    }

    private void doStart() {
        onStart();
    }

    private void doResume() {
        onResume();
    }

    private void doPause() {
        onPause();
    }

    private void doStop() {
        onStop();
    }

    private void doDestroy() {
        if (model != null) {
            model.destroy();
        }
        onDestroy();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    void performRemove() {
        View me = getRoot();
        performLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        ViewGroup parent = (ViewGroup) me.getParent();
        performLifecycleEvent(Lifecycle.Event.ON_STOP);
        performLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        parent.removeView(me);
    }

    protected abstract M initModel();

    protected abstract int modelId();
}
