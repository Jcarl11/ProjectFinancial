package com.example.ratio.Entities;

public class Image extends Entity {
    private String parent;
    private String fileName;
    private String filePath;
    private boolean deleted;

    public Image() {
    }

    public Image(String parent, String fileName, String filePath, boolean deleted) {
        this.parent = parent;
        this.fileName = fileName;
        this.filePath = filePath;
        this.deleted = deleted;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
