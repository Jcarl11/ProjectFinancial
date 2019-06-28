package com.example.ratio.DAO;

import java.util.List;

public interface GetAverage<T> {
    List<T> getTopHighest(int limit);
}
