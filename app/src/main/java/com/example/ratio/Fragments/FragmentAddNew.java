package com.example.ratio.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.Enums.PROJECT_TYPE;
import com.example.ratio.Enums.PROJECT_TYPE_SUBCATEGORY;
import com.example.ratio.Enums.SERVICES;
import com.example.ratio.R;
import com.example.ratio.Utilities.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_add_new)
public class FragmentAddNew extends Fragment {

    private static final String TAG = "FragmentAddNew";
    @ViewById TextInputLayout addnew_field_projectname;
    @ViewById TextInputLayout addnew_field_projectcode;
    @ViewById TextInputLayout addnew_field_projectowner;
    @ViewById AwesomeSpinner addnew_spinner_typeofproject;
    @ViewById AwesomeSpinner addnew_spinner_subcategory;
    @ViewById AwesomeSpinner addnew_spinner_services;
    @ViewById Button addnew_button_create;
    @ViewById TextInputLayout addnew_field_specificservice;
    @ViewById TextInputLayout addnew_field_specifictype;
    @ViewById TextInputLayout addnew_field_specificsubcategory;
    @ViewById ImageView addnew_imageview_thumbnail;
    @ViewById CheckBox addnew_checkbox_active;
    @ViewById CheckBox addnew_checkbox_archived;
    @ViewById CheckBox addnew_checkbox_proposal;
    ProjectType OTHERSCHOICE_TYPESOFPROJECT = new ProjectType("Others", true);
    Subcategory OTHERSCHOICE_SUBCATEGORY = new Subcategory("Others", true, null);
    Services OTHERSCHOICE_SERVICES = new Services("Others", true);
    public FragmentAddNew() {}

