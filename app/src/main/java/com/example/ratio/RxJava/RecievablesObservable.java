package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Recievables;
import com.example.ratio.Enums.DATABASES;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class RecievablesObservable {
    private static final String TAG = "RecievablesObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Recievables> recievablesBaseDAO = daoFactory.getRecievablesDAO();

    public Observable<Recievables> recievablesObservable(Recievables recievables) {
        Observable<Recievables> observable = Observable.defer(new Callable<ObservableSource<? extends Recievables>>() {
            @Override
            public ObservableSource<? extends Recievables> call() throws Exception {
                Recievables myRecievables = recievablesBaseDAO.insert(recievables);
                return Observable.just(myRecievables);
            }
        });
        return observable;
    }
}
