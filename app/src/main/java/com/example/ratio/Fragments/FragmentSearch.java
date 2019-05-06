package com.example.ratio.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Entities.Income;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.R;
import com.google.android.material.textfield.TextInputLayout;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    private static final String TAG = "FragmentSearch";
    public FragmentSearch() {}

    @BindView(R.id.search_field_search) TextInputLayout search_field_search;
    @BindView(R.id.search_recyclerview) RecyclerView search_recyclerview;
    ItemAdapter itemAdapter = new ItemAdapter();
    List<Expenses> expensesList = new ArrayList<>();
    List<Income> incomeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        search_field_search.getEditText().setOnEditorActionListener(listener());
        search_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        search_recyclerview.setHasFixedSize(true);
        expensesList.add(new Expenses("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));
        expensesList.add(new Expenses("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));
        expensesList.add(new Expenses("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));

        incomeList.add(new Income("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));
        incomeList.add(new Income("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));
        incomeList.add(new Income("1DE2CF", "MY EXPENSES", "1234.75", false, "14:35:12 2019-08-22"));
        return view;
    }


    private TextView.OnEditorActionListener listener() {
        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if(isNull(search_field_search.getEditText().getText().toString())) {
                        search_field_search.setError(null);
                        ArrayList<ProjectAdapter> values = new ArrayList<>();
                        Projects projects = new Projects();
                        projects.setProjectName("Project Title sample");
                        projects.setCreatedAt("March 10, 2019");
                        projects.setProjectCode("HFDJ343X");
                        projects.setProjectOwner("Joey Francisco");
                        projects.setProjectType(new ProjectType("Sample ProjectType", false));
                        projects.setProjectExpenses(expensesList);
                        projects.setProjectIncome(incomeList);
                        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
                        search_recyclerview.setAdapter(fastAdapter);
                        values.add(new ProjectAdapter(projects));
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
