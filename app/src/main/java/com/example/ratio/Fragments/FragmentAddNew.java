package com.example.ratio.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Dialogs.CheckBoxDialog;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.R;
import com.example.ratio.Utilities.ImageCompressor;
import com.example.ratio.Utilities.TagMaker;
import com.example.ratio.Utilities.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
    @BindView(R.id.addnew_button_create) Button addnew_button_create;
    @BindView(R.id.addnew_button_projectstatus) Button addnew_button_projectstatus;
    @BindView(R.id.addnew_imageview_thumbnail) ImageView addnew_imageview_thumbnail;
    private final ProjectType OTHERSCHOICE_TYPESOFPROJECT = new ProjectType("Others", true);
    private final Subcategory OTHERSCHOICE_SUBCATEGORY = new Subcategory("Others", true, null);
    private final Services OTHERSCHOICE_SERVICES = new Services("Others", true);
    private TagMaker tagMaker = new TagMaker();
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Projects> projectsBaseDAO = null;
    private BaseDAO<ProjectType> projectTypeDAO = null;
    private BaseDAO<Subcategory> subcategoryBaseDAO = null;
    private BaseDAO<Services> servicesBaseDAO = null;
    private BaseDAO<Image> imageBaseDAO = null;
    private BaseDAO<Status> statusBaseDAO = null;
    private AlertDialog dialog = null;
    private String selectedServices = null;
    private String selectedType = null;
    private String selectedSubcategory = null;
    private String thumbnailPath = null;
    private String thumbnailName = null;
    private CheckBoxDialog multipleChoiceDialog;
    private BaseDialog basicDialog = null;
    private List<Status> statusList = null;
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
        statusBaseDAO = parseFactory.getStatusDAO();
        imageBaseDAO = parseFactory.getImageDAO();
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        multipleChoiceDialog = new CheckBoxDialog(getContext());
        Observable myObs = Observable.defer((Callable<ObservableSource<?>>) () ->
                Observable.just(servicesBaseDAO.getBulk(null),
                projectTypeDAO.getBulk(null),
                subcategoryBaseDAO.getBulk(null),
                statusBaseDAO.getBulk(null)));
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
                        if(o instanceof Collection){
                            Object temp = ((Collection) o).iterator().next();
                            if(temp instanceof Services){
                                Log.d(TAG, "onNext: Services");
                                List<Services> myServices = new ArrayList<>((Collection<? extends Services>) o);
                                myServices.add(OTHERSCHOICE_SERVICES);
                                addnew_spinner_services.setItems(myServices);
                            } else if(temp instanceof ProjectType){
                                Log.d(TAG, "onNext: ProjectType");
                                List<ProjectType> myProjectType = new ArrayList<>((Collection<? extends ProjectType>) o);
                                myProjectType.add(OTHERSCHOICE_TYPESOFPROJECT);
                                addnew_spinner_typeofproject.setItems(myProjectType);
                            } else if(temp instanceof Subcategory){
                                Log.d(TAG, "onNext: Subcategory");
                                List<Subcategory> mySubCategory = new ArrayList<>((Collection<? extends Subcategory>) o);
                                mySubCategory.add(OTHERSCHOICE_SUBCATEGORY);
                                addnew_spinner_subcategory.setItems(mySubCategory);
                            } else if(temp instanceof Status){
                                Log.d(TAG, "onNext: Status");
                                List<Status> myStatus = new ArrayList<>((Collection<? extends Status>) o);
                                statusList = myStatus;
                            }
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
        addnew_spinner_typeofproject.setOnItemSelectedListener(typeOfProjectListener());
        addnew_spinner_services.setOnItemSelectedListener(servicesListener());
        addnew_spinner_subcategory.setOnItemSelectedListener(subcategoryListener());
        Log.d(TAG, "onCreateView: Listener initialization done");
        return view;
    }
    @OnClick(R.id.addnew_imageview_thumbnail)
    void imageChoose(View view) {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL)
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(getResources().getColor(R.color.colorPrimary))
                .includeVideo(false)
                .single()
                .limit(1)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: Here");
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Log.d(TAG, "onActivityResult: Goes in");
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            thumbnailName = image.getName();
            Log.d(TAG, "onActivityResult: getPath(): " + image.getPath());
            Log.d(TAG, "onActivityResult: Image name: " + image.getName());
            thumbnailPath = image.getPath();
            addnew_imageview_thumbnail.setImageDrawable(null);
            Picasso.get().load(new File(thumbnailPath)).into(addnew_imageview_thumbnail);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.addnew_button_create)
    void createBtnClicked(View view) {
        Projects projects = new Projects();
        if( !validateField(addnew_field_projectname) 
                | !validateField(addnew_field_projectcode) 
                | !validateField(addnew_field_projectowner) | !validateThumbnail(addnew_imageview_thumbnail)
            | !validateSpinner(selectedServices, addnew_spinner_services) | !validateSpinner(selectedType, addnew_spinner_typeofproject)
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
        ImageCompressor.getInstance().setContext(getContext());
        Observable deferObs = Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                return Observable.just(projectsBaseDAO.insert(projects))
                        .map(new Function<Projects, Image>() {
                            @Override
                            public Image apply(Projects projects) throws Exception {
                                Log.d(TAG, "apply: Project ObjectID: " + projects.getObjectId() );
                                return imageBaseDAO.insert(new Image(projects.getObjectId(), thumbnailName, thumbnailPath, false));
                            }
                        })
                        .flatMap(new Function<Image, ObservableSource<Integer>>() {
                            @Override
                            public ObservableSource<Integer> apply(Image image) throws Exception {
                                Log.d(TAG, "apply: Image Parent: " + image.getParent());
                                List<Status> projectStatuses = new ArrayList<>();
                                for(String statuses : multipleChoiceDialog.getSelectedValues()){
                                    projectStatuses.add(new Status(statuses, image.getParent()));
                                }

                                return Observable.just(statusBaseDAO.insertAll(projectStatuses));
                            }
                        });
            }
        });
        deferObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Error: " + e.getMessage());
                        basicDialog.setTitle("Result");
                        basicDialog.setMessage("Error: " + e.getMessage());
                        basicDialog.setCancellable(true);
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                    }
                });

    }

    @OnClick(R.id.addnew_button_projectstatus)
    void projectStatusClicked(View view){
        String[] arrayString = new String[statusList.size()];
        for(int x = 0; x < statusList.size(); x++){
            arrayString[x] = statusList.get(x).getName();
        }
        multipleChoiceDialog.setCancellable(true);
        multipleChoiceDialog.setSourceList(arrayString);
        multipleChoiceDialog.setPositiveText("Confirm");
        multipleChoiceDialog.setTitle("Status");
        multipleChoiceDialog.showDialog();
    }
    private MaterialSpinner.OnItemSelectedListener servicesListener() {
        MaterialSpinner.OnItemSelectedListener listener = (view, position, id, item) -> {
            Services selected = (Services) item;
            Log.d(TAG, "servicesListener: " + selected.getObjectId());
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
            Log.d(TAG, "typeOfProjectListener: " + selected.getObjectId());
            selectedType = selected.toString();
            addnew_spinner_subcategory.setVisibility(View.VISIBLE);
            if(selected.toString().equalsIgnoreCase("others")){
                addnew_spinner_subcategory.setVisibility(View.GONE);
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
            Log.d(TAG, "subcategoryListener: " + selected.getObjectId());
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
