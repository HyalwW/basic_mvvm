package com.demo.basic.pages;

import android.util.Log;

import androidx.databinding.ObservableField;

import com.demo.basic.base.BaseFloatModel;
import com.demo.basic.utils.BindingCommand;
import com.demo.basic.utils.SingleLiveEvent;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/3
 * Description: blablabla
 */
public class TestModel extends BaseFloatModel implements Serializable {
    private BindingCommand hideCommand;
    private ObservableField<String> text;

    private SingleLiveEvent<Boolean> hideEvent;

    @Override
    public void onCreate() {
        hideEvent = new SingleLiveEvent<>();
        hideCommand = new BindingCommand(() -> {
            hideEvent.setValue(true);
            addDisposable(Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                try {
                    Thread.sleep(3000);
                    emitter.onNext(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Log.e("wwh", "TestModel --> time's up: ");
                        hideEvent.setValue(aBoolean);
                    }));
        });
        text = new ObservableField<>("这是一个页面");
    }

    @Override
    public void onDestroy() {

    }

    public BindingCommand getHideCommand() {
        return hideCommand;
    }

    public ObservableField<String> getText() {
        return text;
    }

    public SingleLiveEvent<Boolean> getHideEvent() {
        return hideEvent;
    }
}
