package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetDistinct;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.ImageCompressor;
import com.example.ratio.HelperClasses.TagMaker;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.R;
import com.example.ratio.RxJava.ProjectTypeObservable;
import com.example.ratio.RxJava.ProjectsObservable;
import com.example.ratio.RxJava.ServicesObservable;
import com.example.ratio.RxJava.StatusObservable;
import com.example.ratio.RxJava.SubcategoryObservable;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
    @BindView(R.id.addnew_imageview_thumbnail) ImageView addnew_imageview_thumbnail;
    @BindView(R.id.addnew_spinner_status) MultiSpinnerSearch addnew_spinner_status;
    private final ProjectType OTHERSCHOICE_TYPESOFPROJECT = new ProjectType("Others", true);
    private final Subcategory OTHERSCHOICE_SUBCATEGORY = new Subcategory("Others", true, null);
    private final Services OTHERSCHOICE_SERVICES = new Services("Others", true);
    private TagMaker tagMaker = new TagMaker();
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private BaseDAO<Projects> projectsBaseDAO = null;
    private BaseDAO<ProjectType> projectTypeDAO = null;
    private BaseDAO<Subcategory> subcategoryBaseDAO = null;
    private BaseDAO<Services> servicesBaseDAO = null;
    private BaseDAO<Image> imageBaseDAO = null;
    private GetDistinct<Status> statusGetDistinct = null;
    private BaseDAO<Status> statusBaseDAO = null;
    private AlertDialog dialog = null;
    private String selectedServices = null;
    private String selectedType = null;
    private String selectedSubcategory = null;
    private String thumbnailPath = null;
    private String thumbnailName = null;
    private BaseDialog basicDialog = null;
    private ProjectsObservable projectsObservable = new ProjectsObservable();
    private StatusObservable statusObservable = new StatusObservable();
    private ServicesObservable servicesObservable = new ServicesObservable();
    private ProjectTypeObservable projectTypeObservable = new ProjectTypeObservable();
    private SubcategoryObservable subcategoryObservable = new SubcategoryObservable();
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
        statusGetDistinct = (GetDistinct<Status>) parseFactory.getStatusDAO();
        statusBaseDAO = parseFactory.getStatusDAO();
        imageBaseDAO = parseFactory.getImageDAO();
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", false);
        statusBaseDAO = sqliteFactory.getStatusDAO();
        servicesBaseDAO = sqliteFactory.getServicesDAO();
        projectTypeDAO = sqliteFactory.getProjectTypeDAO();
        subcategoryBaseDAO = sqliteFactory.getSubcategoryDAO();
        if(statusBaseDAO.getBulk(null).size() <= 0) {
            Log.d(TAG, "onCreateView: Status Empty");
            statusObservable.statusObservable().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Status>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: Status onSubscribe...");
                            dialog.setMessage("Fetching status");
                            dialog.show();
                        }

                        @Override
                        public void onNext(List<Status> statuses) {
                            Log.d(TAG, "onNext: statuses: " + statuses.size());
                            statusBaseDAO = sqliteFactory.getStatusDAO();
                            statusBaseDAO.insertAll(statuses);

                            statusGetDistinct = (GetDistinct<Status>) sqliteFactory.getStatusDAO();
                            List<Status> distinctStatus = statusGetDistinct.getDistinct();
                            List<KeyPairBoolData> items = new ArrayList<>();
                            for(int x = 0; x < distinctStatus.size(); x++){
                                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                                keyPairBoolData.setName(statuses.get(x).getName());
                                keyPairBoolData.setSelected(false);
                                items.add(keyPairBoolData);
                            }
                            addnew_spinner_status.setItems(items, -1, new SpinnerListener() {
                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> list) {
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).isSelected()) {
                                            Log.i(TAG, i + " : " + list.get(i).getName() + " : " + list.get(i).isSelected());
                                        }
                                    }
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.d(TAG, "onError: First: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            Log.d(TAG, "onComplete: status fetch finished");
                        }
                    });
        } else {
            statusGetDistinct = (GetDistinct<Status>) sqliteFactory.getStatusDAO();
            List<Status> distinctStatus = statusGetDistinct.getDistinct();
            List<KeyPairBoolData> items = new ArrayList<>();
            for(int x = 0; x < distinctStatus.size(); x++){
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setName(distinctStatus.get(x).getName());
                keyPairBoolData.setSelected(false);
                items.add(keyPairBoolData);
            }
            addnew_spinner_status.setItems(items, -1, new SpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> list) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelected()) {
                            Log.i(TAG, i + " : " + list.get(i).getName() + " : " + list.get(i).isSelected());
                        }
                    }
                }
            });
        }

        if(servicesBaseDAO.getBulk(null).size() <= 0) {
            Log.d(TAG, "onCreateView: Services Empty");
            servicesObservable.servicesListObs().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Services>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: Services");
                            dialog.setMessage("Fetching services");
                            dialog.show();
                        }

                        @Override
                        public void onNext(List<Services> services) {
                            Log.d(TAG, "onNext: services size: " + services.size());
                            servicesBaseDAO = sqliteFactory.getServicesDAO();
                            servicesBaseDAO.insertAll(services);
                            services.add(OTHERSCHOICE_SERVICES);
                            addnew_spinner_services.setItems(services);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.d(TAG, "onError: Error: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            Log.d(TAG, "onComplete: services fetch finished");
                        }
                    });
        } else {
            servicesBaseDAO = sqliteFactory.getServicesDAO();
            List<Services> servicesList = servicesBaseDAO.getBulk(null);
            servicesList.add(OTHERSCHOICE_SERVICES);
            addnew_spinner_services.setItems(servicesList);
        }

        if (projectTypeDAO.getBulk(null).size() <= 0) {
            Log.d(TAG, "onCreateView: ProjectType empty");
            projectTypeObservable.projectTypeObservable().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ProjectType>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ProjectType onSubscribe");
                            dialog.setMessage("Fetching project types");
                            dialog.show();
                        }

                        @Override
                        public void onNext(List<ProjectType> projectTypes) {
                            Log.d(TAG, "onNext: Project type size: " + projectTypes.size());
                            projectTypeDAO = sqliteFactory.getProjectTypeDAO();
                            int result = projectTypeDAO.insertAll(projectTypes);
                            Log.d(TAG, "onNext: Result: " + result);
                            projectTypes.add(OTHERSCHOICE_TYPESOFPROJECT);
                            addnew_spinner_typeofproject.setItems(projectTypes);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.d(TAG, "onError: Error: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            Log.d(TAG, "onComplete: Proejct type fetch done");
                        }
                    });
        } else {
            projectTypeDAO = sqliteFactory.getProjectTypeDAO();
            List<ProjectType> projectTypeList = projectTypeDAO.getBulk(null);
            projectTypeList.add(OTHERSCHOICE_TYPESOFPROJECT);
            addnew_spinner_typeofproject.setItems(projectTypeList);
        }

        if (subcategoryBaseDAO.getBulk(null).size() <= 0){
            subcategoryObservable.subcategoryObservable().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Subcategory>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: Subcategory onSubscribe");
                            dialog.setMessage("Fetching subcategories");
                            dialog.show();
                        }

                        @Override
                        public void onNext(List<Subcategory> subcategoryList) {
                            Log.d(TAG, "onNext: Subcategory size: " + subcategoryList.size());
                            subcategoryBaseDAO = sqliteFactory.getSubcategoryDAO();
                            int result = subcategoryBaseDAO.insertAll(subcategoryList);
                            Log.d(TAG, "onNext: Result size: " + result);
                            subcategoryList.add(OTHERSCHOICE_SUBCATEGORY);
                            addnew_spinner_subcategory.setItems(subcategoryList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                            dialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            Log.d(TAG, "onComplete: subcategory fetch done");
                        }
                    });
        } else {
            subcategoryBaseDAO = sqliteFactory.getSubcategoryDAO();
            List<Subcategory> subcategoryList = subcategoryBaseDAO.getBulk(null);
            subcategoryList.add(OTHERSCHOICE_SUBCATEGORY);
            addnew_spinner_subcategory.setItems(subcategoryList);
        }

        addnew_spinner_typeofproject.setOnItemSelectedListener(typeOfProjectListener());
        addnew_spinner_services.setOnItemSelectedListener(servicesListener());
        addnew_spinner_subcategory.setOnItemSelectedListener(subcategoryListener());
        Log.d(TAG, "onCreateView: Listener initialization done");
        return view;
    }

    @OnClick(R.id.addnew_imageview_thumbnail)
    void imageChoose(View view) {
        Log.d(TAG, "imageChoose: thumbnail clicked");
        Intent intent = new Intent(getContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(true)
                        .setShowAudios(false)
                        .setShowFiles(false)
                        .setShowVideos(false)
                        .enableImageCapture(true)
                        .setMaxSelection(1)
                        .build());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: Here");
        if (resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "onActivityResult: Result ok");
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            MediaFile chosen_image = mediaFiles.size() <= 0 ? null : mediaFiles.get(0);
            Log.d(TAG, "onActivityResult: Image Path: " + chosen_image.getPath()); // has Extension
            Log.d(TAG, "onActivityResult: Image Name: " + chosen_image.getName());
            thumbnailName = chosen_image.getName(); //No extension
            thumbnailPath = chosen_image.getPath();
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
            | !validateSpinner(selectedSubcategory, addnew_spinner_subcategory) | !validateMultiSpinner(addnew_spinner_status)) {
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
        projects.setTags(tagMaker.toArray(tagMaker.createTags(projects.toString())));
        Image thumbnail = new Image();
        thumbnail.setFilePath(thumbnailPath);
        thumbnail.setFileName(thumbnailName);
        thumbnail.setDeleted(false);
        projectsObservable.uploadProjectConnectable(projects, thumbnail, addnew_spinner_status.getSelectedItems())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Projects>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Projects projects) {
                        Log.d(TAG, "onNext: Project: " + projects.getObjectId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean validateMultiSpinner(MultiSpinnerSearch multiSpinnerSearch) {
        List<KeyPairBoolData> items = multiSpinnerSearch.getSelectedItems();
        if(items.size() <= 0) {
            multiSpinnerSearch.setBackground(getResources().getDrawable(R.drawable.bg_error));
            return false;
        } else {
            multiSpinnerSearch.setBackground(null);
            return true;
        }
    }
}
