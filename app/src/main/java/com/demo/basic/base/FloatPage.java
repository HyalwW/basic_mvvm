package com.demo.basic.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public abstract class FloatPage<DB extends ViewDataBinding> {
    protected DB rootView;
    protected Context context;

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

    protected abstract View onCreate(LayoutInflater inflater, ViewGroup parent);

    protected abstract void onAddToWindow();

    protected abstract void onVisibleChanged(boolean visible);

    protected abstract void onRemoveFromWindow();

    protected abstract void onDestroy();
}
