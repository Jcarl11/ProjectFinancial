package com.example.ratio.Dialogs;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public abstract class BaseDialog {
    protected String title = "Title";
    protected String message = "Message";
    protected boolean cancellable = true;
    protected String[] sourceList = null;
    protected String positiveText = "OK";
    protected String negativeText = "NO";
    protected AlertDialog.Builder dialogBuilder = null;
    protected Context context;

    public BaseDialog(Context context) {
        this.context = context;
        dialogBuilder = new AlertDialog.Builder(context);
    }

    public void showDialog(){
        dialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable).show();
    }

    protected abstract DialogInterface.OnClickListener positiveButton();
    protected abstract DialogInterface.OnClickListener negativeButton();

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public void setSourceList(String[] sourceList) {
        this.sourceList = sourceList;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }
}
