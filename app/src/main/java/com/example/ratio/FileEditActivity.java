package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratio.Entities.Pdf;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.FileObservable;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FileEditActivity extends AppCompatActivity {
    private static final String TAG = "FileEditActivity";
    @BindView(R.id.file_edit_thumbnail) ImageView file_edit_thumbnail;
    @BindView(R.id.file_edit_filename) TextInputLayout file_edit_filename;
    @BindView(R.id.file_edit_createdAt) TextInputLayout file_edit_createdAt;
    @BindView(R.id.file_edit_updatedAt) TextInputLayout file_edit_updatedAt;
    @BindView(R.id.file_edit_path) TextView file_edit_path;
    @BindView(R.id.file_edit_save) Button file_edit_save;
    private Pdf pdf = new Pdf();
    private FileObservable fileObservable = new FileObservable();
    private AlertDialog dialog;
    private Intent intent = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        ButterKnife.bind(this);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        pdf = (Pdf) getIntent().getSerializableExtra(Constant.FILE_OBJ);
        file_edit_filename.getEditText().setText(pdf.getFileName());
        file_edit_createdAt.getEditText().setText(pdf.getCreatedAt());
        file_edit_updatedAt.getEditText().setText(pdf.getUpdatedAt());
        file_edit_path.setText(pdf.getFilePath());
    }

    @OnClick(R.id.file_edit_save)
    public void saveClicked(View view) {
        if(!validateField(file_edit_filename)) {
            return;
        }
        pdf.setFilePath(file_edit_path.getText().toString());
        pdf.setFileName(file_edit_filename.getEditText().getText().toString());
        fileObservable.updatedFile(pdf)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pdf>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Pdf file) {
                        Log.d(TAG, "onNext: Image: " + file.getObjectId());
                        intent = new Intent();
                        intent.putExtra(Constant.FILE_OBJ, file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }

    @OnClick(R.id.file_edit_thumbnail)
    public void imagePickClicked(View view) {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowAudios(false)
                .setShowFiles(true)
                .setShowVideos(false)
                .enableImageCapture(false)
                .setSuffixes("pdf")
                .setMaxSelection(1)
                .build());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Result ok");
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            MediaFile chosen_image = mediaFiles.size() <= 0 ? null : mediaFiles.get(0);
            Log.d(TAG, "onActivityResult: Image Path: " + chosen_image.getPath()); // has Extension
            Log.d(TAG, "onActivityResult: Image Name: " + chosen_image.getName());
            file_edit_filename.getEditText().setText(chosen_image.getName());
            file_edit_path.setText(chosen_image.getPath());
        }
    }
    private boolean validateField(TextInputLayout textInputLayout) {
        String input = textInputLayout.getEditText().getText().toString();
        if(TextUtils.isEmpty(input) == true) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}
