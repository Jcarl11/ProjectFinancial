package com.example.ratio.Fragments;


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

import com.example.ratio.R;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

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

    private ArrayList<String> TYPE_OF_PROJECTS = new ArrayList<String>(Arrays.asList("Residence", "Commercial", "Workplace", "Industrial"));
    private ArrayList<String> SERVICES = new ArrayList<>(Arrays.asList("Architectural Design", "Engineering Design", "Project Management"));
    private ArrayList<List<String>> subCategories = new ArrayList<>();
    private List<String> residenceSub = new ArrayList<>(Arrays.asList("Residence sub category 1","Residence sub category 2","Residence sub category 3"));
    private List<String> commercialSub = new ArrayList<>(Arrays.asList("Retail","F & B"));
    private List<String> workplaceSub = new ArrayList<>(Arrays.asList("Office"));
    private List<String> industrialSub = new ArrayList<>(Arrays.asList("Industrial sub category 1","Industrial sub category 2","Industrial sub category 3"));

    String selectedTypeOfProject = null;
    String selectedSubcategory = null;
    String selectedServices = null;
    public FragmentAddNew() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);
        ButterKnife.bind(this, view);
        subCategories.add(residenceSub);
        subCategories.add(commercialSub);
        subCategories.add(workplaceSub);
        subCategories.add(industrialSub);
        addnew_spinner_typeofproject.setItems(TYPE_OF_PROJECTS);
        SERVICES.add(SERVICES.size(), "Others");
        addnew_spinner_services.setItems(SERVICES);

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
                Toast.makeText(getContext(), "Specify the service type", Toast.LENGTH_SHORT).show();
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
                addnew_spinner_subcategory.setItems(subCategories.get(position));
                addnew_spinner_subcategory.setVisibility(View.VISIBLE);
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


}
