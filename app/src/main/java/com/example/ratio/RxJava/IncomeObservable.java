package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Income;
import com.example.ratio.Enums.DATABASES;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class IncomeObservable {

    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Income> incomeBaseDAO = daoFactory.getIncomeDAO();

    public Observable<Income> insertIncome(Income income) {
        Observable<Income> observable = Observable.defer(new Callable<ObservableSource<? extends Income>>() {
            @Override
            public ObservableSource<? extends Income> call() throws Exception {
                Income myIncome = incomeBaseDAO.insert(income);
                return Observable.just(myIncome);
            }
        });
        return observable;
    }
}
