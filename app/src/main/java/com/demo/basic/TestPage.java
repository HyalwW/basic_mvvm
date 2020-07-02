package com.demo.basic;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.basic.base.FloatPage;
import com.demo.basic.databinding.PageTestBinding;

import java.util.Random;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/2
 * Description: blablabla
 */
public class TestPage extends FloatPage<PageTestBinding> {
    private int page;

    public TestPage(int page) {
        this.page = page;
    }

    @Override
    protected View onCreate(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.page_test, parent, false);
    }

    @Override
    protected void onAddToWindow() {
        rootView.text.setText("这是一个页面：" + page);
        rootView.base.setBackgroundColor(randomColor());
    }

    private int randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Override
    protected void onVisibleChanged(boolean visible) {

    }

    @Override
    protected void onRemoveFromWindow() {

    }

    @Override
    protected void onDestroy() {
        Log.e("wwh", "TestPage --> onDestroy: " + page);
    }
}
