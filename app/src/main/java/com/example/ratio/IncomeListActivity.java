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
import android.widget.Toast;

import com.example.ratio.Adapters.IncomeAdapter;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Income;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.FileObservable;
import com.example.ratio.RxJava.ImageObservable;
import com.example.ratio.RxJava.IncomeObservable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncomeListActivity extends AppCompatActivity {
    private static final String TAG = "IncomeListActivity";
    @BindView(R.id.incomelist_recyclerview) RecyclerView incomelist_recyclerview;
    private String PARENT_ID = null;
    private String PARENT_CODE = null;
    private IncomeObservable incomeObservable = new IncomeObservable();
    private ImageObservable imageObservable = new ImageObservable();
    private FileObservable fileObservable = new FileObservable();
    private AlertDialog dialog;
    private int pos = -1;
    private List<Income> incomeList = new ArrayList<>();
    private BasicDialog basicDialog = null;
    private CurrencyFormat currencyFormat = new CurrencyFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        PARENT_CODE = getIntent().getStringExtra(Constant.PARENTCODE);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        incomelist_recyclerview.setHasFixedSize(true);
        incomelist_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        basicDialog = new BasicDialog(this);
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
                        incomeList = incomes;
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
                        getSupportActionBar().setTitle(String.format("Total income: %s", currencyFormat.toPhp(getTotalIncome(incomeList))));
                        IncomeAdapter adapter = new IncomeAdapter(IncomeListActivity.this, incomeList);
                        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                getSupportActionBar().setTitle(String.format("Total income: %s", currencyFormat.toPhp(getTotalIncome(incomeList))));
                            }
                        });
                        incomelist_recyclerview.setAdapter(adapter);
                    }
                });
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(incomelist_recyclerview, swipeCallBack())
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(IncomeListActivity.this);
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
                                        basicDialog.setMessage(incomeList.get(pos).getDescription());
                                        basicDialog.setPositiveText("OK");
                                        basicDialog.setNegativeText("");
                                        basicDialog.showDialog();
                                        break;
                                    case 1:
                                        Intent showImageAttachments = new Intent(IncomeListActivity.this, ImageAttachmentsActivity.class);
                                        showImageAttachments.putExtra(Constant.PARENTID, incomeList.get(pos).getObjectId());
                                        startActivity(showImageAttachments);
                                        break;
                                    case 2:
                                        Intent showFileAttachments = new Intent(IncomeListActivity.this, FileAttachmentsActivity.class);
                                        showFileAttachments.putExtra(Constant.PARENTID, incomeList.get(pos).getObjectId());
                                        startActivity(showFileAttachments);
                                        break;
                                    case 3:
                                        deleteIncome(incomeList.get(pos));
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
                incomelist_recyclerview.setOnTouchListener(listener);
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
    private void deleteIncome(Income income) {
        imageObservable.deleteImage(income.getObjectId())
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return fileObservable.deleteFile(s);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        return incomeObservable.deleteIncome(income);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed..");
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
                        Log.d(TAG, "onComplete: Completed...");
                        dialog.dismiss();
                        incomeList.remove(pos);
                        incomelist_recyclerview.getAdapter().notifyItemRemoved(pos);
                        incomelist_recyclerview.getAdapter().notifyDataSetChanged();
                        basicDialog.setTitle("");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage("Record deleted");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }
                });
    }
    private double getTotalIncome(List<Income> sourceList) {
        double total = 0;
        for (Income income : sourceList) {
            total += Double.parseDouble(income.getAmount());
        }
        return total;
    }
}
