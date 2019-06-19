package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class ExpensesObservable {
    private static final String TAG = "ExpensesObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Expenses> expensesBaseDAO = daoFactory.getExpensesDAO();

    public Observable<Expenses> insertExpenses(Expenses expenses) {
        Observable<Expenses> observable = Observable.defer(new Callable<ObservableSource<? extends Expenses>>() {
            @Override
            public ObservableSource<? extends Expenses> call() throws Exception {
                Expenses myExpenses = expensesBaseDAO.insert(expenses);
                return Observable.just(myExpenses);
            }
        });
        return observable;
    }

    public Observable<List<Expenses>> retrieveExpenses(String parent) {
        Observable<List<Expenses>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Expenses>>>() {
            @Override
            public ObservableSource<? extends List<Expenses>> call() throws Exception {
                List<Expenses> expensesList = expensesBaseDAO.getBulk("1000");
                return Observable.just(expensesList);
            }
        });
        return observable;
    }

}
