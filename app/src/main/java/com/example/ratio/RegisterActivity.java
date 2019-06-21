package com.example.ratio;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.User;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.UserObservable;
import com.example.ratio.RxJava.UserinfoObservable;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.register_email) TextInputLayout register_email;
    @BindView(R.id.register_fullname) TextInputLayout register_fullname;
    @BindView(R.id.register_username) TextInputLayout register_username;
    @BindView(R.id.register_password) TextInputLayout register_password;
    @BindView(R.id.register_repassword) TextInputLayout register_repassword;
    @BindView(R.id.register_submit) Button register_submit;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    AlertDialog dialog;
    private UserObservable userObservable = new UserObservable();
    private UserinfoObservable userinfoObservable = new UserinfoObservable();
    private Intent intent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
    }

    @OnClick(R.id.register_submit)
    void submitClicked(View view){
        if(!validateField(register_email) | !validateField(register_username)
                | !validateField(register_password) | !validateField(register_repassword) | !validateField(register_fullname)) {
            return;
        }
        if(!register_password.getEditText().getText().toString().trim().equals(register_repassword.getEditText().getText().toString().trim())) {
            register_password.setError("Password does not match");
            register_repassword.setError("Password does not match");
            return;
        }
        if(!validateEmailAddress(register_email)) {
            return;
        }
        User user = new User();
        Userinfo userinfo = new Userinfo();
        user.setEmail(register_email.getEditText().getText().toString().trim());
        user.setUsername(register_username.getEditText().getText().toString().trim());
        user.setPassword(register_password.getEditText().getText().toString().trim());

        userObservable.registerUser(user)
                .map(new Function<User, User>() {
                    @Override
                    public User apply(User user) throws Exception {
                        userinfo.setVerified(false);
                        userinfo.setStatus(Constant.PENDING);
                        userinfo.setPosition(Constant.PENDING);
                        userinfo.setFullname(register_fullname.getEditText().getText().toString().trim());
                        userinfo.setEmail(register_email.getEditText().getText().toString().trim());
                        userinfo.setParent(user.getObjectId());
                        userinfo.setUsername(user.getUsername());
                        DAOFactory daoFactory = DAOFactory.getDatabase(DATABASES.PARSE);
                        BaseDAO<Userinfo> userinfoBaseDAO = daoFactory.getUserinfoDAO();
                        Userinfo myInfo = userinfoBaseDAO.insert(userinfo);
                        return user;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        dialog.show();
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: User: " + user.getObjectId());
                        intent = new Intent();
                        intent.putExtra(Constant.REGISTER_RESULT, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception: " + e.getMessage());
                        dialog.dismiss();
                        intent = new Intent();
                        intent.putExtra(Constant.REGISTER_RESULT, false);
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        dialog.dismiss();
                        intent = new Intent();
                        intent.putExtra(Constant.REGISTER_RESULT, true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }
    private boolean validateField(TextInputLayout editText) {
        String data = editText.getEditText().getText().toString().trim();
        if(data.isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public boolean validateEmailAddress(TextInputLayout textInputLayout) {
        String emailStr = textInputLayout.getEditText().getText().toString().trim();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        if(matcher.find() == false) {
            textInputLayout.setError("Email format is not valid");
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}
