package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Projects;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProjectDAO implements BaseDAO<Projects> {
    private static final String TAG = "ProjectDAO";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    int result = 0;
    int defaultLimit = 50;
    boolean isSuccessful = false;
    ParseObject parseObject = null;

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
