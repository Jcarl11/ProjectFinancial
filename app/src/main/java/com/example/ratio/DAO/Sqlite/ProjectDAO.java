package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Projects;

import java.util.List;

public class ProjectDAO implements BaseDAO<Projects> {
    @Override
    public int insert(Projects objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<Projects> objectList) {
        return 0;
    }

    @Override
    public Projects get(String objectId) {
        return null;
    }

    @Override
    public List<Projects> getBulk(String sqlCommand) {
        return null;
    }


    @Override
    public boolean update(Projects newRecord) {
        return false;
    }

    @Override
    public int delete(Projects object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Projects> objectList) {
        return 0;
    }
}
