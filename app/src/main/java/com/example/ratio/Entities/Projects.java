package com.example.ratio.Entities;

import java.util.List;

public class Projects extends Entity {
    private String projectName;
    private String projectCode;
    private String projectOwner;
    private Subcategory projectSubCategory;
    private ProjectType projectType;
    private List<Expenses> projectExpenses;
    private List<Income> projectIncome;
    private List<Recievables> projectRecievables;
    private Services projectServices;
    private Status projectStatus;
    private Image thumbnail;
    private boolean deleted;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Subcategory getProjectSubCategory() {
        return projectSubCategory;
    }

    public void setProjectSubCategory(Subcategory projectSubCategory) {
        this.projectSubCategory = projectSubCategory;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public List<Expenses> getProjectExpenses() {
        return projectExpenses;
    }

    public void setProjectExpenses(List<Expenses> projectExpenses) {
        this.projectExpenses = projectExpenses;
    }

    public List<Income> getProjectIncome() {
        return projectIncome;
    }

    public void setProjectIncome(List<Income> projectIncome) {
        this.projectIncome = projectIncome;
    }

    public List<Recievables> getProjectRecievables() {
        return projectRecievables;
    }

    public void setProjectRecievables(List<Recievables> projectRecievables) {
        this.projectRecievables = projectRecievables;
    }

    public Services getProjectServices() {
        return projectServices;
    }

    public void setProjectServices(Services projectServices) {
        this.projectServices = projectServices;
    }

    public Status getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Status projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
