package com.demo.basic.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;

import com.demo.basic.PageManager;

public abstract class BaseFloatService<DB extends ViewDataBinding> extends Service {
    protected final String TAG = getClass().getSimpleName();
    protected DB dataBinding;
    protected WindowManager windowManager;
    protected WindowManager.LayoutParams params;
    protected boolean isShowing;
    private int startX, startY;
    protected int displayWidth, displayHeight;
    protected int[] measuredMatrix;
    protected int statusBarHeight, navigationBarHeight;
    private final float mTouchSlop = 10;
    //VP128底部距离
    private static final int BOTTOM_DISTANCE = 200;

    private PageManager manager;
    private LayoutInflater inflater;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutId(), null, false);
        statusBarHeight = getStatusHeight();
        navigationBarHeight = getNavBarHeight();
        initData();
        initView();
        initWindow();
    }

    private void initWindow() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.format = PixelFormat.TRANSPARENT;
        params.gravity = Gravity.START | Gravity.TOP;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        if (getAnimId() != 0) params.windowAnimations = getAnimId();

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;
        measuredMatrix = measureView(dataBinding.getRoot());
        params.x = showX() == 0 ? (displayWidth - measuredMatrix[0]) / 2 : showX();
        params.y = showY() == 0 ? (displayHeight - measuredMatrix[1]) / 2 : showY();
    }

    /**
     * 测量某个View高宽
     *
     * @param view view
     * @return 返回{宽，高}
     */
    protected int[] measureView(View view) {
        int[] size = new int[2];
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        size[0] = view.getMeasuredWidth();
        size[1] = view.getMeasuredHeight();
        return size;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        show();
        return START_NOT_STICKY;
    }

    private boolean isClick;

    /**
     * 设置移动悬浮窗的view
     *
     * @param view view
     */
    protected void triggerMove(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    view.setActivated(true);
                    isClick = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(startX - event.getX()) >= mTouchSlop
                            || Math.abs(startY - event.getY()) >= mTouchSlop) {
                        isClick = false;
                    }
                    params.x = (int) event.getRawX() - startX;
                    params.y = (int) event.getRawY() - startY;
                    handleMove();
                    update();
                    break;
                case MotionEvent.ACTION_UP:
                    if (isClick) {
                        view.performClick();
                    }
                    view.setActivated(false);
                    break;
            }
            return true;
        });
    }

    private void handleMove() {
        if (params.y <= statusBarHeight) {
            params.y = statusBarHeight;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (isShowing) {
            update();
        }
        //todo 待添加横竖屏切换处理
        super.onConfigurationChanged(newConfig);
    }

    protected void show() {
        if (!isShowing) {
            addToWindow();
            isShowing = true;
        }
    }

    protected void remove() {
        if (isShowing) {
            removeFromWindow();
            isShowing = false;
        }
    }

    protected void update() {
        if (dataBinding != null && windowManager != null) {
            updateToWindow();
        }
    }


    private boolean addToWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "未开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!dataBinding.getRoot().isAttachedToWindow()) {
                    windowManager.addView(dataBinding.getRoot(), params);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (dataBinding.getRoot().getParent() == null) {
                        windowManager.addView(dataBinding.getRoot(), params);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void removeFromWindow() {
        try {
            if (windowManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (dataBinding.getRoot().isAttachedToWindow()) {
                        //dataBinding.getRoot().onDetachWindow()
                        windowManager.removeViewImmediate(dataBinding.getRoot());
                    } else {
                        if (dataBinding.getRoot().getParent() != null) {
                            windowManager.removeView(dataBinding.getRoot());
//                            windowManager.removeViewImmediate(view);
                        }
                    }
                } else {
                    if (dataBinding.getRoot().getParent() != null) {
                        windowManager.removeViewImmediate(dataBinding.getRoot());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateToWindow() {
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (dataBinding.getRoot().isAttachedToWindow()) {
                    windowManager.updateViewLayout(dataBinding.getRoot(), params);
                }
            } else {
                try {
                    if (dataBinding.getRoot().getParent() != null) {
                        windowManager.updateViewLayout(dataBinding.getRoot(), params);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <F extends BaseFloatService> void start(Context context, Bundle argument, Class<F> clz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                return;
            }
        }
        Intent intent = new Intent(context, clz);
        if (argument != null) {
            intent.putExtras(argument);
        }
        context.startService(intent);
    }

    public static <F extends BaseFloatService> void stop(Context context, Class<F> clz) {
        context.stopService(new Intent(context, clz));
    }

    public boolean isShowing() {
        return isShowing;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        clearAllPages();
        remove();
        super.onDestroy();
    }

    public int getNavBarHeight() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    private int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    public void addPage(FloatPage page, int containerId) {
        if (manager == null) {
            initPageAbout();
        }
        ViewGroup parent = dataBinding.getRoot().findViewById(containerId);
        View child = page.init(this, inflater, parent);
        page.performLifecycleEvent(Lifecycle.Event.ON_CREATE);
        manager.add(this, page);
        page.performLifecycleEvent(Lifecycle.Event.ON_START);
        parent.addView(child);
        page.performLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    public void replacePage(FloatPage page, int containerId) {
        if (manager == null) {
            initPageAbout();
        }
        ViewGroup parent = dataBinding.getRoot().findViewById(containerId);
        View child = page.init(this, inflater, parent);
        page.performLifecycleEvent(Lifecycle.Event.ON_CREATE);
        parent.addView(child);
        page.performLifecycleEvent(Lifecycle.Event.ON_START);
        page.performLifecycleEvent(Lifecycle.Event.ON_RESUME);
        clearAllPages();
        manager.add(this, page);
    }

    public void removeTop() {
        if (manager == null) {
            return;
        }
        int size = manager.getPageSize(this);
        if (size > 0) {
            FloatPage floatPage = manager.pop(this);
            removePage(floatPage);
        }
    }

    private void removePage(FloatPage floatPage) {
        if (manager == null) {
            return;
        }
        floatPage.performRemove();
    }

    private void clearAllPages() {
        if (manager != null) {
            while (manager.getPageSize(this) > 0) {
                removeTop();
            }
        }
    }

    private void initPageAbout() {
        manager = PageManager.getManager();
        inflater = LayoutInflater.from(this);
    }

    protected abstract int layoutId();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int getAnimId();

    protected abstract int showX();

    protected abstract int showY();
}
