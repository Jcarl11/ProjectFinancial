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

import com.example.ratio.Adapters.ExpensesAdapter;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ExpensesObservable;
import com.example.ratio.RxJava.FileObservable;
import com.example.ratio.RxJava.ImageObservable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpensesListActivity extends AppCompatActivity {
    private static final String TAG = "ExpensesListActivity";
    @BindView(R.id.expenseslist_recyclerview) RecyclerView expenseslist_recyclerview;
    private String PARENT_ID = null;
    private String PARENT_CODE = null;
    private ExpensesObservable expensesObservable = new ExpensesObservable();
    private ImageObservable imageObservable = new ImageObservable();
    private FileObservable fileObservable = new FileObservable();
    private AlertDialog dialog;
    private int pos = -1;
    private List<Expenses> expensesList = new ArrayList<>();
    private BasicDialog basicDialog = null;
    private CurrencyFormat currencyFormat = new CurrencyFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        PARENT_CODE = getIntent().getStringExtra(Constant.PARENTCODE);
        getSupportActionBar().setTitle(String.format("Expenses for %s", PARENT_CODE));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        basicDialog = new BasicDialog(this);
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
                        expensesList = expenses;

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
                        getSupportActionBar().setTitle(String.format("Total expenses: %s", currencyFormat.toPhp(getTotalIncome(expensesList))));
                        ExpensesAdapter expensesAdapter = new ExpensesAdapter(ExpensesListActivity.this, expensesList);
                        expensesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                getSupportActionBar().setTitle(String.format("Total income: %s", currencyFormat.toPhp(getTotalIncome(expensesList))));
                            }
                        });
                        expenseslist_recyclerview.setAdapter(expensesAdapter);
                    }
                });
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(expenseslist_recyclerview, swipeCallBack())
                .setIsVertical(false)
                .setItemTouchCallback(new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                    @Override
                    public void onTouch(int position) {

                    }
                })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        String[] choices = new String[]{"Show full description", "Show image attachments", "Show file attachments", "Delete"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ExpensesListActivity.this);
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
                                        basicDialog.setMessage(expensesList.get(pos).getDescription());
                                        basicDialog.setPositiveText("OK");
                                        basicDialog.setNegativeText("");
                                        basicDialog.showDialog();
                                        break;
                                    case 1:
                                        Intent showImageAttachments = new Intent(ExpensesListActivity.this, ImageAttachmentsActivity.class);
                                        showImageAttachments.putExtra(Constant.PARENTID, expensesList.get(pos).getObjectId());
                                        startActivity(showImageAttachments);
                                        break;
                                    case 2:
                                        Intent showFileAttachments = new Intent(ExpensesListActivity.this, FileAttachmentsActivity.class);
                                        showFileAttachments.putExtra(Constant.PARENTID, expensesList.get(pos).getObjectId());
                                        startActivity(showFileAttachments);
                                        break;
                                    case 3:
                                        deleteExpenses(expensesList.get(0));
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        expenseslist_recyclerview.setOnTouchListener(listener);
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

    private void deleteExpenses (Expenses expenses) {
        imageObservable.deleteImage(expenses.getObjectId())
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return fileObservable.deleteFile(s);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        return expensesObservable.deleteExpenses(expenses);
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
                        basicDialog.setTitle("");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        dialog.dismiss();
                        expensesList.remove(pos);
                        expenseslist_recyclerview.getAdapter().notifyItemRemoved(pos);
                        expenseslist_recyclerview.getAdapter().notifyDataSetChanged();
                        basicDialog.setTitle("");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage("Record deleted");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }
                });
    }
    private double getTotalIncome(List<Expenses> sourceList) {
        double total = 0;
        for (Expenses expenses : sourceList) {
            total += Double.parseDouble(expenses.getAmount());
        }

        return total;
    }
}

