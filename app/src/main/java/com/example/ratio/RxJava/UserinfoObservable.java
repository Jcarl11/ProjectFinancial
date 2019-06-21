package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.GetFromPosition;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class UserinfoObservable {
    private static final String TAG = "UserinfoObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Userinfo> userinfoBaseDAO = daoFactory.getUserinfoDAO();
    private GetFromParent<Userinfo> userinfoGetFromParent = (GetFromParent<Userinfo>) daoFactory.getUserinfoDAO();
    private GetFromPosition<Userinfo> userinfoGetFromPosition = (GetFromPosition<Userinfo>) daoFactory.getUserinfoDAO();

    public Observable<Userinfo> insertInfo(Userinfo userinfo) {
        Observable<Userinfo> observable = Observable.defer(new Callable<ObservableSource<? extends Userinfo>>() {
            @Override
            public ObservableSource<? extends Userinfo> call() throws Exception {
                Userinfo myInfo = userinfoBaseDAO.insert(userinfo);
                return Observable.just(myInfo);
            }
        });
        return observable;
    }

    public Observable<List<Userinfo>> retrieveUsers(String status){
        Observable<List<Userinfo>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Userinfo>>>() {
            @Override
            public ObservableSource<? extends List<Userinfo>> call() throws Exception {
                List<Userinfo> userinfoList = userinfoGetFromPosition.getUsers(status);
                return Observable.just(userinfoList);
            }
        });
        return observable;
    }
}
