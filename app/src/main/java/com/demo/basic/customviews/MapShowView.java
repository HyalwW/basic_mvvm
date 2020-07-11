package com.demo.basic.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.demo.basic.base.BaseSurfaceView;
import com.demo.basic.data.AppDatabase;
import com.demo.basic.data.beans.LocalMapBean;
import com.demo.basic.data.beans.MapBean;
import com.demo.basic.data.daos.LocalMapDao;
import com.demo.basic.net.ListenerAdapter;
import com.demo.basic.net.Requester;
import com.demo.basic.utils.PathUtil;
import com.demo.basic.utils.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class MapShowView extends BaseSurfaceView {
    private LocalMapBean nowMapBean;
    private List<Path> mapPaths;
    private Map<LocalMapBean, List<Path>> insides;
    private PathEffect dash;
    private int selectCode = 0;
    private float canvasScale;
    private float baseX, baseY, scaleX, scaleY;
    private RectF bounds;
    private static final String base = "https://geo.datav.aliyun.com/areas_v2/bound/";
    private boolean canTouch;
    private String name = "";
    private Matrix matrix, inverse;
    private float[] pts;

    private LocalMapDao mapDao;

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
        mapPaths = new CopyOnWriteArrayList<>();
        mPaint.setTextAlign(Paint.Align.CENTER);
        insides = new ConcurrentHashMap<>();
        dash = new DashPathEffect(new float[]{5, 5}, 0);
        bounds = new RectF();
        if (canvasScale == 0) {
            canvasScale = 1;
        }
        matrix = new Matrix();
        inverse = new Matrix();
        pts = new float[2];
        mapDao = AppDatabase.getDatabase().localMapDao();
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
        canvas.drawColor(0xFF20B2AA);
        canvas.save();
        transformCanvas(canvas);
        drawMainArea(canvas);
        drawChildAreas(canvas);
        canvas.restore();
        drawName(canvas);
    }

    private void transformCanvas(Canvas canvas) {
        if (baseX != 0 || baseY != 0 || canvasScale != 1) {
            matrix.reset();
            matrix.preScale(canvasScale, canvasScale, scaleX, scaleY);
            matrix.postTranslate(baseX, baseY);
            canvas.concat(matrix);
        }
    }

    private void drawMainArea(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1.2f);
        for (Path mapPath : mapPaths) {
            canvas.drawPath(mapPath, mPaint);
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        for (Path mapPath : mapPaths) {
            canvas.drawPath(mapPath, mPaint);
        }
        mPaint.setStrokeWidth(0.5f);
    }

    private void drawChildAreas(Canvas canvas) {
        for (Map.Entry<LocalMapBean, List<Path>> entry : insides.entrySet()) {
            for (Path path : entry.getValue()) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.BLACK);
                mPaint.setPathEffect(dash);
                canvas.drawPath(path, mPaint);
                if (selectCode == entry.getKey().getAdCode()) {
                    mPaint.setPathEffect(null);
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(0xFFFF8C00);
                    canvas.drawPath(path, mPaint);
                }
            }
        }
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

    private void setOnLineLocation(MapBean bean) {
        canTouch = true;
        selectCode = 0;
        baseX = baseY = scaleX = scaleY = 0;
        canvasScale = 1;
        insides.clear();
        bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        nowMapBean = getLocalMapBean(bean.getFeatures().get(0), true);
        mapPaths.clear();
        pos2Path(mapPaths, nowMapBean.getCoords(), true);
        if (nowMapBean.getChildrenNum() > 0) {
            List<LocalMapBean> list = new ArrayList<>();
            list.add(nowMapBean);
            Requester.getBean(base + nowMapBean.getAdCode() + "_full.json", new ListenerAdapter<MapBean>() {
                @Override
                public void onSucceed(MapBean mapBean) {
                    super.onSucceed(mapBean);
                    for (MapBean.FeaturesBean feature : mapBean.getFeatures()) {
                        if (feature.getProperties().getLevel() == null)
                            break;
                        Path path = new Path();
                        LocalMapBean localMapBean = getLocalMapBean(feature, false);
                        list.add(localMapBean);
                        List<Path> paths = new ArrayList<>();
                        pos2Path(paths, feature.getGeometry().getCoordinates(), false);
                        insides.put(localMapBean, paths);
                    }
                    callDraw("");
                    if (list.size() > 0) {
                        ThreadPool.fixed().execute(() -> mapDao.insertLocalMaps(list));
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    super.onFailed(throwable);
                    Log.e("wwh", "MapShowView --> onFailed: " + throwable.getMessage());
                }
            });
        } else {
            ThreadPool.fixed().execute(() -> mapDao.insertLocalMap(nowMapBean));
        }
        name = nowMapBean.getName();
        callDraw("");
    }

    private LocalMapBean getLocalMapBean(MapBean.FeaturesBean feature, boolean isFull) {
        LocalMapBean localMapBean = new LocalMapBean();
        localMapBean.setAdCode(feature.getProperties().getAdcode());
        localMapBean.setName(feature.getProperties().getName());
        localMapBean.setLevel(feature.getProperties().getLevel());
        localMapBean.setChildrenNum(feature.getProperties().getChildrenNum());
        localMapBean.setFull(isFull);
        MapBean.FeaturesBean.PropertiesBean.ParentBean parent = feature.getProperties().getParent();
        if (parent != null) {
            localMapBean.setParent(parent.getAdcode());
        }
        localMapBean.setCoords(feature.getGeometry().getCoordinates());
        return localMapBean;
    }

    private void pos2Path(List<Path> paths, List<List<List<List<Double>>>> coords, boolean needCompute) {
        if (needCompute) {
            bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        List<List<float[]>> list = new ArrayList<>();
        for (List<List<List<Double>>> lists : coords) {
            for (List<List<Double>> listList : lists) {
                List<float[]> ll = new ArrayList<>();
                for (List<Double> doubles : listList) {
                    float[] pos = new float[2];
                    pos[0] = (float) (doubles.get(0) * 1000000);
                    pos[1] = (float) (doubles.get(1) * 1200000);
                    ll.add(pos);
                    if (needCompute) {
                        compute(pos[0], pos[1]);
                    }
                }
                list.add(ll);
            }
        }
        float h = bounds.bottom - bounds.top;
        float w = bounds.right - bounds.left;
        float offsetX = 0, offsetY = 0;
        float scale;
        if (h > w) {
            scale = getMeasuredHeight() / h;
            offsetX = (getMeasuredWidth() - w * scale) / 2;
        } else {
            scale = getMeasuredWidth() / w;
            offsetY = (h * scale - getMeasuredHeight()) / 2;
        }
        for (List<float[]> ll : list) {
            Path path = new Path();
            for (float[] ints : ll) {
                ints[0] = (ints[0] - bounds.left) * scale;
                ints[1] = (ints[1] - bounds.top) * scale;
            }
            float[] is = ll.get(0);
            path.moveTo(is[0] + offsetX, getMeasuredHeight() - is[1] + offsetY);
            for (int i = 1; i < ll.size(); i++) {
                float[] ints = ll.get(i);
                path.lineTo(ints[0] + offsetX, getMeasuredHeight() - ints[1] + offsetY);
            }
            paths.add(path);
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
                    matrix.invert(inverse);
                    d1x = (d1x + d2x) / 2;
                    d1y = (d1y + d2y) / 2;
                    pts[0] = d1x;
                    pts[1] = d1y;
                    inverse.mapPoints(pts);
                    scaleX = pts[0];
                    scaleY = pts[1];
                    matrix.reset();
                    matrix.preScale(canvasScale, canvasScale, scaleX, scaleY);
                    matrix.postTranslate(baseX, baseY);
                    matrix.mapPoints(pts);
                    bx += (d1x - pts[0]);
                    by += (d1y - pts[1]);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (count > 1) {
                    float nl = distance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    canvasScale = bs + (nl - bl) * 0.005f;
                    if (canvasScale < 0.2) {
                        canvasScale = 0.2f;
                    } else if (canvasScale > 15f) {
                        canvasScale = 15;
                    }
                    pts[0] = (event.getX(0) + event.getX(1)) / 2;
                    pts[1] = (event.getY(0) + event.getY(1)) / 2;
                    baseX = bx + pts[0] - d1x;
                    baseY = by + pts[1] - d1y;
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
                        for (Map.Entry<LocalMapBean, List<Path>> entry : insides.entrySet()) {
                            pts[0] = event.getX();
                            pts[1] = event.getY();
                            if (baseX != 0 || baseY != 0 || canvasScale != 1) {
                                matrix.invert(inverse);
                                inverse.mapPoints(pts);
                            }
                            for (Path path : entry.getValue()) {
                                if (PathUtil.isInside(path, (int) pts[0], (int) pts[1])) {
                                    int key = entry.getKey().getAdCode();
                                    if (selectCode != key) {
                                        selectCode = key;
                                        name = entry.getKey().getName();
                                        callDraw("");
                                    } else {
                                        showLocation(key, "正在获取：" + entry.getKey().getName(), "获取失败，请重试！");
                                    }
                                    return true;
                                }
                            }
                        }
                        if (selectCode != 0) {
                            selectCode = 0;
                            name = nowMapBean.getName();
                            callDraw("");
                        } else if (baseX != 0 || baseY != 0 || canvasScale != 1) {
                            scaleX = scaleY = baseX = baseY = 0;
                            canvasScale = 1;
                            matrix.reset();
                            callDraw("");
                        } else {
                            long parent = nowMapBean.getParent();
                            if (parent != 0) {
                                canTouch = false;
                                String tempName = name;
                                showLocation((int) parent, "正在获取上一级", tempName);
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

    public void showLocation(int adCode, String processName, String failName) {
        name = processName;
        callDraw("");
        canTouch = false;
        LocalMapBean mapBean = mapDao.loadLocalMap(adCode, true);
        if (mapBean == null) {
            mapBean = mapDao.loadLocalMap(adCode, false);
            if (mapBean != null) {
                selectCode = 0;
                baseX = baseY = scaleX = scaleY = 0;
                canvasScale = 1;
                insides.clear();
                bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
                nowMapBean = mapBean;
                mapPaths.clear();
                pos2Path(mapPaths, nowMapBean.getCoords(), true);
                name = nowMapBean.getName();
                callDraw("");
            }
            Log.e("wwh", "MapShowView --> showLocation: 从网络获取");
            Requester.getBean(base + adCode + ".json", new ListenerAdapter<MapBean>() {
                @Override
                public void onSucceed(MapBean mapBean) {
                    super.onSucceed(mapBean);
                    setOnLineLocation(mapBean);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    super.onFailed(throwable);
                    name = failName;
                    callDraw("");
                    canTouch = true;
                }
            });
        } else {
            Log.e("wwh", "MapShowView --> showLocation: 从本地获取");
            setLocalLocation(mapBean);
        }
    }

    private void setLocalLocation(LocalMapBean mapBean) {
        canTouch = true;
        selectCode = 0;
        baseX = baseY = scaleX = scaleY = 0;
        canvasScale = 1;
        insides.clear();
        bounds.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        nowMapBean = mapBean;
        mapPaths.clear();
        pos2Path(mapPaths, nowMapBean.getCoords(), true);
        List<LocalMapBean> children = mapDao.loadChildren(nowMapBean.getAdCode(), false);
        if (children != null && children.size() > 0) {
            for (LocalMapBean child : children) {
                List<Path> paths = new ArrayList<>();
                pos2Path(paths, child.getCoords(), false);
                insides.put(child, paths);
            }
            name = nowMapBean.getName();
            callDraw("");
        } else {
            Log.e("wwh", "MapShowView-->setLocalLocation(): 网络获取子项");
            Requester.getBean(base + nowMapBean.getAdCode() + "_full.json", new ListenerAdapter<MapBean>() {
                @Override
                public void onSucceed(MapBean mapBean) {
                    super.onSucceed(mapBean);
                    List<LocalMapBean> list = new ArrayList<>();
                    for (MapBean.FeaturesBean feature : mapBean.getFeatures()) {
                        if (feature.getProperties().getLevel() == null)
                            break;
                        LocalMapBean localMapBean = getLocalMapBean(feature, false);
                        list.add(localMapBean);
                        List<Path> path = new ArrayList<>();
                        pos2Path(path, feature.getGeometry().getCoordinates(), false);
                        insides.put(localMapBean, path);
                    }
                    callDraw("");
                    if (list.size() > 0) {
                        ThreadPool.fixed().execute(() -> mapDao.insertLocalMaps(list));
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    super.onFailed(throwable);
                    Log.e("wwh", "MapShowView --> onFailed: " + throwable.getMessage());
                }
            });
            name = nowMapBean.getName();
            callDraw("");
        }
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
