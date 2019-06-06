package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.AddExpensesActivity;
import com.example.ratio.AddIncomeActivity;
import com.example.ratio.AddRecievablesActivity;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.R;
import com.example.ratio.RxJava.ProjectsObservable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortfolio extends Fragment {

    private static final String TAG = "FragmentPortfolio";
    @BindView(R.id.portfolio_recyclerview) RecyclerView portfolio_recyclerview;
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Projects> projectsBaseDAO = null;
    private BaseDAO<Status> statusBaseDAO = null;
    private BaseDAO<Image> imageBaseDAO = null;
    private AlertDialog dialog;
    private BaseDialog basicDialog = null;
    private ProjectsObservable projectsObservable = new ProjectsObservable();
    private int pos = -1;
    private List<Projects> projectsList = new ArrayList<>();
    public static String PARENTID = "PARENTID";
    public FragmentPortfolio() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        ButterKnife.bind(this, view);
        projectsBaseDAO = parseFactory.getProjectDAO();
        statusBaseDAO = parseFactory.getStatusDAO();
        imageBaseDAO = parseFactory.getImageDAO();
        dialog = Utility.getInstance().showLoading(getContext(), "Please wait", true);
        basicDialog = new BasicDialog(getContext());
        portfolio_recyclerview.setHasFixedSize(true);
        portfolio_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        if(portfolio_recyclerview.getAdapter() == null) {
            Log.d(TAG, "onCreateView: Loaded...");
            getProjects();
        }

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(portfolio_recyclerview,  callBack())
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
                                        addincome.putExtra(PARENTID, projectsList.get(pos).getObjectId());
                                        startActivityForResult(addincome, 1);
                                        break;
                                    case 1:
                                        Intent addexpenses = new Intent(getContext(), AddExpensesActivity.class);
                                        addexpenses.putExtra(PARENTID, projectsList.get(pos).getObjectId());
                                        startActivityForResult(addexpenses, 2);
                                        break;
                                    case 2:
                                        Intent addreceivables = new Intent(getContext(), AddRecievablesActivity.class);
                                        addreceivables.putExtra(PARENTID, projectsList.get(pos).getObjectId());
                                        startActivityForResult(addreceivables, 3);
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        portfolio_recyclerview.setOnTouchListener(listener);
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
    public void getProjects() {

        projectsObservable.getProjectCompleteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Projects>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        dialog.show();
                        Log.d(TAG, "onSubscribe: Projects subscribed...");
                    }

                    @Override
                    public void onNext(List<Projects> projects) {
                        Log.d(TAG, "onNext: Size: "  + projects.size());
                        projectsList = projects;
                        ProjectAdapter adapter = new ProjectAdapter(getContext(), projects);
                        portfolio_recyclerview.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.d(TAG, "onError: Projects error: " + e.getMessage());
                        basicDialog.setTitle("Result");
                        basicDialog.setMessage("Error: " + e.getMessage());
                        basicDialog.setCancellable(true);
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        Log.d(TAG, "onComplete: Completed...");
                    }
                });
    }
}
