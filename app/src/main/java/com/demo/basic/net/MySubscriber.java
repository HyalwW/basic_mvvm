package com.demo.basic.net;

import io.reactivex.observers.DisposableObserver;

public class MySubscriber<T> extends DisposableObserver<T> {

    private final Requester.Listener<T> listener;

    public MySubscriber(Requester.Listener<T> listener) {
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        listener.onStart();
    }

    @Override
    public void onNext(T o) {
        listener.onSucceed(o);
    }

    @Override
    public void onError(Throwable e) {
        listener.onFailed(e);
    }

    @Override
    public void onComplete() {
        listener.onComplete();
    }

}
