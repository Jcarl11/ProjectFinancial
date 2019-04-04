package com.example.ratio.Entities;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProjectTypeEntity extends SugarRecord<ProjectTypeEntity> {
    private String objectId;
    private String name;
    private boolean others;

    public ProjectTypeEntity() {}

    public ProjectTypeEntity(String objectId, String name, boolean others) {
        this.objectId = objectId;
        this.name = name;
        this.others = others;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOthers() {
        return others;
    }

    public void setOthers(boolean others) {
        this.others = others;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
