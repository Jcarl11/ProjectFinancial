package com.example.ratio.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Entity;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.R;
import com.example.ratio.Utilities.TagMaker;
import com.example.ratio.Utilities.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddNew extends Fragment {

    private static final String TAG = "FragmentAddNew";
    @BindView(R.id.addnew_field_projectname) TextInputLayout addnew_field_projectname;
    @BindView(R.id.addnew_field_projectcode) TextInputLayout addnew_field_projectcode;
    @BindView(R.id.addnew_field_projectowner) TextInputLayout addnew_field_projectowner;
    @BindView(R.id.addnew_field_specificservice) TextInputLayout addnew_field_specificservice;
    @BindView(R.id.addnew_field_specifictype) TextInputLayout addnew_field_specifictype;
    @BindView(R.id.addnew_field_specificsubcategory) TextInputLayout addnew_field_specificsubcategory;
    @BindView(R.id.addnew_spinner_typeofproject) MaterialSpinner addnew_spinner_typeofproject;
    @BindView(R.id.addnew_spinner_subcategory) MaterialSpinner addnew_spinner_subcategory;
    @BindView(R.id.addnew_spinner_services) MaterialSpinner addnew_spinner_services;
    @BindView(R.id.addnew_checkbox_active) CheckBox addnew_checkbox_active;
    @BindView(R.id.addnew_checkbox_archived) CheckBox addnew_checkbox_archived;
    @BindView(R.id.addnew_checkbox_proposal) CheckBox addnew_checkbox_proposal;
    @BindView(R.id.addnew_button_create) Button addnew_button_create;
    @BindView(R.id.addnew_imageview_thumbnail) ImageView addnew_imageview_thumbnail;
    private ProjectType OTHERSCHOICE_TYPESOFPROJECT = new ProjectType("Others", true);
    private Subcategory OTHERSCHOICE_SUBCATEGORY = new Subcategory("Others", true, null);
    private Services OTHERSCHOICE_SERVICES = new Services("Others", true);
    private TagMaker tagMaker = new TagMaker();
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Projects> projectsBaseDAO = null;
    private BaseDAO<ProjectType> projectTypeDAO = null;
    private BaseDAO<Subcategory> subcategoryBaseDAO = null;
    private BaseDAO<Services> servicesBaseDAO = null;
    private BaseDAO<Image> imageBaseDAO = null;
    private AlertDialog dialog = null;
    private String selectedServices = null;
    private String selectedType = null;
    private String selectedSubcategory = null;
    private String thumbnailPath = null;
    private String thumbnailName = null;
    public FragmentAddNew() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "afterView: Addnew started...");
        projectsBaseDAO = parseFactory.getProjectDAO();
        projectTypeDAO = parseFactory.getProjectTypeDAO();
        subcategoryBaseDAO = parseFactory.getSubcategoryDAO();
        servicesBaseDAO = parseFactory.getServicesDAO();
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        /*dialog.show();
        retrieveServices();
        retrieveProjectTypes();
        retrieveSubcategory();
        dialog.dismiss();*/
        /*Log.d(TAG, "onCreateView: RetrieveServices");
        new RetrieveServices().execute((Void)null);
        Log.d(TAG, "onCreateView: RetrieveTypes");
        new RetrieveTypes().execute((Void)null);
        Log.d(TAG, "onCreateView: RetrieveSubcategory");
        new RetrieveSubcategory().execute((Void)null);
        Log.d(TAG, "onCreateView: Retrieval done");
        Log.d(TAG, "onCreateView: Retrieved spinner values");*/
        /*List<Services> servicesList = servicesBaseDAO.getBulk(null);
        List<ProjectType> projectTypeList = projectTypeDAO.getBulk(null);
        List<Subcategory> subcategoryList = subcategoryBaseDAO.getBulk(null);*/

        Observable myObs = Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                return Observable.just(servicesBaseDAO.getBulk(null), projectTypeDAO.getBulk(null), subcategoryBaseDAO.getBulk(null));
            }
        });
        myObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        dialog.show();
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Object o) {
                        List<?> tmp = Arrays.asList(o);
                        if(tmp.get(0) instanceof Services){
                            Log.d(TAG, "onNext: Services");
                        } else if(tmp.get(0) instanceof ProjectType){
                            Log.d(TAG, "onNext: ProjectType");
                        } else if(tmp.get(0) instanceof Subcategory){
                            Log.d(TAG, "onNext: Subcategory");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        Log.d(TAG, "onComplete: ");
                    }
                });
        addnew_checkbox_active.setOnCheckedChangeListener(checkBoxListener());
        addnew_checkbox_archived.setOnCheckedChangeListener(checkBoxListener());
        addnew_checkbox_proposal.setOnCheckedChangeListener(checkBoxListener());
        addnew_spinner_typeofproject.setOnItemSelectedListener(typeOfProjectListener());
        addnew_spinner_services.setOnItemSelectedListener(servicesListener());
        addnew_spinner_subcategory.setOnItemSelectedListener(subcategoryListener());
        Log.d(TAG, "onCreateView: Listener initialization done");
        return view;
    }

    void createProjectTask(Projects projects){
        Projects createdProject = projectsBaseDAO.insert(projects);
        Image image = imageBaseDAO.insert(new Image(createdProject.getObjectId(), thumbnailName, thumbnailPath, false));
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
    @OnClick(R.id.addnew_imageview_thumbnail)
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
                        thumbnailPath = file.getPath();
                        thumbnailName = file.getName();
                        Picasso.get().load(new File(thumbnailPath)).into(addnew_imageview_thumbnail);
                    }
                } else {
                    Snackbar.make(getView(), "Error", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.addnew_button_create)
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

        Services services = (Services) addnew_spinner_services.getItems().get(addnew_spinner_services.getSelectedIndex());
        ProjectType projectType = (ProjectType) addnew_spinner_typeofproject.getItems().get(addnew_spinner_typeofproject.getSelectedIndex());
        Subcategory subcategory = (Subcategory) addnew_spinner_subcategory.getItems().get(addnew_spinner_subcategory.getSelectedIndex());
        projects.setProjectCode(addnew_field_projectcode.getEditText().getText().toString());
        projects.setProjectName(addnew_field_projectname.getEditText().getText().toString().toUpperCase());
        projects.setProjectOwner(addnew_field_projectowner.getEditText().getText().toString().toUpperCase());
        projects.setProjectServices(services);
        projects.setProjectType(projectType);
        projects.setProjectSubCategory(subcategory);
        projects.setDeleted(false);
        projects.setTags(tagMaker.createTags(projects.toString()));
        dialog.show();
        createProjectTask(projects);
        dialog.dismiss();
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

    private class RetrieveServices extends AsyncTask<Void, Void, List<Services>>{

        @Override
        protected List<Services> doInBackground(Void... voids) {
            List<Services> servicesList = servicesBaseDAO.getBulk(null);
            return servicesList;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Services> services) {
            dialog.dismiss();
            if(services.size() <= 0)
                return;
            services.add(OTHERSCHOICE_SERVICES);
            addnew_spinner_services.setItems(services);
        }
    }

    private class RetrieveTypes extends AsyncTask<Void, Void, List<ProjectType>>{

        @Override
        protected List<ProjectType> doInBackground(Void... voids) {
            List<ProjectType> projectTypeList = projectTypeDAO.getBulk(null);
            return projectTypeList;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<ProjectType> projectTypeList) {
            dialog.dismiss();
            if(projectTypeList.size() <= 0)
                return;
            projectTypeList.add(OTHERSCHOICE_TYPESOFPROJECT);
            addnew_spinner_typeofproject.setItems(projectTypeList);
        }
    }

    private class RetrieveSubcategory extends AsyncTask<Void, Void, List<Subcategory>>{

        @Override
        protected List<Subcategory> doInBackground(Void... voids) {
            List<Subcategory> subcategoryList = subcategoryBaseDAO.getBulk(null);
            return subcategoryList;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Subcategory> subcategoryList) {
            dialog.dismiss();
            if(subcategoryList.size() <= 0)
                return;
            subcategoryList.add(OTHERSCHOICE_SUBCATEGORY);
            addnew_spinner_subcategory.setItems(subcategoryList);
        }
    }


}
