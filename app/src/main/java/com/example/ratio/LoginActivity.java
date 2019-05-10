package com.example.ratio;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.Nullable;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_forgotpassword) TextView login_forgotpassword;
    @BindView(R.id.login_button) Button login_button;
    @BindView(R.id.login_createaccount) TextView login_createaccount;
    @BindView(R.id.login_username) TextInputLayout login_username;
    @BindView(R.id.login_password) TextInputLayout login_password;
    BaseDialog basicDialog;
    AlertDialog alertDialog;
    DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    UserOperations<User> userOperations;

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        basicDialog = new BasicDialog(this);
    }

    @OnClick(R.id.login_button)
    void loginClicked(View view) {
        if(!validateField(login_username) | !validateField(login_password)) {
            return;
        }
        alertDialog = Utility.getInstance().showLoading(this, "Logging in", false);
        User user = new User();
        user.setUsername(login_username.getEditText().getText().toString().trim());
        user.setPassword(login_password.getEditText().getText().toString().trim());
        userOperations = (UserOperations<User>) parseFactory.getUserDAO();
        Observable.fromCallable(() -> {
            User currentUser = userOperations.loginUser(user);
            return currentUser != null ? currentUser : null;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        alertDialog.show();
                    }

                    @Override
                    public void onNext(User user) {
                        if(user == null){
                            return;
                        }
                        Log.d(TAG, "onNext: User: " + user.getUsername());
                        alertDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        basicDialog.setTitle("Result");
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    @OnClick(R.id.login_forgotpassword)
    void forgotPassword(View view) {
        Snackbar.make(view, "forgot", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_createaccount)
    void createAccount(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                basicDialog.setTitle("Result");
                basicDialog.setMessage("Account registered successfully");
                basicDialog.showDialog();
            } else if(resultCode == RESULT_CANCELED) {
                if(data == null)
                    return;

                basicDialog.setTitle("Result");
                basicDialog.setMessage("Register failed");
                basicDialog.showDialog();
            }
        }
    }

    private boolean validateField(TextInputLayout editText) {
        String value = editText.getEditText().getText().toString().trim();
        if(value.isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }
}
