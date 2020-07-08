package com.demo.basic.utils;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public class PathUtil {
    private static RectF bounds = new RectF();
    private static Region region = new Region();
    private static Region clip = new Region();

    public static boolean isInside(Path path, int x, int y) {
        bounds.setEmpty();
        path.computeBounds(bounds, true);
        clip.set(((int) bounds.left), ((int) bounds.top), ((int) bounds.right), (int) bounds.bottom);
        region.setPath(path, clip);
        return region.contains(x, y);
    }
}
