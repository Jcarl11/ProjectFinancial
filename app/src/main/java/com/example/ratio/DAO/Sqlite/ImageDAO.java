package com.example.ratio.DAO.Sqlite;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Image;

import java.util.List;

public class ImageDAO implements BaseDAO<Image> {
    @Override
    public int insert(Image objectEntity) {
        return 0;
    }

    @Override
    public int insertAll(List<Image> objectList) {
        return 0;
    }

    @Override
    public Image get(String objectId) {
        return null;
    }

    @Override
    public List<Image> getBulk(String sqlCommand) {
        return null;
    }

    @Override
    public boolean update(Image oldRecord, Image newRecord) {
        return false;
    }

    @Override
    public int delete(Image object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Image> objectList) {
        return 0;
    }
}
