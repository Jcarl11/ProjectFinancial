package com.example.ratio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetDistinct;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ProjectsObservable;
import com.example.ratio.RxJava.StatusObservable;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearch extends AppCompatActivity {
    private static final String TAG = "AdvancedSearch";
    public static final String RESULT = "RESULT";
    @BindView(R.id.advanced_spinner_status) MaterialSpinner advanced_spinner_status;
    @BindView(R.id.advanced_field_tags) EditText advanced_field_tags;
    @BindView(R.id.advanceed_button_search) Button advanceed_button_search;
    private DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Status> statusBaseDAO = null;
    private BaseDAO<Projects> projectsBaseDAO = null;
    private GetDistinct<Status> statusGetDistinct = null;
    private String selectedStatus = null;
    private StatusObservable statusObservable = new StatusObservable();
    private ProjectsObservable projectsObservable = new ProjectsObservable();
    private AlertDialog dialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Advanced");
        statusGetDistinct = (GetDistinct<Status>) sqliteFactory.getStatusDAO();
        statusBaseDAO = parseFactory.getStatusDAO();
        projectsBaseDAO = parseFactory.getProjectDAO();
        List<Status> statusList = statusGetDistinct.getDistinct();
        Log.d(TAG, "onCreate: StatusList: " + statusList.size());
        advanced_spinner_status.setItems(statusList);
        advanced_spinner_status.setOnItemSelectedListener(spinnerListener());
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        intent = new Intent();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.advanceed_button_search)
    public void searchClicked(View view) {
        if (selectedStatus == null | !validateField(advanced_field_tags)) {
            advanced_spinner_status.setBackground(getResources().getDrawable(R.drawable.bg_error));
            return;
        }

        statusObservable.getStatusesObservable()
                .map(new Function<List<Status>, List<Projects>>() {
                    @Override
                    public List<Projects> apply(List<Status> statuses) throws Exception {
                        List<Projects> projectsList = new ArrayList<>();
                        for (Status status : statuses) {
                            if (status.getName().equalsIgnoreCase(selectedStatus) && !status.getParent().equalsIgnoreCase("DEFAULTS")) {
                                Log.d(TAG, "apply: Status parent: " + status.getParent());
                                projectsObservable.getProjectFromIDCompleteObservable(status.getParent())
                                        .subscribe(new Consumer<Projects>() {
                                            @Override
                                            public void accept(Projects projects) throws Exception {
                                                projectsList.add(projects);
                                            }
                                        });
                            }
                        }

                        return projectsList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Projects>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Projects> projects) {
                        Log.d(TAG, "onNext: Projects size: " + projects.size());
                        intent.putExtra(RESULT, (Serializable) projects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Excetion:" + e.getMessage());
                        dialog.dismiss();
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
        Log.d(TAG, "searchClicked: ");
    }

    private MaterialSpinner.OnItemSelectedListener spinnerListener() {
        MaterialSpinner.OnItemSelectedListener listener = new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Status selectedItem = (Status) item;
                selectedStatus = selectedItem.getName();
            }
        };
        return listener;
    }

    private boolean validateField(EditText textInput) {
        if (textInput.getText().toString().isEmpty() == true) {
            textInput.setError("Cannot be empty");
            return false;
        } else {
            textInput.setError(null);
            return true;
        }
    }
}
