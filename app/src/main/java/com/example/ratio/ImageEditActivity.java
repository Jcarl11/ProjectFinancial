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

import com.example.ratio.Entities.Image;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ImageObservable;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageEditActivity extends AppCompatActivity {
    private static final String TAG = "ImageEditActivity";
    @BindView(R.id.image_edit_thumbnail) ImageView image_edit_thumbnail;
    @BindView(R.id.image_edit_filename) TextInputLayout image_edit_filename;
    @BindView(R.id.image_edit_createdAt) TextInputLayout image_edit_createdAt;
    @BindView(R.id.image_edit_updatedAt) TextInputLayout image_edit_updatedAt;
    @BindView(R.id.image_edit_path) TextView image_edit_path;
    @BindView(R.id.image_edit_save) Button image_edit_save;
    private Image image = new Image();
    private ImageObservable imageObservable = new ImageObservable();
    private AlertDialog dialog;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        ButterKnife.bind(this);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        image = (Image) getIntent().getSerializableExtra(Constant.IMAGE_OBJ);
        Log.d(TAG, "onCreate: Created at: " + image.getCreatedAt());
        Log.d(TAG, "onCreate: Updated at: " + image.getUpdatedAt());
        Picasso.get().load(image.getFilePath()).into(image_edit_thumbnail);
        image_edit_filename.getEditText().setText(image.getFileName());
        image_edit_createdAt.getEditText().setText(image.getCreatedAt());
        image_edit_updatedAt.getEditText().setText(image.getUpdatedAt());
        image_edit_path.setText(image.getFilePath());
    }

    @OnClick(R.id.image_edit_save)
    public void saveClicked(View view) {
        if(!validateField(image_edit_filename)) {
            return;
        }
        image.setFilePath(image_edit_path.getText().toString());
        image.setFileName(image_edit_filename.getEditText().getText().toString());
        imageObservable.updateImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Image image) {
                        Log.d(TAG, "onNext: Image: " + image.getObjectId());
                        intent = new Intent();
                        intent.putExtra(Constant.IMAGE_OBJ, image);
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

    @OnClick(R.id.image_edit_thumbnail)
    public void imagePickClicked(View view) {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowAudios(false)
                .setShowFiles(false)
                .setShowVideos(false)
                .enableImageCapture(true)
                .setMaxSelection(1)
                .build());
        startActivityForResult(intent, 1);
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
            image_edit_thumbnail.setImageDrawable(null);
            image_edit_filename.getEditText().setText(chosen_image.getName());
            image_edit_path.setText(chosen_image.getPath());
            Picasso.get().load(new File(chosen_image.getPath())).into(image_edit_thumbnail);
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
