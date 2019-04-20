package com.example.ratio.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.Entities.Projects;
import com.example.ratio.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortfolio extends Fragment {

    private static final String TAG = "FragmentPortfolio";
    @BindView(R.id.portfolio_recyclerview) RecyclerView portfolio_recyclerview;
    ItemAdapter itemAdapter = new ItemAdapter();
    FastAdapter fastAdapter;
    public FragmentPortfolio() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.bind(this, view);
        portfolio_recyclerview.setHasFixedSize(true);
        portfolio_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        fastAdapter = FastAdapter.with(itemAdapter);
        portfolio_recyclerview.setAdapter(fastAdapter);
        itemAdapter.add(generateDummyData(8));
        return view;
    }

    public ArrayList<ProjectAdapter> generateDummyData(int rowCount) {
        ArrayList<ProjectAdapter> data = new ArrayList<>();
        for( int x = 0; x < rowCount ; x++ ) {
            Projects rows = new Projects();
            rows.setProjectCode(String.format("CT2019%s", String.valueOf(x)));
            rows.setProjectStatus("ACTIVE");
            rows.setProjectName(String.format("Project number %s", String.valueOf(x)));
            rows.setImagePath(String.format("https://picsum.photos/500/300/?image=2%s", String.valueOf(x)));
            data.add(new ProjectAdapter(rows));
        }
        return data;
    }


}
