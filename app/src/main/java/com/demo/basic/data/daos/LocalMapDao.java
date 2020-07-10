package com.demo.basic.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.demo.basic.data.beans.LocalMapBean;

import java.util.List;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/10
 * Description: blablabla
 */
@Dao
public interface LocalMapDao {
    @Query("select * from localmapbean where adCode = :adCode")
    LocalMapBean loadLocalMap(int adCode);

    @Query("select * from localmapbean where parent = :parentCode")
    List<LocalMapBean> loadChildren(int parentCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocalMap(LocalMapBean localMapBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocalMaps(List<LocalMapBean> list);

}
