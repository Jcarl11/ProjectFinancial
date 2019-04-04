package com.example.ratio.Entities;

public class ProjectsEntity {
    String objectId;
    String projectName;
    String createdAt;
    String projectCode;
    String projectOwner;
    String projectSubCategory;
    String projectType;
    String projectExpenses;
    String projectRevenue;
    String projectServices;

    public ProjectsEntity() {}

    public ProjectsEntity(String objectId, String projectName, String createdAt, String projectCode,
                          String projectOwner, String projectSubCategory, String projectType,
                          String projectExpenses, String projectRevenue, String projectServices) {
        this.objectId = objectId;
        this.projectName = projectName;
        this.createdAt = createdAt;
        this.projectCode = projectCode;
        this.projectOwner = projectOwner;
        this.projectSubCategory = projectSubCategory;
        this.projectType = projectType;
        this.projectExpenses = projectExpenses;
        this.projectRevenue = projectRevenue;
        this.projectServices = projectServices;
    }

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

    public String getProjectSubCategory() {
        return projectSubCategory;
    }

    public void setProjectSubCategory(String projectSubCategory) {
        this.projectSubCategory = projectSubCategory;
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

    public String getProjectServices() {
        return projectServices;
    }

    public void setProjectServices(String projectServices) {
        this.projectServices = projectServices;
    }
}
