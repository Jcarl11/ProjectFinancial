package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Recievables;

import java.util.List;

public class RecievablesDAO implements BaseDAO<Recievables> {
    @Override
    public Recievables insert(Recievables objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<Recievables> objectList) {
        return 0;
    }

    @Override
    public Recievables get(String objectId) {
        return null;
    }

    @Override
    public List<Recievables> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public Recievables update(Recievables newRecord) {
        return null;
    }

    @Override
    public int delete(Recievables object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Recievables> objectList) {
        return 0;
    }
}
