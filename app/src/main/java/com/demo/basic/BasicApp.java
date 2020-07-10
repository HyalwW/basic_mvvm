package com.demo.basic;

import android.app.Application;
import android.content.Context;

import com.demo.basic.net.Requester;
import com.google.gson.Gson;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class BasicApp extends Application {
    private static Gson gson;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        context = this;
        Requester.init(this);
    }

    public static Gson getGson() {
        return gson;
    }

    public static Context getContext() {
        return context;
    }
}
