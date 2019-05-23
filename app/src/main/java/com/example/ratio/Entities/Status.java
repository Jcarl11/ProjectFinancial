package com.example.ratio.Entities;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Status extends Entity implements Serializable {
    private String name;
    private String parent;

    public Status(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    public Status() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
