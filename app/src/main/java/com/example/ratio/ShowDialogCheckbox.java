package com.example.ratio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowDialogCheckbox implements BaseDialog {
    AutoLabelUI autoLabelUI = null;
    AlertDialog.Builder dialog;
    Context context;
    ArrayList<String> selectedValues = new ArrayList<>();
    @Override
    public void showDialog(Context context, String title, String[] source) {
        this.context = context;
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        dialog.setPositiveButton("Confirm", setPositiveButton());
        dialog.setNegativeButton("Cancel", setNegativeButton());
        dialog.setMultiChoiceItems(source, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    selectedValues.add(source[which]);
                } else if(selectedValues.contains(source[which])) {
                    selectedValues.remove(which);
                }
            }
        });
        Dialog myDialog = dialog.create();
        myDialog.show();

    }

    @Override
    public DialogInterface.OnClickListener setPositiveButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(autoLabelUI != null) {
                    autoLabelUI.clear();
                    for(String value : selectedValues) {
                        autoLabelUI.addLabel(value);
                    }
                }
            }
        };
        return listener;
    }

    @Override
    public DialogInterface.OnClickListener setNegativeButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        return listener;
    }

    public void setAutoLabelUI(AutoLabelUI autoLabelUI) {
        this.autoLabelUI = autoLabelUI;
    }
}
