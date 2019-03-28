package com.example.projectfinancial.Fragments;


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

import com.example.projectfinancial.R;
import com.google.android.material.snackbar.Snackbar;
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
    @BindView(R.id.addnew_spinner_category) MaterialSpinner addnew_spinner_category;
    @BindView(R.id.addnew_spinner_subcategory) MaterialSpinner addnew_spinner_subcategory;
    @BindView(R.id.addnew_button_create) Button addnew_button_create;

    private ArrayList<String> CATEGORIES = new ArrayList<String>(Arrays.asList("Residence", "Commercial", "Workplace", "Industrial"));
    private ArrayList<List<String>> subCategories = new ArrayList<>();
    private List<String> residenceSub = new ArrayList<>(Arrays.asList("Residence sub category 1","Residence sub category 2","Residence sub category 3"));
    private List<String> commercialSub = new ArrayList<>(Arrays.asList("Retail","F & B"));
    private List<String> workplaceSub = new ArrayList<>(Arrays.asList("Office"));
    private List<String> industrialSub = new ArrayList<>(Arrays.asList("Industrial sub category 1","Industrial sub category 2","Industrial sub category 3"));

    String selectedCategory = null;
    String selectedSubcategory = null;
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
        addnew_spinner_category.setItems(CATEGORIES);
        addnew_spinner_category.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedCategory = String.valueOf(item);
                addnew_spinner_subcategory.setVisibility(View.VISIBLE);
                addnew_spinner_subcategory.setItems(subCategories.get(position));
            }
        });

        addnew_spinner_subcategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedSubcategory = String.valueOf(item);
            }
        });
        return view;
    }

    @OnClick(R.id.addnew_button_create)
    public void createBtnClicked( View view ) {
        if(!validateField(addnew_field_projectname) | !validateField(addnew_field_projectcode) | !validateField(addnew_field_projectowner)) {
            return;
        }
        if(selectedCategory == null) {
            Log.d(TAG, "createBtnClicked: Empty");
            Toast.makeText(getContext(), "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedSubcategory == null){
            Log.d(TAG, "createBtnClicked: Empty");
            Toast.makeText(getContext(), "Select a subcategory", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "createBtnClicked: " + selectedCategory);
        Log.d(TAG, "createBtnClicked: " + selectedSubcategory);
        Log.d(TAG, "createBtnClicked: all fields are valid");

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
