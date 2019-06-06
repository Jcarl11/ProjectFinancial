package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class AddIncomeActivity extends AppCompatActivity {

    private static final String TAG = "AddIncomeActivity";
    @BindView(R.id.addincome_field_amount) TextInputLayout addincome_field_amount;
    @BindView(R.id.addincome_field_description) TextInputLayout addincome_field_description;
    @BindView(R.id.addincome_button_attachimage) Button addincome_button_attachimage;
    @BindView(R.id.addincome_button_attachfile) Button addincome_button_attachfile;
    @BindView(R.id.addincome_files_attachments) AutoLabelUI addincome_files_attachments;
    @BindView(R.id.addincome_button_add) Button addincome_button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Add income");
    }

    @OnClick(R.id.addincome_button_attachimage)
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

    @OnClick(R.id.addincome_button_attachfile)
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
                        addincome_files_attachments.addLabel(file.getPath());
                    }
            break;
            case 2:
                Log.d(TAG, "onActivityResult: Result ok");
                ArrayList<MediaFile> mediaFiles_file = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Log.d(TAG, "onActivityResult: Media files size: " + mediaFiles_file.size());
                for (MediaFile file : mediaFiles_file) {
                    addincome_files_attachments.addLabel(file.getPath());
                }
            break;
        }
    }
}
