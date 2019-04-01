package com.example.ratio.Entities;

public class ProjectSubcategoryEntity implements Comparable<ProjectTypeEntity> {
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
    public int compareTo(ProjectTypeEntity o) {
        if(parent.equals(o.getObjectId())) {
            return 0;
        } else
            return -1;
    }
}
