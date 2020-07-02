package com.demo.basic;

import android.app.Application;

import androidx.annotation.NonNull;

import com.demo.basic.base.BaseViewModel;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class MainViewModel extends BaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {

    }

    @Override
    protected void onDestroy() {

    }
}
