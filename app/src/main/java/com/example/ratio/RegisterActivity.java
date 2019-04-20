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

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.Databases;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.register_email) TextInputLayout register_email;
    @BindView(R.id.register_username) TextInputLayout register_username;
    @BindView(R.id.register_password) TextInputLayout register_password;
    @BindView(R.id.register_repassword) TextInputLayout register_repassword;
    @BindView(R.id.register_submit) Button register_submit;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_submit)
    public void submitClicked(View view){
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
        new RegisterTask(user).execute((Void) null);
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

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        AlertDialog dialog;
        User user;
        boolean isSuccessful = false;
        public RegisterTask(User user) {
            this.user = user;
            Log.d(TAG, "RegisterTask: Constructor");
            dialog = Utility.getInstance().showLoading(RegisterActivity.this, "Please wait", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: Operation started");
            ParseUser parseUser = new ParseUser();
            parseUser.setEmail(user.getEmail());
            parseUser.setUsername(user.getUsername());
            parseUser.setPassword(user.getPassword());
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
