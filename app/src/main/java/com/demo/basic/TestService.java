package com.demo.basic;

import android.util.Log;

import com.demo.basic.base.BaseFloatService;
import com.demo.basic.databinding.ServiceTestBinding;
import com.demo.basic.pages.TestPage;

public class TestService extends BaseFloatService<ServiceTestBinding> {

    @Override
    protected int layoutId() {
        return R.layout.service_test;
    }

    @Override
    protected void initData() {
        triggerMove(dataBinding.base);
        dataBinding.add.setOnClickListener(v -> addPage(new TestPage(), R.id.page_container));
        dataBinding.replace.setOnClickListener(v -> {
            replacePage(new TestPage(), R.id.page_container);
        });
        dataBinding.back.setOnClickListener(v -> {
            removeTop();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("wwh", "TestService --> onDestroy: ");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getAnimId() {
        return 0;
    }

    @Override
    protected int showX() {
        return 0;
    }

    @Override
    protected int showY() {
        return 0;
    }
}
