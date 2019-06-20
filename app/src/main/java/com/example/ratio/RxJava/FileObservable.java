package com.example.ratio.RxJava;

import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class FileObservable {
    private static final String TAG = "FileObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private GetFromParent<Pdf> pdfGetFromParent = (GetFromParent<Pdf>) daoFactory.getFileDAO();

    public Observable<List<Pdf>> retrieveFileFromParent(String parent) {
        Observable<List<Pdf>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Pdf>>>() {
            @Override
            public ObservableSource<? extends List<Pdf>> call() throws Exception {
                List<Pdf> pdfList = pdfGetFromParent.getObjects(parent);
                return Observable.just(pdfList);
            }
        });
        return observable;
    }
}
