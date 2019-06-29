package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetAverage;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Entities.Income;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.CurrencyFormat;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ExpensesObservable;
import com.example.ratio.RxJava.IncomeObservable;
import com.example.ratio.RxJava.RecievablesObservable;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private static final String TAG = "ReportsActivity";
    @BindView(R.id.reports_piechart) PieChart reports_piechart;
    @BindView(R.id.reports_projectcode) TextInputLayout reports_projectcode;
    @BindView(R.id.reports_projectname) TextInputLayout reports_projectname;
    @BindView(R.id.reports_projectowner) TextInputLayout reports_projectowner;
    @BindView(R.id.reports_createdAt) TextInputLayout reports_createdAt;
    @BindView(R.id.reports_totalincome) TextInputLayout reports_totalincome;
    @BindView(R.id.reports_toolbar) Toolbar reports_toolbar;
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private BaseDAO<Projects> projectsBaseDAO = daoFactory.getProjectDAO();
    private BaseDAO<Income> incomeBaseDAO = daoFactory.getIncomeDAO();
    private BaseDAO<Expenses> expensesBaseDAO = daoFactory.getExpensesDAO();
    private BaseDAO<Receivables> receivablesBaseDAO = daoFactory.getRecievablesDAO();
    private NukeOperations<Income> incomeNukeOperations = (NukeOperations<Income>) daoFactory.getIncomeDAO();
    private NukeOperations<Expenses> expensesNukeOperations = (NukeOperations<Expenses>) daoFactory.getIncomeDAO();
    private NukeOperations<Receivables> receivablesNukeOperations = (NukeOperations<Receivables>) daoFactory.getIncomeDAO();
    private GetAverage<Income> incomeGetAverage = (GetAverage<Income>) daoFactory.getIncomeDAO();
    private GetAverage<Expenses> expensesGetAverage = (GetAverage<Expenses>) daoFactory.getExpensesDAO();
    private GetAverage<Receivables> receivablesGetAverage = (GetAverage<Receivables>) daoFactory.getRecievablesDAO();
    private IncomeObservable incomeObservable = new IncomeObservable();
    private ExpensesObservable expensesObservable = new ExpensesObservable();
    private RecievablesObservable recievablesObservable = new RecievablesObservable();
    private AlertDialog dialog = null;
    private BasicDialog basicDialog = null;
    private List<Income> incomeList = new ArrayList<>();
    private List<Expenses> expensesList = new ArrayList<>();
    private List<Receivables> receivablesList = new ArrayList<>();
    private CurrencyFormat currencyFormat = new CurrencyFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        ButterKnife.bind(this);
        setSupportActionBar(reports_toolbar);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        basicDialog = new BasicDialog(this);
        Observable.zip(incomeObservable.retrieveAllIncome().subscribeOn(Schedulers.io()),
                expensesObservable.retrieveAllExpenses().subscribeOn(Schedulers.io()),
                recievablesObservable.retrieveAllReceivables().subscribeOn(Schedulers.io()), new Function3<List<Income>, List<Expenses>, List<Receivables>, Integer>() {
                    @Override
                    public Integer apply(List<Income> incomes, List<Expenses> expenses, List<Receivables> receivables) throws Exception {
                        Log.d(TAG, "apply: Income size: " + incomes.size());
                        Log.d(TAG, "apply: Expenses size: " + expenses.size());
                        Log.d(TAG, "apply: Receivables size: " + receivables.size());
                        int income_deleted = incomeNukeOperations.deleteRows();
                        int expenses_deleted = expensesNukeOperations.deleteRows();
                        int receivables_deleted = receivablesNukeOperations.deleteRows();
                        Log.d(TAG, "apply: Income deleted: " + income_deleted);
                        Log.d(TAG, "apply: Receivables deleted: " + receivables_deleted);
                        Log.d(TAG, "apply: Expenses deleted: " + expenses_deleted);
                        int income_result = incomeBaseDAO.insertAll(incomes);
                        int expenses_result = expensesBaseDAO.insertAll(expenses);
                        int receivables_result = receivablesBaseDAO.insertAll(receivables);
                        incomeList = incomeGetAverage.getTopHighest(5);
                        expensesList = expensesGetAverage.getTopHighest(5);
                        receivablesList = receivablesGetAverage.getTopHighest(5);
                        int failedOperations = (incomes.size() + expenses.size() + receivables.size()) - (income_result + expenses_result + receivables_result);
                        Log.d(TAG, "apply: Failed operations: " + failedOperations);
                        return failedOperations;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: Result: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                        basicDialog.setTitle("Error");
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.setCancellable(true);
                        basicDialog.setPositiveText("OK");
                        basicDialog.setNegativeText("");
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        Log.d(TAG, "onComplete: Completed");
                    }
                });
        reports_piechart.setRotationEnabled(true);
        reports_piechart.setHoleRadius(25f);
        reports_piechart.setTransparentCircleRadius(33f);
        reports_piechart.setTransparentCircleAlpha(110);
        reports_piechart.setEntryLabelColor(Color.BLACK);
        reports_piechart.setOnChartValueSelectedListener(ReportsActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reportsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.reports_1:
                reports_toolbar.setTitle("Report 1");
                Log.d(TAG, "onOptionsItemSelected: Report 1");
                ArrayList<PieEntry> incomePieEntry = new ArrayList<>();
                for(int i = 0; i < incomeList.size(); i++){
                    String parent = incomeList.get(i).getParent();
                    String amount = incomeList.get(i).getAmount();
                    Log.d(TAG, "populatePie: Parent: " + parent);
                    Log.d(TAG, "populatePie: Amount: " + amount);
                    incomePieEntry.add(new PieEntry(Float.parseFloat(amount) , projectsBaseDAO.get(parent).getProjectCode()));
                }
                reports_piechart.setData(populatePie(incomePieEntry));
                reports_piechart.invalidate();
                break;
            case R.id.reports_2:
                reports_toolbar.setTitle("Report 2");
                Log.d(TAG, "onOptionsItemSelected: Report 2");
                ArrayList<PieEntry> expensesPieEntry = new ArrayList<>();
                for(int i = 0; i < expensesList.size(); i++){
                    String parent = expensesList.get(i).getParent();
                    String amount = expensesList.get(i).getAmount();
                    Log.d(TAG, "populatePie: Parent: " + parent);
                    Log.d(TAG, "populatePie: Amount: " + amount);
                    expensesPieEntry.add(new PieEntry(Float.parseFloat(amount) , projectsBaseDAO.get(parent).getProjectCode()));
                }
                reports_piechart.setData(populatePie(expensesPieEntry));
                reports_piechart.invalidate();
                break;
            case R.id.reports_3:
                reports_toolbar.setTitle("Report 3");
                Log.d(TAG, "onOptionsItemSelected: Report 3");
                ArrayList<PieEntry> receivablesPieEntry = new ArrayList<>();
                for(int i = 0; i < receivablesList.size(); i++){
                    String parent = receivablesList.get(i).getParent();
                    String amount = receivablesList.get(i).getAmount();
                    Log.d(TAG, "populatePie: Parent: " + parent);
                    Log.d(TAG, "populatePie: Amount: " + amount);
                    receivablesPieEntry.add(new PieEntry(Float.parseFloat(amount) , projectsBaseDAO.get(parent).getProjectCode()));
                }
                reports_piechart.setData(populatePie(receivablesPieEntry));
                reports_piechart.invalidate();
                break;
        }
        return true;
    }

    private PieData populatePie(ArrayList<PieEntry> pieEntries) {
        Log.d(TAG, "populatePie: Populate start");


        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Projects");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#768bab"));
        colors.add(Color.parseColor("#bdae8c"));
        colors.add(Color.parseColor("#92b87f"));
        colors.add(Color.parseColor("#84bdbd"));
        colors.add(Color.parseColor("#b18bb3"));

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: Entry: " + e.getY());
        Log.d(TAG, "onValueSelected: Highlight: " + h.getX());
        Income income = incomeList.get((int)h.getX());
        Projects projects = projectsBaseDAO.get(income.getParent());
        reports_projectcode.getEditText().setText(projects.getProjectCode());
        reports_projectname.getEditText().setText(projects.getProjectName());
        reports_projectowner.getEditText().setText(projects.getProjectOwner());
        reports_createdAt.getEditText().setText(projects.getCreatedAt());
        reports_totalincome.getEditText().setText(currencyFormat.toPhp(Double.parseDouble(income.getAmount())));
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "onNothingSelected: Nothing selected");
    }
}
