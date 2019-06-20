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

import com.example.ratio.Adapters.FileAdapter;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.FileObservable;

import java.util.List;

public class FileAttachmentsActivity extends AppCompatActivity {
    private static final String TAG = "FileAttachmentsActivity";
    @BindView(R.id.file_attachments_recyclerview) RecyclerView file_attachments_recyclerview;
    private String PARENT_ID = null;
    private AlertDialog dialog;
    private FileObservable fileObservable = new FileObservable();
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
