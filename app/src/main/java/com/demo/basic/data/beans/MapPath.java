package com.demo.basic.data.beans;

import android.graphics.Path;

import com.demo.basic.utils.Reusable;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/13
 * Description: blablabla
 */
public class MapPath extends Path implements Reusable {
    @Override
    public boolean isLeisure() {
        return isEmpty();
    }
}
