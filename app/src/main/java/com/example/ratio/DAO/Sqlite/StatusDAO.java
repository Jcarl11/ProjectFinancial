package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Status;

import java.util.List;

public class StatusDAO implements BaseDAO<Status> {
    @Override
    public Status insert(Status objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<Status> objectList) {
        return 0;
    }

    @Override
    public Status get(String objectId) {
        return null;
    }

    @Override
    public List<Status> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public Status update(Status newRecord) {
        return null;
    }

    @Override
    public int delete(Status object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Status> objectList) {
        return 0;
    }
}
