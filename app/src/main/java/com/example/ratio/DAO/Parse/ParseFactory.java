package com.example.ratio.DAO.Parse;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;

public class ParseFactory extends DAOFactory {

    @Override
    public BaseDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    public BaseDAO getProjectDAO() {
        return new ProjectDAO();
    }

    @Override
    public BaseDAO getServicesDAO() {
        return new ServicesDAO();
    }

    @Override
    public BaseDAO getImageDAO() {
        return new ImageDAO();
    }

    @Override
    public BaseDAO getRecievablesDAO() {
        return new RecievablesDAO();
    }

    @Override
    public BaseDAO getIncomeDAO() {
        return new IncomeDAO();
    }

    @Override
    public BaseDAO getExpensesDAO() {
        return new ExpensesDAO();
    }

    @Override
    public BaseDAO getStatusDAO() {
        return new StatusDAO();
    }

    @Override
    public BaseDAO getProjectTypeDAO() {
        return new ProjectDAO();
    }

    @Override
    public BaseDAO getSubcategoryDAO() {
        return new SubCategoryDAO();
    }
}
