package com.example.ratio.RxJava;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetDistinct;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class StatusObservable {
    private static final String TAG = "StatusObservable";
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private GetDistinct<Status> statusBaseDAO;

    public StatusObservable() {
        statusBaseDAO = (GetDistinct<Status>) parseFactory.getStatusDAO();
    }

    public Observable<List<Status>> statusObservable() {
        Observable<List<Status>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Status>>>() {
            @Override
            public ObservableSource<? extends List<Status>> call() throws Exception {
                List<Status> statuses = statusBaseDAO.getDistinct();
                Log.d(TAG, "call: statuses size: " + statuses.size());
                return Observable.just(statuses);
            }
        });
        return observable;
    }
}
