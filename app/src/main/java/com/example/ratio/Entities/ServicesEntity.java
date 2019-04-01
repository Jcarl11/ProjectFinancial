package com.example.ratio.Entities;

public class ServicesEntity {
    private String objectId;
    private String name;
    private boolean others;

    public ServicesEntity() {}

    public ServicesEntity(String objectId, String name, boolean others) {
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
}
