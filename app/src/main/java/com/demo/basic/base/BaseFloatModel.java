package com.demo.basic.base;

import android.content.Context;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/3
 * Description: blablabla
 */
public abstract class BaseFloatModel {
    protected Context context;

    public void create(Context context) {
        this.context = context;
        onCreate();
    }

    public void destroy() {
        onDestroy();
    }

    public abstract void onCreate();

    public abstract void onDestroy();
}
