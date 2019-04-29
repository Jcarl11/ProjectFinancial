package com.example.ratio.DAO;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

public interface SpecificOperations<T> {
    List<T> getObject(ParseQuery<ParseObject> customQuery);
}
