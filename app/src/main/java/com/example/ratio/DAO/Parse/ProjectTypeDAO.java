package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.ProjectType;

import java.util.List;

public class ProjectTypeDAO implements BaseDAO<ProjectType> {
    @Override
    public int insert(ProjectType objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<ProjectType> objectList) {
        return 0;
    }

    @Override
    public ProjectType get(String objectId) {
        return null;
    }

    @Override
    public List<ProjectType> getBulk(String sqlCommand) {
        return null;
    }


    @Override
    public boolean update(ProjectType oldRecord, ProjectType newRecord) {
        return false;
    }

    @Override
    public int delete(ProjectType object) {
        return 0;
    }

    @Override
    public int deleteAll(List<ProjectType> objectList) {
        return 0;
    }
}
