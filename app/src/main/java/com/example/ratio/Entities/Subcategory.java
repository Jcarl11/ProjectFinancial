package com.example.ratio.Entities;


import java.io.Serializable;

public class Subcategory extends Entity implements Serializable {
    private String name;
    private boolean others;
    private String parent;

    public Subcategory() {}

    public Subcategory(String name, boolean others, String parent) {
        this.name = name;
        this.others = others;
        this.parent = parent;
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
}
