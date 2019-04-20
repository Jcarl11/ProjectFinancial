package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Services;

import java.util.List;

public class ServicesDAO implements BaseDAO<Services> {
    @Override
    public int insert(Services objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<Services> objectList) {
        return 0;
    }

    @Override
    public Services get(String objectId) {
        return null;
    }

    @Override
    public List<Services> getBulk(String sqlCommand) {
        return null;
    }


    @Override
    public boolean update(Services oldRecord, Services newRecord) {
        return false;
    }

    @Override
    public int delete(Services object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Services> objectList) {
        return 0;
    }
}
