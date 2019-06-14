package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Enums.DATABASES;

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

}
