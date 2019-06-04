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
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

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
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(getResources().getColor(R.color.colorPrimary))
                .includeVideo(false)
                .multi()
                .limit(9)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: Here");
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Log.d(TAG, "onActivityResult: Goes in");
            List<Image> images = ImagePicker.getImages(data);
            Log.d(TAG, "onActivityResult: Images size: " + images.size());
            for (Image object : images) {
                addincome_files_attachments.addLabel(object.getPath());
            }
        }
    }
}
