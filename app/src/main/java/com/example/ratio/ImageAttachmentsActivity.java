package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.ratio.Adapters.ImageAdapter;
import com.example.ratio.Entities.Image;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ImageObservable;

import java.util.List;

public class ImageAttachmentsActivity extends AppCompatActivity {
    private static final String TAG = "ImageAttachmentsActivit";
    @BindView(R.id.image_attachments_recyclerview) RecyclerView image_attachments_recyclerview;
    private String PARENT_ID = null;
    private ImageObservable imageObservable = new ImageObservable();
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_attachments);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        Log.d(TAG, "onCreate: Parent: " + PARENT_ID);
        getSupportActionBar().setTitle(String.format("Image Attachments"));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        image_attachments_recyclerview.setHasFixedSize(true);
        image_attachments_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        imageObservable.retrieveImageFromParent(PARENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Image> images) {
                        Log.d(TAG, "onNext: Image Size: " + images.size());
                        ImageAdapter imageAdapter = new ImageAdapter(ImageAttachmentsActivity.this, images);
                        image_attachments_recyclerview.setAdapter(imageAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                    }
                });
    }
}
