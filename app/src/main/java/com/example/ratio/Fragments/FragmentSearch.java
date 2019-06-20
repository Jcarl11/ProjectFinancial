package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.AddExpensesActivity;
import com.example.ratio.AddIncomeActivity;
import com.example.ratio.AddRecievablesActivity;
import com.example.ratio.AdvancedSearch;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Projects;
import com.example.ratio.ExpensesListActivity;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.IncomeListActivity;
import com.example.ratio.R;
import com.example.ratio.ReceivablesListActivity;
import com.example.ratio.RxJava.ProjectsObservable;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
    private int pos = -1;
    private List<Projects> projectsList = new ArrayList<>();

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
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(search_recyclerview,  callBack())
                .setIsVertical(false)
                .setItemTouchCallback(new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                    @Override
                    public void onTouch(int position) {

                    }
                })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        String[] choices = new String[]{"Add Income", "Add Expenses", "Add Recivables",
                                "Show Income", "Show Expenses", "Show Recivables"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Choose");
                        builder.setCancelable(true);
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: Which: " + which);
                                switch (which) {
                                    case 0:
                                        Intent addincome = new Intent(getContext(), AddIncomeActivity.class);
                                        addincome.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        addincome.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivityForResult(addincome, 1);
                                        break;
                                    case 1:
                                        Intent addexpenses = new Intent(getContext(), AddExpensesActivity.class);
                                        addexpenses.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        addexpenses.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivityForResult(addexpenses, 2);
                                        break;
                                    case 2:
                                        Intent addreceivables = new Intent(getContext(), AddRecievablesActivity.class);
                                        addreceivables.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        addreceivables.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivityForResult(addreceivables, 3);
                                        break;
                                    case 3:
                                        Intent showIncome = new Intent(getContext(), IncomeListActivity.class);
                                        showIncome.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        showIncome.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivity(showIncome);
                                        break;
                                    case 4:
                                        Intent showExpenses = new Intent(getContext(), ExpensesListActivity.class);
                                        showExpenses.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        showExpenses.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivity(showExpenses);
                                        break;
                                    case 5:
                                        Intent showReceivables = new Intent(getContext(), ReceivablesListActivity.class);
                                        showReceivables.putExtra(Constant.PARENTID, projectsList.get(pos).getObjectId());
                                        showReceivables.putExtra(Constant.PARENTCODE, projectsList.get(pos).getProjectCode());
                                        startActivity(showReceivables);
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        search_recyclerview.setOnTouchListener(listener);
        return view;
    }

    private SwipeDismissRecyclerViewTouchListener.DismissCallbacks callBack() {
        SwipeDismissRecyclerViewTouchListener.DismissCallbacks callback = new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                pos = position;
                return true;
            }

            @Override
            public void onDismiss(View view) {

            }
        };
        return callback;
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
                                    projectsList = projects;
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
        Intent intent = new Intent(getContext(), AdvancedSearch.class);
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
        projectAdapter = new ProjectAdapter(getContext(), projectsList);
        search_recyclerview.setAdapter(projectAdapter);
    }
}





















