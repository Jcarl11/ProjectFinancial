package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Enums.DATABASES;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {
    private static final String TAG = "ReportsActivity";
    @BindView(R.id.reports_piechart) PieChart reports_piechart;
    @BindView(R.id.reports_projectcode) TextInputLayout reports_projectcode;
    @BindView(R.id.reports_projectname) TextInputLayout reports_projectname;
    @BindView(R.id.reports_projectowner) TextInputLayout reports_projectowner;
    @BindView(R.id.reports_createdAt) TextInputLayout reports_createdAt;
    @BindView(R.id.reports_totalincome) TextInputLayout reports_totalincome;
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private BaseDAO<Projects> projectsBaseDAO = daoFactory.getProjectDAO();
    private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f};
    private String[] xData = {"CT201902", "CT201903" , "CT201904" , "CT201905", "CT201906"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Top 5 projects with highest income");
        reports_piechart.setRotationEnabled(true);
        reports_piechart.setHoleRadius(25f);
        reports_piechart.setTransparentCircleAlpha(0);
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Projects");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        reports_piechart.setData(pieData);
        reports_piechart.invalidate();

        reports_piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Entry: " + e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}
