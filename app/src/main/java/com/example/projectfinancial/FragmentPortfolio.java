package com.example.projectfinancial;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortfolio extends Fragment {
    private static String TAG = FragmentPortfolio.class.getCanonicalName();
    public FragmentPortfolio() {}
    private ArrayList<String> CATEGORIES = new ArrayList<String>(Arrays.asList("Residence", "Commercial", "Workplace", "Industrial"));
    private ArrayList<List<String>> subCategories = new ArrayList<>();
    private List<String> residenceSub = new ArrayList<>(Arrays.asList("Residence sub category 1","Residence sub category 2","Residence sub category 3"));
    private List<String> commercialSub = new ArrayList<>(Arrays.asList("Retail","F & B"));
    private List<String> workplaceSub = new ArrayList<>(Arrays.asList("Office"));
    private List<String> industrialSub = new ArrayList<>(Arrays.asList("Industrial sub category 1","Industrial sub category 2","Industrial sub category 3"));
    @BindView(R.id.portfolio_spinner) MaterialBetterSpinner portfolio_spinner;
    @BindView(R.id.portfolio_subcategory) Spinner portfolio_subcategory;
    @BindView(R.id.portfolio_button_search) Button portfolio_button_search;
    @BindView(R.id.portfolio_recyclerview_projectlist) RecyclerView portfolio_recyclerview_projectlist;
    ItemAdapter itemAdapter = new ItemAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.bind(this, view);
        portfolio_recyclerview_projectlist.setLayoutManager(new LinearLayoutManager(getContext()));
        portfolio_recyclerview_projectlist.setHasFixedSize(true);
        portfolio_spinner.setAdapter(portfolio_spinner_Adapter(CATEGORIES));
        portfolio_spinner.setOnItemClickListener(portfolio_spinner_listener());


        residenceSub.add(0, "Choose subcategory");
        commercialSub.add(0, "Choose subcategory");
        workplaceSub.add(0, "Choose subcategory");
        industrialSub.add(0, "Choose subcategory");
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
    private AdapterView.OnItemClickListener portfolio_spinner_listener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                portfolio_subcategory.setAdapter(portfolio_spinner_Adapter(subCategories.get(position)));
                portfolio_subcategory.setSelection(0);
            }
        };
        return listener;
    }

    @OnClick(R.id.portfolio_button_search)
    public void portfolio_button_search(View view) {
        if(portfolio_subcategory.getSelectedItemPosition() == 0 || portfolio_subcategory.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Choose a subcategory", Toast.LENGTH_SHORT).show();
            return;
        }
        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        portfolio_recyclerview_projectlist.setAdapter(fastAdapter);
        ArrayList<PortfolioProjects> values = new ArrayList<>();
        PortfolioProjects portfolioProjects = new PortfolioProjects();
        portfolioProjects.portfolio_rowlayout_projectname = "Project Title sample";
        portfolioProjects.portfolio_rowlayout_createdat = "March 10, 2019";
        portfolioProjects.portfolio_rowlayout_projectcode = "HFDJ343X";
        portfolioProjects.portfolio_rowlayout_projectowner = "Joey Francisco";
        portfolioProjects.portfolio_rowlayout_projectcategory = "Workplace";
        portfolioProjects.portfolio_rowlayout_projecttype = "Office";
        portfolioProjects.portfolio_rowlayout_expenses = "1235.23 Php";
        portfolioProjects.portfolio_rowlayout_revenue = "5844185.52 Php";
        values.add(portfolioProjects);
        itemAdapter.add(values);
    }
}
