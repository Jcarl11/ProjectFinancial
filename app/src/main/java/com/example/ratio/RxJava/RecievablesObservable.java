package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class RecievablesObservable {
    private static final String TAG = "RecievablesObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Receivables> recievablesBaseDAO = daoFactory.getRecievablesDAO();
    private GetFromParent<Receivables> receivablesGetFromParent = (GetFromParent<Receivables>) daoFactory.getRecievablesDAO();

    public Observable<Receivables> recievablesObservable(Receivables receivables) {
        Observable<Receivables> observable = Observable.defer(new Callable<ObservableSource<? extends Receivables>>() {
            @Override
            public ObservableSource<? extends Receivables> call() throws Exception {
                Receivables myReceivables = recievablesBaseDAO.insert(receivables);
                return Observable.just(myReceivables);
            }
        });
        return observable;
    }

    public Observable<List<Receivables>> retrieveReceivables(String parent) {
        Observable<List<Receivables>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Receivables>>>() {
            @Override
            public ObservableSource<? extends List<Receivables>> call() throws Exception {
                List<Receivables> recievables = receivablesGetFromParent.getObjects(parent);
                return Observable.just(recievables);
            }
        });
        return observable;
    }

    public Observable<Integer> deleteReceivable(Receivables receivables) {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                int result = recievablesBaseDAO.delete(receivables);
                return Observable.just(result);
            }
        });
        return observable;
    }

    public Observable<List<Receivables>> retrieveAllReceivables() {
        Observable<List<Receivables>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Receivables>>>() {
            @Override
            public ObservableSource<? extends List<Receivables>> call() throws Exception {
                List<Receivables> recievables = recievablesBaseDAO.getBulk("1000");
                return Observable.just(recievables);
            }
        });
        return observable;
    }
}
