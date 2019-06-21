package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ratio.Adapters.ImageAdapter;
import com.example.ratio.Entities.Image;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.ImageObservable;

import java.util.ArrayList;
import java.util.List;

public class ImageAttachmentsActivity extends AppCompatActivity {
    private static final String TAG = "ImageAttachmentsActivit";
    @BindView(R.id.image_attachments_recyclerview) RecyclerView image_attachments_recyclerview;
    private String PARENT_ID = null;
    private ImageObservable imageObservable = new ImageObservable();
    private AlertDialog dialog;
    private int pos = -1;
    private List<Image> imageList = new ArrayList<>();
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
                        imageList = images;
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
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(image_attachments_recyclerview, swipeCallBack())
                .setIsVertical(false)
                .setItemTouchCallback(new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                    @Override
                    public void onTouch(int position) {

                    }
                })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        String[] choices = new String[]{"Edit", "Download"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageAttachmentsActivity.this);
                        builder.setTitle("Choose");
                        builder.setCancelable(true);
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: Clicked position: " + which);
                                switch (which) {
                                    case 0:
                                        Intent edit = new Intent(ImageAttachmentsActivity.this, ImageEditActivity.class);
                                        edit.putExtra(Constant.IMAGE_OBJ, imageList.get(pos));
                                        Log.d(TAG, "onClick: Created: " + imageList.get(pos).getCreatedAt());
                                        startActivityForResult(edit, 1);
                                        break;
                                    case 1:
                                        Toast.makeText(ImageAttachmentsActivity.this, "Not implemented yet", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        image_attachments_recyclerview.setOnTouchListener(listener);
    }
    public SwipeDismissRecyclerViewTouchListener.DismissCallbacks swipeCallBack() {
        SwipeDismissRecyclerViewTouchListener.DismissCallbacks listener = new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                pos = position;
                return true;
            }

            @Override
            public void onDismiss(View view) {

            }
        };
        return listener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1) {
            Toast.makeText(ImageAttachmentsActivity.this, "Record updated", Toast.LENGTH_LONG).show();
        } else if(resultCode == RESULT_CANCELED && data == null){
            Toast.makeText(ImageAttachmentsActivity.this, "Operation failed, Please try again", Toast.LENGTH_LONG).show();
        }
    }
}
