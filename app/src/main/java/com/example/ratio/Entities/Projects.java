package com.example.ratio.Entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Projects extends Entity implements Serializable {
    private String projectName;
    private String projectCode;
    private String projectOwner;
    private Subcategory projectSubCategory;
    private ProjectType projectType;
    private List<Expenses> projectExpenses;
    private List<Income> projectIncome;
    private List<Receivables> projectRecievables;
    private Services projectServices;
    private List<Status> projectStatus;
    private Image thumbnail;
    private boolean deleted;
    private ArrayList<String> tags;

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

    public List<Receivables> getProjectRecievables() {
        return projectRecievables;
    }

    public void setProjectRecievables(List<Receivables> projectRecievables) {
        this.projectRecievables = projectRecievables;
    }

    public Services getProjectServices() {
        return projectServices;
    }

    public void setProjectServices(Services projectServices) {
        this.projectServices = projectServices;
    }

    public List<Status> getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(List<Status> projectStatus) {
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public String toString() {
        String SEPARATOR = ",";
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        List<String> values = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        for(int x = 0; x < fields.length; x++) {
            Object name = null;
            try {

                name = fields[x].get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(name != null){
                if((name instanceof ArrayList) == false)
                    values.add(String.valueOf(name.toString()));
            }
        }

        for(int y = 0 ; y < values.size() ; y++) {
            stringBuilder.append(values.get(y).trim());
            if((values.size() - y) > 1) {
                stringBuilder.append(SEPARATOR);
            }
        }
        return stringBuilder.toString();
    }
}
