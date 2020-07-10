package com.demo.basic;

import android.view.View;

import com.demo.basic.base.BaseActivity;
import com.demo.basic.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> implements View.OnClickListener {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.start.setOnClickListener(this);
        dataBinding.stop.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        viewModel.getLocateEvent().observe(this, integer -> dataBinding.mapView.showLocation(integer, "失败"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                TestService.start(this, null, TestService.class);
                break;
            case R.id.stop:
                TestService.stop(this, TestService.class);
                break;
        }
    }
}
