package com.example.ratio.RxJava;

import android.util.Log;

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

    public Observable<Image> updateImage(Image newRecord) {
        Observable<Image> observable = Observable.defer(new Callable<ObservableSource<? extends Image>>() {
            @Override
            public ObservableSource<? extends Image> call() throws Exception {
                Image image = imageBaseDAO.update(newRecord);
                return Observable.just(image);
            }
        });
        return observable;
    }

    public Observable<String> deleteImage(String parentID) {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                int result = imageBaseDAO.deleteAll(imageGetFromParent.getObjects(parentID));
                Log.d(TAG, "call: Result: " + result);
                return Observable.just(parentID);
            }
        });
        return observable;
    }
}
