package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class UserObservable {
    private static final String TAG = "UserObservable";
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<User> userBaseDAO = daoFactory.getUserDAO();
    private UserOperations<User> userOperations = (UserOperations<User>) daoFactory.getUserDAO();

    public Observable<User> registerUser(User userObject) {
        Observable<User> observable = Observable.defer(new Callable<ObservableSource<? extends User>>() {
            @Override
            public ObservableSource<? extends User> call() throws Exception {
                User user = userBaseDAO.insert(userObject);
                return Observable.just(user);
            }
        });
        return observable;
    }

    public Observable<User> loginUser(User user) {
        Observable<User> observable = Observable.defer(new Callable<ObservableSource<? extends User>>() {
            @Override
            public ObservableSource<? extends User> call() throws Exception {
                User myUser = userOperations.loginUser(user);
                return Observable.just(myUser);
            }
        });
        return observable;
    }

    public Observable<Integer> deleteUser(String objectId) {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                int result = userBaseDAO.delete(userBaseDAO.get(objectId));
                return Observable.just(result);
            }
        });
        return observable;
    }
}
