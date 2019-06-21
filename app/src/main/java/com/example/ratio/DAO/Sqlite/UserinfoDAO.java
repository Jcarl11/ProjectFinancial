package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Userinfo;

import java.util.List;

import androidx.annotation.Nullable;

public class UserinfoDAO implements BaseDAO<Userinfo> {
    @Override
    public Userinfo insert(Userinfo objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<Userinfo> objectList) {
        return 0;
    }

    @Override
    public Userinfo get(String objectId) {
        return null;
    }

    @Override
    public List<Userinfo> getBulk(@Nullable String sqlCommand) {
        return null;
    }

    @Override
    public Userinfo update(Userinfo newRecord) {
        return null;
    }

    @Override
    public int delete(Userinfo object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Userinfo> objectList) {
        return 0;
    }
}
