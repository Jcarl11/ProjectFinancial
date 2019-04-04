package com.example.ratio.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.Entities.ProjectsEntity;
import com.example.ratio.R;
import com.google.android.material.textfield.TextInputLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    public FragmentSearch() {}

    private static final String TAG = "FragmentSearch";
    @BindView(R.id.search_field_search) TextInputLayout search_field_search;
    @BindView(R.id.search_recyclerview) RecyclerView search_recyclerview;
    ItemAdapter itemAdapter = new ItemAdapter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        search_field_search.getEditText().setOnEditorActionListener(listener());
        search_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        search_recyclerview.setHasFixedSize(true);
        return view;
    }

    private TextView.OnEditorActionListener listener() {
        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if(isNull(search_field_search.getEditText().getText().toString()) == false) {
                        search_field_search.setError(null);
                        ArrayList<ProjectAdapter> values = new ArrayList<>();
                        ProjectsEntity projectsEntity = new ProjectsEntity();
                        projectsEntity.setProjectName("Project Title sample");
                        projectsEntity.setCreatedAt("March 10, 2019");
                        projectsEntity.setProjectCode("HFDJ343X");
                        projectsEntity.setProjectOwner("Joey Francisco");
                        projectsEntity.setProjectType("Workplace");
                        projectsEntity.setProjectType("Office");
                        projectsEntity.setProjectExpenses("1235.23 Php");
                        projectsEntity.setProjectRevenue("5844185.52 Php");
                        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
                        search_recyclerview.setAdapter(fastAdapter);
                        values.add(new ProjectAdapter(projectsEntity));
                        itemAdapter.add(values);
                    } else {
                        search_field_search.setError("This field cannot be empty");
                    }
                }
                return false;
            }
        };
        return listener;
    }

    private boolean isNull(String input) {
        return input.isEmpty() ? true : false;
    }

}
