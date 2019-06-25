package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
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
    private BaseDAO<Pdf> pdfBaseDAO = daoFactory.getFileDAO();
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

    public Observable<Pdf> updatedFile(Pdf newRecord) {
        Observable<Pdf> observable = Observable.defer(new Callable<ObservableSource<? extends Pdf>>() {
            @Override
            public ObservableSource<? extends Pdf> call() throws Exception {
                Pdf pdf = pdfBaseDAO.update(newRecord);
                return Observable.just(pdf);
            }
        });
        return observable;
    }

    public Observable<Integer> deleteFile(String parentID) {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                int result = pdfBaseDAO.deleteAll(pdfGetFromParent.getObjects(parentID));
                return Observable.just(result);
            }
        });
        return observable;
    }
}
