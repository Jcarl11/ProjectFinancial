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

import com.example.ratio.Adapters.ReceivablesAdapter;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.RecievablesObservable;

import java.util.List;

public class ReceivablesListActivity extends AppCompatActivity {
    private static final String TAG = "ReceivablesListActivity";
    @BindView(R.id.receivableslist_recyclerview) RecyclerView receivableslist_recyclerview;
    private String PARENT_ID = null;
    private String PARENT_CODE = null;
    private RecievablesObservable recievablesObservable = new RecievablesObservable();
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivables_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        PARENT_CODE = getIntent().getStringExtra(Constant.PARENTCODE);
        getSupportActionBar().setTitle(String.format("Receivables for %s", PARENT_CODE));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        receivableslist_recyclerview.setHasFixedSize(true);
        receivableslist_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recievablesObservable.retrieveReceivables(PARENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Receivables>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Receivables> recievables) {
                        Log.d(TAG, "onNext: Receivables size: " + recievables.size());
                        ReceivablesAdapter receivablesAdapter = new ReceivablesAdapter(ReceivablesListActivity.this, recievables);
                        receivableslist_recyclerview.setAdapter(receivablesAdapter);
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
