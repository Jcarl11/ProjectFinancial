package com.example.ratio;

import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

public interface BaseDialog {

    void showDialog(Context context, String title, String[] source);
    DialogInterface.OnClickListener setPositiveButton();
    DialogInterface.OnClickListener setNegativeButton();

}
