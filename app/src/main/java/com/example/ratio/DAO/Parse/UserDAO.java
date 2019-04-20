package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.User;

import java.util.List;

public class UserDAO implements BaseDAO<User> {
    @Override
    public int insert(User objectEntity) {
        return 0;
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
    public boolean update(User oldRecord, User newRecord) {
        return false;
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
