package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Utility;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class AddRecievablesActivity extends AppCompatActivity {
    private static final String TAG = "AddRecievablesActivity";
    @BindView(R.id.addrecievables_field_amount) TextInputLayout addrecievables_field_amount;
    @BindView(R.id.addrecievables_field_description) TextInputLayout addrecievables_field_description;
    @BindView(R.id.addrecievables_button_attachimage) Button addrecievables_button_attachimage;
    @BindView(R.id.addrecievables_button_attachfile) Button addrecievables_button_attachfile;
    @BindView(R.id.addrecievables_files_attachments) AutoLabelUI addrecievables_files_attachments;
    @BindView(R.id.addrecievables_button_add) Button addrecievables_button_add;
    private AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recievables);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Add Receivables");
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
    }

    @OnClick(R.id.addrecievables_button_attachimage)
    public void attachImagesClicked(View view) {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowAudios(false)
                .setShowFiles(false)
                .setShowVideos(false)
                .enableImageCapture(true)
                .setMaxSelection(9)
                .build());
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.addrecievables_button_attachfile)
    public void attachFileClicked(View view) {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowAudios(false)
                .setShowFiles(true)
                .setShowVideos(false)
                .enableImageCapture(false)
                .setSuffixes("pdf")
                .setMaxSelection(9)
                .build());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: Here");
        if(resultCode != RESULT_OK && data == null) {
            Log.d(TAG, "onActivityResult: Back pressed");
            return;
        }
        if(resultCode != RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: Error");
            return;
        }
        switch (requestCode) {
            case 1:
                Log.d(TAG, "onActivityResult: Result ok");
                ArrayList<MediaFile> mediaFiles_img = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Log.d(TAG, "onActivityResult: Media files size: " + mediaFiles_img.size());
                for (MediaFile file : mediaFiles_img) {
                    addrecievables_files_attachments.addLabel(file.getPath());
                }
                break;
            case 2:
                Log.d(TAG, "onActivityResult: Result ok");
                ArrayList<MediaFile> mediaFiles_file = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Log.d(TAG, "onActivityResult: Media files size: " + mediaFiles_file.size());
                for (MediaFile file : mediaFiles_file) {
                    addrecievables_files_attachments.addLabel(file.getPath());
                }
                break;
        }
    }
}
