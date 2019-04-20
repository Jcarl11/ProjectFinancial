package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Expenses;

import java.util.List;

public class ExpensesDAO implements BaseDAO<Expenses> {
    @Override
    public int insert(Expenses objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<Expenses> objectList) {
        return 0;
    }

    @Override
    public Expenses get(String objectId) {
        return null;
    }

    @Override
    public List<Expenses> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public boolean update(Expenses newRecord) {
        return false;
    }

    @Override
    public int delete(Expenses object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Expenses> objectList) {
        return 0;
    }
}
