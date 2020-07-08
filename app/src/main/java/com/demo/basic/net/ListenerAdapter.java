package com.demo.basic.net;

public class ListenerAdapter<T> implements Requester.Listener<T> {
    @Override
    public void onStart() {

    }

    @Override
    public void onSucceed(T t) {

    }

    @Override
    public void onFailed(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
