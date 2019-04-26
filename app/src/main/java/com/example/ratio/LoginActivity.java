package com.example.ratio;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.Nullable;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @ViewById TextView login_forgotpassword;
    @ViewById Button login_button;
    @ViewById TextView login_createaccount;
    @ViewById TextInputLayout login_username;
    @ViewById TextInputLayout login_password;
    BaseDialog basicDialog;
    AlertDialog alertDialog;
    @AfterViews
    void afterView(){
        basicDialog = new BasicDialog(this);
    }

    @Click(R.id.login_button)
    void loginClicked(View view) {
        if(!validateField(login_username) | !validateField(login_password)) {
            return;
        }
        alertDialog = Utility.getInstance().showLoading(this, "Logging in", false);
        User user = new User();
        user.setUsername(login_username.getEditText().getText().toString().trim());
        user.setPassword(login_password.getEditText().getText().toString().trim());
        alertDialog.show();
        loginUser(user);

    }
    @Background void loginUser(User user){
        boolean isSuccessful = false;
        DAOFactory factoryParse = DAOFactory.getDatabase(DATABASES.PARSE);
        UserOperations<User> userDao = (UserOperations<User>) factoryParse.getUserDAO();
        loginFinished(userDao.loginUser(user));
    }

    @UiThread void loginFinished(User user){
        alertDialog.dismiss();
        if(user == null){
            basicDialog.setTitle("Result");
            basicDialog.setMessage("Login failed");
            basicDialog.showDialog();
            return;
        }
        MainActivity_.intent(this).start();
        finish();
    }

    @Click(R.id.login_forgotpassword)
    void forgotPassword(View view) {
        Snackbar.make(view, "forgot", Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.login_createaccount)
    void createAccount(View view) {
        RegisterActivity_.intent(this).startForResult(1);
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
