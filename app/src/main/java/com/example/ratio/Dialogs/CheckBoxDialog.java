package com.example.ratio.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import com.dpizarro.autolabel.library.AutoLabelUI;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckBoxDialog extends BaseDialog {
    AutoLabelUI autoLabelUI = null;
    ArrayList<String> selectedValues = new ArrayList<>();
    public CheckBoxDialog(Context context, String title, String[] sourceList) {
        super(context, title, sourceList);
    }

    @Override
    public void showDialog() {
        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton("OK", positiveButton());
        dialogBuilder.setNegativeButton("CANCEL", negativeButton());
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
