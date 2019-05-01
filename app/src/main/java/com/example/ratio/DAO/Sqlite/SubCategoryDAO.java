package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Subcategory;

import java.util.List;

public class SubCategoryDAO implements BaseDAO<Subcategory> {
    @Override
    public Subcategory insert(Subcategory objectEntity) {
        return null;
    }

    @Override
    public int insertAll(List<Subcategory> objectList) {
        return 0;
    }

    @Override
    public Subcategory get(String objectId) {
        return null;
    }

    @Override
    public List<Subcategory> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public Subcategory update(Subcategory newRecord) {
        return null;
    }

    @Override
    public int delete(Subcategory object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Subcategory> objectList) {
        return 0;
    }
}
