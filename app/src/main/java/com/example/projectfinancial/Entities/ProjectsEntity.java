package com.example.projectfinancial.Entities;

public class ProjectsEntity {
    String objectId;
    String projectName;
    String createdAt;
    String projectCode;
    String projectOwner;
    String projectCategory;
    String projectType;
    String projectExpenses;
    String projectRevenue;

    public ProjectsEntity() {}

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectExpenses() {
        return projectExpenses;
    }

    public void setProjectExpenses(String projectExpenses) {
        this.projectExpenses = projectExpenses;
    }

    public String getProjectRevenue() {
        return projectRevenue;
    }

    public void setProjectRevenue(String projectRevenue) {
        this.projectRevenue = projectRevenue;
    }
}
