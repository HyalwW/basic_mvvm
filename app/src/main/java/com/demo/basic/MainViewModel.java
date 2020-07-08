package com.demo.basic;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.basic.base.BaseViewModel;
import com.demo.basic.data.MapBean;
import com.demo.basic.net.ListenerAdapter;
import com.demo.basic.net.Requester;
import com.demo.basic.utils.BindingCommand;
import com.demo.basic.utils.SingleLiveEvent;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class MainViewModel extends BaseViewModel {
    private BindingCommand locateCommand;
    private SingleLiveEvent<MapBean> locateEvent;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate(Application application) {
        locateEvent = new SingleLiveEvent<>();
        locateCommand = new BindingCommand(() -> {
            Requester.getBean("https://geo.datav.aliyun.com/areas_v2/bound/100000.json", new ListenerAdapter<MapBean>() {
                @Override
                public void onSucceed(MapBean mapBean) {
                    super.onSucceed(mapBean);
                    locateEvent.setValue(mapBean);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    super.onFailed(throwable);
                    Log.e("wwh", "MainViewModel --> onFailed: " + throwable.getMessage());
                }
            });
        });
    }

    @Override
    protected void onDestroy() {

    }

    public BindingCommand getLocateCommand() {
        return locateCommand;
    }

    public SingleLiveEvent<MapBean> getLocateEvent() {
        return locateEvent;
    }
}
