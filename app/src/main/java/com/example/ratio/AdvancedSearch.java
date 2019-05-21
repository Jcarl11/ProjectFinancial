package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetDistinct;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

public class AdvancedSearch extends AppCompatActivity {
    private static final String TAG = "AdvancedSearch";
    public static final String RESULT = "RESULT";
    @BindView(R.id.advanced_spinner_status) MaterialSpinner advanced_spinner_status;
    @BindView(R.id.advanced_field_tags) TextInputLayout advanced_field_tags;
    @BindView(R.id.advanceed_button_search) Button advanceed_button_search;
    private DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private BaseDAO<Status> statusBaseDAO = null;
    private GetDistinct<Status> statusGetDistinct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Advanced");
        statusGetDistinct = (GetDistinct<Status>) sqliteFactory.getStatusDAO();
        List<Status> statusList = statusGetDistinct.getDistinct();
        Log.d(TAG, "onCreate: StatusList: " + statusList.size());
        advanced_spinner_status.setItems(statusList);
        advanced_spinner_status.setOnItemSelectedListener(spinnerListener());
    }

    @OnClick(R.id.advanceed_button_search)
    public void searchClicked(View view) {
        String object = ((Status)advanced_spinner_status.getItems().get(advanced_spinner_status.getSelectedIndex())).getObjectId();
        Log.d(TAG, "searchClicked: Objectid: " + object);

    }

    private MaterialSpinner.OnItemSelectedListener spinnerListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Status selectedItem = (Status) item;
            }
        };
        return listener;
    }
}
