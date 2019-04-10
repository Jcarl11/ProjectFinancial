package com.example.ratio;

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

import com.example.ratio.Entities.UserEntity;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseUser;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.register_email) EditText register_email;
    @BindView(R.id.register_username) EditText register_username;
    @BindView(R.id.register_password) EditText register_password;
    @BindView(R.id.register_repassword) EditText register_repassword;
    @BindView(R.id.register_submit) Button register_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_submit)
    public void submitClicked(View view){
        if(!validateField(register_email) | !validateField(register_username) | !validateField(register_password) | !validateField(register_repassword)) {
            return;
        }
        if(!register_password.getText().toString().trim().equals(register_repassword.getText().toString().trim())) {
            register_password.setError("Password does not match");
            register_repassword.setError("Password does not match");
            return;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register_email.getText().toString().trim());
        userEntity.setUsername(register_username.getText().toString().trim());
        userEntity.setPassword(register_password.getText().toString().trim());
        new RegisterTask(userEntity).execute((Void) null);
    }

    private boolean validateField(EditText editText) {
        String data = editText.getText().toString().trim();
        if(data.isEmpty()) {
            editText.setError("Field cannot be empty");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        AlertDialog dialog;
        UserEntity userEntity;
        boolean isSuccessful = false;
        public RegisterTask(UserEntity userEntity) {
            this.userEntity = userEntity;
            Log.d(TAG, "RegisterTask: Constructor");
            dialog = Utility.getInstance().showLoading(RegisterActivity.this, "Please wait", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Operation started");
            ParseUser parseUser = new ParseUser();
            parseUser.setEmail(userEntity.getEmail());
            parseUser.setUsername(userEntity.getUsername());
            parseUser.setPassword(userEntity.getPassword());
            try {
                Log.d(TAG, "doInBackground: Signing up user...");
                parseUser.signUp();
                ParseUser.logOut();
                isSuccessful = true;
                Log.d(TAG, "doInBackground: Signup successful");
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Signup failed, Exception thrown: " + e.getMessage());
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
            Bundle bundle = new Bundle();
            bundle.putBoolean("RESULT", aBoolean);
            if(aBoolean) {
                setResult(RESULT_OK, new Intent().putExtras(bundle));
                finish();
            } else {
                setResult(RESULT_CANCELED, new Intent().putExtras(bundle));
                finish();
            }
        }
    }
}
