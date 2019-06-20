package com.example.ratio;

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

import com.example.ratio.Adapters.FileAdapter;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.FileObservable;

import java.util.ArrayList;
import java.util.List;

public class FileAttachmentsActivity extends AppCompatActivity {
    private static final String TAG = "FileAttachmentsActivity";
    @BindView(R.id.file_attachments_recyclerview) RecyclerView file_attachments_recyclerview;
    private String PARENT_ID = null;
    private AlertDialog dialog;
    private FileObservable fileObservable = new FileObservable();
    private int pos = -1;
    private List<Pdf> pdfList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_attachments);
        ButterKnife.bind(this);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
        Log.d(TAG, "onCreate: Parent: " + PARENT_ID);
        getSupportActionBar().setTitle(String.format("File Attachments"));
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        file_attachments_recyclerview.setHasFixedSize(true);
        file_attachments_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        fileObservable.retrieveFileFromParent(PARENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Pdf>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(List<Pdf> pdfs) {
                        Log.d(TAG, "onNext: PDF size: " + pdfs.size());
                        FileAdapter fileAdapter = new FileAdapter(FileAttachmentsActivity.this, pdfs);
                        file_attachments_recyclerview.setAdapter(fileAdapter);
                        pdfList = pdfs;
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
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(file_attachments_recyclerview, swipeCallBack())
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(FileAttachmentsActivity.this);
                        builder.setTitle("Choose");
                        builder.setCancelable(true);
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: Clicked position: " + which);
                                switch (which) {
                                    case 0:
                                        Intent edit = new Intent(FileAttachmentsActivity.this, FileEditActivity.class);
                                        edit.putExtra(Constant.IMAGE_OBJ, pdfList.get(pos));
                                        startActivityForResult(edit, 1);
                                        break;
                                    case 1:
                                        Toast.makeText(FileAttachmentsActivity.this, "Not implemented yet", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        file_attachments_recyclerview.setOnTouchListener(listener);

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
}
