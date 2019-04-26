package com.example.ratio.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_portfolio)
public class FragmentPortfolio extends Fragment {

    private static final String TAG = "FragmentPortfolio";
    @ViewById RecyclerView portfolio_recyclerview;
    ItemAdapter itemAdapter = new ItemAdapter();
    FastAdapter fastAdapter;
    public FragmentPortfolio() {}

    @AfterViews void afterView(){
        portfolio_recyclerview.setHasFixedSize(true);
        portfolio_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        fastAdapter = FastAdapter.with(itemAdapter);
        portfolio_recyclerview.setAdapter(fastAdapter);
        itemAdapter.add(generateDummyData(8));
    }

    public ArrayList<ProjectAdapter> generateDummyData(int rowCount) {
        ArrayList<ProjectAdapter> data = new ArrayList<>();
        for( int x = 0; x < rowCount ; x++ ) {
            Projects rows = new Projects();
            rows.setProjectCode(String.format("CT2019%s", String.valueOf(x)));
            rows.setProjectStatus(new Status("ACTIVE"));
            rows.setProjectName(String.format("Project number %s", String.valueOf(x)));
            rows.setThumbnail(new Image(null,"RANDOM IMAGE", String.format("https://picsum.photos/500/300/?image=2%s", String.valueOf(x)), false));
            data.add(new ProjectAdapter(rows));
        }
        return data;
    }


}
