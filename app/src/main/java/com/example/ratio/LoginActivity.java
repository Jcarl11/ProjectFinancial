package com.example.ratio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.UserEntity;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_username) EditText login_username;
    @BindView(R.id.login_password) EditText login_password;
    @BindView(R.id.login_forgotpassword) TextView login_forgotpassword;
    @BindView(R.id.login_button) Button login_button;
    @BindView(R.id.login_createaccount) TextView login_createaccount;
    BaseDialog basicDialog;
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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(login_username.getText().toString().trim());
        userEntity.setPassword(login_password.getText().toString().trim());
        new LoginTask(userEntity).execute((Void)null);
    }

    @OnClick(R.id.login_forgotpassword)
    public void forgotPassword(View view) {
        Snackbar.make(view, "forgot", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_createaccount)
    public void createAccount(View view) {
        startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                basicDialog = new BasicDialog(this, "Result", "Account registered successfully");
                basicDialog.showDialog();
            } else if(resultCode == RESULT_CANCELED) {
                if(data != null) {
                    basicDialog = new BasicDialog(this, "Result", "Register failed");
                    basicDialog.showDialog();
                }
            }
        }
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

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        AlertDialog dialog;
        UserEntity userEntity;
        boolean isSuccessful = false;
        public LoginTask(UserEntity userEntity) {
            this.userEntity = userEntity;
            dialog = Utility.getInstance().showLoading(LoginActivity.this, "Logging in", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: Logging in user...");
                ParseUser.logIn(userEntity.getUsername(), userEntity.getPassword());
                isSuccessful = true;
                Log.d(TAG, "doInBackground: Login successful");

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Login failed, Exception thrown: " + e.getMessage());
            }
            return isSuccessful;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            if(aBoolean) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                basicDialog = new BasicDialog(LoginActivity.this, "Result","Wrong credentials" );
                basicDialog.showDialog();
            }
        }
    }
}
