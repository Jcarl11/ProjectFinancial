package com.example.projectfinancial.Fragments;


import android.opengl.Visibility;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfinancial.Adapters.ProjectAdapter;
import com.example.projectfinancial.Entities.ProjectsEntity;
import com.example.projectfinancial.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortfolio extends Fragment {

    private static final String TAG = "FragmentPortfolio";

    @BindView(R.id.portfolio_category) MaterialSpinner portfolio_category;
    @BindView(R.id.portfolio_button_search) Button portfolio_button_search;
    @BindView(R.id.portfolio_recyclerview_projectlist) RecyclerView portfolio_recyclerview_projectlist;
    @BindView(R.id.portfolio_sample) MaterialSpinner portfolio_sample;
    private ArrayList<String> CATEGORIES = new ArrayList<String>(Arrays.asList("Residence", "Commercial", "Workplace", "Industrial"));
    private ArrayList<List<String>> subCategories = new ArrayList<>();
    private List<String> residenceSub = new ArrayList<>(Arrays.asList("Residence sub category 1","Residence sub category 2","Residence sub category 3"));
    private List<String> commercialSub = new ArrayList<>(Arrays.asList("Retail","F & B"));
    private List<String> workplaceSub = new ArrayList<>(Arrays.asList("Office"));
    private List<String> industrialSub = new ArrayList<>(Arrays.asList("Industrial sub category 1","Industrial sub category 2","Industrial sub category 3"));
    ItemAdapter itemAdapter = new ItemAdapter();
    FastAdapter fastAdapter;

    public FragmentPortfolio() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.bind(this, view);
        portfolio_recyclerview_projectlist.setLayoutManager(new LinearLayoutManager(getContext()));
        portfolio_recyclerview_projectlist.setHasFixedSize(true);
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withOnClickListener(adapterListener());
        portfolio_category.setItems(CATEGORIES);
        portfolio_category.setOnItemSelectedListener(categorySpinnerListener());

        subCategories.add(residenceSub);
        subCategories.add(commercialSub);
        subCategories.add(workplaceSub);
        subCategories.add(industrialSub);
        return view;
    }

    private ArrayAdapter<String> portfolio_spinner_Adapter(Collection<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>(list));
        return adapter;
    }

    private MaterialSpinner.OnItemSelectedListener categorySpinnerListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                portfolio_sample.setVisibility(View.VISIBLE);
                portfolio_sample.setItems(subCategories.get(position));
            }
        };
        return listener;
    }
    private OnClickListener adapterListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                TextView projectName = v.findViewById(R.id.portfolio_rowlayout_projectname);
                Log.d(TAG, String.format("position(%s) - item(%s)",
                        String.valueOf(position),
                        projectName.getText()));
                return false;
            }
        };
        return listener;
    }
    @OnClick(R.id.portfolio_button_search)
    public void portfolio_button_search(View view) {
        Log.d(TAG, String.format("Position(%s)",
                String.valueOf(portfolio_category.getSelectedIndex())));
        if(portfolio_sample.getSelectedIndex() < 0) {
            Toast.makeText(getContext(), "Choose a subcategory", Toast.LENGTH_SHORT).show();
            return;
        }
        portfolio_recyclerview_projectlist.setAdapter(fastAdapter);
        ArrayList<ProjectAdapter> values = new ArrayList<>();
        ProjectsEntity projectsEntity = new ProjectsEntity();
        projectsEntity.setProjectName("Project Title sample");
        projectsEntity.setCreatedAt("March 10, 2019");
        projectsEntity.setProjectCode("HFDJ343X");
        projectsEntity.setProjectOwner("Joey Francisco");
        projectsEntity.setProjectCategory("Workplace");
        projectsEntity.setProjectType("Office");
        projectsEntity.setProjectExpenses("1235.23 Php");
        projectsEntity.setProjectRevenue("5844185.52 Php");
        values.add(new ProjectAdapter(projectsEntity));
        itemAdapter.add(values);
    }
}
