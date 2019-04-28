package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Utilities.Utility;
import com.google.android.material.textfield.TextInputLayout;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @ViewById TextInputLayout register_email;
    @ViewById TextInputLayout register_username;
    @ViewById TextInputLayout register_password;
    @ViewById TextInputLayout register_repassword;
    @ViewById Button register_submit;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    AlertDialog dialog;


    @Click(R.id.register_submit)
    void submitClicked(View view){
        if(!validateField(register_email) | !validateField(register_username)
                | !validateField(register_password) | !validateField(register_repassword)) {
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
        user.setEmail(register_email.getEditText().getText().toString().trim());
        user.setUsername(register_username.getEditText().getText().toString().trim());
        user.setPassword(register_password.getEditText().getText().toString().trim());
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        dialog.show();
        registerUser(user);

    }
    @Background void registerUser(User user){
        DAOFactory factory = DAOFactory.getDatabase(DATABASES.PARSE);
        BaseDAO<User> userDAO = factory.getUserDAO();
        int result = userDAO.insert(user);
        registerDone(result);
    }
    @UiThread void registerDone(int result){
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putInt("RESULT", result);
        if(result <= 0) {
            setResult(RESULT_CANCELED, new Intent().putExtras(bundle));
            finish();
            return;
        }
        setResult(RESULT_OK, new Intent().putExtras(bundle));
        finish();
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
