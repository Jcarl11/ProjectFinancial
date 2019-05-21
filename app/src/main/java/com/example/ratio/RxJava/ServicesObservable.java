package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.DATABASES;
import java.util.List;
import io.reactivex.Observable;

public class ServicesObservable {
    private static final String TAG = "ServicesObservable";
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Services> servicesBaseDAO;

    public ServicesObservable() {
        servicesBaseDAO = parseFactory.getServicesDAO();
    }

    public Observable<List<Services>> servicesListObs() {
        Observable<List<Services>> observable = Observable.defer(() -> {
            List<Services> servicesList = servicesBaseDAO.getBulk(null);
            return Observable.just(servicesList);
        });
        return observable;
    }
}
