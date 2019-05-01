package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.User;

import java.util.List;

public class UserDAO implements BaseDAO<User> {
    @Override
    public User insert(User objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<User> objectList) {
        return 0;
    }

    @Override
    public User get(String objectId) {
        return null;
    }

    @Override
    public List<User> getBulk(String sqlCommand) {
        return null;
    }


    @Override
    public User update(User newRecord) {
        return null;
    }

    @Override
    public int delete(User object) {
        return 0;
    }

    @Override
    public int deleteAll(List<User> objectList) {
        return 0;
    }
}
