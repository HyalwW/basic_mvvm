package com.demo.basic.pages;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.basic.BR;
import com.demo.basic.R;
import com.demo.basic.base.FloatPage;
import com.demo.basic.databinding.PageTestBinding;

import java.util.Random;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class TestPage extends FloatPage<TestModel, PageTestBinding> {


    @Override
    protected View onCreate(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.page_test, parent, false);
    }

    @Override
    protected TestModel initModel() {
        return new TestModel();
    }

    @Override
    protected int modelId() {
        return BR.model;
    }

    @Override
    protected void onAddToWindow() {
        rootView.base.setBackgroundColor(randomColor());
        model.getHideEvent().observe(this, aBoolean -> {
            Log.e("wwh", "TestPage --> onAddToWindow: " );
            if (aBoolean) {
                hide();
            } else {
                show();
            }
        });
    }

    private int randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Override
    protected void onVisibleChanged(boolean visible) {
        Log.e("wwh", "TestPage --> onVisibleChanged: " + visible);
    }

    @Override
    protected void onRemoveFromWindow() {

    }

    @Override
    protected void onDestroy() {
        Log.e("wwh", "TestPage --> onDestroy: " + this);
    }
}
