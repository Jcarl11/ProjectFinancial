package com.example.ratio.Dialogs;

import android.content.Context;
import android.content.DialogInterface;

public class BasicDialog extends BaseDialog {

    public BasicDialog(Context context) {
        super(context);
    }

    @Override
    public void showDialog() {
        dialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton(positiveText, positiveButton())
                .setNegativeButton(negativeText, negativeButton())
                .show();
    }

    @Override
    protected DialogInterface.OnClickListener positiveButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return listener;
    }

    @Override
    protected DialogInterface.OnClickListener negativeButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return listener;
    }
}
