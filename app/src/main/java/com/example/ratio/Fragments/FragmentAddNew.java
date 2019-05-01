package com.example.ratio.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.Parse.ProjectDAO;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.DATABASES;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
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
    @ViewById TextInputLayout addnew_field_specificservice;
    @ViewById TextInputLayout addnew_field_specifictype;
    @ViewById TextInputLayout addnew_field_specificsubcategory;
    @ViewById MaterialSpinner addnew_spinner_typeofproject;
    @ViewById MaterialSpinner addnew_spinner_subcategory;
    @ViewById MaterialSpinner addnew_spinner_services;
    @ViewById CheckBox addnew_checkbox_active;
    @ViewById CheckBox addnew_checkbox_archived;
    @ViewById CheckBox addnew_checkbox_proposal;
    @ViewById Button addnew_button_create;
    @ViewById ImageView addnew_imageview_thumbnail;
    private ProjectType OTHERSCHOICE_TYPESOFPROJECT = new ProjectType("Others", true);
    private Subcategory OTHERSCHOICE_SUBCATEGORY = new Subcategory("Others", true, null);
    private Services OTHERSCHOICE_SERVICES = new Services("Others", true);
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Projects> projectsBaseDAO = null;
    private BaseDAO<ProjectType> projectDAO = null;
    private BaseDAO<Subcategory> subcategoryBaseDAO = null;
    private BaseDAO<Services> servicesBaseDAO = null;
    private BaseDAO<Image> imageBaseDAO = null;
    private AlertDialog dialog = null;
    private String selectedServices = null;
    private String selectedType = null;
    private String selectedSubcategory = null;
    public FragmentAddNew() {}

    @AfterViews void afterView(){ //Gets called after the views are injected...
        projectsBaseDAO = parseFactory.getProjectDAO();
        projectDAO = parseFactory.getProjectDAO();
        subcategoryBaseDAO = parseFactory.getSubcategoryDAO();
        servicesBaseDAO = parseFactory.getServicesDAO();
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        dialog.show();
        retrieveProjectTypes();
        retrieveSubcategory();
        retrieveServices();
        dialog.dismiss();
        addnew_checkbox_active.setOnCheckedChangeListener(checkBoxListener());
        addnew_checkbox_archived.setOnCheckedChangeListener(checkBoxListener());
        addnew_checkbox_proposal.setOnCheckedChangeListener(checkBoxListener());
        addnew_spinner_typeofproject.setOnItemSelectedListener(typeOfProjectListener());
        addnew_spinner_services.setOnItemSelectedListener(servicesListener());
        addnew_spinner_subcategory.setOnItemSelectedListener(subcategoryListener());
    }

    @Background void retrieveProjectTypes(){
        List<ProjectType> projectTypeList = projectDAO.getBulk(null);
        projectTypeDone(projectTypeList);
    }

    @UiThread void projectTypeDone(List<ProjectType> projectTypeList){
        Log.d(TAG, "retrievalFinished: projectTypeList size: " + projectTypeList.size());
        if(projectTypeList.size() <= 0) {
            Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        projectTypeList.add(OTHERSCHOICE_TYPESOFPROJECT); // Add 'Others' on the last position in array
        addnew_spinner_typeofproject.setItems(projectTypeList);
    }

    @Background void retrieveSubcategory() {
        List<Subcategory> subcategoryList = subcategoryBaseDAO.getBulk(null);
        subcategoryDone(subcategoryList);
    }

    @UiThread void subcategoryDone(List<Subcategory> subcategoryList){
        Log.d(TAG, "subcategoryDone: subcategoryList size: " + subcategoryList.size());
        if(subcategoryList.size() <= 0) {
            Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        subcategoryList.add(OTHERSCHOICE_SUBCATEGORY); // Add 'Others' on the last position in array
        addnew_spinner_subcategory.setItems(subcategoryList);
    }

    @Background void retrieveServices() {
        List<Services> servicesList = servicesBaseDAO.getBulk(null);
        servicesDone(servicesList);
    }

    @UiThread void servicesDone(List<Services> servicesList){
        Log.d(TAG, "servicesDone: servicesList size: " + servicesList.size());
        if(servicesList.size() <= 0) {
            Snackbar.make(getView(), "Result is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        servicesList.add(OTHERSCHOICE_SERVICES); // Add 'Others' on the last position in array
        addnew_spinner_services.setItems(servicesList);
    }

    @Background void createProjectTask(Projects projects){
        Projects createdProject = projectsBaseDAO.insert(projects);
        createProjectDone(createdProject);
    }

    @UiThread void createProjectDone(Projects projects){
        dialog.dismiss();
        Log.d(TAG, "createProjectDone: Created project: " + projects.getObjectId());
        Log.d(TAG, "createProjectDone: Created project: " + projects.getCreatedAt());
        Log.d(TAG, "createProjectDone: Created project: " + projects.getUpdatedAt());
    }
    private ArrayAdapter<String> adapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        return adapter;
    }
    private CompoundButton.OnCheckedChangeListener checkBoxListener() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if(isChecked){
                addnew_checkbox_active.setError(null);
                addnew_checkbox_archived.setError(null);
                addnew_checkbox_proposal.setError(null);
            }
        };
        return listener;
    }
    private List<String> convertList(List<?> originalList) {
        List<String> result = new ArrayList<>();
        for(Object name : originalList) {
            result.add(name.toString());
        }
        return result;
    }
    @Click(R.id.addnew_imageview_thumbnail)
    void imageChoose() {
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
    void createBtnClicked() {
        Projects projects = new Projects();
        if( !validateField(addnew_field_projectname) 
                | !validateField(addnew_field_projectcode) 
                | !validateField(addnew_field_projectowner) | !validateThumbnail(addnew_imageview_thumbnail)
            | !validateCheckbox() | !validateSpinner(selectedServices, addnew_spinner_services) | !validateSpinner(selectedType, addnew_spinner_typeofproject)
            | !validateSpinner(selectedSubcategory, addnew_spinner_subcategory)) {
            return;
        }
        if(selectedServices.equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specificservice)) {
                return;
            }
        }
        if(selectedType.equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specifictype) | !validateField(addnew_field_specificsubcategory)) {
                return;
            }
        }
        if(selectedSubcategory.equalsIgnoreCase("Others")) {
            if(!validateField(addnew_field_specificsubcategory)) {
                return;
            }
        }
        projects.setProjectCode(addnew_field_projectcode.getEditText().getText().toString());
        projects.setProjectName(addnew_field_projectname.getEditText().getText().toString().toUpperCase());
        projects.setProjectOwner(addnew_field_projectowner.getEditText().getText().toString().toUpperCase());

        dialog.show();
        createProjectTask(projects);
    }

    private MaterialSpinner.OnItemSelectedListener servicesListener() {
        MaterialSpinner.OnItemSelectedListener listener = (view, position, id, item) -> {
            Services selected = (Services) item;
            selectedServices = selected.toString();
            if(selected.toString().equalsIgnoreCase("others")){
                addnew_field_specificservice.setVisibility(View.VISIBLE);
            } else {
                addnew_field_specificservice.setVisibility(View.GONE);
            }
        };
        return listener;
    }
    private MaterialSpinner.OnItemSelectedListener typeOfProjectListener() {
        MaterialSpinner.OnItemSelectedListener listener = (view, position, id, item) -> {
            ProjectType selected = (ProjectType) item;
            selectedType = selected.toString();
            if(selected.toString().equalsIgnoreCase("others")){
                addnew_field_specifictype.setVisibility(View.VISIBLE);
                addnew_field_specificsubcategory.setVisibility(View.VISIBLE);
            } else {
                addnew_field_specifictype.setVisibility(View.GONE);
                addnew_field_specificsubcategory.setVisibility(View.GONE);
            }
        };

        return listener;
    }
    private MaterialSpinner.OnItemSelectedListener subcategoryListener() {
        MaterialSpinner.OnItemSelectedListener listener = (view, position, id, item) -> {
            Subcategory selected = (Subcategory) item;
            selectedSubcategory = selected.toString();
            if(selected.toString().equalsIgnoreCase("others")){
                addnew_field_specificsubcategory.setVisibility(View.VISIBLE);
            } else {
                addnew_field_specificsubcategory.setVisibility(View.GONE);
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
    private boolean validateSpinner(String selectedItem, MaterialSpinner spinner) {
        if(selectedItem == null) {
            spinner.setBackground(getResources().getDrawable(R.drawable.bg_error));
            return false;
        } else {
            spinner.setBackground(null);
            return true;
        }
    }
}
