package com.example.ratio.DAO;

public interface UserOperations<T> {

    T loginUser(T userObject);
    void logoutUser();

}
