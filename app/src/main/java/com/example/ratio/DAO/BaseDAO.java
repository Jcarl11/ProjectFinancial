package com.example.ratio.DAO;

import java.util.List;

import androidx.annotation.Nullable;

public interface BaseDAO<T> {
    int insert(T objectEntity);
    int insertAll(List<T> objectList);
    T get(String objectId);
    List<T> getBulk(@Nullable String sqlCommand);
    boolean update(T newRecord);
    int delete(T object);
    int deleteAll(List<T> objectList);
}
