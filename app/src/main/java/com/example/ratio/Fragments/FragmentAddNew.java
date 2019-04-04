package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ratio.Entities.ProjectSubcategoryEntity;
import com.example.ratio.Enums.CloudTableNames;
import com.example.ratio.Entities.ProjectTypeEntity;
import com.example.ratio.R;
import com.example.ratio.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddNew extends Fragment {

    private static final String TAG = "FragmentAddNew";
    @BindView(R.id.addnew_field_projectname) TextInputLayout addnew_field_projectname;
    @BindView(R.id.addnew_field_projectcode) TextInputLayout addnew_field_projectcode;
    @BindView(R.id.addnew_field_projectowner) TextInputLayout addnew_field_projectowner;
    @BindView(R.id.addnew_spinner_typeofproject) MaterialSpinner addnew_spinner_typeofproject;
    @BindView(R.id.addnew_spinner_subcategory) MaterialSpinner addnew_spinner_subcategory;
    @BindView(R.id.addnew_spinner_services) MaterialSpinner addnew_spinner_services;
    @BindView(R.id.addnew_button_create) Button addnew_button_create;
    @BindView(R.id.addnew_field_specificservice) TextInputLayout addnew_field_specificservice;
    ArrayList<String> subCategory = new ArrayList<>();
    ArrayList<ProjectTypeEntity> projectTypeEntities = new ArrayList<>();
    public static String COLUMN_NAME = "NAME";
    public static String COLUMN_PARENT = "PARENT";
    String selectedTypeOfProject = null;
    String selectedSubcategory = null;
    String selectedServices = null;
    public FragmentAddNew() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);
        ButterKnife.bind(this, view);
        List<ProjectTypeEntity> projectTypes = ProjectTypeEntity.listAll(ProjectTypeEntity.class);
        Log.d(TAG, "onCreateView: ProjectTypeEntity size: " + String.valueOf(projectTypes.size()));
        if(projectTypes.size() <= 0) {
            new RetrieveProjectTypesTask().execute((Void)null);
        }
        addnew_spinner_services.setOnItemSelectedListener(servicesListener());
        addnew_spinner_typeofproject.setOnItemSelectedListener(typeOfProjectListener());
        addnew_spinner_subcategory.setOnItemSelectedListener(subcategoryListener());
        return view;
    }

    @OnClick(R.id.addnew_button_create)
    public void createBtnClicked( View view ) {
        if( !validateField(addnew_field_projectname) 
                | !validateField(addnew_field_projectcode) 
                | !validateField(addnew_field_projectowner)) {
            return;
        }
        if(selectedServices == null) {
            Log.d(TAG, "createBtnClicked: Empty");
            Toast.makeText(getContext(), "Select a service", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedTypeOfProject == null) {
            Log.d(TAG, "createBtnClicked: Empty");
            Toast.makeText(getContext(), "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if(addnew_spinner_services.getSelectedIndex() == addnew_spinner_services.getItems().size() - 1) {
            Log.d(TAG, "createBtnClicked: Others");
            if(addnew_field_specificservice.getEditText().getText().toString().isEmpty()) {
                addnew_field_specificservice.setError("Specify the service type");
                return;
            }
        }
        if(selectedSubcategory == null){
            Log.d(TAG, "createBtnClicked: Empty");
            Toast.makeText(getContext(), "Select a subcategory", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "createBtnClicked: " + selectedTypeOfProject);
        Log.d(TAG, "createBtnClicked: " + selectedSubcategory);
        Log.d(TAG, "createBtnClicked: all fields are valid");

    }
    private MaterialSpinner.OnItemSelectedListener servicesListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedServices = item.toString();
                addnew_field_specificservice.setVisibility(View.GONE);
                if(position == view.getItems().size() - 1) {
                    addnew_field_specificservice.setVisibility(View.VISIBLE);
                }
            }
        };
        return listener;
    }
    private MaterialSpinner.OnItemSelectedListener typeOfProjectListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedTypeOfProject = item.toString();
                //addnew_spinner_subcategory.setItems(subCategories.get(position));
                //addnew_spinner_subcategory.setVisibility(View.VISIBLE);
            }
        };
        return listener;
    }
    private MaterialSpinner.OnItemSelectedListener subcategoryListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSubcategory = item.toString();
            }
        };
        return listener;
    }
    private boolean validateField(TextInputLayout input) {
        String inputString = input.getEditText().getText().toString().trim();

        if (inputString.isEmpty()) {
            input.setError("Field can't be empty");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    private class RetrieveProjectTypesTask extends AsyncTask<Void, Void, ArrayList<ProjectTypeEntity>> {

        AlertDialog dialog;
        ArrayList<ProjectTypeEntity> projectTypeEntities = new ArrayList<>();

        public RetrieveProjectTypesTask() {
            Log.d(TAG, "RetrieveProjectTypesTask: Started");
            this.dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        }

        @Override
        protected ArrayList<ProjectTypeEntity> doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Retrieving types");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(CloudTableNames.PROJECT_TYPE.toString());
            try {
                List<ProjectTypeEntity> check = ProjectTypeEntity.listAll(ProjectTypeEntity.class);
                Log.d(TAG, "doInBackground: ProjectTypeEntity size: " + String.valueOf(check.size()));
                if(check.size() <= 0){
                    List<ParseObject> result = query.addAscendingOrder(COLUMN_NAME).find();
                    Log.d(TAG, "doInBackground: result size: " + String.valueOf(result.size()));
                    for(ParseObject parseObject : result) {
                        ProjectTypeEntity projectTypeEntity = new ProjectTypeEntity();
                        projectTypeEntity.setObjectId(parseObject.getObjectId());
                        projectTypeEntity.setName(parseObject.getString(COLUMN_NAME));
                        projectTypeEntity.save();
                        projectTypeEntities.add(projectTypeEntity);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Exception thrown: " + e.getMessage());
            }
            return projectTypeEntities;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: load");
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ProjectTypeEntity> projectTypeEntities) {
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: result size: " + String.valueOf(projectTypeEntities.size()));
            dialog.dismiss();
            if(projectTypeEntities.size() > 0) {
                this.projectTypeEntities = projectTypeEntities;
                ArrayList<String> values = new ArrayList<>();
                for(ProjectTypeEntity projectTypeEntity : projectTypeEntities) {
                    values.add(projectTypeEntity.getName());
                }
                values.add(values.size(), "Others"); // Add 'Others' on the last position in array
                addnew_spinner_typeofproject.setItems(values);
            }else {
                Log.d(TAG, "onPostExecute: Result is empty");
                Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class RetrieveSubCategoryTask extends AsyncTask<Void, Void, ArrayList<ProjectSubcategoryEntity>>{

        AlertDialog dialog;
        ArrayList<ProjectSubcategoryEntity> projectSubcategoryEntities = new ArrayList<>();
        public RetrieveSubCategoryTask() {
            dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
            Log.d(TAG, "RetrieveSubCategoryTask: Started");
        }

        @Override
        protected ArrayList<ProjectSubcategoryEntity> doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Opertion started");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(CloudTableNames.PROJECT_TYPE_SUBCATEGORY.toString());
            try {
                List<ParseObject> result = query.addAscendingOrder(COLUMN_NAME).find();
                Log.d(TAG, "doInBackground: result size: " + String.valueOf(result.size()));
                for(ParseObject parseObject : result) {
                    ProjectSubcategoryEntity projectSubcategoryEntity = new ProjectSubcategoryEntity();
                    projectSubcategoryEntity.setObjectId(parseObject.getObjectId());
                    projectSubcategoryEntity.setName(parseObject.getString(COLUMN_NAME));
                    projectSubcategoryEntity.setOthers(false);
                    projectSubcategoryEntity.setParent(parseObject.getString(COLUMN_PARENT));
                    projectSubcategoryEntities.add(projectSubcategoryEntity);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Exception throw " + e.getMessage());
            }
            return projectSubcategoryEntities;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ProjectSubcategoryEntity> projectSubcategoryEntities) {
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: Result size: " + String.valueOf(projectSubcategoryEntities.size()));
            if(projectSubcategoryEntities.size() > 0) {
                ArrayList<String> values = new ArrayList<>();
                for(ProjectSubcategoryEntity projectSubcategoryEntity : projectSubcategoryEntities) {
                    values.add(projectSubcategoryEntity.getName());
                }
                subCategory = values;
            } else {
                Log.d(TAG, "onPostExecute: Result is empty");
                Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
