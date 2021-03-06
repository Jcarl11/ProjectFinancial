package com.example.ratio.DAO;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public interface BaseDAO<T> {
    T insert(T objectEntity);
    int insertAll(List<T> objectList);
    T get(String objectId);
    List<T> getBulk(@Nullable String sqlCommand);
    T update(T newRecord);
    int delete(T object);
    int deleteAll(List<T> objectList);
}
