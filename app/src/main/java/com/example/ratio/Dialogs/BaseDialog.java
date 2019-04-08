package com.example.ratio.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

public abstract class BaseDialog {
    String[] sourceList = null;
    AlertDialog.Builder dialogBuilder = null;
    Context context;
    String title;
    String message;

    public BaseDialog(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
        dialogBuilder = new AlertDialog.Builder(context);
    }

    public BaseDialog(Context context, String title, String[] sourceList) {
        this.context = context;
        this.title = title;
        this.sourceList = sourceList;
        dialogBuilder = new AlertDialog.Builder(context);
    }

    public void showDialog() {
        dialogBuilder
                .setTitle(title)
                .setCancelable(true)
                .setMessage(message);
        dialogBuilder.show();
    }

    public DialogInterface.OnClickListener positiveButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return listener;
    }
    public DialogInterface.OnClickListener negativeButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return listener;
    }

}
