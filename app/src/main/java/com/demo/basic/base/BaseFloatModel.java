package com.demo.basic.base;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/3
 * Description: blablabla
 */
public abstract class BaseFloatModel {
    protected Context context;
    private CompositeDisposable compositeDisposable;

    public void create(Context context) {
        this.context = context;
        onCreate();
    }

    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void destroy() {
        onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

    public abstract void onCreate();

    public abstract void onDestroy();
}
