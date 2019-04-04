package com.example.ratio.Entities;

import com.orm.SugarRecord;

public class ProjectSubcategoryEntity
        extends SugarRecord<ProjectSubcategoryEntity>
        implements Comparable<ProjectSubcategoryEntity> {
    private String objectId;
    private String name;
    private boolean others;
    private String parent;

    public ProjectSubcategoryEntity() {}

    public ProjectSubcategoryEntity(String objectId, String name, boolean others, String parent) {
        this.objectId = objectId;
        this.name = name;
        this.others = others;
        this.parent = parent;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(ProjectSubcategoryEntity o) {
        return this.getName().compareTo(o.name);
    }
}
