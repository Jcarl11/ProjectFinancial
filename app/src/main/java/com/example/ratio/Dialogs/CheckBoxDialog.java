package com.example.ratio.Dialogs;

import android.content.Context;
import android.content.DialogInterface;

import com.dpizarro.autolabel.library.AutoLabelUI;

import java.util.ArrayList;

public class CheckBoxDialog extends BaseDialog {
    private static final String TAG = "CheckBoxDialog";
    private AutoLabelUI autoLabelUI = null;
    private ArrayList<String> selectedValues = new ArrayList<>();
    private boolean[] selectedPositions = null;
    public CheckBoxDialog(Context context) {
        super(context);
    }

    @Override
    public void showDialog() {
        if(selectedPositions == null){
            selectedPositions = new boolean[sourceList.length];
        }
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(cancellable);
        dialogBuilder.setPositiveButton(positiveText, positiveButton());
        dialogBuilder.setNegativeButton(negativeText, negativeButton());
        dialogBuilder.setMultiChoiceItems(sourceList, selectedPositions, (dialog, which, isChecked) -> {
            if(isChecked) {
                selectedValues.add(sourceList[which]);
                selectedPositions[which] = true;
            } else if(selectedValues.contains(sourceList[which])) {
                selectedValues.remove(which);
            }
        });
        dialogBuilder.show();
    }

    @Override
    public DialogInterface.OnClickListener positiveButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        return listener;
    }

    @Override
    public DialogInterface.OnClickListener negativeButton() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return listener;
    }

    public void setAutoLabelUI(AutoLabelUI autoLabelUI) {
        this.autoLabelUI = autoLabelUI;
    }

    public ArrayList<String> getSelectedValues() {
        return selectedValues;
    }
}
