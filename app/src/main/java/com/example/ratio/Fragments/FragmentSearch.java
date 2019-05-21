package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.AdvancedSearch;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Entities.Income;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.R;
import com.example.ratio.RxJava.ProjectsObservable;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    private static final String TAG = "FragmentSearch";
    @BindView(R.id.search_field_search) TextInputLayout search_field_search;
    @BindView(R.id.search_recyclerview) RecyclerView search_recyclerview;
    @BindView(R.id.search_button_advanced) Button search_button_advanced;
    private ProjectsObservable projectsObservable = new ProjectsObservable();
    private AlertDialog dialog;
    private BaseDialog basicDialog = null;
    private ProjectAdapter projectAdapter = null;

    public FragmentSearch() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", true);
        basicDialog = new BasicDialog(getContext());
        search_field_search.getEditText().setOnEditorActionListener(listener());
        search_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        search_recyclerview.setHasFixedSize(true);
        return view;
    }

    private TextView.OnEditorActionListener listener() {
        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {

                    if(search_field_search.getEditText().getText().toString().isEmpty()) {
                        search_field_search.setError("This field cannot be empty");
                        return true;
                    }

                    search_field_search.setError(null);
                    String projectCode = search_field_search.getEditText().getText().toString().toUpperCase();
                    projectsObservable.getProjectSpecificCompleteObservable(projectCode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<Projects>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    Log.d(TAG, "onSubscribe: Subscribed...");
                                    dialog.show();
                                }

                                @Override
                                public void onNext(List<Projects> projects) {
                                    Log.d(TAG, "onNext: Projects size: " + projects.size());
                                    if(projects.size() <= 0) {
                                        basicDialog.setTitle("Result");
                                        basicDialog.setMessage("No records found");
                                        basicDialog.setCancellable(true);
                                        basicDialog.showDialog();
                                        return;
                                    }
                                    projectAdapter = new ProjectAdapter(getContext(), projects);
                                    search_recyclerview.setAdapter(projectAdapter);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    dialog.dismiss();
                                    Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                                    basicDialog.setTitle("Result");
                                    basicDialog.setMessage("Error: " + e.getMessage());
                                    basicDialog.setCancellable(true);
                                    basicDialog.showDialog();
                                }

                                @Override
                                public void onComplete() {
                                    dialog.dismiss();
                                    Log.d(TAG, "onComplete: Operation Completed...");
                                }
                            });
                }
                return false;
            }
        };
        return listener;
    }

    @OnClick(R.id.search_button_advanced)
    public void advancedClicked(View view) {
        Log.d(TAG, "advancedClicked: Advanced clicked...");
        Intent intent = new Intent(getActivity(), AdvancedSearch.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 1) { return; }
        if (data == null) { return; }
        if (resultCode != getActivity().RESULT_OK) { return; }

        List<Projects> projectsList = (List<Projects>) data.getSerializableExtra(AdvancedSearch.RESULT);
        Log.d(TAG, "onActivityResult: Result size: " + projectsList.size());

    }
}





















