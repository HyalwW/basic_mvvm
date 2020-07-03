package com.demo.basic.pages;

import android.util.Log;

import androidx.databinding.ObservableField;

import com.demo.basic.base.BaseFloatModel;
import com.demo.basic.utils.BindingCommand;
import com.demo.basic.utils.SingleLiveEvent;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/3
 * Description: blablabla
 */
public class TestModel extends BaseFloatModel {
    private BindingCommand hideCommand;
    private ObservableField<String> text;

    private SingleLiveEvent<Boolean> hideEvent;

    @Override
    public void onCreate() {
        hideEvent = new SingleLiveEvent<>();
        hideCommand = new BindingCommand(() -> {
            Log.e("wwh", "TestModel --> onCreate: " );
            hideEvent.setValue(true);
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
