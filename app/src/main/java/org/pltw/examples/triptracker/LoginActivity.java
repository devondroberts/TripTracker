package org.pltw.examples.triptracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/*
 * Created by klaidley on 4/14/2015.
 */
public class LoginActivity extends AppCompatActivity {
    public final static String TAG = "LoginActivity";

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private TextView mSignUpText;
    private EditText mNameEditText;
    private Button mSignMeUpButton;

    BackendlessUser user = new BackendlessUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameEditText = (EditText)findViewById(R.id.enter_name);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mSignMeUpButton = (Button)findViewById(R.id.sign_me_up_button);

        mSignUpText = (TextView)findViewById(R.id.sign_up_text);
        MySignUpTextOnClickListener signUpTextListener = new MySignUpTextOnClickListener();
        mSignUpText.setOnClickListener(signUpTextListener);

        /* 3.1.2 Part II */
        Backendless.initApp( this,
                getString(R.string.be_app_id),
                getString(R.string.be_android_api_key));

        mEmailEditText = (EditText)findViewById(R.id.enter_email);
        mPasswordEditText = (EditText)findViewById(R.id.enter_password);

        MySignMeUpOnClickListener signMeUpListener = new MySignMeUpOnClickListener();
        mSignMeUpButton.setOnClickListener(signMeUpListener);

        MyLoginOnClickListener loginListener = new MyLoginOnClickListener();
        mLoginButton.setOnClickListener(loginListener);
    }

    /*
     StepToggle the UI elements, hiding and showing the necessary
     views for Sign Up
     */
    private class MySignUpTextOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mNameEditText.getVisibility() == View.GONE) {
                mNameEditText.setVisibility(View.VISIBLE);
                mSignMeUpButton.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                mSignUpText.setText(R.string.cancel_sign_up_text);
            } else {
                mNameEditText.setVisibility(View.GONE);
                mSignMeUpButton.setVisibility(View.GONE);
                mLoginButton.setVisibility(View.VISIBLE);
                mSignUpText.setText(R.string.sign_up_text);
            }
        }
    }

    /*
    3.1.2 Part II
     */
    private class MySignMeUpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userEmail = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String name = mNameEditText.getText().toString();

            userEmail = userEmail.trim();
            password = password.trim();
            name = name.trim();

            if (!userEmail.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                /* authenticate to Backendless */
                user.setEmail(userEmail);
                user.setPassword(password);
                user.setProperty("name", name);

                /* 3.1.2 Step 27 */
                // validate user data
                if ((userEmail.indexOf("@") == -1) || (userEmail.indexOf(".") == -1)) {
                    warnUser(getString(R.string.invalid_email_error));
                } else if (password.length() < 6) {
                    warnUser(getString(R.string.password_six_characters_error));
                } else if (password.equals(userEmail)) {
                    warnUser(getString(R.string.password_cannot_equal_email_error));
                } else {

                    /* signup */
                    final ProgressDialog pDialog = ProgressDialog.show(LoginActivity.this,
                           getString(R.string.progress_title), getString(R.string.progress_sign_up_message), true);

                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>(){
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            Log.i(TAG, "Registration successful for " + backendlessUser.getEmail());
                            pDialog.dismiss();
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.w(TAG, "Registration failed! " + fault.getMessage());
                            pDialog.dismiss();
                            warnUser(fault.getMessage());
                        }
                    });
                }
            }
            else {
                /* warn the user of the problem */
                warnUser(getString(R.string.empty_field_signup_error));
            }
        }
    }

    /*
    3.1.2 Step Part III
     */
    private class MyLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userEmail = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            userEmail = userEmail.trim();
            password = password.trim();

            if (!userEmail.isEmpty() && !password.isEmpty()) {
                /* login to Backendless */
                final ProgressDialog pDialog = ProgressDialog.show(LoginActivity.this,
                        getString(R.string.progress_title), getString(R.string.progress_login_message), true);

                Backendless.UserService.login(userEmail, password, new AsyncCallback<BackendlessUser>(){
                    @Override
                    public void handleResponse(BackendlessUser user) {
                        Log.i(TAG, "Login:" + user.getEmail() + " successfully logged in.");
                        pDialog.dismiss();
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.w(TAG, "Login failed:" + fault.getMessage());
                        pDialog.dismiss();
                        warnUser(fault.getMessage());
                    }
                });
            } else {
                warnUser(getString(R.string.empty_field_login_error));
            }
        }
    }

    /*
    3.1.2 Part V
    warn user of a problem
    */
    public void warnUser(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(errorMessage);
        builder.setTitle(R.string.authentication_error_title);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

