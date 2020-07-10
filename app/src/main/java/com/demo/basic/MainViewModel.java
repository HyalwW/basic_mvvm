package com.demo.basic;

import android.app.Application;

import androidx.annotation.NonNull;

import com.demo.basic.base.BaseViewModel;
import com.demo.basic.utils.BindingCommand;
import com.demo.basic.utils.SingleLiveEvent;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class MainViewModel extends BaseViewModel {
    private BindingCommand locateCommand;
    private SingleLiveEvent<Integer> locateEvent;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        locateEvent = new SingleLiveEvent<>();
        locateCommand = new BindingCommand(() -> {
            locateEvent.setValue(100000);
        });
    }

    @Override
    protected void onDestroy() {

    }

    public BindingCommand getLocateCommand() {
        return locateCommand;
    }

    public SingleLiveEvent<Integer> getLocateEvent() {
        return locateEvent;
    }
}
