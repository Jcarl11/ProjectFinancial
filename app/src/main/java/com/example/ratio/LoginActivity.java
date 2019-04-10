package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_username) EditText login_username;
    @BindView(R.id.login_password) EditText login_password;
    @BindView(R.id.login_forgotpassword) TextView login_forgotpassword;
    @BindView(R.id.login_button) Button login_button;
    @BindView(R.id.login_createaccount) TextView login_createaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_button)
    public void loginClicked(View view) {
        if(!validateField(login_username) | !validateField(login_password)) {
            return;
        }

        Snackbar.make(view, "OK", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_forgotpassword)
    public void forgotPassword(View view) {
        Snackbar.make(view, "forgot", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_createaccount)
    public void createAccount(View view) {
        Snackbar.make(view, "create", Snackbar.LENGTH_LONG).show();
    }
    private boolean validateField(EditText editText) {
        String value = editText.getText().toString().trim();
        if(value.isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        } else {
            editText.setError(null);
             return true;
        }
    }
}
