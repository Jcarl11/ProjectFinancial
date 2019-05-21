package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class ProjectTypeObservable {
    private static final String TAG = "ProjectTypeObservable";
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<ProjectType> projectTypeBaseDAO;

    public ProjectTypeObservable() {
        projectTypeBaseDAO = parseFactory.getProjectTypeDAO();
    }

    public Observable<List<ProjectType>> projectTypeObservable() {
        Observable<List<ProjectType>> observable = Observable.defer(new Callable<ObservableSource<? extends List<ProjectType>>>() {
            @Override
            public ObservableSource<? extends List<ProjectType>> call() throws Exception {
                List<ProjectType> projectTypeList = projectTypeBaseDAO.getBulk(null);
                return Observable.just(projectTypeList);
            }
        });
        return observable;
    }
}
