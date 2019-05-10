package com.example.ratio.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratio.Adapters.ProjectAdapter;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.R;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ProjectsObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


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
        return view;
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
