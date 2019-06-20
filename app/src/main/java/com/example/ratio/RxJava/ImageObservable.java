package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Image;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class ImageObservable {
    private static final String TAG = "ImageObservable";

    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Image> imageBaseDAO = daoFactory.getImageDAO();
    private GetFromParent<Image> imageGetFromParent = (GetFromParent<Image>) daoFactory.getImageDAO();

    public Observable<List<Image>> retrieveImageFromParent(String parentid) {
        Observable<List<Image>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Image>>>() {
            @Override
            public ObservableSource<? extends List<Image>> call() throws Exception {
                List<Image> imageList = imageGetFromParent.getObjects(parentid);
                return Observable.just(imageList);
            }
        });
        return observable;
    }
}
