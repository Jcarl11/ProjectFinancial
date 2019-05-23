package com.example.ratio.Entities;

import java.io.Serializable;

public class Services extends Entity implements Serializable {
    private String name;
    private boolean others;

    public Services() {}

    public Services(String name, boolean others) {
        this.name = name;
        this.others = others;
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

    @Override
    public String toString() {
        return getName();
    }


}
