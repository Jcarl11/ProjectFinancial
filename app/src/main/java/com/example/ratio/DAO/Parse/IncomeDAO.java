package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Income;

import java.util.List;

public class IncomeDAO implements BaseDAO<Income> {
    @Override
    public int insert(Income objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<Income> objectList) {
        return 0;
    }

    @Override
    public Income get(String objectId) {
        return null;
    }

    @Override
    public List<Income> getBulk(String sqlCommand) {
        return null;
    }


    @Override
    public boolean update(Income oldRecord, Income newRecord) {
        return false;
    }

    @Override
    public int delete(Income object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Income> objectList) {
        return 0;
    }
}
