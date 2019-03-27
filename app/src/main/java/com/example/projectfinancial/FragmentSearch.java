package com.example.projectfinancial;


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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    public FragmentSearch() {}
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
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
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
                return true;
            }
        };
        return listener;
    }

}
