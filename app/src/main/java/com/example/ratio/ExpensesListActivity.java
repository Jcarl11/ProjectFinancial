package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.ratio.Adapters.ExpensesAdapter;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ExpensesObservable;

import java.util.List;

public class ExpensesListActivity extends AppCompatActivity {
    private static final String TAG = "ExpensesListActivity";
    @BindView(R.id.expenseslist_recyclerview) RecyclerView expenseslist_recyclerview;
    private String PARENT_ID = null;
    private String PARENT_CODE = null;
    private ExpensesObservable expensesObservable = new ExpensesObservable();
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(FragmentPortfolio.PARENTID);
        PARENT_CODE = getIntent().getStringExtra(FragmentPortfolio.PARENTCODE);
        getSupportActionBar().setTitle(String.format("Expenses for %s", PARENT_CODE));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        expenseslist_recyclerview.setHasFixedSize(true);
        expenseslist_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        expensesObservable.retrieveExpenses(PARENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Expenses>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Expenses> expenses) {
                        Log.d(TAG, "onNext: Expenses size: " + expenses.size());
                        ExpensesAdapter expensesAdapter = new ExpensesAdapter(ExpensesListActivity.this, expenses);
                        expenseslist_recyclerview.setAdapter(expensesAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                    }
                });
    }
}
