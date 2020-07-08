package com.demo.basic;

import android.app.Application;

import com.demo.basic.net.Requester;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class BasicApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Requester.init(this);
    }
}
