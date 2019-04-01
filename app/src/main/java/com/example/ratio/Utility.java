package com.example.ratio;

import android.app.AlertDialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;

public class Utility {
    private static final Utility ourInstance = new Utility();
    public static Utility getInstance() {
        return ourInstance;
    }
    private Utility() {}

    public AlertDialog showLoading(Context context, String message, boolean cancellable) {
        AlertDialog dialog = new SpotsDialog.Builder()
                .setMessage(message)
                .setContext(context)
                .setCancelable(cancellable)
                .build();
        return dialog;
    }
}
