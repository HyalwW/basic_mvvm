package com.demo.basic.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.demo.basic.base.BaseSurfaceView;
import com.demo.basic.data.MapBean;
import com.demo.basic.net.ListenerAdapter;
import com.demo.basic.net.Requester;
import com.demo.basic.utils.PathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class MapShowView extends BaseSurfaceView {
    private MapBean.FeaturesBean nowFeature;
    private Path mapPath;
    private Map<MapBean.FeaturesBean, Path> insides;
    private PathEffect dash;
    private int selectCode = 0;
    private float scale, canvasScale;
    private float baseX, baseY, scaleX, scaleY;
    private RectF bounds;
    private static final String base = "https://geo.datav.aliyun.com/areas_v2/bound/";
    private boolean canTouch;
    private String name = "";

    public MapShowView(Context context) {
        super(context);
    }

    public MapShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInit() {
        mapPath = new Path();
        mPaint.setTextAlign(Paint.Align.CENTER);
        insides = new ConcurrentHashMap<>();
        dash = new DashPathEffect(new float[]{5, 5}, 0);
        bounds = new RectF();
        if (canvasScale == 0) {
            canvasScale = 1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onReady() {
        callDraw("");
    }

    @Override
    protected void onDataUpdate() {

    }

    @Override
    protected void onRefresh(Canvas canvas) {

    }

    @Override
    protected void draw(Canvas canvas, Object data) {
        canvas.drawColor(Color.WHITE);
        canvas.save();
        if (baseX != 0 || baseY != 0 || canvasScale != 1) {
            canvas.translate(baseX, baseY);
            canvas.scale(canvasScale, canvasScale, scaleX, scaleY);
        }
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mapPath, mPaint);
        for (Map.Entry<MapBean.FeaturesBean, Path> entry : insides.entrySet()) {
            Path path = entry.getValue();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.BLACK);
            mPaint.setPathEffect(dash);
            canvas.drawPath(path, mPaint);
            if (selectCode == entry.getKey().getProperties().getAdcode()) {
                mPaint.setPathEffect(null);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.CYAN);
                canvas.drawPath(path, mPaint);
            }
        }
        canvas.restore();
        drawName(canvas);
    }

    private void drawName(Canvas canvas) {
        mPaint.setPathEffect(null);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        float textSize = getMeasuredWidth() * 0.04f;
        mPaint.setTextSize(textSize);
        canvas.drawText(name, getMeasuredWidth() >> 1, textSize * 1.5f, mPaint);
    }

    @Override
    protected void onDrawRect(Canvas canvas, Object data, Rect rect) {

    }

    @Override
    protected boolean preventClear() {
        return false;
    }

    public void setLocation(MapBean bean) {
        canTouch = true;
        selectCode = 0;
        insides.clear();
        bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        nowFeature = bean.getFeatures().get(0);
        pos2Path(mapPath, nowFeature, true);
        Requester.getBean(base + nowFeature.getProperties().getAdcode() + "_full.json", new ListenerAdapter<MapBean>() {
            @Override
            public void onSucceed(MapBean mapBean) {
                super.onSucceed(mapBean);
                for (MapBean.FeaturesBean feature : mapBean.getFeatures()) {
                    Path path = new Path();
                    pos2Path(path, feature, false);
                    insides.put(feature, path);
                    callDraw("");
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Log.e("wwh", "MapShowView --> onFailed: " + throwable.getMessage());
            }
        });
        name = nowFeature.getProperties().getName();
        callDraw("");
    }

    private void pos2Path(Path path, MapBean.FeaturesBean featuresBean, boolean needCompute) {
        path.reset();
        if (needCompute) {
            bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        List<float[]> list = new ArrayList<>();
        for (List<Double> doubles : featuresBean.getGeometry().getCoordinates().get(0).get(0)) {
            float[] pos = new float[2];
            pos[0] = (float) (doubles.get(0) * 1000000);
            pos[1] = (float) (doubles.get(1) * 1000000);
            list.add(pos);
            if (needCompute) {
                compute(pos[0], pos[1]);
            }
        }
        float h = bounds.bottom - bounds.top;
        float w = bounds.right - bounds.left;
        float offsetX = 0, offsetY = 0;
        if (h > w) {
            scale = getMeasuredHeight() / h;
            offsetX = (getMeasuredWidth() - w * scale) / 2;
        } else {
            scale = getMeasuredWidth() / w;
            offsetY = (h * scale - getMeasuredHeight()) / 2;
        }
        for (float[] ints : list) {
            ints[0] = (ints[0] - bounds.left) * scale;
            ints[1] = (ints[1] - bounds.top) * scale;
        }
        float[] is = list.get(0);
        path.moveTo(is[0] + offsetX, getMeasuredHeight() - is[1] + offsetY);
        for (int i = 1; i < list.size(); i++) {
            float[] ints = list.get(i);
            path.lineTo(ints[0] + offsetX, getMeasuredHeight() - ints[1] + offsetY);
        }
    }

    private void compute(float x, float y) {
        bounds.left = Math.min(x - 10, bounds.left);
        bounds.right = Math.max(x + 10, bounds.right);
        bounds.top = Math.min(y - 10, bounds.top);
        bounds.bottom = Math.max(y + 10, bounds.bottom);
    }

    private float d1x, d1y, d2x, d2y;
    private boolean isClick, doubleTouch;
    private float bl, bs, bx, by;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canTouch) {
            return super.onTouchEvent(event);
        }
        int count = event.getPointerCount();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (id == 0) {
                    isClick = true;
                    d1x = event.getX(0);
                    d1y = event.getY(0);
                    bx = baseX;
                    by = baseY;
                } else if (count > 1 && id == 1) {
                    isClick = false;
                    doubleTouch = true;
                    d2x = event.getX(1);
                    d2y = event.getY(1);
                    bl = distance(d1x, d1y, d2x, d2y);
                    bs = canvasScale;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (count > 1) {
                    //缩放暂缓
                    float nl = distance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    canvasScale = bs + (nl - bl) * 0.005f;
                    if (canvasScale < 0.2) {
                        canvasScale = 0.2f;
                    }
                    scaleX = (event.getX(0) + event.getX(1)) / 2;
                    scaleY = (event.getY(0) + event.getY(1)) / 2;
                } else if (!doubleTouch) {
                    isClick = false;
                    baseX = bx + event.getX() - d1x;
                    baseY = by + event.getY() - d1y;
                }
                callDraw("");
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!doubleTouch) {
                    if (isClick) {
                        if (baseX != 0 || baseY != 0 || canvasScale != 1) {
                            scaleX = scaleY = baseX = baseY = 0;
                            canvasScale = 1;
                            callDraw("");
                            return true;
                        }
                        for (Map.Entry<MapBean.FeaturesBean, Path> entry : insides.entrySet()) {
                            if (PathUtil.isInside(entry.getValue(), (int) event.getX(), (int) event.getY())) {
                                int key = entry.getKey().getProperties().getAdcode();
                                if (selectCode != key) {
                                    selectCode = key;
                                    name = entry.getKey().getProperties().getName();
                                    callDraw("");
                                } else {
                                    canTouch = false;
                                    name = "正在获取：" + entry.getKey().getProperties().getName();
                                    callDraw("");
                                    Requester.getBean(base + key + ".json", new ListenerAdapter<MapBean>() {
                                        @Override
                                        public void onSucceed(MapBean mapBean) {
                                            super.onSucceed(mapBean);
                                            setLocation(mapBean);
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            super.onFailed(throwable);
                                            canTouch = true;
                                            name = "获取失败，请重试！";
                                            callDraw("");
                                        }
                                    });
                                }
                                return true;
                            }
                        }
                        if (selectCode != 0) {
                            selectCode = 0;
                            name = nowFeature.getProperties().getName();
                            callDraw("");
                        } else {
                            MapBean.FeaturesBean.PropertiesBean.ParentBean parent = nowFeature.getProperties().getParent();
                            if (parent != null && parent.getAdcode() != 0) {
                                canTouch = false;
                                String tempName = name;
                                name = "正在获取上一级";
                                callDraw("");
                                Requester.getBean(base + parent.getAdcode() + ".json", new ListenerAdapter<MapBean>() {
                                    @Override
                                    public void onSucceed(MapBean mapBean) {
                                        super.onSucceed(mapBean);
                                        setLocation(mapBean);
                                    }

                                    @Override
                                    public void onFailed(Throwable throwable) {
                                        super.onFailed(throwable);
                                        name = tempName;
                                        callDraw("");
                                        canTouch = true;
                                    }
                                });
                            }
                        }
                    }
                }
                if (count == 1) {
                    doubleTouch = isClick = false;
                }
                break;
        }
        return true;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
