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

import com.example.ratio.Adapters.IncomeAdapter;
import com.example.ratio.Entities.Income;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.IncomeObservable;

import java.util.List;

public class IncomeListActivity extends AppCompatActivity {
    private static final String TAG = "IncomeListActivity";
    @BindView(R.id.incomelist_recyclerview) RecyclerView incomelist_recyclerview;
    private String PARENT_ID = null;
    private IncomeObservable incomeObservable = new IncomeObservable();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(FragmentPortfolio.PARENTID);
        getSupportActionBar().setTitle(String.format("Income for %s", PARENT_ID));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        incomelist_recyclerview.setHasFixedSize(true);
        incomelist_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        incomeObservable.retrieveIncome(PARENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Income>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Income> incomes) {
                        Log.d(TAG, "onNext: Income size: " + incomes.size());
                        IncomeAdapter adapter = new IncomeAdapter(IncomeListActivity.this, incomes);
                        incomelist_recyclerview.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
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
