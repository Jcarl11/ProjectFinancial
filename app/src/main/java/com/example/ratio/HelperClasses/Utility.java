package com.example.ratio.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import dmax.dialog.SpotsDialog;

public class Utility {
    private static final Utility ourInstance = new Utility();
    public static Utility getInstance() {
        return ourInstance;
    }
    private Utility() {}

    private static final String TAG = "Utility";
    public AlertDialog showLoading(Context context, String message, boolean cancellable) {
        AlertDialog dialog = new SpotsDialog.Builder()
                .setMessage(message)
                .setContext(context)
                .setCancelable(cancellable)
                .build();
        return dialog;
    }
    public boolean checkIfInteger(String input){
        if(input == null){
            return false;
        }
        boolean isInteger = false;
        try{
            Integer.parseInt(input);
            isInteger = true;
        }catch(Exception ex){
            Log.d(TAG, "checkIfInteger: Exception thrown: " + ex.getMessage());
        }
        return isInteger;
    }
}
