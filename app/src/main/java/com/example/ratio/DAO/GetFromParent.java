package com.example.ratio.DAO;

import java.util.List;

public interface GetFromParent<T> {
    List<T> getObjects(String parentID);
}
