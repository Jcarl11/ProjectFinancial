package com.example.ratio.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            rows.setProjectName(String.format("Project number %s", String.valueOf(x)));
            rows.setThumbnail(new Image(null,"RANDOM IMAGE", String.format("https://picsum.photos/500/300/?image=2%s", String.valueOf(x)), false));
            data.add(new ProjectAdapter(rows));
        }
        return data;
    }


}
