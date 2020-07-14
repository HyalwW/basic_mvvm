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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
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
    public void onCreate() {
        super.onCreate();
        Log.e("wwh", "TestPage --> onCreate: " );
        rootView.base.setBackgroundColor(randomColor());
        model.getHideEvent().observe(this, aBoolean -> {
            Log.e("wwh", "TestPage --> hideChanged: ");
            if (aBoolean) {
                hide();
            } else {
                show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("wwh", "TestPage --> onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("wwh", "TestPage --> onResume: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("wwh", "TestPage --> onPause: " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("wwh", "TestPage --> onStop: " );
    }

    private int randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Override
    public void onVisibleChanged(boolean visible) {
        Log.e("wwh", "TestPage --> onVisibleChanged: " + visible);
    }

    @Override
    public void onDestroy() {
        Log.e("wwh", "TestPage --> onDestroy: " + this);
    }
}
