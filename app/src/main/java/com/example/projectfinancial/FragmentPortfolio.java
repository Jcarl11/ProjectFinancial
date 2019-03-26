package com.example.projectfinancial;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortfolio extends Fragment {
    private static String TAG = FragmentPortfolio.class.getCanonicalName();
    public FragmentPortfolio() {}
    private static String[] CATEGORIES = new String[]{"Residence", "Community", "Workplace", "Industrial"};
    @BindView(R.id.portfolio_spinner)
    MaterialBetterSpinner portfolio_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.bind(this, view);
        portfolio_spinner.setAdapter(portfolio_spinner_Adapter(CATEGORIES));
        portfolio_spinner.setOnItemClickListener(portfolio_spinner_listener());
        return view;
    }

    private ArrayAdapter<String> portfolio_spinner_Adapter(String[] CATEGORIES) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                CATEGORIES);
        return adapter;
    }
    private AdapterView.OnItemClickListener portfolio_spinner_listener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(parent.getItemAtPosition(position)), Toast.LENGTH_SHORT).show();
            }
        };
        return listener;
    }


}
