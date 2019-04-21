package com.example.ratio.DAO;

import com.example.ratio.DAO.Parse.ParseFactory;
import com.example.ratio.DAO.Sqlite.SqliteFactory;
import com.example.ratio.Enums.DATABASES;

public abstract class DAOFactory {
    public abstract BaseDAO getUserDAO();
    public abstract BaseDAO getProjectDAO();
    public abstract BaseDAO getServicesDAO();
    public abstract BaseDAO getImageDAO();
    public abstract BaseDAO getRecievablesDAO();
    public abstract BaseDAO getIncomeDAO();
    public abstract BaseDAO getExpensesDAO();
    public abstract BaseDAO getStatusDAO();
    public abstract BaseDAO getProjectTypeDAO();
    public abstract BaseDAO getSubcategoryDAO();

    public static DAOFactory getDatabase(DATABASES db){
        DAOFactory daoFactory = null;
        switch (db){
            case PARSE:
                daoFactory = new ParseFactory();
                break;
            case SQLITE:
                daoFactory = new SqliteFactory();
                break;
        }
        return daoFactory;
    }

}
