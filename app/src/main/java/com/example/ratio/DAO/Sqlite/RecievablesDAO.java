package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Receivables;

import java.util.List;

public class RecievablesDAO implements BaseDAO<Receivables> {
    @Override
    public Receivables insert(Receivables objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<Receivables> objectList) {
        return 0;
    }

    @Override
    public Receivables get(String objectId) {
        return null;
    }

    @Override
    public List<Receivables> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public Receivables update(Receivables newRecord) {
        return null;
    }

    @Override
    public int delete(Receivables object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Receivables> objectList) {
        return 0;
    }
}
