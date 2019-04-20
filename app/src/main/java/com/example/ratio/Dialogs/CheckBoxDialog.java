package com.example.ratio.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import com.dpizarro.autolabel.library.AutoLabelUI;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckBoxDialog extends BaseDialog {

    private AutoLabelUI autoLabelUI = null;
    private ArrayList<String> selectedValues = new ArrayList<>();

    public CheckBoxDialog(Context context) {
        super(context);
    }

    @Override
    public void showDialog() {
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(cancellable);
        dialogBuilder.setPositiveButton(positiveText, positiveButton());
        dialogBuilder.setNegativeButton(negativeText, negativeButton());
        dialogBuilder.setMultiChoiceItems(sourceList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    selectedValues.add(sourceList[which]);
                } else if(selectedValues.contains(sourceList[which])) {
                    selectedValues.remove(which);
                }
            }
        });
        dialogBuilder.show();
    }

    @Override
    public DialogInterface.OnClickListener positiveButton() {
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

}