    @AfterViews void afterView(){
        addnew_checkbox_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addnew_checkbox_active.setError(null);
                    addnew_checkbox_archived.setError(null);
                    addnew_checkbox_proposal.setError(null);
                }
            }
        });
        addnew_checkbox_archived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addnew_checkbox_active.setError(null);
                    addnew_checkbox_archived.setError(null);
                    addnew_checkbox_proposal.setError(null);
                }
            }
        });
        addnew_checkbox_proposal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addnew_checkbox_active.setError(null);
                    addnew_checkbox_archived.setError(null);
                    addnew_checkbox_proposal.setError(null);
                }
            }
        });
        new RetrieveProjectTypesTask().execute((Void)null);
        new RetrieveSubCategoryTask().execute((Void) null);
        new RetrieveServicesTask().execute((Void)null);
        addnew_spinner_typeofproject.setOnSpinnerItemClickListener(typeOfProjectListener());
        addnew_spinner_services.setOnSpinnerItemClickListener(servicesListener());
        addnew_spinner_subcategory.setOnSpinnerItemClickListener(subcategoryListener());
    }

    private ArrayAdapter<String> adapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        return adapter;
    }

    private List<String> convertList(List<?> originalList) {
        List<String> result = new ArrayList<>();
        for(Object name : originalList) {
            result.add(name.toString());
        }
        return result;
    }
    @Click(R.id.addnew_imageview_thumbnail)
    void imageChoose(View view) {
        String IS_NEED_CAMERA = new String();
        Intent intent1 = new Intent(getContext(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    addnew_imageview_thumbnail.setBackground(null);
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    for (ImageFile file : list) {
                        Picasso.get().load(new File(file.getPath())).into(addnew_imageview_thumbnail);
                    }
                } else {
                    Snackbar.make(getView(), "Error", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Click(R.id.addnew_button_create)
    void createBtnClicked( View view ) {
        Projects projects = new Projects();
        if( !validateField(addnew_field_projectname) 
                | !validateField(addnew_field_projectcode) 
                | !validateField(addnew_field_projectowner) | !validateThumbnail(addnew_imageview_thumbnail)
            | !validateCheckbox() | !validateSpinner(addnew_spinner_services) | !validateSpinner(addnew_spinner_typeofproject)
            | !validateSpinner(addnew_spinner_subcategory)) {
            return;
        }
        if(addnew_spinner_services.getSelectedItem().equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specificservice)) {
                return;
            }
        }
        if(addnew_spinner_typeofproject.getSelectedItem().equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specifictype) | !validateField(addnew_field_specificsubcategory)) {
                return;
            }
        }
        if(addnew_spinner_subcategory.getSelectedItem().equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specificsubcategory)) {
                return;
            }
        }
        projects.setProjectCode(addnew_field_projectcode.getEditText().getText().toString());
        projects.setProjectName(addnew_field_projectname.getEditText().getText().toString().toUpperCase());
        projects.setProjectOwner(addnew_field_projectowner.getEditText().getText().toString().toUpperCase());
        Snackbar.make(getView(), "OK", Snackbar.LENGTH_SHORT).show();
        //new CreateProjectTask(projects).execute((Void)null);
    }

    private AwesomeSpinner.onSpinnerItemClickListener<String> servicesListener() {
        AwesomeSpinner.onSpinnerItemClickListener<String> listener = new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if(itemAtPosition.equalsIgnoreCase("Others")) {
                    addnew_field_specificservice.setVisibility(View.VISIBLE);
                } else {
                    addnew_field_specificservice.setVisibility(View.GONE);
                }
            }
        };
        return listener;
    }
    private AwesomeSpinner.onSpinnerItemClickListener<String> typeOfProjectListener() {
        AwesomeSpinner.onSpinnerItemClickListener<String> listener = new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if(itemAtPosition.equalsIgnoreCase("Others")) {
                    addnew_field_specifictype.setVisibility(View.VISIBLE);
                    addnew_field_specificsubcategory.setVisibility(View.VISIBLE);
                } else {
                    addnew_field_specifictype.setVisibility(View.GONE);
                    addnew_field_specificsubcategory.setVisibility(View.GONE);
                }
            }
        };
        return listener;
    }
    private AwesomeSpinner.onSpinnerItemClickListener<String> subcategoryListener() {
        AwesomeSpinner.onSpinnerItemClickListener<String> listener = new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if(itemAtPosition.equalsIgnoreCase("Others")) {
                    addnew_field_specificsubcategory.setVisibility(View.VISIBLE);
                } else {
                    addnew_field_specificsubcategory.setVisibility(View.GONE);
                }
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean validateThumbnail(ImageView imageView) {
        if(imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_person).getConstantState())) {
            imageView.setBackground(getResources().getDrawable(R.drawable.bg_error));
            return false;
        } else {
            imageView.setBackground(null);
            return true;
        }
    }
    private boolean validateCheckbox() {
        if(!addnew_checkbox_active.isChecked() && !addnew_checkbox_archived.isChecked() && !addnew_checkbox_proposal.isChecked()) {
            addnew_checkbox_active.setError("Choose at least 1");
            addnew_checkbox_archived.setError("Choose at least 1");
            addnew_checkbox_proposal.setError("Choose at least 1");
            return false;
        } else {
            addnew_checkbox_active.setError(null);
            addnew_checkbox_archived.setError(null);
            addnew_checkbox_proposal.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean validateSpinner(AwesomeSpinner servicesSpinner) {
        if(servicesSpinner.getSelectedItemPosition() == -1) {
            servicesSpinner.setBackground(getResources().getDrawable(R.drawable.bg_error));
            return false;
        } else {
            addnew_spinner_services.setBackground(null);
            return true;
        }
    }

    private class RetrieveServicesTask extends AsyncTask<Void, Void, ArrayList<Services>> {
        AlertDialog dialog;
        ArrayList<Services> servicesEntities = new ArrayList<>();
        public RetrieveServicesTask() {
            dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        }

        @Override
        protected ArrayList<Services> doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Operation Started");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.SERVICES.toString());
            try {
                Log.d(TAG, "doInBackground: Retrieving Services");
                List<ParseObject> result = query.find();
                Log.d(TAG, "doInBackground: result size: " + String.valueOf(result.size()));
                for(ParseObject parseObject : result) {
                    Services servicesEntity = new Services();
                    servicesEntity.setObjectId(parseObject.getObjectId());
                    servicesEntity.setName(parseObject.getString(SERVICES.NAME.toString()));
                    servicesEntity.setOthers(false);
                    servicesEntities.add(servicesEntity);
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Exception thrown: " + e.getMessage());
            }

            return servicesEntities;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Services> servicesEntities) {
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: result size: " + String.valueOf(servicesEntities.size()));
            dialog.dismiss();
            if(servicesEntities.size() > 0) {
                servicesEntities.add(servicesEntities.size(), OTHERSCHOICE_SERVICES);
                addnew_spinner_services.setAdapter(adapter(convertList(servicesEntities)));
            } else {
                Log.d(TAG, "onPostExecute: result empty");
            }
        }
    }
    private class RetrieveProjectTypesTask extends AsyncTask<Void, Void, ArrayList<ProjectType>> {

        AlertDialog dialog;
        ArrayList<ProjectType> projectTypeEntities = new ArrayList<>();

        public RetrieveProjectTypesTask() {
            Log.d(TAG, "RetrieveProjectTypesTask: Started");
            this.dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        }

        @Override
        protected ArrayList<ProjectType> doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Retrieving types");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE.toString());
            try {
                Log.d(TAG, "doInBackground: Retrieving ProjectType...");
                List<ParseObject> result = query.addAscendingOrder(PROJECT_TYPE.NAME.toString()).find();
                Log.d(TAG, "doInBackground: result size: " + String.valueOf(result.size()));
                for(ParseObject parseObject : result) {
                    ProjectType projectType = new ProjectType();
                    projectType.setObjectId(parseObject.getObjectId());
                    projectType.setName(parseObject.getString(PROJECT_TYPE.NAME.toString()));
                    projectType.setOthers(false);
                    projectTypeEntities.add(projectType);
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
        protected void onPostExecute(ArrayList<ProjectType> projectTypeEntities) {
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: result size: " + String.valueOf(projectTypeEntities.size()));
            dialog.dismiss();
            if(projectTypeEntities.size() > 0) {
                projectTypeEntities.add(OTHERSCHOICE_TYPESOFPROJECT); // Add 'Others' on the last position in array
                addnew_spinner_typeofproject.setAdapter(adapter(convertList(projectTypeEntities)));
            }else {
                Log.d(TAG, "onPostExecute: Result is empty");
                Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class RetrieveSubCategoryTask extends AsyncTask<Void, Void, ArrayList<Subcategory>>{

        AlertDialog dialog;
        ArrayList<Subcategory> projectSubcategoryEntities = new ArrayList<>();
        public RetrieveSubCategoryTask() {
            dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
            Log.d(TAG, "RetrieveSubCategoryTask: Started");
        }

        @Override
        protected ArrayList<Subcategory> doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Opertion started");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
            try {
                Log.d(TAG, "doInBackground: Retrieving Subcategory...");
                List<ParseObject> result = query.addAscendingOrder(PROJECT_TYPE_SUBCATEGORY.NAME.toString()).find();
                Log.d(TAG, "doInBackground: result size: " + String.valueOf(result.size()));
                for(ParseObject parseObject : result) {
                    Subcategory subcategory = new Subcategory();
                    subcategory.setObjectId(parseObject.getObjectId());
                    subcategory.setName(parseObject.getString(PROJECT_TYPE_SUBCATEGORY.NAME.toString()));
                    subcategory.setOthers(false);
                    subcategory.setParent(parseObject.getString(PROJECT_TYPE_SUBCATEGORY.PARENT.toString()));
                    projectSubcategoryEntities.add(subcategory);
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
        protected void onPostExecute(ArrayList<Subcategory> projectSubcategoryEntities) {
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: Result size: " + String.valueOf(projectSubcategoryEntities.size()));
            if(projectSubcategoryEntities.size() > 0) {
                projectSubcategoryEntities.add(OTHERSCHOICE_SUBCATEGORY); // Add 'Others' on the last position in array
                addnew_spinner_subcategory.setAdapter(adapter(convertList(projectSubcategoryEntities)));
            } else {
                Log.d(TAG, "onPostExecute: Result is empty");
                Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class CreateProjectTask extends AsyncTask<Void, Void, Boolean> {

        AlertDialog dialog;
        Projects projects;
        boolean successful = false;
        public CreateProjectTask(Projects projectEntity) {
            this.projects = projectEntity;
            dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Upload started");
            ParseObject project = new ParseObject(PARSECLASS.PROJECT.toString());
            project.put(PROJECT.PROJECT_CODE.toString(), projects.getProjectCode());
            project.put(PROJECT.PROJECT_TITLE.toString(), projects.getProjectName());
            project.put(PROJECT.PROJECT_OWNER.toString(), projects.getProjectOwner());
            project.put(PROJECT.SERVICES.toString(), projects.getProjectServices());
            project.put(PROJECT.TYPE.toString(), projects.getProjectType());
            project.put(PROJECT.SUBCATEGORY.toString(), projects.getProjectSubCategory());
            try {
                project.save();
                successful = true;
                Log.d(TAG, "doInBackground: project saved");
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Exception thrown: " + e.getMessage());
            }
            return successful;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            Log.d(TAG, "onPostExecute: Operation done");
            Log.d(TAG, "onPostExecute: result: " + String.valueOf(aBoolean));
            if(aBoolean) {
                Snackbar.make(getView(), "Record created successfully", Snackbar.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onPostExecute: Operation failed");
                Snackbar.make(getView(), "Operation failed", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
