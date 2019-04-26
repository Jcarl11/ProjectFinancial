package com.example.ratio.Entities;

public class Status extends Entity {
    private String name;

    public Status(String name) {
        this.name = name;
    }

    public Status() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
