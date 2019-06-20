package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.FileValidator;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.RecievablesObservable;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRecievablesActivity extends AppCompatActivity {
    private static final String TAG = "AddRecievablesActivity";
    @BindView(R.id.addrecievables_field_amount) TextInputLayout addrecievables_field_amount;
    @BindView(R.id.addrecievables_field_description) TextInputLayout addrecievables_field_description;
    @BindView(R.id.addrecievables_button_attachimage) Button addrecievables_button_attachimage;
    @BindView(R.id.addrecievables_button_attachfile) Button addrecievables_button_attachfile;
    @BindView(R.id.addrecievables_files_attachments) AutoLabelUI addrecievables_files_attachments;
    @BindView(R.id.addrecievables_button_add) Button addrecievables_button_add;
    private AlertDialog dialog = null;
    private FileValidator fileValidator = new FileValidator();
    private String PARENT_ID = null;
    private DateTransform dateTransform = new DateTransform();
    private RecievablesObservable recievablesObservable = new RecievablesObservable();
    private DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Image> imageBaseDAO = daoFactory.getImageDAO();
    private BaseDAO<Pdf> pdfBaseDAO = daoFactory.getFileDAO();
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recievables);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Add Receivables");
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        PARENT_ID = getIntent().getStringExtra(Constant.PARENTID);
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

    @OnClick(R.id.addrecievables_button_add)
    public void addClicked(View view) {
        Log.d(TAG, "addClicked: Add clicked");
        List<Image> imagesList = new ArrayList<>();
        List<Pdf> filesList = new ArrayList<>();
        boolean hasAttachments = false;
        if(addrecievables_files_attachments.getLabels().size() > 0) {
            List<Label> files = addrecievables_files_attachments.getLabels();
            for (Label label : files) {
                if(fileValidator.isImage(label.getText())) {
                    Image img = new Image();
                    img.setFileName(FilenameUtils.getName(label.getText()));
                    img.setDeleted(false);
                    img.setFilePath(label.getText());
                    imagesList.add(img);
                } else if (fileValidator.isFile(label.getText())) {
                    Pdf pdf = new Pdf();
                    pdf.setFileName(FilenameUtils.getName(label.getText()));
                    pdf.setDeleted(false);
                    pdf.setFilePath(label.getText());
                    filesList.add(pdf);
                }
            }
            Log.d(TAG, "addClicked: Images size: " + imagesList.size());
            Log.d(TAG, "addClicked: Files size: " + filesList.size());
            hasAttachments = true;
        }
        Receivables receivables = new Receivables();
        receivables.setParent(PARENT_ID);
        receivables.setDescription(addrecievables_field_description.getEditText().getText().toString().trim().toUpperCase());
        receivables.setAmount(addrecievables_field_amount.getEditText().getText().toString().trim());
        receivables.setAttachments(hasAttachments);
        Date date = new Date();
        Log.d(TAG, "addClicked: Date now: " + dateTransform.toISO8601String(date));
        receivables.setTimestamp(dateTransform.toISO8601String(date));
        recievablesObservable.recievablesObservable(receivables)
                .map(new Function<Receivables, Receivables>() {
                    @Override
                    public Receivables apply(Receivables receivables) throws Exception {
                        List<Image> processed = new ArrayList<>();
                        for(Image image : imagesList) {
                            image.setParent(receivables.getObjectId());
                            processed.add(image);
                        }
                        int result = imageBaseDAO.insertAll(processed);
                        Log.d(TAG, "apply: Result: " + result);
                        return receivables;
                    }
                })
                .map(new Function<Receivables, Receivables>() {
                    @Override
                    public Receivables apply(Receivables receivables) throws Exception {
                        List<Pdf> pdfList = new ArrayList<>();
                        for (Pdf pdf : filesList) {
                            pdf.setParent(receivables.getObjectId());
                            pdfList.add(pdf);
                        }
                        int result = pdfBaseDAO.insertAll(pdfList);
                        Log.d(TAG, "apply: Result: " + result);
                        return receivables;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Receivables>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Receivables receivables) {
                        Log.d(TAG, "onNext: Expenses: " + receivables.getObjectId());
                        intent.putExtra("RESULT", receivables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        dialog.dismiss();
                        intent.putExtra("RESULT", "ERROR");
                        setResult(RESULT_CANCELED,intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }
}
