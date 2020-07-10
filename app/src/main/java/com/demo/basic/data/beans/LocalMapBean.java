package com.demo.basic.data.beans;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.demo.basic.BasicApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/10
 * Description: blablabla
 */
@Entity
public class LocalMapBean {
    @PrimaryKey
    private long adCode;
    private String name;
    private String level;
    private long parent;
    private String coordinates;

    public long getAdCode() {
        return adCode;
    }

    public void setAdCode(long adCode) {
        this.adCode = adCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setCoords(List<List<Double>> coordinates) {
        Gson gson = BasicApp.getGson();
        this.coordinates = gson.toJson(coordinates);
        if (adCode == 100000) {
            Log.e("wwh", "LocalMapBean --> setCoords: " + coordinates.size());
        }
    }

    public List<List<Double>> getCoords() {
        Gson gson = BasicApp.getGson();
        return gson.fromJson(coordinates, new TypeToken<List<List<Double>>>() {
        }.getType());
    }

    @Override
    public int hashCode() {
        if (level == null) {
            level = "";
        }
        return (int) adCode + name.hashCode() + level.hashCode();
    }
}
