package com.demo.basic.net;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class Requester {
    private static Gson mGson;
    private static Application mContext;
    private static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static void init(Application context) {
        mContext = context;
        mGson = new Gson();
    }

    /*------------------------------------------post---------------------------------------------------*/

    public static void postJson(String url, Map<String, Object> params, Listener<JsonObject> listener) {
        postJson(url, params, null, listener);
    }

    public static void postJson(String url, Map<String, Object> params, Map<String, String> headers, Listener<JsonObject> listener) {

    }

    public static void postJson(String url, RequestBody body, Map<String, String> headers, Listener<JsonObject> listener) {

    }

    /*------------------------------------------get---------------------------------------------------*/

    public static void getJson(String url, Listener<JsonObject> listener) {
        MySubscriber<JsonObject> subscriber = new MySubscriber<>(listener);
        Observable<JsonObject> observable = RetrofitManager.getInstance()
                .getService()
                .getJson(url);
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static <T> void getBean(String url, Listener<T> listener) {
        getJson(url, new ListenerAdapter<JsonObject>() {
            @Override
            public void onSucceed(JsonObject object) {
                super.onSucceed(object);
                try {
                    ParameterizedType parameterizedType = (ParameterizedType) listener.getClass().getGenericSuperclass();
                    T t = mGson.fromJson(object, parameterizedType.getActualTypeArguments()[0]);
                    listener.onSucceed(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed(e);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                listener.onFailed(throwable);
            }
        });
    }

    private static RequestBody map2Json(Map map) {
        return RequestBody.create(MEDIA_TYPE_JSON, mGson.toJson(map));
    }

    public interface Listener<T> {

        /**
         * 请求发起时
         */
        void onStart();

        /**
         * @param t 成功返回
         */
        void onSucceed(T t);

        void onFailed(Throwable throwable);

        void onComplete();
    }
}
