package com.demo.basic.data.beans;

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
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int adCode;
    private String name;
    private String level;
    private int childrenNum;
    private long parent;
    private String coordinates;
    private boolean isFull;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAdCode() {
        return adCode;
    }

    public void setAdCode(int adCode) {
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

    public int getChildrenNum() {
        return childrenNum;
    }

    public void setChildrenNum(int childrenNum) {
        this.childrenNum = childrenNum;
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

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setCoords(List<List<Double>> coordinates) {
        Gson gson = BasicApp.getGson();
        setCoordinates(gson.toJson(coordinates));
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
