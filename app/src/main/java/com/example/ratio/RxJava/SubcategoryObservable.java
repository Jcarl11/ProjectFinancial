package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class SubcategoryObservable {
    private static final String TAG = "SubcategoryObservable";
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Subcategory> subcategoryBaseDAO;

    public SubcategoryObservable() {
        subcategoryBaseDAO = parseFactory.getSubcategoryDAO();
    }

    public Observable<List<Subcategory>> subcategoryObservable() {
        Observable<List<Subcategory>> observable = Observable.defer(() -> {
            List<Subcategory> subcategoryList = subcategoryBaseDAO.getBulk(null);
            return Observable.just(subcategoryList);
        });
        return observable;
    }
}
