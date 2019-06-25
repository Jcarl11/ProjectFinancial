package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ratio.Adapters.ReceivablesAdapter;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Income;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.FileObservable;
import com.example.ratio.RxJava.ImageObservable;
import com.example.ratio.RxJava.IncomeObservable;
import com.example.ratio.RxJava.RecievablesObservable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceivablesListActivity extends AppCompatActivity {
    private static final String TAG = "ReceivablesListActivity";
    @BindView(R.id.receivableslist_recyclerview) RecyclerView receivableslist_recyclerview;
    private String PARENT_ID = null;
    private String PARENT_CODE = null;
    private RecievablesObservable recievablesObservable = new RecievablesObservable();
    private AlertDialog dialog;
    private int pos = -1;
    private List<Receivables> receivablesList = new ArrayList<>();
    private BasicDialog basicDialog = null;
    private IncomeObservable incomeObservable = new IncomeObservable();
    private ImageObservable imageObservable = new ImageObservable();
    private FileObservable fileObservable = new FileObservable();
    private CurrencyFormat currencyFormat = new CurrencyFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivables_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        PARENT_CODE = getIntent().getStringExtra(Constant.PARENTCODE);
        getSupportActionBar().setTitle(String.format("Receivables for %s", PARENT_CODE));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        basicDialog = new BasicDialog(this);
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
                        receivablesList = recievables;
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
                        getSupportActionBar().setTitle(String.format("Total receivables: %s", currencyFormat.toPhp(getTotalIncome(receivablesList))));
                        ReceivablesAdapter receivablesAdapter = new ReceivablesAdapter(ReceivablesListActivity.this, receivablesList);
                        receivablesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                getSupportActionBar().setTitle(String.format("Total receivables: %s", currencyFormat.toPhp(getTotalIncome(receivablesList))));
                            }
                        });
                        receivableslist_recyclerview.setAdapter(receivablesAdapter);
                    }
                });
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(receivableslist_recyclerview, swipeCallBack())
                .setIsVertical(false)
                .setItemTouchCallback(new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                    @Override
                    public void onTouch(int position) {

                    }
                })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        String[] choices = new String[]{"Show full description", "Show image attachments", "Show file attachments", "Add to income", "Delete"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivablesListActivity.this);
                        builder.setTitle("Choose");
                        builder.setCancelable(true);
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: Clicked position: " + which);
                                switch (which) {
                                    case 0:
                                        dialog.dismiss();
                                        basicDialog.setTitle("");
                                        basicDialog.setCancellable(true);
                                        basicDialog.setMessage(receivablesList.get(pos).getDescription());
                                        basicDialog.setPositiveText("OK");
                                        basicDialog.setNegativeText("");
                                        basicDialog.showDialog();
                                        break;
                                    case 1:
                                        Intent showImageAttachments = new Intent(ReceivablesListActivity.this, ImageAttachmentsActivity.class);
                                        showImageAttachments.putExtra(Constant.PARENTID, receivablesList.get(pos).getObjectId());
                                        startActivity(showImageAttachments);
                                        break;
                                    case 2:
                                        Intent showFileAttachments = new Intent(ReceivablesListActivity.this, FileAttachmentsActivity.class);
                                        showFileAttachments.putExtra(Constant.PARENTID, receivablesList.get(pos).getObjectId());
                                        startActivity(showFileAttachments);
                                        break;
                                    case 3:
                                        addReceivablesToIncome(receivablesList.get(pos));
                                        break;
                                    case 4:
                                        deleteReceivable(receivablesList.get(pos));
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        receivableslist_recyclerview.setOnTouchListener(listener);
    }

    public SwipeDismissRecyclerViewTouchListener.DismissCallbacks swipeCallBack() {
        SwipeDismissRecyclerViewTouchListener.DismissCallbacks listener = new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                pos = position;
                return true;
            }

            @Override
            public void onDismiss(View view) {

            }
        };
        return listener;
    }

    private void addReceivablesToIncome(Receivables receivables) {
        Income income = new Income();
        income.setTimestamp(receivables.getTimestamp());
        income.setAttachments(receivables.isAttachments());
        income.setAmount(receivables.getAmount());
        income.setDescription(receivables.getDescription());
        income.setParent(receivables.getParent());
        incomeObservable.insertIncome(income)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Income>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Income income) {
                        Log.d(TAG, "onNext: On next: " + income.getObjectId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed...");
                        dialog.dismiss();
                        receivablesList.remove(pos);
                        receivableslist_recyclerview.getAdapter().notifyItemRemoved(pos);
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage("Record added");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }
                });
    }
    private void deleteReceivable(Receivables receivables) {
        imageObservable.deleteImage(receivables.getObjectId())
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return fileObservable.deleteFile(s);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        return recievablesObservable.deleteReceivable(receivables);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed...");
                        dialog.dismiss();
                        receivablesList.remove(pos);
                        receivableslist_recyclerview.getAdapter().notifyItemRemoved(pos);
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage("Record deleted");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }
                });
    }
    private double getTotalIncome(List<Receivables> sourceList) {
        double total = 0;
        for (Receivables receivables : sourceList) {
            total += Double.parseDouble(receivables.getAmount());
        }
        return total;
    }
}
