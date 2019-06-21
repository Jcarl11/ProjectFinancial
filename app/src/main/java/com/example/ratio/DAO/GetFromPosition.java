package com.example.ratio.DAO;

import java.util.List;

public interface GetFromPosition<T> {
    List<T> getUsers(String position);
}
