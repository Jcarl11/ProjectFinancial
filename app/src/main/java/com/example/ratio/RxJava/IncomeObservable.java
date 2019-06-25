package com.example.ratio.RxJava;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Income;
import com.example.ratio.Enums.DATABASES;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public class IncomeObservable {

    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Income> incomeBaseDAO = daoFactory.getIncomeDAO();
    private GetFromParent<Income> incomeGetFromParent = (GetFromParent<Income>) daoFactory.getIncomeDAO();

    public Observable<Income> insertIncome(Income income) {
        Observable<Income> observable = Observable.defer(new Callable<ObservableSource<? extends Income>>() {
            @Override
            public ObservableSource<? extends Income> call() throws Exception {
                Income myIncome = incomeBaseDAO.insert(income);
                return Observable.just(myIncome);
            }
        });
        return observable;
    }

    public Observable<List<Income> > retrieveIncome(String parent) {
        Observable<List<Income>> observable = Observable.defer(new Callable<ObservableSource<? extends List<Income>>>() {
            @Override
            public ObservableSource<? extends List<Income>> call() throws Exception {
                List<Income> incomeList = incomeGetFromParent.getObjects(parent);
                return Observable.just(incomeList);
            }
        });
        return observable;
    }

    public Observable<Integer> deleteIncome(Income income) {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                int result = incomeBaseDAO.delete(income);
                return Observable.just(result);
            }
        });
        return observable;
    }
}
